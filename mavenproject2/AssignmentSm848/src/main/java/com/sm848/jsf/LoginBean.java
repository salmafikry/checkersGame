package com.sm848.jsf;

//import com.sm848.ejb.UserService;
import com.sm848.ejb.UserServiceInterface;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Bean for logging in and logging out users and admins 
 * @author salmafikry
 */
@Named
@RequestScoped
public class LoginBean implements Serializable {

    private String email;
    private String pw;
    @EJB
    UserServiceInterface userService;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    
    
    
    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            //this method will disassociate the principal from the session (effectively logging him/her out)
            request.logout();
            context.addMessage(null, new FacesMessage("User is logged out"));
            return "homee";
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage("Logout failed."));
            return null;
        }
        
    }
    
   public String login(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try
        {
            request.login(this.email, this.pw);
        }
        catch (ServletException e)
        {
            // If the password was incorrect or the email does not exist
            // notify user of eror by returning an error page
            return "error";
        }
        if (context.getExternalContext().isUserInRole("users"))
        {
            return "user";
        }
        else 
        {
            return "admin";
        }
        
    }
    
}
