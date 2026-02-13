package service;

import entity.ParkingSession;
import entity.ParkingSpot;
import entity.Vehicle;

public interface FineService {

    double evaluateAndRecord(ParkingSession session, ParkingSpot spot, Vehicle vehicle);

    default void recordOutstanding(String plateNo, double amount, String reason) {
        // no-op by default (safe for integration)
    }

    default double getOutstanding(String plateNo) {
        return 0.0;
    }
}
