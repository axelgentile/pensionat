package org.example.pensionatbackend1.Models.modelenums;

public enum RoomType {
    SINGLE("Enkelrum"),
    DOUBLE("Dubbelrum");
    private String description;

    RoomType(String description) {
        this.description = description;
    }
    @Override
    public String toString() {
        return description;
    }
}
