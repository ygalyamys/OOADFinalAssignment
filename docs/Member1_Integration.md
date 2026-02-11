# Member 1: Core Infrastructure - Integration Guide

## Overview
Member 1 has completed the **foundational entity classes** and **spot allocation service** for the parking lot system.

## Completed Components

### 1. Enumerations (`enums` package)
- **SpotStatus** - AVAILABLE, OCCUPIED
- **SpotCategory** - COMPACT, REGULAR, HANDICAPPED, RESERVED (with rates)

### 2. Entity Classes (`entity` package)
- **ParkingSpot** - Individual parking space
  - Attributes: spotID, status, category, currentVehicle, isReleased, hourlyRate
  - Methods: occupy(), release(), getters/setters
  
- **Floor** - One floor in the building
  - Manages multiple ParkingSpot objects
  - Methods: initializeSpots(), getAvailableSpots(), getSpotByID()
  
- **ParkingLot** - Main container
  - Manages multiple Floor objects
  - Methods: findAvailableSpots(), getSpotByID(), getOccupancyRate()

### 3. Service Classes (`service` package)
- **SpotAllocator** - Business logic for spot allocation
  - Methods: findSuitableSpots(), allocateSpot(), releaseSpot()

## Integration Points for Other Members

### For Member 2 (Vehicle & Entry Management)
**What you need:**
- Vehicle class hierarchy (Motorcycle, Car, SUV, HandicappedVehicle)
- Each vehicle should have: `String getPlateNo()`, `String getVehicleType()`

**How to integrate:**
````````java
// In SpotAllocator, change method signature:
public List<ParkingSpot> findSuitableSpots(Vehicle vehicle) {
    String vehicleType = vehicle.getVehicleType();
    // rest of logic remains same
}
````````

**Methods you can use:**
- `allocator.findSuitableSpots(vehicleType)` - Get available spots
- `allocator.allocateSpot(spotID, vehicle.getPlateNo())` - Park vehicle
- `spot.getSpotInfo()` - Display spot details

### For Member 3 (Billing & Payment)
**Methods you can use:**
- `spot.getHourlyRate()` - Get rate for calculation
- `spot.getCurrentVehicle()` - Get parked vehicle
- `spot.getCategory()` - Get spot type (for handicapped discount logic)
- `allocator.releaseSpot(spotID)` - After payment complete

**Example usage:**
````````java
ParkingSpot spot = lot.getSpotByID(spotID);
double rate = spot.getHourlyRate();
double totalFee = duration * rate;
````````

### For Member 4 (Fine Management)
**Methods you can use:**
- `spot.getCurrentVehicle()` - Get license plate for fine tracking
- Access vehicle entry time (you'll add this to Ticket class)

### For Member 5 (Admin & Reporting)
**Methods you can use:**
- `lot.getOccupancyRate()` - Overall occupancy percentage
- `lot.getTotalOccupied()` - Total occupied spots
- `lot.getTotalAvailable()` - Total available spots
- `floor.getOccupancyRate()` - Per-floor occupancy
- `lot.getFloors()` - Iterate through all floors
- `floor.getSpots()` - Iterate through all spots on a floor

**Example for revenue calculation:**
````````java
double totalRevenue = 0;
for (Floor floor : lot.getFloors()) {
    for (ParkingSpot spot : floor.getSpots()) {
        if (spot.getStatus() == SpotStatus.OCCUPIED) {
            // Calculate revenue using entry time, current time, and hourly rate
        }
    }
}
````````

## Vehicle Type → Spot Category Mapping

Based on assignment requirements:
- **Motorcycle** → COMPACT only
- **Car** → COMPACT or REGULAR
- **SUV/Truck** → REGULAR only
- **Handicapped** → ANY spot type (gets RM 2/hour rate in handicapped spots)

## Spot ID Format
All spot IDs follow this format: `F{floor}-R{row}-S{spot}`

Examples:
- `F1-R1-S1` = Floor 1, Row 1, Spot 1
- `F3-R2-S7` = Floor 3, Row 2, Spot 7

## Current Spot Distribution
The system creates a mix of spot types using this pattern:
- Every 10th spot = RESERVED (RM 10/hour)
- Every 7th spot (not 10th) = HANDICAPPED (RM 2/hour)
- Every 3rd spot (not 7th or 10th) = COMPACT (RM 2/hour)
- All others = REGULAR (RM 5/hour)

## Testing
Run `TestMember1.java` to verify all components work correctly:
````````bash
cd src
javac enums\*.java entity\*.java service\*.java TestMember1.java
java TestMember1
````````

## Next Steps
1. **Team Meeting** - Discuss integration points
2. **Member 2** - Create Vehicle classes that work with SpotAllocator
3. **Member 3** - Create Ticket and Payment classes
4. **Design Pattern** - Team decides on which pattern to implement

## Questions?
Contact Member 1 for any clarification on these classes.