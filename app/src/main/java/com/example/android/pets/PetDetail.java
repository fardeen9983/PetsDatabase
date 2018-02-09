package com.example.android.pets;



public class PetDetail {

    private int _id;
    private String name;
    private String breed;
    private int gender;
    private int weight;

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
}
