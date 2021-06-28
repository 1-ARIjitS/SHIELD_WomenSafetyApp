package com.example.womensafety.Admin.Models;

public class SuperAdmins {
    String s_name,s_password,s_age,s_email,s_mob_num,s_address;
    String s_gender;

    public SuperAdmins(String s_name, String s_password, String s_age, String s_email, String s_mob_num, String s_address, String s_gender) {
        this.s_name = s_name;
        this.s_password = s_password;
        this.s_age = s_age;
        this.s_email = s_email;
        this.s_mob_num = s_mob_num;
        this.s_address = s_address;
        this.s_gender = s_gender;
    }

    public SuperAdmins() {
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getS_password() {
        return s_password;
    }

    public void setS_password(String s_password) {
        this.s_password = s_password;
    }

    public String getS_age() {
        return s_age;
    }

    public void setS_age(String s_age) {
        this.s_age = s_age;
    }

    public String getS_email() {
        return s_email;
    }

    public void setS_email(String s_email) {
        this.s_email = s_email;
    }

    public String getS_mob_num() {
        return s_mob_num;
    }

    public void setS_mob_num(String s_mob_num) {
        this.s_mob_num = s_mob_num;
    }

    public String getS_address() {
        return s_address;
    }

    public void setS_address(String s_address) {
        this.s_address = s_address;
    }

    public String getS_gender() {
        return s_gender;
    }

    public void setS_gender(String s_gender) {
        this.s_gender = s_gender;
    }
}
