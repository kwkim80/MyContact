package ca.algonquin.kw2446.contactsapp;

import java.util.Date;

public class Contact {

    private int cId;
    private String name;
    private String email;
    private String phone;
    private String userEmail;
    private String createDate;

    public Contact(int cId, String name, String email, String phone, String userEmail, String createDate) {
        this.cId=cId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.userEmail = userEmail;
        this.createDate = createDate;
    }

    public Contact() {
        this(0,null,null,null,null,null);
    }

    public Contact(String name, String email, String phone){
        this(0,name,email,phone,null,null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

}
