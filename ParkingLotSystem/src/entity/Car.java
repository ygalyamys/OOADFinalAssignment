package entity;

import enums.SpotCategory;

public class Car extends Vehicle {

    public Car(String plateNumber) {
        super(plateNumber, SpotCategory.COMPACT, false);
    }
}
