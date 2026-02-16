package control;

import entity.ParkingSession;
import observer.OccupancyObserver;
import observer.RevenueObserver;
import service.FineService;
import service.FineScheme;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AdminController {

    private final OccupancyObserver occupancyObserver;
    private final RevenueObserver revenueObserver;
    private final FineService fineService;

    public AdminController(OccupancyObserver occupancyObserver,
                           RevenueObserver revenueObserver,
                           FineService fineService) {
        this.occupancyObserver = occupancyObserver;
        this.revenueObserver = revenueObserver;
        this.fineService = fineService;
    }

    // Parking statistics
    public int getTotalSpots() {
        return occupancyObserver.getTotalSpots();
    }

    public int getOccupiedSpots() {
        return occupancyObserver.getOccupiedSpots();
    }

    public int getAvailableSpots() {
        return occupancyObserver.getAvailableSpots();
    }

    public double getOccupancyRate() {
        return occupancyObserver.getOccupancyRate();
    }

    // Revenue tracking
    public double getTotalRevenue() {
        return revenueObserver.getTotalRevenue();
    }

    // Reporting data
    public Map<String, Double> getOutstandingFines() {
        Map<String, Double> fines = fineService.getOutstandingFines();
        return fines != null ? fines : Collections.emptyMap();
    }

    public List<ParkingSession> getCurrentVehicles() {
        return fineService.getActiveSessions();
    }

    public void setFineScheme(FineScheme scheme) {
        fineService.setFineScheme(scheme);
    }
}
