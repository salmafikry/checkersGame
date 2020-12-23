/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sm848.jsf;

import com.sm848.ejb.AdminInterface;
import com.sm848.entity.UserAccounts;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Display the logged in admin's home page.
 * @author salmafikry
 */
@Named
@RequestScoped
public class AdminHomeBean {
   @EJB
    AdminInterface admSrv;
   
   public AdminHomeBean(){
       
   }
   
   
   public String helloAdmin(){
       FacesContext context = FacesContext.getCurrentInstance();
       String name = context.getExternalContext().getUserPrincipal().getName();
       return "Hello " + name + "!";
   }
}
