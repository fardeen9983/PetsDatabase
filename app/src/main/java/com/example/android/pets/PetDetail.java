package com.example.android.pets;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class PetDetail implements Serializable {

    private int _id;
    private String name;
    private String breed;
    private int gender;
    private int weight;

    public PetDetail() {
    }

    public PetDetail(int _id, String name, String breed, int gender, int weight) {
        this._id = _id;
        this.name = name;
        this.breed = breed;
        this.gender = gender;
        this.weight = weight;
    }

    public int get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public int getGender() {
        return gender;
    }

    public int getWeight() {
        return weight;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        String string = _id + " " + name + " " + breed + " " + gender + " " + weight + "\n";
        return string;
    }

}
