package control;

import entity.ParkingLot;
import entity.ParkingSpot;

public class ParkingSpotLookup {
    private final ParkingLot parkingLot;

    public ParkingSpotLookup(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public ParkingSpot findSpotById(String spotId) {
        return parkingLot.getSpotByID(spotId);
    }

    public void releaseSpot(ParkingSpot spot) {
        if (spot != null) spot.release();
    }
}