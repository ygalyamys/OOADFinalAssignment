package entity;

import enums.SpotCategory;

public class HandicappedVehicle extends Vehicle {

    public HandicappedVehicle(String plateNumber) {
        super(plateNumber, SpotCategory.HANDICAPPED, true);
    }
}
