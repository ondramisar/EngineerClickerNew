package com.companybest.ondra.engineerclickernew.firebasePostModels;



public class PostMaterial extends Object {
    private String id;
        private int numberOf;

    public PostMaterial() {
    }

    public PostMaterial(String id, int numberOf) {
        this.id = id;
        this.numberOf = numberOf;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumberOf() {
        return numberOf;
    }

    public void setNumberOf(int numberOf) {
        this.numberOf = numberOf;
    }
}
