package com.unina.natour.domain.model;

public class User {

    private Long id;
    private String username;
    private String photoUrl;

    public User() {
    }

    public User(Long id, String username, String photoUrl) {
        this.id = id;
        this.username = username;
        this.photoUrl = photoUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
