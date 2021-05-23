package com.example.womensafety.SuperAdmin.Models;

public class Admins {
    String r_name, r_age, r_address, r_email, r_mobile, r_gender, r_password, r_country, r_state, r_city;

   public Admins(String r_name, String r_age, String r_address, String r_email, String r_mobile, String r_gender, String r_password) {
        this.r_name = r_name;
        this.r_age = r_age;
        this.r_address = r_address;
        this.r_email = r_email;
        this.r_mobile = r_mobile;
        this.r_gender = r_gender;
        this.r_password = r_password;
    }
    public Admins(String r_name, String r_age, String r_address, String r_email, String r_mobile, String r_gender, String r_password,String r_country,String r_state,String r_city) {
        this.r_name = r_name;
        this.r_age = r_age;
        this.r_address = r_address;
        this.r_email = r_email;
        this.r_mobile = r_mobile;
        this.r_gender = r_gender;
        this.r_password = r_password;
        this.r_country = r_country;
        this.r_state = r_state;
        this.r_city = r_city;
    }

    public Admins() {
    }

    public String getR_name() {
        return r_name;
    }

    public void setR_name(String r_name) {
        this.r_name = r_name;
    }

    public String getR_age() {
        return r_age;
    }

    public void setR_age(String r_age) {
        this.r_age = r_age;
    }

    public String getR_address() {
        return r_address;
    }

    public void setR_address(String r_address) {
        this.r_address = r_address;
    }

    public String getR_gender() {
        return r_gender;
    }

    public void setR_gender(String r_gender) {
        this.r_gender = r_gender;
    }

    public String getR_email() {
        return r_email;
    }

    public void setR_email(String r_email) {
        this.r_email = r_email;
    }

    public String getR_mobile() {
        return r_mobile;
    }

    public void setR_mobile(String r_mobile) {
        this.r_mobile = r_mobile;
    }

    public String getR_password() {
        return r_password;
    }

    public void setR_password(String r_password) {
        this.r_password = r_password;
    }

    public String getR_country() {
        return r_country;
    }

    public void setR_country(String r_country) {
        this.r_country = r_country;
    }

    public String getR_state() {
        return r_state;
    }

    public void setR_state(String r_state) {
        this.r_state = r_state;
    }

    public String getR_city() {
        return r_city;
    }

    public void setR_city(String r_city) {
        this.r_city = r_city;
    }
}
