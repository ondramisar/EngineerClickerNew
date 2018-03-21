package com.companybest.ondra.engineerclickernew.firebasePostModels;


public class PostMachine {
    public String ID;
    public String Name;
    public int TimeToReach;
    public int NumberOfMaterialsToGive;
    public String IdMaterialToGive;
    public int Lvl;
    public String WorkerId;
    public String UserId;

    public PostMachine(String id, String name, int timeToReach, int numberOfMaterialsToGive, String idMaterialToGive, int lvl, String workerId, String userId) {
        this.ID = id;
        this.Name = name;
        this.TimeToReach = timeToReach;
        this.IdMaterialToGive = idMaterialToGive;
        this.UserId = userId;
        this.Lvl = lvl;
        this.NumberOfMaterialsToGive = numberOfMaterialsToGive;
        this.WorkerId = workerId;
    }
}
