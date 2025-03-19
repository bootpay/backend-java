package kr.co.bootpay.store.model.pojo;

import java.util.List;
import java.util.UUID;


public class SOrderSubscription {
    public String orderSubscriptionId = UUID.randomUUID().toString();
    public String sellerId;
    public String projectId;
    public String orderId;
    public String orderPreId;
    public String userId;
    public String userGroupId;
    public String walletId;

    public int subscriptionBillingType;
    public int subscriptionPaymentCycleType;
    public int subscriptionPaymentDate;
    public int subscriptionBillingBaseDay;

    public int quantity = 1;
    public boolean isFirstPrepaid;

    public double oneUnitPrice;
    public double oneUnitTaxFreePrice;
    public double price;
    public double taxFreePrice;
    public double setupPrice;

    public int unit;
    public String orderName;
    public String productName;
    public List<String> optionNames;

    public String serviceStartAt;
    public String serviceEndAt;

    public String lastBillingCreatedAt;
    public String latestPurchasedAt;
    public String latestFailedAt;
    public String paymentNextAt;

    public int currentDuration = 1;
    public int createdLastDuration = 0;
    public int paymentLastDuration = 0;
    public int totalSubscriptionDuration = 0;

    public int membershipType;
    public boolean useSubscriptionTimes = false;

    public int renewalStatus;
    public int cancelStatus;
    public int status;
    public String cancelAt;
}

