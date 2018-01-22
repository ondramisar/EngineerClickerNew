package com.companybest.ondra.engineerclickernew.firebasePostModels;


public class PostMachine {
    public String name;
    public int timeToReach;
    public String idMaterialToGive;
    public String userId;
    public int lvl;

    public PostMachine() {
    }

    public PostMachine(String name, int timeToReach, String idMaterialToGive, String userId, int lvl) {
        this.name = name;
        this.timeToReach = timeToReach;
        this.idMaterialToGive = idMaterialToGive;
        this.userId = userId;
        this.lvl = lvl;
    }
}
