package entity;

import java.util.HashMap;
import java.util.Map;

public class FineLedger {
    private final Map<String, Double> outstandingByPlate = new HashMap<>();

    public void addOutstanding(String plateNo, double amount) {
        outstandingByPlate.put(plateNo, outstandingByPlate.getOrDefault(plateNo, 0.0) + amount);
    }

    public double getOutstanding(String plateNo) {
        return outstandingByPlate.getOrDefault(plateNo, 0.0);
    }

    public void clearOutstanding(String plateNo) {
        outstandingByPlate.remove(plateNo);
    }
}