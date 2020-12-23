package com.sm848.ejb;

import com.sm848.entity.SystemUser;
import com.sm848.entity.UserAccounts;
import com.sm848.entity.SystemUserGroup;
import com.sm848.entity.Transactions;
//import com.sm848.entity.SystemUserGroup;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBTransactionRolledbackException;


import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


/**
 * implements user functionality
 * @author salmafikry
 */
@DeclareRoles({"users","admins"})
@Stateless
public class UserService implements UserServiceInterface{

    @PersistenceContext
    EntityManager em;
    
    

    public UserService() {
    }

    
    @Override
    public void registerUser(String email, String username, String userpassword, String name, String surname, String phoneNum, String currency, Date dob) {
        try {
            
            UserAccounts user;
            SystemUserGroup sys_user_group;
            SystemUser sys_user;
            
            //hash the input password
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String passwd = userpassword;
            md.update(passwd.getBytes("UTF-8"));
            byte[] digest = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }
            String paswdToStoreInDB = sb.toString();

            //initialise balance to 1000 pounds or the equivalent in the user's chosen currency 
            BigDecimal balance= getAmountfromPounds(1000.00, currency);
            
  
            sys_user = new SystemUser(username,paswdToStoreInDB);
            sys_user_group = new SystemUserGroup(username, "users");
            user = new UserAccounts(sys_user,balance, email, username, paswdToStoreInDB, name, surname, phoneNum, currency, dob);
          

            //insert the data into the database
            em.persist(sys_user);
            em.persist(sys_user_group);
            em.persist(user);
            
            

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public boolean emailExists(String email){
        
        //create a query that finds a User with the given email
        Query query = em.createNamedQuery("findUserEm");
        
        //set the query's parameter with the user's email
        query.setParameter("em",email);
        
        //if the resultList is emoty then no user with the given email exists
        return !query.getResultList().isEmpty();
    }
    
    
    @Override
    public boolean usernameExists(String username){
        //create a query that finds a User with the given username
        Query query = em.createNamedQuery("findUn");
        
        //set the query's parameter with the user's username
        query.setParameter("un",username);
        
        //if the resultList is emoty then no user with the given username exists
        return !query.getResultList().isEmpty();
    }
    
    @RolesAllowed("users")
    @Override
    public UserAccounts getUserByUn(String username){
        UserAccounts u;
        //create a query that finds the User with the given username
        Query query = em.createNamedQuery("findUn");
        
        //set the query's parameter with the user's username
        query.setParameter("un",username);
        
        //return the user with the given username
        u= (UserAccounts) query.getResultList().get(0);
        return u;
        
    }
    
    @RolesAllowed("users")
    @Override
    public UserAccounts getUserByEmail(String email){
        UserAccounts u;
        //create a query that finds the User with the given email
        Query query = em.createNamedQuery("findUserEm");
        
        //set the query's parameter with the user's email
        query.setParameter("em",email);
        
        //return the user with the given email
        u= (UserAccounts) query.getResultList().get(0);
        return u;
        
    }
    
    
    @RolesAllowed("users")
    @Override
    public BigDecimal getBalanceUn(String username){
        
        BigDecimal bal;
        
        //create a query that finds the balance of the user with the given username
        Query query= em.createNamedQuery("findBal");
        
        //set the query's parameter with the user's username
        query.setParameter("un", username);
        
        //return the balance 
        bal= (BigDecimal) query.getResultList().get(0);
        return bal;
    }
    
    @RolesAllowed("users")
    @Override
    public BigDecimal getBalanceEmail(String email){
        BigDecimal bal;
        //create a query that finds the balance of the user with the given email
        Query query= em.createNamedQuery("findBalanceByEm");
        
        //set the query's parameter with the user's email
        query.setParameter("em", email);
        
        //return the balance
        bal= (BigDecimal) query.getResultList().get(0);
        return bal;
    }
    
    @RolesAllowed("users")
    @Override
    public boolean sufficientFunds(String username, double amount){
        
       
        //get the balance of the user with the given username
        BigDecimal bal= getBalanceUn(username);
        
        //convert the balance to double 
        double balance= bal.doubleValue();
        
        //return true if the balance is greater than the given amount
        return balance>amount;
        
        
        
    }
    
    @RolesAllowed("users")
    @Override
    public BigDecimal getNewBalance(BigDecimal balance, BigDecimal amount, String m){
        
        // "increase" refers to addition
        if (m.equals("increase")){
            BigDecimal newamount=balance.add(amount).setScale(2,RoundingMode.DOWN);
            return newamount;
            
        }
        // "decrease" refers to subtraction
        else{
            BigDecimal newamount=balance.subtract(amount).setScale(2,RoundingMode.DOWN);
            return newamount;
            
        }
    }
    
    @RolesAllowed("users")
    @Override
    public String makePayment(String sender, String receiver, double amount){
       
          try{ //get the user making the payment
           UserAccounts userSender= getUserByEmail(sender);
           
           //get the username of the user making the payment
           String usernameSender= userSender.getUsername();
           
           //get the user receiving the payment
           UserAccounts userReceiver= getUserByEmail(receiver);
           
           
           //get the balance of the user receiving the payment
           BigDecimal oldvalR= userReceiver.getBalance();
           //get the balance of the user making the payment
           BigDecimal oldvalS= userSender.getBalance();
               
           //if the 2 users dont have the same currency
           if(!userReceiver.getCurrency().equals(userSender.getCurrency()))
           {
                   //convert the balance of the user receiving the payment into pounds from his registered currency
                   BigDecimal oldvalRPounds= getAmountinPounds(oldvalR.doubleValue(),userReceiver.getCurrency() );
                   
                   //convert the balance of the user making the payment into pounds from his registered currency
                   BigDecimal oldvalSPounds= getAmountinPounds(oldvalS.doubleValue(),userSender.getCurrency() );
                   
                   //concert the amount being sent into pounds from the user making the payment's registered currency
                   BigDecimal amountPounds= getAmountinPounds(amount, userSender.getCurrency());
                   
                   //get the balance of the user making the payment after subtracting the amount (in pounds)
                   BigDecimal newbalS= getNewBalance(oldvalSPounds, amountPounds, "decrease");
                   //get the balance of the user receiving the payment after adding the amount (in pounds)
                   BigDecimal newbalR= getNewBalance(oldvalRPounds, amountPounds, "increase");
                   
                   //convert the balance of the user making the payment to his original currency
                   BigDecimal newbalSPounds= getAmountfromPounds(newbalS.doubleValue(),userSender.getCurrency() );
                   //convert the balance of the user receiving the payment to his original currency
                   BigDecimal newbalRPounds= getAmountfromPounds(newbalR.doubleValue(),userReceiver.getCurrency() );
                   
                   //create and execute the query that updates the user making the payment's balance
                   Query updateBalanceS= em.createNamedQuery("updateBal");
                   //set the paramater to the new balance after the currency conversion
                   updateBalanceS.setParameter("bal", newbalSPounds.setScale(2,RoundingMode.DOWN));
                   //set the paraneter to the user's email
                   updateBalanceS.setParameter("em", userSender.getEmail());
                   //execute the query
                   updateBalanceS.executeUpdate();
                   
                   //create and execute the query that updates the user receiving the payment's balance
                   Query updateBalanceR= em.createNamedQuery("updateBal");
                   //set the paramater to the new balance after the currency conversion
                   updateBalanceR.setParameter("bal", newbalRPounds.setScale(2,RoundingMode.DOWN));
                   //set the paraneter to the user's email
                   updateBalanceR.setParameter("em", userReceiver.getEmail());
                   //execute the query
                   updateBalanceR.executeUpdate();
                   
               }
               //if the users have the same currency
               else
               {
                   //get the balance of the user making the payment after subtracting the amount
                   BigDecimal newbalS= getNewBalance(oldvalS, new BigDecimal(amount), "decrease");
                   //get the balance of the user receiving the payment after subtracting the amount (in pounds)
                   BigDecimal newbalR= getNewBalance(oldvalR, new BigDecimal(amount), "increase");
                   
                   //create and execute the query that updates the user making the payment's balance
                   Query updateBalanceS= em.createNamedQuery("updateBal");
                   //set the paramater to the new balance after the currency conversion
                   updateBalanceS.setParameter("bal", newbalS.setScale(2,RoundingMode.DOWN));
                   //set the paraneter to the user's email
                   updateBalanceS.setParameter("em", userSender.getEmail());
                   //execute the query
                   updateBalanceS.executeUpdate();
                   
                   //create and execute the query that updates the user receiving the payment's balance
                   Query updateBalanceR= em.createNamedQuery("updateBal");
                   //set the paramater to the new balance after the currency conversion
                   updateBalanceR.setParameter("bal", newbalR.setScale(2,RoundingMode.DOWN));
                   //set the paraneter to the user's email
                   updateBalanceR.setParameter("em", userReceiver.getEmail());
                   //execute the query
                   updateBalanceR.executeUpdate();
               }
               //create a new transaction entry
               Transactions t;
               t= new Transactions(sender, receiver, "completed", new BigDecimal(amount), userSender.getCurrency());
               
               //insert the transaction into the database
               em.persist(t);
               //ensures the transaction is persisted immediately 
               em.flush();
          return "success";}
          catch(EJBTransactionRolledbackException e) {
            
            return "conflict";  
        }
           
        
    }
    
    @RolesAllowed("users")
    @Override
    public void requestPayment(String requester, String u, double amount){
          //get the user that the request is sent to
          UserAccounts user= getUserByEmail(u);
          //get the user making the request
          UserAccounts req= getUserByEmail(requester);
          
          //create a new "pending" transaction with the user making the request and the receiver of the payment 
          Transactions t= new Transactions(u, requester, "pending", new BigDecimal(amount), req.getCurrency());
          //insert the transaction into the database
          em.persist(t);
             
        
        
    }
    
    @RolesAllowed("users")
    @Override
    public String acceptRequest(Long transactionID){
        try{
        //create a new query that gets the transaction with the given id
        Query getTrans= em.createNamedQuery("getTransaction");
        //set the paramater to the given id
        getTrans.setParameter("id", transactionID);
        //store the result in t
        Transactions t= (Transactions)getTrans.getResultList().get(0);
        
        //get the email of the user receiving the payment
        String emailR=t.getReceiver();
        //get the email of the user making the payment
        String emailS=t.getSender();
        
        //get the user making the payment
        UserAccounts userSender= getUserByEmail(emailS);
        //get the username of the user making the payment
        String usernameSender= userSender.getUsername();
        //get the user receiving the payment
        UserAccounts userReceiver= getUserByEmail(emailR);
        
        //check if the user making the payment has sufficient funds
        
               //get the balance of the user receiving the payment
               BigDecimal oldvalR= userReceiver.getBalance();
               //get the balance of the user making the payment
               BigDecimal oldvalS= userSender.getBalance();
               
               //if the 2 users dont have the same currency
               if(!userReceiver.getCurrency().equals(userSender.getCurrency()))
               {
                   //convert the balance of the user receiving the payment into pounds from his registered currency
                   BigDecimal oldvalRPounds= getAmountinPounds(oldvalR.doubleValue(),userReceiver.getCurrency() );
                   //convert the balance of the user msking the payment into pounds from his registered currency
                   BigDecimal oldvalSPounds= getAmountinPounds(oldvalS.doubleValue(),userSender.getCurrency() );
                   
                   //since the user receiving the payment is the one who made the request, then the currency of the amount is in his registered currency
                   BigDecimal amountPounds= getAmountinPounds(t.getTransAmount().doubleValue(), userReceiver.getCurrency());
                   
                    //get the balance of the user making the payment after subtracting the amount (in pounds)
                   BigDecimal newbalS= getNewBalance(oldvalSPounds, amountPounds, "decrease");
                    //get the balance of the user receiving the payment after subtracting the amount (in pounds)
                   BigDecimal newbalR= getNewBalance(oldvalRPounds, amountPounds, "increase");
                   
                   //convert the balance of the user making the payment to his original/registered currency
                   BigDecimal newbalSPounds= getAmountfromPounds(newbalS.doubleValue(),userSender.getCurrency() );
                   //convert the balance of the user receiving the payment to his original/registered currency
                   BigDecimal newbalRPounds= getAmountfromPounds(newbalR.doubleValue(),userReceiver.getCurrency() );
                   
                    //create and execute the query that updates the user making the payment's balance
                   Query updateBalanceS= em.createNamedQuery("updateBal");
                   //set the paramater to the new balance after the currency conversion
                   updateBalanceS.setParameter("bal", newbalSPounds.setScale(2,RoundingMode.DOWN));
                   //set the paraneter to the user's email
                   updateBalanceS.setParameter("em", userSender.getEmail());
                   //execute the query
                   updateBalanceS.executeUpdate();
                   
                    //create and execute the query that updates the user receiving the payment's balance
                   Query updateBalanceR= em.createNamedQuery("updateBal");
                   //set the paramater to the new balance after the currency conversion
                   updateBalanceR.setParameter("bal", newbalRPounds.setScale(2,RoundingMode.DOWN));
                   //set the paraneter to the user's email
                   updateBalanceR.setParameter("em", userReceiver.getEmail());
                   //execute the query
                   updateBalanceR.executeUpdate();
                   
               }
               else
               {
                    //get the balance of the user making the payment after subtracting the amount
                   BigDecimal newbalS= getNewBalance(oldvalS, t.getTransAmount(), "decrease");
                    //get the balance of the user making the payment after adding the amount 
                   BigDecimal newbalR= getNewBalance(oldvalR, t.getTransAmount(), "increase");
                   //create and execute the query that updates the user making the payment's balance
                   Query updateBalanceS= em.createNamedQuery("updateBal");
                   //set the paramater to the new balance after the currency conversion
                   updateBalanceS.setParameter("bal", newbalS.setScale(2,RoundingMode.DOWN));
                   //set the paraneter to the user's email
                   updateBalanceS.setParameter("em", userSender.getEmail());
                   //execute the query
                   updateBalanceS.executeUpdate();
                   
                   //create and execute the query that updates the user receiving the payment's balance
                   Query updateBalanceR= em.createNamedQuery("updateBal");
                   //set the paramater to the new balance after the currency conversion
                   updateBalanceR.setParameter("bal", newbalR.setScale(2,RoundingMode.DOWN));
                   //set the paraneter to the user's email
                   updateBalanceR.setParameter("em", userReceiver.getEmail());
                   //execute the query
                   updateBalanceR.executeUpdate();
               }
           
        
        //create a query that updates the status of the transaction
        Query updateStatus= em.createNamedQuery("updateStat");
        //set the status paramater to "completed" (instead of pending)
        updateStatus.setParameter("status", "completed");
        //set the paramatert to the id of the transaction
        updateStatus.setParameter("id", transactionID);
        //execute the query
        updateStatus.executeUpdate();
        return "success";}
         catch(EJBTransactionRolledbackException e) {
            
            return "conflict";  
        }
    }
    
    @RolesAllowed("users")
    @Override
    public void rejectRequest(Long transactionID){
        //create a query that updates the status of the transaction
        Query updateStatus= em.createNamedQuery("updateStat");
        //set the status paramater to "reected" (instead of pending)
        updateStatus.setParameter("status", "rejected");
        //set the paramatert to the id of the transaction
        updateStatus.setParameter("id", transactionID);
        //execute the query
        updateStatus.executeUpdate();
    }
    
    @RolesAllowed("users")
    @Override
    public List<Transactions> findRequests(String username){
        UserAccounts u= getUserByUn(username);
        //create a query that finds all the pending transaction of a specific user
        Query query= em.createNamedQuery("findTransactionRequests");
        //set the paramater to "pending" 
        query.setParameter("p", "pending");
        /**set the paramater the user's email (the parameter of the 'sender' in the transaction's 
         * table since the user making the payment is the one receiving the request
        */
        query.setParameter("em", u.getEmail());
        //return the list of transactions
        return query.getResultList();
        
    }
    
   
    @RolesAllowed("users")
    @Override
    public boolean reqExists(Long reqid, String username){
        
        UserAccounts u= getUserByUn(username);
        //create a query that finds if a pendingtransaction with a specific id exists for the given username
        Query query= em.createNamedQuery("findTransReqwithID");
        //set the parameter to 'pending'  
        query.setParameter("p", "pending");
        //set the email to the user's email
        query.setParameter("em", u.getEmail());
        //set the id to the given id (the id the user inputs)
        query.setParameter("id", reqid);
        //return true if the query results' list is not empty
        return !query.getResultList().isEmpty();
        
    }
    
    @RolesAllowed("users")
    @Override
    public BigDecimal getAmountinPounds(double am, String currency) {
        //convert the amount to be converted to BigDecimal
        BigDecimal amount= new BigDecimal(am);
        //fixed usd rate (from dollars to pounds) 
        double usdRate= 0.81;
        //fixed eur rate (from euros to pounds)
        double eurRate=0.88;
        //convert rates to BigDecimal
        BigDecimal usd= new BigDecimal(usdRate);
        BigDecimal eur= new BigDecimal(eurRate);
        //initialize variable to store the new amount
        BigDecimal newamount;
        if (currency.equals("US Dollars")){
            //if the currency to be converted is in dollars multiply with the usd rate
            newamount=amount.multiply(usd).setScale(2,RoundingMode.DOWN);
            return newamount;
        }
        else if (currency.equals("Euros")){
            //if the currency to be converted is in euros multiply with the eur rate
            newamount=amount.multiply(eur).setScale(2,RoundingMode.DOWN);
            return newamount;
        }
        else{
            //if currency to be converted is in pounds, no conversion needed, return same amount
            return amount;
        }
        
    }
    
    @RolesAllowed("users")
    @Override
    public BigDecimal getAmountfromPounds(double am, String currency) {
        //convert the amount to be converted to BigDecimal
        BigDecimal amount= new BigDecimal(am);
        //fixed usd rate (from pounds to dollars) 
        double usdRate= 1.24;
        //fixed eur rate (from pounts to euros)
        double eurRate=1.14;
        //convert rates to BigDecimal
        BigDecimal usd= new BigDecimal(usdRate);
        BigDecimal eur= new BigDecimal(eurRate);
        //initialize variable to store the new amount
        BigDecimal newamount;
        if (currency.equals("US Dollars")){
            //if the currency wanted is in dollars multiply with the usd rate
            newamount=amount.multiply(usd).setScale(2,RoundingMode.DOWN);
            return newamount;
        }
        else if (currency.equals("Euros")){
            //if the currency wanted is in euros multiply with the eur rate
            newamount=amount.multiply(eur).setScale(2,RoundingMode.DOWN);
            return newamount;
        }
        else{
            //if the currency wanted is in pounds, no conversion needed, return same amount
            return amount;
        }
    }
    
    @RolesAllowed({"users","admins"})
    @Override
    public List<Transactions> getTransactions(String email){
        //create a query that finds all the transaction of given email
        Query getTrans= em.createNamedQuery("getUserTransaction");
        //set the paramaters to the email (finds all transaction where the sender or receiver is of the given emaik)
        getTrans.setParameter("s", email);
        getTrans.setParameter("r", email);
        //return the obtained list of transactions
        List<Transactions> transList= getTrans.getResultList();
        return transList;
        
    }
    

    
    
    
    
}
