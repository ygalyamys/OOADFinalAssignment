package service;

import entity.Bill;
import entity.Payment;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

//PaymentService processes payments using a pluggable PaymentMethod strategy.
public class PaymentService {


    //PaymentMethod interface, defining a strategy for processing payments.
    public interface PaymentMethod {
        String name();
        Payment pay(Bill bill, double amount);
    }

    //Cash implementation (simple: accept amount and create Payment entity).
    public static class CashPayment implements PaymentMethod {
        @Override public String name() { return "CASH"; }
        @Override public Payment pay(Bill bill, double amount) {
            // In real system, could validate cash received etc.
            return new Payment(amount, name(), LocalDateTime.now());
        }
    }

    //Card implementation if want to connect to bank gateway).
    public static class CardPayment implements PaymentMethod {
        @Override public String name() { return "CARD"; }
        @Override public Payment pay(Bill bill, double amount) {
            // TODO: integrate bank gateway / authorization later if needed
            return new Payment(amount, name(), LocalDateTime.now());
        }
    }

    // Registry of payment strategies (key = method name)
    private final Map<String, PaymentMethod> methods = new HashMap<>();

    public PaymentService() {
        // Register default methods
        register(new CashPayment());
        register(new CardPayment());
    }

    //egister new payment method (used to extend system without modifying core logic).
    public void register(PaymentMethod method) {
        methods.put(method.name().toUpperCase(), method);
    }

    /**
     * Main API used by ExitController:
     * - Select method by name
     * - Delegate to strategy
     */
    public Payment takePayment(Bill bill, String methodName, double amount) {
        PaymentMethod method = methods.get(methodName.toUpperCase());
        if (method == null) throw new IllegalArgumentException("Unknown payment method: " + methodName);
        return method.pay(bill, amount);
    }
}
