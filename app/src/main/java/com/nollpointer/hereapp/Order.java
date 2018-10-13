package com.nollpointer.hereapp;

import com.here.android.mpa.common.GeoCoordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Order {
    private String address;
    private TreeMap<String,Integer> products;

    private GeoCoordinate coordinates;

    public Order(String address, GeoCoordinate coordinates, TreeMap<String, Integer> products) {
        this.address = address;
        this.products = products;
        this.coordinates = coordinates;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public TreeMap<String, Integer> getProducts() {
        return products;
    }

    public void setProducts(TreeMap<String, Integer> products) {
        this.products = products;
    }

    public GeoCoordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(GeoCoordinate coordinates) {
        this.coordinates = coordinates;
    }
}
