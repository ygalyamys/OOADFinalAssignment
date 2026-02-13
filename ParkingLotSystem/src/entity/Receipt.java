package entity;

import java.time.LocalDateTime;

/**
 * Receipt (Entity / Value Object):
 * - Immutable snapshot of exit transaction details shown to the user.
 * - Used by ExitPanel to print/display the outcome.
 */
public class Receipt {
    private final String plateNo;
    private final String spotId;
    private final LocalDateTime entryTime;
    private final LocalDateTime exitTime;

    // Billing details
    private final long hours;          // billable hours (ceiling rounding)
    private final double hourlyRate;   // rate taken from spot
    private final double parkingFee;   // hours * rate (or 0 if free rule applies)
    private final double fines;        // total fines (outstanding + new)

    // Payment details
    private final double totalPaid;
    private final String method;
    private final double balance; // positive = change, negative = outstanding

    public Receipt(String plateNo, String spotId,
                   LocalDateTime entryTime, LocalDateTime exitTime,
                   long hours, double hourlyRate,
                   double parkingFee, double fines,
                   double totalPaid, String method, double balance) {

        this.plateNo = plateNo;
        this.spotId = spotId;
        this.entryTime = entryTime;
        this.exitTime = exitTime;

        this.hours = hours;
        this.hourlyRate = hourlyRate;
        this.parkingFee = parkingFee;
        this.fines = fines;

        this.totalPaid = totalPaid;
        this.method = method;
        this.balance = balance;
    }

    /**
     * Formats receipt for console/text UI.
     * For real system, this can be replaced with PDF/print formatting later.
     */
    public String format() {
        double totalDue = parkingFee + fines;

        String balanceLabel = (balance >= 0) ? "Change" : "Outstanding";
        double balanceAmount = Math.abs(balance);

        return ""
            + "==== EXIT RECEIPT ====" + "\n"
            + "Plate      : " + plateNo + "\n"
            + "Spot       : " + spotId + "\n"
            + "Entry Time : " + entryTime + "\n"
            + "Exit  Time : " + exitTime + "\n"
            + "----------------------" + "\n"
            + "Billable Hours (ceil): " + hours + "\n"
            + "Rate (RM/hr)         : " + String.format("%.2f", hourlyRate) + "\n"
            + "Breakdown            : " + hours + " x RM " + String.format("%.2f", hourlyRate)
            + " = RM " + String.format("%.2f", parkingFee) + "\n"
            + "Parking Fee          : RM " + String.format("%.2f", parkingFee) + "\n"
            + "Fines                : RM " + String.format("%.2f", fines) + "\n"
            + "TOTAL DUE            : RM " + String.format("%.2f", totalDue) + "\n"
            + "----------------------" + "\n"
            + "Paid (" + method + ")       : RM " + String.format("%.2f", totalPaid) + "\n"
            + balanceLabel + "             : RM " + String.format("%.2f", balanceAmount) + "\n";
    }
}
