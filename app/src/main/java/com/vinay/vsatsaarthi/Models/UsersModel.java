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
    public UsersModel(){}
}
