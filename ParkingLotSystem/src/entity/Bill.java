package entity;

public class Bill {
    private final String plateNo;
    private final String spotId;
    private final long hours;
    private final double parkingFee;
    private final double fines;
    private final double totalDue;

    public Bill(String plateNo, String spotId, long hours, double parkingFee, double fines) {
        this.plateNo = plateNo;
        this.spotId = spotId;
        this.hours = hours;
        this.parkingFee = parkingFee;
        this.fines = fines;
        this.totalDue = parkingFee + fines;
    }

    public String getPlateNo() { return plateNo; }
    public String getSpotId() { return spotId; }
    public long getHours() { return hours; }
    public double getParkingFee() { return parkingFee; }
    public double getFines() { return fines; }
    public double getTotalDue() { return totalDue; }
}