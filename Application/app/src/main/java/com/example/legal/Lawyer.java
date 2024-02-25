package com.example.legal;

public class Lawyer {

    private String userId;
    private String name;
    private String money;
    private String officeAddress;
    private String barCouncilNumber;
    private String photoUrl;
    private String fcmToken;
    private  String lawyerType;




    public Lawyer() {

    }

    public Lawyer(String userId, String name, String money, String officeAddress, String barCouncilNumber, String photoUrl, String fcmToken, String lawyerType) {
        this.userId = userId;
        this.name = name;
        this.money = money;
        this.officeAddress = officeAddress;
        this.barCouncilNumber = barCouncilNumber;
        this.photoUrl = photoUrl;
        this.fcmToken = fcmToken;
        this.lawyerType= lawyerType;

    }

    public String getUserId() {
        return userId;
    }
public String getLawyerType(){
        return lawyerType;
}


    public String getName() {
        return name;
    }

    public String getMoney() {
        return money;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public String getBarCouncilNumber() {
        return barCouncilNumber;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getFcmToken() {
        return fcmToken;
    }


}
