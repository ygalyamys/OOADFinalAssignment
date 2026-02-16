package control;

import entity.Bill;
import entity.FineBill;
import entity.ParkingSession;
import entity.ParkingSpot;
import entity.Vehicle;
import service.BillingService;
import service.FineService;

import java.time.LocalDateTime;

public class ExitWithFineController {
    private final BillingService billingService;
    private final FineService fineService;

    public ExitWithFineController(BillingService billingService, FineService fineService) {
        this.billingService = billingService;
        this.fineService = fineService;
    }

    public FineBill exitLot(ParkingSession session, ParkingSpot spot, Vehicle vehicle, LocalDateTime exitTime) {
        double fineAmount = fineService.evaluateAndRecord(session, spot, vehicle);
        Bill baseBill = billingService.buildBill(session, spot, vehicle, exitTime, fineAmount);
        return new FineBill(baseBill, fineAmount);
    }
}