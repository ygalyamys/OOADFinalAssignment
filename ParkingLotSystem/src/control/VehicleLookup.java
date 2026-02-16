package control;

public interface VehicleLookup {
    /**
     * For demo, we just verify plate exists or return a type string.
     * Later Member 2 can replace with real Vehicle object.
     */
    String getVehicleType(String plateNo);
}