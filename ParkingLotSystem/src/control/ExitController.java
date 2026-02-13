package control;

import entity.*;
import service.BillingService;
import service.FineService;
import service.PaymentService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/*
ExitController Controller class:
- Orchestrates the EXIT use-case (Scenario 2: Exit/Pay).
- Coordinates Entity objects (ParkingSession, ParkingSpot, Vehicle, Bill, Payment, Receipt)
  and Service objects (BillingService, FineService, PaymentService).

Business rules live inside services (BillingService, FineService, PaymentService).
*/
public class ExitController {

    //Dependencies (Services)
    private final BillingService billingService;
    private final FineService fineService;
    private final PaymentService paymentService;
    private final ParkingSpotLookup spotLookup;

    // ===== Temporary demo storage =====
    // For demo ONLY, can replace this with ParkingSessionRepository + VehicleRepository later.
    private final Map<String, ParkingSession> activeSessionsByPlate = new HashMap<>();
    private final Map<String, Vehicle> vehiclesByPlate = new HashMap<>();

    public ExitController(BillingService billingService,
                          FineService fineService,
                          PaymentService paymentService,
                          //ParkingSessionRepository sessionRepo,
                          //VehicleRepository vehicleRepo,
                          ParkingSpotLookup spotLookup) {
        this.billingService = billingService;
        this.fineService = fineService;
        this.paymentService = paymentService;
        this.spotLookup = spotLookup;
        //this.sessionRepo = sessionRepo;
        //this.vehicleRepo = vehicleRepo;
    }

    /*
    private final ParkingSessionRepository sessionRepo;
    private final VehicleRepository vehicleRepo;
    */
    
    // ===== Demo helper =====
    // Seeds a fake session so ExitPanel can run without needing EntryPanel integration yet.
    public void seedDemoSession(String plateNo, boolean handicappedCardHolder, String spotId) {
        vehiclesByPlate.put(plateNo, new Vehicle(plateNo, handicappedCardHolder));
        activeSessionsByPlate.put(
                plateNo,
                new ParkingSession("S-" + plateNo, plateNo, spotId, LocalDateTime.now().minusHours(2))
        );
    }

    /*
    Main EXIT use-case:
    1) Find active session & related entities
    2) Evaluate fines (including unpaid from ledger)
    3) Build a Bill (duration rounding + rates)
    4) Take payment (Cash/Card/etc.)
    5) If fully paid: release spot + close session
       Else: record outstanding amount into FineLedger via FineService hook
    6) Return Receipt for UI display/printing
    */
    public Receipt exitLot(String plateNo, String methodName, double amountPaid) {

        // --- 1) Retrieve current session ---
        ParkingSession session = activeSessionsByPlate.get(plateNo);
        //ParkingSession session = sessionRepo.findActiveByPlate(plateNo);
        if (session == null) {
            throw new IllegalArgumentException("No active session found for plate: " + plateNo);
        }

        Vehicle vehicle = vehiclesByPlate.get(plateNo);
        //Vehicle vehicle = vehicleRepo.findByPlate(plateNo);
        if (vehicle == null) {
            throw new IllegalStateException("Vehicle not found for plate: " + plateNo);
        }

        ParkingSpot spot = spotLookup.findSpotById(session.getSpotID());
        if (spot == null) {
            throw new IllegalStateException("Spot not found: " + session.getSpotID());
        }

        LocalDateTime exitTime = LocalDateTime.now();

        // --- 2) Fine calculation (Member 4 can implement real policy later) ---
        // This should already include unpaid fines from previous visits if FineService is ledger-backed.
        double fines = fineService.evaluateAndRecord(session, spot, vehicle);

        // --- 3) Build bill (duration rounding + rate + handicapped rules) ---
        Bill bill = billingService.buildBill(session, spot, vehicle, exitTime, fines);

        // --- 4) Payment (OCP: method is resolved inside PaymentService) ---
        Payment payment = paymentService.takePayment(bill, methodName, amountPaid);

        // balance = positive change OR negative outstanding
        double balance = payment.getAmount() - bill.getTotalDue();

        // --- 5) Decide release/keep session ---
        if (balance >= 0) {
            // Fully paid -> free the spot & remove active session
            spotLookup.releaseSpot(spot);
            activeSessionsByPlate.remove(plateNo);
        } else {
            // Underpaid -> record outstanding into ledger (so next exit can pay it)
            double outstanding = -balance;
            fineService.recordOutstanding(plateNo, outstanding, "UNDERPAY_AT_EXIT");
        }

        // --- 6) Generate receipt for UI ---
        return new Receipt(
                plateNo,
                spot.getSpotID(),
                session.getEntryTime(),
                exitTime,
                bill.getHours(),
                spot.getHourlyRate(),
                bill.getParkingFee(),
                bill.getFines(),
                payment.getAmount(),
                payment.getMethodName(),
                balance
        );
    }


    /*
    BILL PREVIEW (no payment yet):
    - This supports a realistic UX: driver sees total due BEFORE paying.
    - No side effects: does NOT release spot, does NOT close the session, does NOT record underpayment.
    
    Note: ExitPanel uses this to show price breakdown first.
    */
    public Receipt previewBill(String plateNo) {

        // --- 1) Retrieve current session ---
        ParkingSession session = activeSessionsByPlate.get(plateNo);
        //ParkingSession session = sessionRepo.findActiveByPlate(plateNo);
        if (session == null) {
            throw new IllegalArgumentException("No active session found for plate: " + plateNo);
        }

        Vehicle vehicle = vehiclesByPlate.get(plateNo);
        //Vehicle vehicle = vehicleRepo.findByPlate(plateNo);
        if (vehicle == null) {
            throw new IllegalStateException("Vehicle not found for plate: " + plateNo);
        }

        ParkingSpot spot = spotLookup.findSpotById(session.getSpotID());
        if (spot == null) {
            throw new IllegalStateException("Spot not found: " + session.getSpotID());
        }

        LocalDateTime previewTime = LocalDateTime.now();

        // --- 2) Fine calculation (ledger + new fine stub) ---
        double fines = fineService.evaluateAndRecord(session, spot, vehicle);

        // --- 3) Build bill ---
        Bill bill = billingService.buildBill(session, spot, vehicle, previewTime, fines);

        // --- 4) Return a Receipt-like preview object (payment = 0) ---
        // We reuse Receipt for convenience; UI will label it as "BILL PREVIEW".
        double totalDue = bill.getTotalDue();
        return new Receipt(
                plateNo,
                spot.getSpotID(),
                session.getEntryTime(),
                previewTime,
                bill.getHours(),
                spot.getHourlyRate(),
                bill.getParkingFee(),
                bill.getFines(),
                0.0,
                "PREVIEW",
                -totalDue
        );
    }

    // Optional: expose outstanding for UI demo/debug
    public double getOutstandingFines(String plateNo) {
        return fineService.getOutstanding(plateNo);
    }
}
