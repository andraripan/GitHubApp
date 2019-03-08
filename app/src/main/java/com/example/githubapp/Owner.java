package com.example.githubapp;

import java.io.Serializable;

public class Owner implements Serializable {

    String username;
    String imageUrl;

    public Owner(String username, String imageUrl){
        this.username = username;
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
