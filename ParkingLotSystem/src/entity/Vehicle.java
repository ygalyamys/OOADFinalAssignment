package entity;

import enums.SpotCategory;

public abstract class Vehicle {
    protected String plateNumber;
    protected SpotCategory category;
    protected boolean handicapped;

    public Vehicle(String plateNumber, SpotCategory category, boolean handicapped) {
        this.plateNumber = plateNumber;
        this.category = category;
        this.handicapped = handicapped;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public SpotCategory getCategory() {
        return category;
    }

    public boolean isHandicappedCardHolder() {
        return handicapped;
    }
}
