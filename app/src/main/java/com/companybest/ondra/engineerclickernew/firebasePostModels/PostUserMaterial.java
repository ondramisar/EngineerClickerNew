package com.companybest.ondra.engineerclickernew.firebasePostModels;

public class PostUserMaterial {
    public String iD;
    public String name;
    public int value;
    public int numberOf;
    public String defaultMaterialId;
    public String idUser;

    public PostUserMaterial(String id, String name, int value, int numberOf, String defaultMaterialId, String userId) {
        this.iD = id;
        this.name = name;
        this.value = value;
        this.idUser = userId;
        this.defaultMaterialId = defaultMaterialId;
        this.numberOf = numberOf;
    }
}

