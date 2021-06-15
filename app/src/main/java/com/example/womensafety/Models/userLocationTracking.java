package com.example.womensafety.Models;

import android.graphics.Bitmap;
import android.net.Uri;

public class userLocationTracking {

    String travelling_from,travelling_to;
    String vehicle_number;
    String actual_date;
    String estimated_time,actual_time;
    String vehicle_image;
    Bitmap map_bitmap;

    public userLocationTracking(String travelling_from, String travelling_to, String vehicle_number, String estimated_time, String actual_time, String vehicle_image, Bitmap map_bitmap) {
        this.travelling_from = travelling_from;
        this.travelling_to = travelling_to;
        this.vehicle_number = vehicle_number;
        this.estimated_time = estimated_time;
        this.actual_time = actual_time;
        this.vehicle_image = vehicle_image;
        this.map_bitmap = map_bitmap;
    }

    public userLocationTracking() {
    }

    public userLocationTracking(String travelling_from, String travelling_to, String vehicle_number, String estimated_time, String actual_time,String actual_date, String vehicle_image) {
        this.travelling_from = travelling_from;
        this.travelling_to = travelling_to;
        this.vehicle_number = vehicle_number;
        this.estimated_time = estimated_time;
        this.actual_time = actual_time;
        this.actual_date=actual_date;
        this.vehicle_image = vehicle_image;
    }

    public String getTravelling_from() {
        return travelling_from;
    }

    public void setTravelling_from(String travelling_from) {
        this.travelling_from = travelling_from;
    }

    public String getTravelling_to() {
        return travelling_to;
    }

    public void setTravelling_to(String travelling_to) {
        this.travelling_to = travelling_to;
    }

    public String getVehicle_number() {
        return vehicle_number;
    }

    public void setVehicle_number(String vehicle_number) {
        this.vehicle_number = vehicle_number;
    }

    public String getEstimated_time() {
        return estimated_time;
    }

    public void setEstimated_time(String estimated_time) {
        this.estimated_time = estimated_time;
    }

    public String getActual_time() {
        return actual_time;
    }

    public void setActual_time(String actual_time) {
        this.actual_time = actual_time;
    }

    public String getVehicle_image() {
        return vehicle_image;
    }

    public void setVehicle_image(String vehicle_image) {
        this.vehicle_image = vehicle_image;
    }

    public Bitmap getMap_bitmap() {
        return map_bitmap;
    }

    public void setMap_bitmap(Bitmap map_bitmap) {
        this.map_bitmap = map_bitmap;
    }

    public String getActual_date() {
        return actual_date;
    }

    public void setActual_date(String actual_date) {
        this.actual_date = actual_date;
    }
}
