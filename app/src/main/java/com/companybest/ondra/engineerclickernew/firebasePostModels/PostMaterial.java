package com.companybest.ondra.engineerclickernew.firebasePostModels;


public class PostMaterial extends Object {
    public String iD;
    public String name;
    public int value;
    public int numberOf;
    public String idUser;

    public PostMaterial(String id, String name, int value, int numberOf, String userId) {
        this.iD = id;
        this.name = name;
        this.value = value;
        this.idUser = userId;
        this.numberOf = numberOf;
    }
}
