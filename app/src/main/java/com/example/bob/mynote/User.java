package com.example.bob.mynote;

/**
 * Created by Bob on 2017/8/26.
 */

public class User {
    private String account;
    private String password;
    User(String account,String password){
        this.account = account;
        this.password = password;
    }
    public void setAccount(String account){
        this.account = account;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getAccount(){
        return account;
    }
    public String getPassword(){
        return password;
    }
}
