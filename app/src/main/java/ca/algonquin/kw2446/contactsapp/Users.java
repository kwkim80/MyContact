package ca.algonquin.kw2446.contactsapp;

public class Users {
    private int userId;
    private String userName;
    private String userEmail;
    private String userPwd;
    private String regDate;

    public Users() {
        this(0,null,null,null,null);
    }

    public Users(int id, String userName, String userEmail, String userPwd, String regDate) {
        this.userId=id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPwd = userPwd;
        this.regDate = regDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
