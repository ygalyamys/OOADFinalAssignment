package service;

import entity.*;
import enums.SpotCategory;
import enums.SpotStatus;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class responsible for allocating parking spots to vehicles
 * Refactored to use OOP (polymorphism)
 */
public class SpotAllocator {
    private ParkingLot parkingLot;

    // Constructor
    public SpotAllocator(ParkingLot lot) {
        this.parkingLot = lot;
    }

    /**
     * Find suitable available spots for a given Vehicle
     * Uses polymorphism instead of String
     */
    public List<ParkingSpot> findSuitableSpots(Vehicle vehicle) {
        List<SpotCategory> suitableCategories = getSuitableCategories(vehicle);

        List<ParkingSpot> allSuitableSpots = new ArrayList<>();

        for (SpotCategory category : suitableCategories) {
            List<ParkingSpot> spotsOfCategory = parkingLot.findAvailableSpots(category);
            allSuitableSpots.addAll(spotsOfCategory);
        }

        return allSuitableSpots;
    }

    /**
     * Determine suitable categories using Vehicle object (OOP)
     */
    private List<SpotCategory> getSuitableCategories(Vehicle vehicle) {
    List<SpotCategory> suitable = new ArrayList<>();
    
    if (vehicle.isHandicappedCardHolder()) {
        suitable.add(SpotCategory.COMPACT);
        suitable.add(SpotCategory.REGULAR);
        suitable.add(SpotCategory.HANDICAPPED);
        suitable.add(SpotCategory.RESERVED);
        return suitable;
    }

    if (vehicle instanceof Motorcycle) {
        suitable.add(SpotCategory.COMPACT);

    } else if (vehicle instanceof Car) {
        suitable.add(SpotCategory.COMPACT);
        suitable.add(SpotCategory.REGULAR);

    } else if (vehicle instanceof SUV) {
        suitable.add(SpotCategory.REGULAR);

    } else {
        System.out.println("Unknown vehicle type: " + vehicle.getVehicleType());
    }

    return suitable;
}
    // Allocated Spot and create ticket

    public Ticket allocateSpot(Vehicle vehicle) {
    List<ParkingSpot> suitableSpots = findSuitableSpots(vehicle);

    if (suitableSpots.isEmpty()) {
        System.out.println("No available spot for " + vehicle.getPlateNumber());
        return null;
    }

    ParkingSpot spot = suitableSpots.get(0);

    if (spot.getStatus() == SpotStatus.OCCUPIED) {
        System.out.println("Error: Spot already occupied!");
        return null;
    }

    spot.occupy(vehicle.getPlateNumber());

    Ticket ticket = new Ticket(vehicle.getPlateNumber(),spot.getSpotID(),spot.getCategory());

    TicketFileService.saveTicket(ticket);

    System.out.println("Allocated " + spot.getSpotID() + " (" + spot.getCategory() + ")" + " to " + vehicle.getPlateNumber());

    System.out.println("Ticket Generated: " + ticket.getTicketID());

    return ticket;
}
    // Release Spot

    public boolean releaseSpot(String spotID) {
        ParkingSpot spot = parkingLot.getSpotByID(spotID);

        if (spot == null) {
            System.out.println("Error: Spot " + spotID + " not found!");
            return false;
        }

        spot.release();
        return true;
    }


    // Display available spots for a Vehicle

    public void displayAvailableSpots(Vehicle vehicle) {
        List<ParkingSpot> available = findSuitableSpots(vehicle);

        System.out.println("\n=== Available Spots for " + vehicle.getVehicleType().toUpperCase() + " ===");

        if (available.isEmpty()) {
            System.out.println("No available spots found!");
        } else {
            System.out.println("Found " + available.size() + " available spots:");
            for (int i = 0; i < Math.min(10, available.size()); i++) {
                System.out.println((i + 1) + ". " + available.get(i).getSpotInfo());
            }
            if (available.size() > 10) {
                System.out.println("... and " + (available.size() - 10) + " more");
            }
        }
    }
}
