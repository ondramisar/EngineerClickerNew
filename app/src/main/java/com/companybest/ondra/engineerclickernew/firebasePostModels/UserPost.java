package com.companybest.ondra.engineerclickernew.firebasePostModels;

public class UserPost {
    public String idUser;
    public String name;
    public String email;
    public int coins;
    public Long lastUpdateMaterial;
    public Long lastTimeOutOfApp;
    public Long lastPayment;

    public UserPost(String idUser, String username, String email, int coins, Long lastUpdateMaterial, Long lastTimeOutOfApp, Long lastPayment) {
        this.idUser = idUser;
        this.name = username;
        this.email = email;
        this.coins = coins;
        this.lastUpdateMaterial = lastUpdateMaterial;
        this.lastTimeOutOfApp = lastTimeOutOfApp;
        this.lastPayment = lastPayment;
    }

}