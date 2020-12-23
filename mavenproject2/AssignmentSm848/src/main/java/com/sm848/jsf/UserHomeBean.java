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
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Display the logged in user's home page
 * @author salmafikry
 */
@Named
@RequestScoped
public class UserHomeBean{
     @EJB
     UserServiceInterface usrServ;
     
     public UserHomeBean(){
         
     }
     public String helloUser(){
         FacesContext context = FacesContext.getCurrentInstance();
         String name = context.getExternalContext().getUserPrincipal().getName();
         UserAccounts u= usrServ.getUserByUn(name);
         
         return "Hello " + u.getName() + "!";
         
     }
     
    
     
     public String dispBalance(){
         FacesContext context = FacesContext.getCurrentInstance();
         String name = context.getExternalContext().getUserPrincipal().getName();
         UserAccounts u= usrServ.getUserByUn(name);
         
         return "Current Balance: " + u.getBalance() + u.getCurrency();
     }
     
}
