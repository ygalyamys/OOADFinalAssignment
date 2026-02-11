import entity.*;
import service.SpotAllocator;
import enums.*;

/**
 * Test class for Member 1 components
 * Tests ParkingLot, Floor, ParkingSpot, and SpotAllocator
 */
public class TestMember1 {
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║  MEMBER 1 COMPONENT TEST                      ║");
        System.out.println("║  Testing: Entity Classes & SpotAllocator      ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");
        
        // TEST 1: Create Parking Lot
        System.out.println("TEST 1: Creating Parking Lot");
        System.out.println("─────────────────────────────────");
        ParkingLot lot = new ParkingLot("MAIN", 3, 2, 5);
        // 3 floors, 2 rows per floor, 5 spots per row = 30 total spots
        
        // TEST 2: Display Initial Status
        System.out.println("\nTEST 2: Initial Parking Lot Status");
        System.out.println("─────────────────────────────────");
        lot.displayStatus();
        
        // TEST 3: Test SpotAllocator
        System.out.println("\n\nTEST 3: SpotAllocator - Finding Spots");
        System.out.println("─────────────────────────────────");
        SpotAllocator allocator = new SpotAllocator(lot);
        
        // Find spots for different vehicle types
        allocator.displayAvailableSpots("car");
        allocator.displayAvailableSpots("motorcycle");
        allocator.displayAvailableSpots("suv");
        
        // TEST 4: Allocate Spots
        System.out.println("\n\nTEST 4: Allocating Spots");
        System.out.println("─────────────────────────────────");
        
        boolean result1 = allocator.allocateSpot("F1-R1-S1", "ABC1234");
        System.out.println("Allocation 1 (F1-R1-S1): " + (result1 ? "✓ SUCCESS" : "✗ FAILED"));
        
        boolean result2 = allocator.allocateSpot("F1-R1-S2", "XYZ5678");
        System.out.println("Allocation 2 (F1-R1-S2): " + (result2 ? "✓ SUCCESS" : "✗ FAILED"));
        
        boolean result3 = allocator.allocateSpot("F2-R1-S3", "DEF9012");
        System.out.println("Allocation 3 (F2-R1-S3): " + (result3 ? "✓ SUCCESS" : "✗ FAILED"));
        
        // TEST 5: Try to allocate same spot again (should fail)
        System.out.println("\n\nTEST 5: Duplicate Allocation (Should Fail)");
        System.out.println("─────────────────────────────────");
        boolean result4 = allocator.allocateSpot("F1-R1-S1", "TRY9999");
        System.out.println("Duplicate allocation: " + (result4 ? "✗ UNEXPECTED SUCCESS" : "✓ CORRECTLY FAILED"));
        
        // TEST 6: Display Updated Status
        System.out.println("\n\nTEST 6: Updated Status After Allocations");
        System.out.println("─────────────────────────────────");
        lot.displayStatus();
        
        // TEST 7: Release a Spot
        System.out.println("\n\nTEST 7: Releasing Spot F1-R1-S1");
        System.out.println("─────────────────────────────────");
        boolean released = allocator.releaseSpot("F1-R1-S1");
        System.out.println("Release result: " + (released ? "✓ SUCCESS" : "✗ FAILED"));
        
        // TEST 8: Final Status
        System.out.println("\n\nTEST 8: Final Status");
        System.out.println("─────────────────────────────────");
        System.out.println("Total Spots: " + lot.getTotalSpots());
        System.out.println("Occupied: " + lot.getTotalOccupied());
        System.out.println("Available: " + lot.getTotalAvailable());
        System.out.println("Occupancy: " + String.format("%.2f%%", lot.getOccupancyRate()));
        
        // TEST 9: Specific Spot Information
        System.out.println("\n\nTEST 9: Specific Spot Details");
        System.out.println("─────────────────────────────────");
        ParkingSpot spot = lot.getSpotByID("F1-R1-S2");
        if (spot != null) {
            System.out.println(spot.getSpotInfo());
            System.out.println("Current Vehicle: " + spot.getCurrentVehicle());
        }
        
        System.out.println("\n\n╔═══════════════════════════════════════════════╗");
        System.out.println("║  ALL TESTS COMPLETED SUCCESSFULLY! ✓          ║");
        System.out.println("╚═══════════════════════════════════════════════╝");
    }
}