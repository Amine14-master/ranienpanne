package com.example.ranienpanne;

public class PieceDetache {
    private String username, phone, position, description;

    public PieceDetache(String username, String phone, String position, String description) {
        this.username = username;
        this.phone = phone;
        this.position = position;
        this.description = description;
    }

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
}
