/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sm848.jsf;


import com.sm848.ejb.AdminInterface;
import com.sm848.entity.Transactions;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

import javax.inject.Named;

/**
 * View all existing transactions of all users
 * @author salmafikry
 */
@Named
@RequestScoped
public class AdminViewTransactions {
     @EJB
    AdminInterface admnSrv;
     
     public AdminViewTransactions(){
         
     }
     public List<Transactions> gettransList(){
        return admnSrv.getAllTransaction();
       
    }
}
