package enums;

/**
 * Enumeration for parking spot categories with hourly rates
 * As per client requirements in the assignment
 */
public enum SpotCategory {
    COMPACT("Compact", 2.0),           // RM 2/hour - for motorcycles
    REGULAR("Regular", 5.0),           // RM 5/hour - for cars
    HANDICAPPED("Handicapped", 2.0),   // RM 2/hour - for handicapped
    RESERVED("Reserved", 10.0);        // RM 10/hour - for VIP
    
    private final String displayName;
    private final double baseHourlyRate;
    
    // Constructor
    SpotCategory(String displayName, double rate) {
        this.displayName = displayName;
        this.baseHourlyRate = rate;
    }
    
    // Getters
    public String getDisplayName() {
        return displayName;
    }
    
    public double getBaseHourlyRate() {
        return baseHourlyRate;
    }
}