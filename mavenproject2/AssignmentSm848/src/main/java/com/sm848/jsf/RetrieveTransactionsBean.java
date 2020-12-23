/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sm848.jsf;

import com.sm848.ejb.UserServiceInterface;
import com.sm848.entity.Transactions;
import com.sm848.entity.UserAccounts;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Gets transactions of the logged in user
 * @author salmafikry
 */
@Named
@RequestScoped
public class RetrieveTransactionsBean {
    @EJB
    UserServiceInterface usrSrv;
    
    
    
   

    
    public RetrieveTransactionsBean(){
        
    }
    
    public List<Transactions> gettransList(){
        FacesContext context = FacesContext.getCurrentInstance();
        String senderusername = context.getExternalContext().getUserPrincipal().getName();
        UserAccounts us= usrSrv.getUserByUn(senderusername);
       
        return usrSrv.getTransactions(us.getEmail());
       
    }
    
    
    
          
}
