package com.companybest.ondra.engineerclickernew.models;

import android.util.Log;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class User extends RealmObject {
    @PrimaryKey
    private  String idUser;
    private String name;
    private String email;
    private int coins;
    private RealmList<Machine> machines;
    private RealmList<Material> mMaterials;

    public User() {
        machines = new RealmList<>();
        mMaterials = new RealmList<>();
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

    public RealmList<Machine> getMachines() {
        return machines;
    }

    public void setMachines(RealmList<Machine> machines) {
        this.machines = machines;
    }

    public void addMachine(Machine m ){
        if (machines == null){
            Log.i("usern", "WTF WTF");
        }else {
            machines.add(m);
        }
    }

    public RealmList<Material> getMaterials() {
        return mMaterials;
    }

    public void setMaterials(RealmList<Material> materials) {
        mMaterials = materials;
    }

    public void addMaterial(Material material){
        mMaterials.add(material);
    }
}
