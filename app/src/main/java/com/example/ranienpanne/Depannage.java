package com.example.ranienpanne;

public class Depannage {
    private String username;
    private String phone;
    private String position;
    private String description;
    private String type;

    public Depannage(String username, String phone, String position, String description, String type) {
        this.username = username;
        this.phone = phone;
        this.position = position;
        this.description = description;
        this.type = type;
    }
    public Depannage(String username, String phone, String position, String description) {
        this.username = username;
        this.phone = phone;
        this.position = position;
        this.description = description;

    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getPosition() {
        return position;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }
}
