package observer;

import entity.ParkingLot;

/*
 * Observer that tracks parking lot occupancy statistics.
 * It listens to changes in ParkingLot state.
 */
public class OccupancyObserver {

    private ParkingLot parkingLot;

    public OccupancyObserver(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public int getTotalSpots() {
        return parkingLot.getTotalSpots();
    }

    public int getOccupiedSpots() {
        return parkingLot.getOccupiedSpots();
    }

    public int getAvailableSpots() {
        return getTotalSpots() - getOccupiedSpots();
    }

    public double getOccupancyRate() {
        int total = getTotalSpots();
        if (total == 0) return 0;
        return (getOccupiedSpots() * 100.0) / total;
    }
}
