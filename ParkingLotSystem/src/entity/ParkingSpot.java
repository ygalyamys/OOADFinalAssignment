package entity;

import enums.SpotStatus;
import enums.SpotCategory;

/**
 * Represents a single parking spot in the parking lot
 * Attributes and methods as per the class diagram
 */
public class ParkingSpot {
    // Attributes from diagram
    private String spotID;              // Format: F1-R1-S1
    private SpotStatus status;          // AVAILABLE or OCCUPIED
    private SpotCategory category;      // COMPACT, REGULAR, HANDICAPPED, RESERVED
    private String currentVehicle;      // License plate (will be Vehicle object later)
    private boolean isReleased;         // Track if spot is released
    private double hourlyRate;          // Rate based on category
    
    // Constructor
    public ParkingSpot(String spotID, SpotCategory category) {
        this.spotID = spotID;
        this.category = category;
        this.status = SpotStatus.AVAILABLE;
        this.currentVehicle = null;
        this.isReleased = true;
        this.hourlyRate = category.getBaseHourlyRate();
    }
    
    // Methods from diagram
    
    /**
     * Occupy this spot with a vehicle
     * @param vehicle The license plate or vehicle identifier
     */
    public void occupy(String vehicle) {
        if (this.status == SpotStatus.AVAILABLE) {
            this.status = SpotStatus.OCCUPIED;
            this.currentVehicle = vehicle;
            this.isReleased = false;
            System.out.println("Spot " + spotID + " occupied by " + vehicle);
        } else {
            System.out.println("Error: Spot " + spotID + " is already occupied!");
        }
    }
    
    /**
     * Release this spot (make it available again)
     */
    public void release() {
        this.status = SpotStatus.AVAILABLE;
        this.currentVehicle = null;
        this.isReleased = true;
        System.out.println("Spot " + spotID + " released and now available");
    }
    
    // Getters and Setters
    public String getSpotID() {
        return spotID;
    }
    
    public SpotStatus getStatus() {
        return status;
    }
    
    public SpotCategory getCategory() {
        return category;
    }
    
    public String getCurrentVehicle() {
        return currentVehicle;
    }
    
    public boolean isReleased() {
        return isReleased;
    }
    
    public double getHourlyRate() {
        return hourlyRate;
    }
    
    public void setStatus(SpotStatus status) {
        this.status = status;
    }
    
    public void setCurrentVehicle(String vehicle) {
        this.currentVehicle = vehicle;
    }
    
    // Helper method to display spot info
    public String getSpotInfo() {
        return String.format("Spot: %s | Type: %s | Rate: RM%.2f/hr | Status: %s", 
                           spotID, category.getDisplayName(), hourlyRate, status);
    }
}