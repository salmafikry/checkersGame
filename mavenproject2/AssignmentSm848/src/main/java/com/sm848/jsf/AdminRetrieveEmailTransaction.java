/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sm848.jsf;

import com.sm848.ejb.UserServiceInterface;
import com.sm848.entity.Transactions;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * View transactions by a given user email
 * @author salmafikry
 */
@Named
@RequestScoped
public class AdminRetrieveEmailTransaction {
    // email of user to search for
    String email; 
    // transactions of selected user
    List<Transactions> userstransactions; 
    
    @EJB
    UserServiceInterface usrSrv;

    public AdminRetrieveEmailTransaction() {
    }
    

    public AdminRetrieveEmailTransaction(String username, List<Transactions> Usertransactions) {
        this.email = username;
        this.userstransactions = Usertransactions;
    }
    
    

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Transactions> getUserstransactions() {
        return userstransactions;
    }

    public void setUserstransactions(List<Transactions> userstransactions) {
        this.userstransactions = userstransactions;
    }
    public void findUserTransactions(){
        if(usrSrv.emailExists(email)){
             userstransactions=usrSrv.getTransactions(email);
        }
        else
        {
            //otherwise if the username does not exist
            //notify the administrator of the error and return the same view
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Email entered does not match any user"));
        }
    }
}
