/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sm848.ejb;

import com.sm848.entity.Transactions;
import com.sm848.entity.UserAccounts;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * defines user functionality
 * @author salmafikry
 */
public interface UserServiceInterface {
    //registers new users
    public void registerUser(String email, String username, String userpassword, String name, String surname, String phoneNum, String currency, Date dob);
    
    //checks if a user with the given email address already exists in the database
    public boolean emailExists(String email);
    
    //checks if a user with the given email address already exists in the database
    public boolean usernameExists(String Username);
    
    //selects the user with the given username from the database
    public UserAccounts getUserByUn(String username);
    
    //selects the user with the given email from the database
    public UserAccounts getUserByEmail(String email);
    
    //gets the balance of the user with the given username
    public BigDecimal getBalanceUn(String username);
    
    //gets the balance of the user with the given email
    public BigDecimal getBalanceEmail(String email);
    
    //checks if the user with the given username has enough balance to make a paymen of the given amount
    public boolean sufficientFunds(String username, double amount);
    
    /*calculates the new balance 
    * @param balance: the current balance
    * @param amount: amount to be taken or added to balance
    * @param m: defines whether we subtract or add to the balance (its "increase" or "decrease"
    *
    * @return: new balance 
    */
    public BigDecimal getNewBalance(BigDecimal balance, BigDecimal amount, String m);
    
    /* transfers money from sender to receiver
    * @param sender: email of the user making the payment
    * @param receiver: email of the user receiving the payment
    * @param amount: amount to be sent
    * @return: string "conflict" if 2 transactions are modifying a user's balance at the same time
    */
    public String makePayment(String sender, String receiver, double amount);
   
    /*converts from dollars or euros to pounds
    *@param am: amount to be converted
    *@param currency: the currency of the amount to be converted
    *@return: the amount after conversion in pounds
    */
    public BigDecimal getAmountinPounds(double am, String currency);
    
    /*converts from pounds to euros or dollars
    *@param am: amount to be converted
    *@param currency: the currency of the amount to be converted
    *@return: the amount after conversion in the given currency
    */
    public BigDecimal getAmountfromPounds(double am, String currency);
    
    /*requests payment from a user
    *@param requester: email of the user sending the request
    *@param u: email of the user receiving the request
    *@param amount: amount requested
    */
    public void requestPayment(String requester, String u, double amount);
    
    //selets the transactions with the given user email as receiver or sender of the transaction
    public List<Transactions> getTransactions(String email);
    
    //accepts and makes the payment of the transaction with the given id
    //@return: string "conflict" if 2 transactions are modifying a user's balance at the same time
    public String acceptRequest(Long transactionID);
    
    //rejects the request with the given id
    public void rejectRequest(Long transactionID);
    
    //find all the pending requests of the user with the given username
    public List<Transactions> findRequests(String username);
    
    //checks if the request made to the given username with the given id exists
    public boolean reqExists(Long reqid, String username);
}
