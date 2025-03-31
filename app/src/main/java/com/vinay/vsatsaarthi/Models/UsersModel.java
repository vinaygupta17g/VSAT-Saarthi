package com.vinay.vsatsaarthi.Models;

public class UsersModel {
    String userid,username,usermobile,useremail,userpassword;

    public UsersModel(String userid, String username, String usermobile, String useremail, String userpassword) {
        this.userid = userid;
        this.username = username;
        this.usermobile = usermobile;
        this.useremail = useremail;
        this.userpassword = userpassword;
    }
    public UsersModel(String username, String usermobile, String useremail, String userpassword) {
        this.username = username;
        this.usermobile = usermobile;
        this.useremail = useremail;
        this.userpassword = userpassword;
    }

    public UsersModel(){}

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsermobile() {
        return usermobile;
    }

    public void setUsermobile(String usermobile) {
        this.usermobile = usermobile;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }
}
