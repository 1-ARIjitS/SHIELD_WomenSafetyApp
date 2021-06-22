package com.example.womensafety.SuperAdmin.Models;

public class EmergencyContacts {
    String name,mobile;
    private boolean expandable;

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public EmergencyContacts(String name, String mobile) {
        this.name = name;
        this.mobile = mobile;
    }

    public EmergencyContacts() {
    }
}
