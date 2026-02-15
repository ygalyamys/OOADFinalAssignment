package control;

import entity.ParkingLot;
import entity.ParkingSession;
import service.BillingService;
import service.FineService;
import service.FineScheme;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AdminController {

    private final ParkingLot parkingLot;
    private final BillingService billingService;
    private final FineService fineService;

    public AdminController(ParkingLot parkingLot,
                           BillingService billingService,
                           FineService fineService) {

        this.parkingLot = Objects.requireNonNull(parkingLot);
        this.billingService = Objects.requireNonNull(billingService);
        this.fineService = Objects.requireNonNull(fineService);
    }

    public double getOccupancyRate() {
        int totalSpots = parkingLot.getTotalSpots();
        int occupiedSpots = parkingLot.getOccupiedSpots();

        if (totalSpots <= 0) {
            return 0.0;
        }

        return (occupiedSpots * 100.0) / totalSpots;
    }

    public double getTotalRevenue() {
        double parkingRevenue = billingService.getTotalRevenue();
        double fineRevenue = fineService.getTotalCollectedFines();
        return parkingRevenue + fineRevenue;
    }

    public Map<String, Double> getOutstandingFines() {
        Map<String, Double> fines = fineService.getOutstandingFines();
        return fines != null ? fines : Collections.emptyMap();
    }

    public List<ParkingSession> getCurrentVehicles() {
        List<ParkingSession> sessions = parkingLot.getActiveSessions();
        return sessions != null ? sessions : Collections.emptyList();
    }

    public void setFineScheme(FineScheme scheme) {
        if (scheme != null) {
            fineService.setFineScheme(scheme);
        }
    }
}
