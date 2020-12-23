/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sm848.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entity mapped to a table in the database that
 * stores transaction payments and requests.
 * @author salmafikry
 */

@NamedQuery(name="findAllTrans", query="SELECT t FROM Transactions t ")
@NamedQuery(name="findTransactionStatus", query="SELECT t.transStatus FROM Transactions t WHERE t.transID = :id")
@NamedQuery(name="updateStat", query="UPDATE Transactions t SET t.transStatus =:status WHERE t.transID = :id")
@NamedQuery(name="getTransaction", query="SELECT t FROM Transactions t WHERE t.transID= :id")
@NamedQuery(name="getUserTransaction", query="SELECT t FROM Transactions t WHERE t.sender= :s OR t.receiver= :r ")
@NamedQuery(name="findTransactionRequests", query="SELECT t FROM Transactions t WHERE t.transStatus= :p AND t.sender= :em ")
@NamedQuery(name="findTransReqwithID", query="SELECT t FROM Transactions t WHERE t.transStatus= :p AND t.sender= :em AND t.transID= :id")
@Entity
@Table(name="Transactions")
public class Transactions implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transID;
    
    private String sender;
    
    private String receiver;
    
    private String transStatus;
    
    private BigDecimal transAmount;
    
    private String amountCurrency;
    
    
    
    public Transactions(){
        
    }

    public Transactions(String sender, String receiver, String transStatus, BigDecimal transAmount, String amountCurrency) {
        
        this.sender = sender;
        this.receiver = receiver;
        this.transStatus = transStatus;
        this.transAmount = transAmount;
        this.amountCurrency = amountCurrency;
        
    }

    

    public Long getTransID() {
        return transID;
    }

    public void setTransID(Long transID) {
        this.transID = transID;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    public BigDecimal getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(BigDecimal transAmount) {
        this.transAmount = transAmount;
    }

    public String getAmountCurrency() {
        return amountCurrency;
    }

    public void setAmountCurrency(String amountCurrency) {
        this.amountCurrency = amountCurrency;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.transID);
        hash = 29 * hash + Objects.hashCode(this.sender);
        hash = 29 * hash + Objects.hashCode(this.receiver);
        hash = 29 * hash + Objects.hashCode(this.transStatus);
        hash = 29 * hash + Objects.hashCode(this.transAmount);
        hash = 29 * hash + Objects.hashCode(this.amountCurrency);
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
        final Transactions other = (Transactions) obj;
        if (!Objects.equals(this.sender, other.sender)) {
            return false;
        }
        if (!Objects.equals(this.receiver, other.receiver)) {
            return false;
        }
        if (!Objects.equals(this.transStatus, other.transStatus)) {
            return false;
        }
        if (!Objects.equals(this.transAmount, other.transAmount)) {
            return false;
        }
        if (!Objects.equals(this.amountCurrency, other.amountCurrency)) {
            return false;
        }
        if (!Objects.equals(this.transID, other.transID)) {
            return false;
        }
        return true;
    }
    
    
    
}
