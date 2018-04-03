package com.companybest.ondra.engineerclickernew.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class User extends RealmObject {
    @PrimaryKey
    @JsonProperty("IdUser")
    private String idUser;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Email")
    private String email;

    @JsonProperty("Coins")
    private int coins;

    @JsonProperty("LastUpdateMaterial")
    private Long lastUpdateMaterial;

    @JsonProperty("LastTimeOutOfApp")
    private Long lastTimeOutOfApp;

    @JsonProperty("LastPayment")
    private Long lastPayment;

    @JsonIgnore
    private RealmList<UserMachine> mUserMachines;
    @JsonIgnore
    private RealmList<UserMaterial> materials;
    @JsonIgnore
    private RealmList<UserWorker> userWorkers;

    public User() {
        mUserMachines = new RealmList<>();
        materials = new RealmList<>();
        userWorkers = new RealmList<>();
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public Long getLastUpdateMaterial() {
        return lastUpdateMaterial;
    }

    public void setLastUpdateMaterial(Long lastUpdateMaterial) {
        this.lastUpdateMaterial = lastUpdateMaterial;
    }

    public Long getLastTimeOutOfApp() {
        return lastTimeOutOfApp;
    }

    public void setLastTimeOutOfApp(Long lastTimeOutOfApp) {
        this.lastTimeOutOfApp = lastTimeOutOfApp;
    }

    public Long getLastPayment() {
        return lastPayment;
    }

    public void setLastPayment(Long lastPayment) {
        this.lastPayment = lastPayment;
    }

    public RealmList<UserMachine> getUserMachines() {
        return mUserMachines;
    }

    public void setUserMachines(RealmList<UserMachine> userMachines) {
        this.mUserMachines = userMachines;
    }

    public void addMachine(UserMachine m) {
        mUserMachines.add(m);
    }

    public RealmList<UserMaterial> getMaterials() {
        return materials;
    }

    public void setMaterials(RealmList<UserMaterial> materials) {
        this.materials = materials;
    }

    public void addMaterial(UserMaterial material) {
        materials.add(material);
    }

    public RealmList<UserWorker> getUserWorkers() {
        return userWorkers;
    }

    public void setUserWorkers(RealmList<UserWorker> userWorkers) {
        this.userWorkers = userWorkers;
    }

    public void addWorker(UserWorker defaultWorker) {
        userWorkers.add(defaultWorker);
    }
}
