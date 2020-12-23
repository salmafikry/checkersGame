package com.sm848.entity;

import com.sm848.ejb.UserService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.ejb.EJB;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

/**
 * Entity mapped to a table in the database that
 * stores the details of the registered users.
 * @author salmafikry
 */



@NamedQuery(name="findUn", query="SELECT u FROM UserAccounts u WHERE u.username= :un")
@NamedQuery(name="findUserEm", query="SELECT u FROM UserAccounts u WHERE u.email= :em")
@NamedQuery(name="findBal", query="SELECT u.balance FROM UserAccounts u WHERE u.id.username= :un")
@NamedQuery(name="findBalanceByEm", query="SELECT u.balance FROM UserAccounts u WHERE u.email= :em")
@NamedQuery(name="findUsers", query="SELECT u FROM UserAccounts u ")
@NamedQuery(name="updateBal", query="UPDATE UserAccounts u SET u.balance =:bal WHERE u.email = :em")

@Entity
@Table(name="UserAccounts")
public class UserAccounts implements Serializable {
    
    

    @Id
    //one to one relationship with the SystemUSer table
    @OneToOne
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name="USER_ID")
    private SystemUser id;
    
    @Version
    private Long version;
    
    //balance stores to 2 decimnal places
    @Column(precision = 10, scale = 2)
    private BigDecimal balance;
    
    //users cannot have the same email address
    @Column(unique=true)
    private String email;
   
    //users cannot have the same username
    @Column(unique=true)
    private String username;
    
    private String userpassword;
    
    

    
    private String name;

    
    private String surname;
    
    
    private String phoneNum;
    
    
    private String currency;
    
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dob;

    public UserAccounts() {
    }

    /*Constructor
    @param id: user id
    @param balance: the balance of the user
    @param email: the email of the user
    @param username: the username of the user
    @param userpassword: the password of the user
    @param name: first name of the username
    @param surname: last name of the user
    @param phoneNum: mobile number of the user
    @param currency: the currency of the user's account
    @param dob: date of birth of the user
    */
    public UserAccounts(SystemUser id, BigDecimal balance, String email, String username, String userpassword, String name, String surname, String phoneNum, String currency, Date dob) {
        this.id = id;
        this.balance = balance;
        this.email = email;
        this.username = username;
        this.userpassword = userpassword;
        this.name = name;
        this.surname = surname;
        this.phoneNum = phoneNum;
        this.currency = currency;
        this.dob = dob;
    }
    

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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

    public SystemUser getId() {
        return id;
    }

    public void setId(SystemUser id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.id);
        hash = 19 * hash + Objects.hashCode(this.balance);
        hash = 19 * hash + Objects.hashCode(this.email);
        hash = 19 * hash + Objects.hashCode(this.username);
        hash = 19 * hash + Objects.hashCode(this.userpassword);
        hash = 19 * hash + Objects.hashCode(this.name);
        hash = 19 * hash + Objects.hashCode(this.surname);
        hash = 19 * hash + Objects.hashCode(this.phoneNum);
        hash = 19 * hash + Objects.hashCode(this.currency);
        hash = 19 * hash + Objects.hashCode(this.dob);
        hash = 19 * hash + Objects.hashCode(this.version);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserAccounts other = (UserAccounts) obj;
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.userpassword, other.userpassword)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.surname, other.surname)) {
            return false;
        }
        if (!Objects.equals(this.phoneNum, other.phoneNum)) {
            return false;
        }
        if (!Objects.equals(this.currency, other.currency)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.balance, other.balance)) {
            return false;
        }
        if (!Objects.equals(this.dob, other.dob)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        return true;
    }

    

    

}
