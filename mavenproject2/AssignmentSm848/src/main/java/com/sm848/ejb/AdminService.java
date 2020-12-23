/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sm848.ejb;


import com.sm848.entity.SystemUser;
import com.sm848.entity.SystemUserGroup;
import com.sm848.entity.Transactions;
import com.sm848.entity.UserAccounts;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * implements admin functionality
 * @author salmafikry
 */

@DeclareRoles({"admins"})
@Stateless
public class AdminService implements AdminInterface {
    
    @PersistenceContext
    EntityManager em;
    
    public AdminService(){
    }
    
    @RolesAllowed("admins")
    @Override
    public void addAdmin(String username, String password)
    {
        try
        {
            //Hash the password before storing it in the database 
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String passwd = password;
            md.update(passwd.getBytes("UTF-8"));
            byte[] digest = md.digest();
            StringBuffer sb = new StringBuffer();
            
            for (int i = 0; i < digest.length; i++) 
            {
                sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }
            String paswdToStoreInDB = sb.toString();
            
            SystemUser sys_usr = new SystemUser(username,paswdToStoreInDB);
            SystemUserGroup sys_group = new SystemUserGroup(username, "admins");
            
            em.persist(sys_usr);
            em.persist(sys_group);
        }
        catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) 
        {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @RolesAllowed("admins")
    @Override
    public List<Transactions> getAllTransaction()
    {
        //run query that selects all the transactions from the database
        Query query= em.createNamedQuery("findAllTrans");
        //store the query results in a list 
        List<Transactions> allTransactions= query.getResultList();
        return allTransactions;
    }
    

    @RolesAllowed("admins")
    @Override
    public List<UserAccounts> getAllUsers(){
        //run query that selects all the users from the database
        Query query=em.createNamedQuery("findUsers");
        //store the query results in a list 
        List<UserAccounts> allUsers= query.getResultList();
        return allUsers;
    }
    
    @RolesAllowed("admins")
    @Override
    public boolean unAdminExists(String username){
        //run query that selects the entry with the given username
        Query query = em.createNamedQuery("findAdmin");
        //set the parameter of the query to the username of the admin we're looking for
        query.setParameter("un",username);
        //if the result list is empty then the admin with the given username does not exist 
        return !query.getResultList().isEmpty();
    }
    
    
    
    
}
