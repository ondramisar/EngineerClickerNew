package com.companybest.ondra.engineerclickernew.firebasePostModels;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPost {
    private String IdUser;
    private String Username;
    private String Email;
    private int Coins;
 //   private Long LastUpdateMaterial;
 //   private Long LastTimeOutOfApp;
  //  private Long LastPayment;

    public UserPost(String idUser, String username, String email, int coins, Long lastUpdateMaterial, Long lastTimeOutOfApp, Long lastPayment) {
        this.IdUser = idUser;
        this.Username = username;
        this.Email = email;
        this.Coins = coins;
       // this.LastUpdateMaterial = lastUpdateMaterial;
      //  this.LastTimeOutOfApp = lastTimeOutOfApp;
      //  this.LastPayment = lastPayment;
    }

    public UserPost(String idUser, String username, String email, int coins) {
        this.IdUser = idUser;
        this.Username = username;
        this.Email = email;
        this.Coins = coins;
        // this.LastUpdateMaterial = lastUpdateMaterial;
        //  this.LastTimeOutOfApp = lastTimeOutOfApp;
        //  this.LastPayment = lastPayment;
    }

}