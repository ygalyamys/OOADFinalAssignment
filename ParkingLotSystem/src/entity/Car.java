package entity;

import enums.SpotCategory;

public class Car extends Vehicle {

    public Car(String plateNumber) {
        super(plateNumber, false);
    }

    @Override
    public SpotCategory getRequiredSpotType() {
        return SpotCategory.COMPACT;
    }

    @Override
    public String getVehicleType() {
        return "Car";
    }
}
