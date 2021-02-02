package com.flipkart.bean;

import com.flipkart.constants.UserType;

/**
 * @author pooja
 */
public class User {
    String username;
    int userId;
    String emailId;
    String password;
    UserType userType;

    public User()
    {
    	
    }
    
    public User(String username, int userId, String emailId, String password, UserType userType) {
        this.username = username;
        this.userId = userId;
        this.emailId = emailId;
        this.password = password;
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
