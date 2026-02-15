package entity;

import enums.SpotCategory;

public abstract class Vehicle {
    protected String plateNumber;
    protected boolean handicapped;

    public Vehicle(String plateNumber, boolean handicapped) {
        this.plateNumber = plateNumber;
        this.handicapped = handicapped;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public boolean isHandicappedCardHolder() {
        return handicapped;
    }

    public abstract SpotCategory getRequiredSpotType();

    public abstract String getVehicleType();
}
