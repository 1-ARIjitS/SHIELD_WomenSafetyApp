package com.example.womensafety.SuperAdmin.Adapters;

import androidx.annotation.NonNull;


import com.google.firebase.firestore.Exclude;

public class UserId {
    @Exclude
    public String UserId;

    public <T extends UserId> T withId (@NonNull final String id )
    {
        this.UserId=id;
        return (T)this;
    }
}
