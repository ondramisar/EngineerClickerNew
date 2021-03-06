package com.companybest.ondra.engineerclickernew.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class User extends RealmObject {
    @PrimaryKey
    private String idUser;
    private String name;
    private String email;
    private int coins;
    private Long lastUpdateMaterial;
    private Long lastTimeOutOfApp;
    private Long lastPayment;
    private RealmList<Machine> machines;
    private RealmList<Material> mMaterials;
    private RealmList<Worker> mWorkers;

    public User() {
        machines = new RealmList<>();
        mMaterials = new RealmList<>();
        mWorkers = new RealmList<>();
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

    public RealmList<Machine> getMachines() {
        return machines;
    }

    public void setMachines(RealmList<Machine> machines) {
        this.machines = machines;
    }

    public void addMachine(Machine m) {
        machines.add(m);
    }

    public RealmList<Material> getMaterials() {
        return mMaterials;
    }

    public void setMaterials(RealmList<Material> materials) {
        mMaterials = materials;
    }

    public void addMaterial(Material material) {
        mMaterials.add(material);
    }

    public RealmList<Worker> getWorkers() {
        return mWorkers;
    }

    public void setWorkers(RealmList<Worker> workers) {
        mWorkers = workers;
    }

    public void addWorker(Worker worker) {
        mWorkers.add(worker);
    }
}
