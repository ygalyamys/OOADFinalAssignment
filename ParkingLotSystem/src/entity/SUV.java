package entity;

import enums.SpotCategory;

public class SUV extends Vehicle {

    public SUV(String plateNumber) {
        super(plateNumber, false);
    }

    @Override
    public SpotCategory getRequiredSpotType() {
        return SpotCategory.REGULAR;
    }

    @Override
    public String getVehicleType() {
        return "SUV";
    }
}
