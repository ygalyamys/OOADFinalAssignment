package observer;

import service.BillingService;
import service.FineService;

/*
 * Observer that tracks total revenue.
 * Updates when billing or fine data changes.
 */
public class RevenueObserver {

    private BillingService billingService;
    private FineService fineService;

    public RevenueObserver(BillingService billingService, FineService fineService) {
        this.billingService = billingService;
        this.fineService = fineService;
    }

    public double getTotalRevenue() {
        return billingService.getTotalRevenue() +
               fineService.getTotalCollectedFines();
    }
}
