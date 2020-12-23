/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sm848.jsf;

import com.sm848.ejb.UserServiceInterface;
import com.sm848.entity.UserAccounts;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 * Submits payments and requests
 * @author salmafikry
 */
@Named
@RequestScoped
public class TransactionBean {
    @EJB
    UserServiceInterface usrSrv;
    
    
    private String receiver;
    private double amount;

    public TransactionBean() {
    }

    

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    public String submitPayment(){
        FacesContext context = FacesContext.getCurrentInstance();
        if(usrSrv.emailExists(receiver))
        {
            
            
            String senderusername = context.getExternalContext().getUserPrincipal().getName();
            UserAccounts u= usrSrv.getUserByUn(senderusername);
            String senderemail= u.getEmail();
            if(!senderemail.equals(receiver)){
            if(usrSrv.sufficientFunds(senderusername, amount))
            {
            
                String s= usrSrv.makePayment(getEmail(), receiver, amount);
                if(s.equals("success")){
                return "completed";}
                else{
                     context.addMessage(null, new FacesMessage("Please try again!"));
                    return null;
                }
            }
            else
            {
                context.addMessage(null, new FacesMessage("Insufficient Balance!"));
                return null;
            }}
            else{
                context.addMessage(null,  new FacesMessage("You cannot send a payment to yourself!"));
                return null;
            }
        }
        else
        {
            context.addMessage(null, new FacesMessage("Target Email does not exist"));
            return null;
        }
    }
    public void submitRequest(){
        FacesContext context = FacesContext.getCurrentInstance();
        if(usrSrv.emailExists(receiver))
        {
            String senderusername = context.getExternalContext().getUserPrincipal().getName();
            UserAccounts u= usrSrv.getUserByUn(senderusername);
            String senderemail= u.getEmail();
            
            if(!senderemail.equals(receiver)){
            usrSrv.requestPayment(getEmail(), receiver, amount);
            context.addMessage(null, new FacesMessage("Request sent successfully!"));
            }
            else{
                context.addMessage(null,  new FacesMessage("You cannot send a payment to yourself!"));
                
            }
        }
        else{
            context.addMessage(null, new FacesMessage("Target email does not exist!"));
        }
        
    }
    
    public String getEmail(){
        FacesContext context = FacesContext.getCurrentInstance();
        String senderusername = context.getExternalContext().getUserPrincipal().getName();
        UserAccounts u= usrSrv.getUserByUn(senderusername);
        return u.getEmail();
        
    }
    

    
    
    
}
