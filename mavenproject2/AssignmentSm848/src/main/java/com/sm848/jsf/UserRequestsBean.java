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
 * Displays, accepts, and rejects requests.
 * @author salmafikry
 */
@Named
@RequestScoped
public class UserRequestsBean {
     @EJB
     UserServiceInterface usrServ;
     
     Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserRequestsBean() {
    }
    public List<Transactions> getRequests(){
        FacesContext context = FacesContext.getCurrentInstance();
        String name = context.getExternalContext().getUserPrincipal().getName();
        return usrServ.findRequests(name);
    }
    
    public void acceptRequestID(){
        FacesContext context = FacesContext.getCurrentInstance();
        String name = context.getExternalContext().getUserPrincipal().getName();
        if (usrServ.reqExists(id, name)){
            String s=usrServ.acceptRequest(id);
            if(s.equals("success")){
                context.addMessage(null, new FacesMessage("Payment Successful"));}
                else{
                     context.addMessage(null, new FacesMessage("Please try again!"));
                    
                }
        
        
        }
        else{
        context.addMessage(null, new FacesMessage("Request with the given id does not exist"));
        }
        
    }
    public void rejectRequestID(){
        FacesContext context = FacesContext.getCurrentInstance();
        String name = context.getExternalContext().getUserPrincipal().getName();
        if (usrServ.reqExists(id, name)){
        usrServ.rejectRequest(id);
        context.addMessage(null, new FacesMessage("Request number " + id + " has been rejected"));
        }
        else{
            context.addMessage(null, new FacesMessage("Request with the given id does not exist"));
        }
    }
}
