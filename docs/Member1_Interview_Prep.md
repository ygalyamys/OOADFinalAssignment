# Member 1: Interview Preparation Notes

## My Responsibilities
I created the **core infrastructure** of the parking system:
- Entity classes (ParkingSpot, Floor, ParkingLot)
- Enumerations (SpotStatus, SpotCategory)
- Service layer (SpotAllocator)

## Design Decisions

### Q: Why use enums instead of inheritance for spot types?
**Answer:**
"I used enums because:
1. We have a **fixed set** of 4 spot types that won't change frequently
2. The spot types only differ in **rate and name**, not in behavior
3. Enums provide **type safety** - can't create invalid spot types
4. More **memory efficient** than creating separate classes for each type
5. Easy to extend - just add new values to the enum"

### Q: Explain the composition pattern in your design
**Answer:**
"The system uses **composition** (has-a relationship):
- ParkingLot **has** multiple Floors
- Floor **has** multiple ParkingSpots

This is better than inheritance because:
1. More flexible - can change floor/spot structure at runtime
2. Follows real-world model - a parking lot literally contains floors
3. Easier to maintain - changes to ParkingSpot don't affect Floor
4. Supports future expansion - easy to add new floor types"

### Q: How does your design support future enhancements?
**Answer:**
"The design is future-proof:

1. **Adding new vehicle type (Bus):**
   - Just add case in SpotAllocator.getSuitableCategories()
   - No changes to entity classes needed

2. **Adding new spot type (Electric Charging):**
   - Add to SpotCategory enum: `ELECTRIC("Electric", 8.0)`
   - Update getSuitableCategories() for which vehicles can use it
   - No structural changes needed

3. **Adding reservation system:**
   - Add `reservedBy` field to ParkingSpot
   - Add methods: `reserve()`, `isReserved()`
   - SpotAllocator checks reservation before allocation

4. **Multiple parking lots:**
   - Create ParkingLotManager class
   - Manages List<ParkingLot>
   - Each lot operates independently"

### Q: Explain encapsulation in your design
**Answer:**
"I used encapsulation by:
1. All fields are **private**
2. Access only through **public getter/setter methods**
3. Internal logic (like spot ID generation) is **private**
4. This protects data integrity - can't directly modify spotID after creation
5. Example: Can't set invalid status - must use occupy() or release() methods"

### Q: What design pattern did you use?
**Answer:**
"I implemented the **Composition pattern** as the main structural pattern.

Additionally, I would suggest **Singleton pattern** for ParkingLot:
- Only one parking lot instance should exist
- Provides global access point
- Prevents accidental multiple instances

Implementation:
````````java
public class ParkingLot {
    private static ParkingLot instance;
    
    private ParkingLot() { } // Private constructor
    
    public static ParkingLot getInstance() {
        if (instance == null) {
            instance = new ParkingLot(...);
        }
        return instance;
    }
}
```````"

### Q: Walk me through the vehicle entry process using your classes
**Answer:**
"1. User selects vehicle type (e.g., 'car')
2. SpotAllocator.findSuitableSpots('car') returns available spots
3. System displays spots to user
4. User selects spot (e.g., 'F1-R1-S1')
5. SpotAllocator.allocateSpot('F1-R1-S1', 'ABC1234') is called
6. Method finds the spot using ParkingLot.getSpotByID()
7. Calls spot.occupy('ABC1234')
8. Spot status changes from AVAILABLE to OCCUPIED
9. Returns true to confirm success"

### Q: How would you handle concurrent access?
**Answer:**
"To handle multiple users simultaneously:
1. Add **synchronization** to occupy() method
2. Use **locks** when allocating spots
3. Implement **transaction-like behavior** - check availability and occupy in atomic operation
4. Example:
``````java
public synchronized boolean allocateSpot(String spotID, String plate) {
    ParkingSpot spot = getSpotByID(spotID);
    if (spot != null && spot.getStatus() == AVAILABLE) {
        spot.occupy(plate);
        return true;
    }
    return false;
}
`````"

### Q: Explain polymorphism in your system
**Answer:**
"Currently, my classes use polymorphism indirectly through enums.

If we expand to different parking lot types:
````java
abstract class ParkingLot {
    abstract double calculateFee();
}

class StandardParkingLot extends ParkingLot { }
class PremiumParkingLot extends ParkingLot { }
````

Each can override calculateFee() with different logic.

Also applies to Vehicle classes (Member 2's responsibility):
````java
List<Vehicle> vehicles = new ArrayList<>();
vehicles.add(new Car());
vehicles.add(new Motorcycle());

for (Vehicle v : vehicles) {
    v.park(); // Polymorphic call
}
```"

## Code I Can Demonstrate
1. Creating a parking lot
2. Finding available spots
3. Allocating spots
4. Calculating occupancy
5. Releasing spots
6. Handling errors (duplicate allocation)

## Key Methods to Remember
- `ParkingLot(id, floors, rows, spots)` - Constructor
- `findAvailableSpots(category)` - Find by category
- `getSpotByID(spotID)` - Retrieve specific spot
- `getOccupancyRate()` - Calculate percentage
- `spot.occupy(vehicle)` - Park vehicle
- `spot.release()` - Free spot

## Potential Weak Points to Address
1. **No database** - Currently in-memory only
   - "For phase 1, we use in-memory storage for fast development"
   - "Can easily add database by creating Repository classes"

2. **No validation** - Limited input validation
   - "Can add validation layer before calling allocateSpot()"
   - "Example: Validate spotID format, check plate number format"

3. **Thread safety** - Not currently thread-safe
   - "Acknowledged limitation in current version"
   - "Would add synchronized blocks for production"
```

---

## ✅ FINAL CHECKLIST

### Files You Should Have Created:

#### In `src/enums/`:
- [ ] SpotStatus.java
- [ ] SpotCategory.java

#### In `src/entity/`:
- [ ] ParkingSpot.java
- [ ] Floor.java
- [ ] ParkingLot.java

#### In `src/service/`:
- [ ] SpotAllocator.java

#### In `src/`:
- [ ] TestMember1.java

#### In `docs/`:
- [ ] Member1_Integration.md
- [ ] Member1_Questions_For_Team.md
- [ ] Member1_Interview_Prep.md

### Verification Steps:
- [ ] All files compile without errors
- [ ] TestMember1 runs successfully
- [ ] Output shows all tests passing
- [ ] Occupancy calculation works
- [ ] Spot allocation and release work
- [ ] Can find spots by vehicle type

---

## 🎯 WHAT TO DO NEXT

### Before Team Meeting:
1. ✅ Run your test file one more time
2. ✅ Read through all your code
3. ✅ Review the integration document
4. ✅ Prepare your questions

### During Team Meeting:
1. Present your completed classes
2. Show the test results
3. Discuss integration points
4. Ask your questions
5. Take notes on other members' classes

### After Team Meeting:
1. Update your classes based on team decisions
2. Help integrate with other members
3. Start working on Class Diagram (your responsibility)
4. Prepare for the interview using your prep document

---

## 🎓 YOU'RE DONE!

Congratulations! You've successfully completed Member 1's responsibilities. Your foundation is solid and ready for the team to build upon!

**Remember:** During the interview, focus on:
- Why you made each design decision
- How your design supports future changes
- How OOP principles are applied
- How your classes work together

Good luck! 🚀