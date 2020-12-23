/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sm848.ejb;

import com.sm848.entity.Transactions;
import com.sm848.entity.UserAccounts;
import java.util.List;

/**
 *
 * @author salmafikry
 */
public interface AdminInterface {
    
    //adds a new admin
    public void addAdmin(String username, String password);
    
    //Check if admin already exists
    public boolean unAdminExists(String username);
    
    //Get a list of all existing transactions
    public List<Transactions> getAllTransaction();
    
    //Get a list of all existing users
    public List<UserAccounts> getAllUsers();
}
