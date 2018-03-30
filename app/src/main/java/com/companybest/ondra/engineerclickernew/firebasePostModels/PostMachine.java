package com.companybest.ondra.engineerclickernew.firebasePostModels;


public class PostMachine {
    public String iD;
    public String name;
    public int timeToReach;
    public int numberOfMaterialsToGive;
    public String idMaterialToGive;
    public int lvl;
    public String workerId;
    public String idUser;

    public PostMachine(String id, String name, int timeToReach, int numberOfMaterialsToGive, String idMaterialToGive, int lvl, String workerId, String userId) {
        this.iD = id;
        this.name = name;
        this.timeToReach = timeToReach;
        this.idMaterialToGive = idMaterialToGive;
        this.idUser = userId;
        this.lvl = lvl;
        this.numberOfMaterialsToGive = numberOfMaterialsToGive;
        this.workerId = workerId;
    }
}
