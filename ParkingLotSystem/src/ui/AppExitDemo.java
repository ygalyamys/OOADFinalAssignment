package ui;

import control.ExitController;
import control.ParkingSpotLookup;
import entity.ParkingLot;
import service.*;

/**
 * AppExitDemo:
 * - Small runnable demo for Member 3 (Exit/Billing/Payment).
 * - This is NOT final integration with Entry module.
 * - It wires dependencies and seeds a demo session so ExitPanel can be tested.
 */
public class AppExitDemo {

    public static void main(String[] args) {

        // ===== 1) Build ParkingLot =====
        ParkingLot lot = new ParkingLot("MAIN", 2, 2, 5);

        // ===== 2) Wire Services =====
        BillingService billingService = new BillingService();

        // FineService is ledger-backed so we can demonstrate "unpaid fines carry forward"
        FineLedger ledger = new InMemoryFineLedger();
        FineService fineService = new LedgerBackedFineService(ledger); // Member 4 will plug in real fine rules later

        PaymentService paymentService = new PaymentService();

        // Lookup used by controller to find and release spots
        ParkingSpotLookup spotLookup = new ParkingSpotLookup(lot);

        // ===== 3) Controller =====
        ExitController exitController = new ExitController(billingService, fineService, paymentService, spotLookup);

        // ===== 4) Seed demo data (so you can immediately test exit) =====
        // Use a valid spot ID based on how Floor initializes spots (example: F1-R1-S1)
        exitController.seedDemoSession("ABC1234", false, "F1-R1-S1");

        // Optional: simulate existing unpaid fines to show "ledger carry forward"
        ledger.addOutstanding("ABC1234", 5.00, "PREVIOUS_UNPAID_FINE");

        // ===== 5) Launch GUI =====
        javax.swing.SwingUtilities.invokeLater(() -> {
            javax.swing.JFrame f = new javax.swing.JFrame("Parking Exit Panel (Member 3)");
            f.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            f.setContentPane(new ExitPanel(exitController));
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
