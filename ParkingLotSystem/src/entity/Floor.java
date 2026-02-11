package entity;

import enums.SpotStatus;
import enums.SpotCategory;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents one floor in the parking lot
 * Contains multiple parking spots arranged in rows
 */
public class Floor {  // ← CORRECTED: Was "ParkingSpot", now "Floor"
    private int floorNo;
    private List<ParkingSpot> spots;
    
    // Constructor
    public Floor(int floorNo) {
        this.floorNo = floorNo;
        this.spots = new ArrayList<>();
    }
    
    /**
     * Initialize all parking spots for this floor
     * @param rows Number of rows on this floor
     * @param spotsPerRow Number of spots in each row
     */
    public void initializeSpots(int rows, int spotsPerRow) {
        spots.clear(); // Clear any existing spots
        int spotCounter = 1;
        
        for (int row = 1; row <= rows; row++) {
            for (int spot = 1; spot <= spotsPerRow; spot++) {
                // Generate spot ID: F{floor}-R{row}-S{spot}
                String spotID = String.format("F%d-R%d-S%d", floorNo, row, spot);
                
                // Assign category based on pattern
                SpotCategory category = determineSpotCategory(spotCounter);
                
                // Create and add the spot
                spots.add(new ParkingSpot(spotID, category));
                spotCounter++;
            }
        }
        
        System.out.println("Floor " + floorNo + " initialized with " + spots.size() + " spots");
    }
    
    /**
     * Determine spot category based on a distribution pattern
     * This creates a mix of different spot types
     */
    private SpotCategory determineSpotCategory(int spotNumber) {
        // Every 10th spot is RESERVED
        if (spotNumber % 10 == 0) {
            return SpotCategory.RESERVED;
        }
        // Every 7th spot (not 10th) is HANDICAPPED
        else if (spotNumber % 7 == 0) {
            return SpotCategory.HANDICAPPED;
        }
        // Every 3rd spot (not 7th or 10th) is COMPACT
        else if (spotNumber % 3 == 0) {
            return SpotCategory.COMPACT;
        }
        // All others are REGULAR
        else {
            return SpotCategory.REGULAR;
        }
    }
    
    /**
     * Get all available spots of a specific category
     */
    public List<ParkingSpot> getAvailableSpots(SpotCategory category) {
        List<ParkingSpot> available = new ArrayList<>();
        for (ParkingSpot spot : spots) {
            if (spot.getStatus() == SpotStatus.AVAILABLE && 
                spot.getCategory() == category) {
                available.add(spot);
            }
        }
        return available;
    }
    
    /**
     * Find a spot by its ID
     */
    public ParkingSpot getSpotByID(String spotID) {
        for (ParkingSpot spot : spots) {
            if (spot.getSpotID().equals(spotID)) {
                return spot;
            }
        }
        return null;
    }
    
    /**
     * Get count of occupied spots on this floor
     */
    public int getOccupiedCount() {
        int count = 0;
        for (ParkingSpot spot : spots) {
            if (spot.getStatus() == SpotStatus.OCCUPIED) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Get count of available spots on this floor
     */
    public int getAvailableCount() {
        return spots.size() - getOccupiedCount();
    }
    
    /**
     * Get occupancy rate for this floor (as percentage)
     */
    public double getOccupancyRate() {
        if (spots.isEmpty()) return 0.0;
        return (getOccupiedCount() * 100.0) / spots.size();
    }
    
    // Getters
    public int getFloorNo() {
        return floorNo;
    }
    
    public List<ParkingSpot> getSpots() {
        return spots;
    }
    
    public int getTotalSpots() {
        return spots.size();
    }
    
    // Display floor status
    public void displayStatus() {
        System.out.println("\n=== Floor " + floorNo + " Status ===");
        System.out.println("Total Spots: " + getTotalSpots());
        System.out.println("Occupied: " + getOccupiedCount());
        System.out.println("Available: " + getAvailableCount());
        System.out.println("Occupancy Rate: " + String.format("%.2f%%", getOccupancyRate()));
    }
}