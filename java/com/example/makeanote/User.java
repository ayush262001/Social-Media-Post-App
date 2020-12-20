package com.example.makeanote;

public class User {
    private String userNameOrg;
    private String contactNumberOrg;
    private String imageIdOrg;

    public User(String userName, String contactNumber, String imageId){
        this.userNameOrg = userName;
        this.contactNumberOrg = contactNumber;
        this.imageIdOrg = imageId;
    }

    public String getUserName(){return userNameOrg;}
    public String getContactNumber(){return contactNumberOrg;}
    public String getImage_id(){return imageIdOrg;}
}
