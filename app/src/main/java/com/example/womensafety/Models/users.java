package com.example.womensafety.Models;

public class users {

    public String mFull_name;
    public String mAge;
    public String mEmail_id;
    public String mMobile_number;
    public String mAddress;
    public String mPassword;
    public String mUVC;
    public String country;
    public String state;
    public String city;


    public users(String mFull_name, String mAge, String mEmail_id, String mMobile_number, String mAddress, String mPassword, String mUVC, String country, String state, String city) {
        this.mFull_name = mFull_name;
        this.mAge = mAge;
        this.mEmail_id = mEmail_id;
        this.mMobile_number = mMobile_number;
        this.mAddress = mAddress;
        this.mPassword = mPassword;
        this.mUVC = mUVC;
        this.country = country;
        this.state = state;
        this.city = city;
    }

    public users(String mFull_name, String mAge, String mEmail_id, String mMobile_number, String mAddress, String mPassword, String mUVC) {
        this.mFull_name = mFull_name;
        this.mAge = mAge;
        this.mEmail_id = mEmail_id;
        this.mMobile_number = mMobile_number;
        this.mAddress = mAddress;
        this.mPassword = mPassword;
        this.mUVC = mUVC;
    }

    public users() {
    }

    public String getFull_name() {
        return mFull_name;
    }

    public void setFull_name(String mFull_name) {
        this.mFull_name = mFull_name;
    }

    public String getAge() {
        return mAge;
    }

    public void setAge(String mAge) {
        this.mAge = mAge;
    }

    public String getEmail_id() {
        return mEmail_id;
    }

    public void setEmail_id(String mEmail_id) {
        this.mEmail_id = mEmail_id;
    }

    public String getMobile_number() {
        return mMobile_number;
    }

    public void setMobile_number(String mMobile_number) {
        this.mMobile_number = mMobile_number;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getUVC() {
        return mUVC;
    }

    public void setUVC(String mUVC) {
        this.mUVC = mUVC;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }



}
