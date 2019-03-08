package com.example.githubapp;

import java.io.Serializable;

public class Repository implements Serializable {


    private String name;
    private String fullName;
    private int stars;
    private int forks;
    private int watchers;
    private Owner owner;
    private String url;
    protected Repository() {
    }

    public Repository(String name, String fullName, int stars, int forks, int watchers, Owner owner, String url){
        this.name = name;
        this.fullName = fullName;
        this.stars = stars;
        this.forks = forks;
        this.watchers = watchers;
        this.owner = owner;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public int getWatchers() {
        return watchers;
    }

    public void setWatchers(int watchers) {
        this.watchers = watchers;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
