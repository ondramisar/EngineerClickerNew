package com.companybest.ondra.engineerclickernew.firebasePostModels;


public class PostWorker {
    public String iD;
    public String name;
    public float timeCutBy;
    public int materialMultiplayer;
    public int payment;
    public int lvl;
    public boolean isOnMachine;
    public String idUser;

    public PostWorker(String id, String name, float timeToReach, int materialMultiplayer, int payment, int lvl, boolean isOnMachine, String userId) {
        this.iD = id;
        this.name = name;
        this.timeCutBy = timeToReach;
        this.payment = payment;
        this.idUser = userId;
        this.lvl = lvl;
        this.materialMultiplayer = materialMultiplayer;
        this.isOnMachine = isOnMachine;
    }
}
