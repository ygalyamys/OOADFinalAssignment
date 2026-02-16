package service;

import entity.ParkingSession;
import entity.ParkingSpot;
import entity.Vehicle;

import java.time.Duration;
import java.time.LocalDateTime;

public class FixedFineService implements FineService {

    @Override
    public double evaluateAndRecord(ParkingSession session, ParkingSpot spot, Vehicle vehicle) {
        
        LocalDateTime entry = session.getEntryTime();
        LocalDateTime exit = LocalDateTime.now(); // replace with actual exitTime

        long minutes = Duration.between(entry, exit).toMinutes();
        long hours = (minutes + 59) / 60; // ceiling rounding

        if (hours > 24) {
            double fine = 50.0; // Fixed fine scheme
            recordOutstanding(vehicle.getPlateNo(), fine, "Overstaying more than 24 hours");
            return fine;
        }
        return 0.0;
    }

    @Override
    public void recordOutstanding(String plateNo, double amount, String reason) {
        // integrate with FineLedger
        System.out.println("Fine recorded for " + plateNo + ": RM " + amount + " (" + reason + ")");
    }
}