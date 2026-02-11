package entity;

import enums.SpotStatus;
import enums.SpotCategory;
import java.util.ArrayList;
import java.util.List;

/**
 * Main ParkingLot class - represents the entire multi-floor parking structure
 * This is the top-level entity that contains all floors
 */
public class ParkingLot {
    private String id;                  // Parking lot identifier
    private List<Floor> floors;         // All floors in the parking lot
    
    /**
     * Constructor - creates a parking lot with multiple floors
     * @param id Parking lot identifier (e.g., "MAIN")
     * @param numFloors Number of floors to create
     * @param rowsPerFloor Number of rows on each floor
     * @param spotsPerRow Number of spots in each row
     */
    public ParkingLot(String id, int numFloors, int rowsPerFloor, int spotsPerRow) {
        this.id = id;
        this.floors = new ArrayList<>();
        
        System.out.println("Creating Parking Lot: " + id);
        System.out.println("Floors: " + numFloors + " | Rows/Floor: " + rowsPerFloor + 
                         " | Spots/Row: " + spotsPerRow);
        
        // Create all floors
        for (int i = 1; i <= numFloors; i++) {
            Floor floor = new Floor(i);
            floor.initializeSpots(rowsPerFloor, spotsPerRow);
            floors.add(floor);
        }
        
        System.out.println("Parking Lot '" + id + "' created successfully!\n");
    }
    
    /**
     * Find all available spots of a specific category across all floors
     * This is used by SpotAllocator service
     */
    public List<ParkingSpot> findAvailableSpots(SpotCategory category) {
        List<ParkingSpot> allAvailable = new ArrayList<>();
        
        for (Floor floor : floors) {
            allAvailable.addAll(floor.getAvailableSpots(category));
        }
        
        return allAvailable;
    }
    
    /**
     * Find a specific spot by its ID across all floors
     */
    public ParkingSpot getSpotByID(String spotID) {
        for (Floor floor : floors) {
            ParkingSpot spot = floor.getSpotByID(spotID);
            if (spot != null) {
                return spot;
            }
        }
        return null; // Spot not found
    }
    
    /**
     * Calculate overall occupancy rate for the entire parking lot
     */
    public double getOccupancyRate() {
        int totalSpots = 0;
        int occupiedSpots = 0;
        
        for (Floor floor : floors) {
            totalSpots += floor.getTotalSpots();
            occupiedSpots += floor.getOccupiedCount();
        }
        
        if (totalSpots == 0) return 0.0;
        return (occupiedSpots * 100.0) / totalSpots;
    }
    
    /**
     * Get total number of spots in the parking lot
     */
    public int getTotalSpots() {
        int total = 0;
        for (Floor floor : floors) {
            total += floor.getTotalSpots();
        }
        return total;
    }
    
    /**
     * Get total occupied spots
     */
    public int getTotalOccupied() {
        int total = 0;
        for (Floor floor : floors) {
            total += floor.getOccupiedCount();
        }
        return total;
    }
    
    /**
     * Get total available spots
     */
    public int getTotalAvailable() {
        return getTotalSpots() - getTotalOccupied();
    }
    
    // Getters
    public String getId() {
        return id;
    }
    
    public List<Floor> getFloors() {
        return floors;
    }
    
    public int getNumFloors() {
        return floors.size();
    }
    
    /**
     * Display complete parking lot status
     */
    public void displayStatus() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   PARKING LOT STATUS - " + id + "              ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("Total Floors: " + getNumFloors());
        System.out.println("Total Spots: " + getTotalSpots());
        System.out.println("Occupied: " + getTotalOccupied());
        System.out.println("Available: " + getTotalAvailable());
        System.out.println("Occupancy Rate: " + String.format("%.2f%%", getOccupancyRate()));
        System.out.println("─────────────────────────────────────────");
        
        // Show each floor
        for (Floor floor : floors) {
            floor.displayStatus();
        }
    }
}