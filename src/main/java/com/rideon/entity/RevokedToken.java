package com.rideon.entity;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;


import java.util.Date;

@Entity
public class RevokedToken {
    @Id
    private String token;
    private Date revokedAt;
    
    public RevokedToken() {
    	super();
    }
    
    public RevokedToken(String token) {
        this.token = token;
        this.revokedAt = new Date();
    }

	public RevokedToken(String token, Date revokedAt) {
		super();
		this.token = token;
		this.revokedAt = revokedAt;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getRevokedAt() {
		return revokedAt;
	}

	public void setRevokedAt(Date revokedAt) {
		this.revokedAt = revokedAt;
	}
    
    
}
