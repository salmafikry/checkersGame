package com.sm848.jsf;


import com.sm848.ejb.UserServiceInterface;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Bean for registering new users 
 * @author parisis
 */
@Named
@RequestScoped
public class RegistrationBean {

    @EJB
    UserServiceInterface usrSrv;

    String email;

    
    String username;
    String userpassword;
    String name;
    String surname;
    String phoneNum;
    String currency;
    Date dob;
    String cpsw;

    public String getCpsw() {
        return cpsw;
    }

    public void setCpsw(String cpsw) {
        this.cpsw = cpsw;
    }

    public RegistrationBean() {

    }

    
    //call the injected EJB
    public String register(){
        if (userpassword.equals(cpsw))
        {
             if(!usrSrv.emailExists(this.email)){
               
               if(!usrSrv.usernameExists(this.username))
               {
                   usrSrv.registerUser(email, username, userpassword,name, surname, phoneNum, currency, dob);
                   FacesContext context = FacesContext.getCurrentInstance();
                   context.addMessage(null, new FacesMessage("Succesfully registered"));
                   return "index";
                   
             }
               else {
                   FacesContext context = FacesContext.getCurrentInstance();
                   context.addMessage(null, new FacesMessage("User Already Exists!"));
                   return null;
                   
               }
             }
             else{
                 FacesContext context = FacesContext.getCurrentInstance();
                  context.addMessage(null, new FacesMessage("Email Already Exists!"));
                  return null;
                  
             }
           
           
    }
        
        else
        {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Passwords do not match"));
            return null;
        } 
    }

    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }
    
}
