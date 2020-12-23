/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sm848.ejb;

import com.sm848.entity.SystemUser;
import com.sm848.entity.SystemUserGroup;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author salmafikry
 */
@Singleton
@Startup
public class DefaultAdmin {
    
    @PersistenceContext
    EntityManager em;
    
    /**Method executed after dependency injection
     * adds a default admin to the database
     */
    @PostConstruct
    public void defAdmin(){
        try {
            
            //Initializes a new system user
            SystemUser admin = new SystemUser();
            //sets the username of the default admin
            admin.setUsername("admin1");
            
            //sets and hashes the password of the default admin
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String passwd = "admin1";
            md.update(passwd.getBytes("UTF-8"));
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String paswdToStoreInDB = bigInt.toString(16);
            admin.setUserpassword(paswdToStoreInDB);
            
            //stores the defalt admin in the "admins" group
            SystemUserGroup sys_user_group = new SystemUserGroup("admin1", "admins");
            
            //inserts the data into the database
            em.persist(admin);
            em.persist(sys_user_group);
            
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
