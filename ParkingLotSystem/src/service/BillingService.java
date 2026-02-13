package service;

import entity.Bill;
import entity.ParkingSession;
import entity.ParkingSpot;
import entity.Vehicle;

import java.time.Duration;
import java.time.LocalDateTime;

/*
 * BillingService (Service):
 * - Encapsulates ALL pricing rules.
 * - Keeps ExitController clean (controller should orchestrate, not calculate).
 *
 * Member 3 focus:
 * - duration calculation (ceiling to hours)
 * - hourly rates
 * - handicapped rule
 */
public class BillingService {

    /**
     * Duration is rounded UP to nearest hour (ceiling rounding).
     * Example: 1 minute -> 1 hour, 61 minutes -> 2 hours.
     */
    public long calculateBillableHours(LocalDateTime entry, LocalDateTime exit) {
        long minutes = Duration.between(entry, exit).toMinutes();
        if (minutes <= 0) return 0;

        // Ceiling rounding to nearest hour
        return (minutes + 59) / 60;
    }

    /*
    Build the Bill for this exit.
    Inputs are entities (session, spot, vehicle) and exitTime.
    Output is a Bill value object.
    */
    public Bill buildBill(ParkingSession session,
                          ParkingSpot spot,
                          Vehicle vehicle,
                          LocalDateTime exitTime,
                          double fineAmount) {

        // 1) Compute billable duration
        long hours = calculateBillableHours(session.getEntryTime(), exitTime);

        // 2) Spot decides hourly rate (different categories can have different rates)
        double hourlyRate = spot.getHourlyRate();

        // 3) Handicapped rule (based on requirements):
        // - PARKING on HANDICAPPED spot is FREE only if driver is a handicapped card holder.
        // NOTE: This is currently a boolean flag in Vehicle; later can be polymorphism if UML evolves.
        boolean isHandicappedSpot = spot.getCategory().name().equals("HANDICAPPED");
        boolean free = vehicle.isHandicappedCardHolder() && isHandicappedSpot;

        double parkingFee = free ? 0.0 : (hours * hourlyRate);

        // 4) Build final bill (parking fee + fines)
        return new Bill(vehicle.getPlateNo(), spot.getSpotID(), hours, parkingFee, fineAmount);
    }
}
