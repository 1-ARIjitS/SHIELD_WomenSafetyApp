package com.example.womensafety.Models;

public class kin_registered {
    String name;
    String mobile_number;

    public kin_registered(String name, String mobile_number) {
        this.name = name;
        this.mobile_number = mobile_number;
    }

    public kin_registered() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }
}
