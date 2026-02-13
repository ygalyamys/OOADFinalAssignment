package entity;

import enums.SpotCategory;

public class Motorcycle extends Vehicle {

    public Motorcycle(String plateNumber) {
        super(plateNumber, SpotCategory.MOTORCYCLE, false);
    }
}
