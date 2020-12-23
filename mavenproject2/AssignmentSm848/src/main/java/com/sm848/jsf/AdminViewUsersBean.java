/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sm848.jsf;

import com.sm848.ejb.AdminInterface;

import com.sm848.entity.UserAccounts;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * View all registered users
 * @author salmafikry
 */
@Named
@RequestScoped
public class AdminViewUsersBean {
    @EJB
    AdminInterface admnSrv;
     
     public AdminViewUsersBean(){
         
     }
     public List<UserAccounts> findUsers(){
        return admnSrv.getAllUsers();
       
    }
}
