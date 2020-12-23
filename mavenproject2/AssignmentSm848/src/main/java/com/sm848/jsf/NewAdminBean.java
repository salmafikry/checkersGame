/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sm848.jsf;

import com.sm848.ejb.AdminInterface;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Bean for adding new admins
 * @author salmafikry
 */
@Named
@RequestScoped
public class NewAdminBean {
    @EJB
    AdminInterface admSrv;
    
    String password;
    String confirmpw;
    String username;
    
    public NewAdminBean(){
        
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmpw() {
        return confirmpw;
    }

    public void setConfirmpw(String confirmpw) {
        this.confirmpw = confirmpw;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    
    public String addAdmin(){
        if (password.equals(confirmpw)){
            if(!admSrv.unAdminExists(this.username)){
                admSrv.addAdmin(this.username, this.password);
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("Admin added successfully"));
                return "admin";
            }
            else{
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("Admin Already Exists!"));
                return null;
            }
        }
        else{
            FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("Password do not match"));
                return null;
        }
    }
    
}
