package entity;

import java.time.LocalDateTime;

public class Payment {
    private final double amount;
    private final String methodName;
    private final LocalDateTime time;

    public Payment(double amount, String methodName, LocalDateTime time) {
        this.amount = amount;
        this.methodName = methodName;
        this.time = time;
    }

    public double getAmount() { return amount; }
    public String getMethodName() { return methodName; }
    public LocalDateTime getTime() { return time; }
}