package com.example.womensafety;

public class suspect_registered {
    String name,description,identity,mobile_num;

    public suspect_registered(String name, String description, String identity, String mobile_num) {
        this.name = name;
        this.description = description;
        this.identity = identity;
        this.mobile_num = mobile_num;
    }

    public suspect_registered(String name, String mobile_num) {
        this.name = name;
        this.mobile_num = mobile_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getMobile_num() {
        return mobile_num;
    }

    public void setMobile_num(String mobile_num) {
        this.mobile_num = mobile_num;
    }
}
