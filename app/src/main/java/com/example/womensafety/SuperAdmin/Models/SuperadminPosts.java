package com.example.womensafety.SuperAdmin.Models;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Date;

public class SuperadminPosts implements Serializable {

    @Exclude public String id;
    public String image,user,caption;

    public Date time;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
