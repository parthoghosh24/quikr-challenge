package com.quikr.partho.quikrchallenge.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by partho on 22/8/15.
 */
public class Car{

    private String name;
    private String image;
    private double price;
    private String brand;
    private String type;
    private double rating;
    private String color;
    private String engineCC;
    private String mileage;
    private String absExist;
    private String description;
    private String link;
    private List<City> cities;
    private String carJson;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEngineCC() {
        return engineCC;
    }

    public void setEngineCC(String engineCC) {
        this.engineCC = engineCC;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getAbsExist() {
        return absExist;
    }

    public void setAbsExist(String absExist) {
        this.absExist = absExist;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public String getCarJson() {
        return carJson;
    }

    public void setCarJson(String carJson) {
        this.carJson = carJson;
    }

    public static class City
    {
        private String city;
        private int users;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getUsers() {
            return users;
        }

        public void setUsers(int users) {
            this.users = users;
        }


    }
}
