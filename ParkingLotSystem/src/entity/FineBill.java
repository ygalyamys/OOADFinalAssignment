package entity;

public class FineBill {
    private final Bill baseBill;
    private final double fineDue;
    private final double totalDue;

    public FineBill(Bill baseBill, double fineDue) {
        this.baseBill = baseBill;
        this.fineDue = fineDue;
        this.totalDue = baseBill.getParkingFee() + fineDue;
    }

    public Bill getBaseBill() { return baseBill; }
    public double getFineDue() { return fineDue; }
    public double getTotalDue() { return totalDue; }
}