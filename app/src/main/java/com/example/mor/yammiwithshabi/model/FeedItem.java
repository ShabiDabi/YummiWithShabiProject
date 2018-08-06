package com.example.mor.yammiwithshabi.model;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity
public class FeedItem {
    @PrimaryKey
    @NonNull
    public String id;
    public String dateCreated;
    public String picture;
    public String email;
    public String text;


    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getPicture() {
        return picture;
    }

    public String getText() {
        return text;
    }



    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setbDateCreated(String date) {
        this.dateCreated = date;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setText(String text) {
        this.text = text;
    }

}
