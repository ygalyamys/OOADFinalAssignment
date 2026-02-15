package entity;

import enums.SpotCategory;

public class Motorcycle extends Vehicle {

    public Motorcycle(String plateNumber) {
        super(plateNumber, false);
    }

    @Override
    public SpotCategory getRequiredSpotType() {
        return SpotCategory.COMPACT;
    }

    @Override
    public String getVehicleType() {
        return "Motorcycle";
    }
}
