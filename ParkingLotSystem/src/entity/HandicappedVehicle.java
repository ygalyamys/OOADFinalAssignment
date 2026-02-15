package entity;

import enums.SpotCategory;

public class HandicappedVehicle extends Vehicle {

    public HandicappedVehicle(String plateNumber) {
        super(plateNumber, true);
    }

    @Override
    public SpotCategory getRequiredSpotType() {
        return SpotCategory.HANDICAPPED;
    }

    @Override
    public String getVehicleType() {
        return "Handicapped";
    }
}
