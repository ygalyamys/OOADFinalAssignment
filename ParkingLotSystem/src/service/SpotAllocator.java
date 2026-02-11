package service;

import entity.ParkingLot;
import entity.ParkingSpot;
import enums.SpotCategory;
import enums.SpotStatus;
import java.util.List;
import java.util.ArrayList;

/**
 * Service class responsible for allocating parking spots to vehicles
 * Implements the business logic for finding suitable spots
 */
public class SpotAllocator {
    private ParkingLot parkingLot;
    
    // Constructor
    public SpotAllocator(ParkingLot lot) {
        this.parkingLot = lot;
    }
    
    /**
     * Find suitable available spots for a given vehicle type
     * @param vehicleType Type of vehicle (motorcycle, car, suv, handicapped)
     * @return List of available suitable spots
     */
    public List<ParkingSpot> findSuitableSpots(String vehicleType) {
        // Get suitable categories for this vehicle type
        List<SpotCategory> suitableCategories = getSuitableCategories(vehicleType);
        
        List<ParkingSpot> allSuitableSpots = new ArrayList<>();
        
        // Find available spots of suitable categories
        for (SpotCategory category : suitableCategories) {
            List<ParkingSpot> spotsOfCategory = parkingLot.findAvailableSpots(category);
            allSuitableSpots.addAll(spotsOfCategory);
        }
        
        return allSuitableSpots;
    }
    
    /**
     * Determine which spot categories are suitable for a vehicle type
     * Based on assignment requirements
     */
    private List<SpotCategory> getSuitableCategories(String vehicleType) {
        List<SpotCategory> suitable = new ArrayList<>();
        
        switch (vehicleType.toLowerCase()) {
            case "motorcycle":
                // Motorcycles can only park in COMPACT spots
                suitable.add(SpotCategory.COMPACT);
                break;
                
            case "car":
                // Cars can park in COMPACT or REGULAR spots
                suitable.add(SpotCategory.COMPACT);
                suitable.add(SpotCategory.REGULAR);
                break;
                
            case "suv":
            case "truck":
                // SUVs/Trucks can only park in REGULAR spots
                suitable.add(SpotCategory.REGULAR);
                break;
                
            case "handicapped":
                // Handicapped vehicles can park in ANY spot type
                suitable.add(SpotCategory.COMPACT);
                suitable.add(SpotCategory.REGULAR);
                suitable.add(SpotCategory.HANDICAPPED);
                suitable.add(SpotCategory.RESERVED);
                break;
                
            default:
                System.out.println("Unknown vehicle type: " + vehicleType);
        }
        
        return suitable;
    }
    
    /**
     * Allocate a specific spot to a vehicle
     * @param spotID The ID of the spot to allocate
     * @param vehiclePlate License plate number
     * @return true if allocation successful, false otherwise
     */
    public boolean allocateSpot(String spotID, String vehiclePlate) {
        ParkingSpot spot = parkingLot.getSpotByID(spotID);
        
        if (spot == null) {
            System.out.println("Error: Spot " + spotID + " not found!");
            return false;
        }
        
        if (spot.getStatus() == SpotStatus.OCCUPIED) {
            System.out.println("Error: Spot " + spotID + " is already occupied!");
            return false;
        }
        
        // Occupy the spot
        spot.occupy(vehiclePlate);
        return true;
    }
    
    /**
     * Release a spot (make it available again)
     * @param spotID The ID of the spot to release
     * @return true if release successful, false otherwise
     */
    public boolean releaseSpot(String spotID) {
        ParkingSpot spot = parkingLot.getSpotByID(spotID);
        
        if (spot == null) {
            System.out.println("Error: Spot " + spotID + " not found!");
            return false;
        }
        
        spot.release();
        return true;
    }
    
    /**
     * Display available spots for a vehicle type
     */
    public void displayAvailableSpots(String vehicleType) {
        List<ParkingSpot> available = findSuitableSpots(vehicleType);
        
        System.out.println("\n=== Available Spots for " + vehicleType.toUpperCase() + " ===");
        if (available.isEmpty()) {
            System.out.println("No available spots found!");
        } else {
            System.out.println("Found " + available.size() + " available spots:");
            for (int i = 0; i < Math.min(10, available.size()); i++) {
                System.out.println((i+1) + ". " + available.get(i).getSpotInfo());
            }
            if (available.size() > 10) {
                System.out.println("... and " + (available.size() - 10) + " more");
            }
        }
    }
}