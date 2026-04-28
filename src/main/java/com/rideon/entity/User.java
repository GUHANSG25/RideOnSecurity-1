package com.rideon.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "user_name")
    @NotBlank(message = "User name is required")
    @Size(min = 3, max = 20, message = "User name must be between 3 and 20 characters")
    private String userName;

    @Column(name = "mobile", nullable = false, unique = true)
    @NotBlank(message = "Mobile number is required")
    @Size(min = 10, max = 10, message = "Mobile number should be exactly 10 numbers")
    private String mobile;

    @Email(message = "Invalid email format")
    @Column(name = "email")
    private String email;

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role is required")
    private Role userRole;

    @Column(name = "user_status")
    private String userStatus;

    @Column(name = "user_created_date")
    private LocalDate userCreatedDate;
    
    @JsonIgnore
    @OneToMany(mappedBy = "operator", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Bus> buses;

    public User() {}
    
    
    public User(String userName, String mobile, String email, Role userRole, String userStatus,
			LocalDate userCreatedDate) {
		super();
		this.userName = userName;
		this.mobile = mobile;
		this.email = email;
		this.userRole = userRole;
		this.userStatus = userStatus;
		this.userCreatedDate = userCreatedDate;
	}

	public int getUserId() { 
		return userId;
	}
	
    public void setUserId(int userId) { 
    	this.userId = userId;
    }

    public String getUserName() { 
    	return userName; 
    }
    public void setUserName(String userName) { 
    	this.userName = userName; 
    }

    public String getMobile() { 
    	return mobile; 
    }
    public void setMobile(String mobile) { 
    	this.mobile = mobile; 
    }

    public String getEmail() { 
    	return email; 
    }
    public void setEmail(String email) { 
    	this.email = email; 
    }

    public Role getUserRole() { 
    	return userRole; 
    }
    public void setUserRole(Role userRole) {
    	this.userRole = userRole; 
    }

    public String getUserStatus() { 
    	return userStatus; 
    }
    public void setUserStatus(String userStatus) { 
    	this.userStatus = userStatus;
    }

    public LocalDate getUserCreatedDate() { 
    	return userCreatedDate; 
    }
    
    public void setUserCreatedDate(LocalDate userCreatedDate) {
    	this.userCreatedDate = userCreatedDate; 
    }
    
    public List<Bus> getBuses() { return buses; }
    public void setBuses(List<Bus> buses) { this.buses = buses; }

}

