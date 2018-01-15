package com.companybest.ondra.engineerclickernew.firebasePostModels;


public class PostMachine {
    public String name;
    public int timeToReach;
    public String idMaterialToGive;

    public PostMachine() {
    }

    public PostMachine(String name, int timeToReach, String idMaterialToGive) {
        this.name = name;
        this.timeToReach = timeToReach;
        this.idMaterialToGive = idMaterialToGive;
    }
}
