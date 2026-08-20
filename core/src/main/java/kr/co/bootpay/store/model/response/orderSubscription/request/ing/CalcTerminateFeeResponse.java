package kr.co.bootpay.store.model.response.orderSubscription.request.ing;

public class CalcTerminateFeeResponse {
    public String orderSubscriptionId;
    public boolean useTermination;
    public boolean useTerminationApproval;
    public boolean useTerminationReason;
    public boolean useTerminationFee;
    public String terminationFeeLabel;
    public int terminationFeeType;
    public double terminationFeeValue;
    public boolean useLastBillRefund;
    public Integer remainingDuration;
    public double remainingPrice;
    public double terminationFee;
    public double lastBillRefundPrice;
    public double finalFee;
    public int subscriptionType;
    public double price;
    public int unit;
    public String orderName;
    public int subscriptionPaymentCycleType;
    public String originServiceStartAt;
    public String originServiceEndAt;
    public String serviceEndAt;
    public Boolean isUnlimited;
    public String cancelDate;
    public LastBillInfo lastBillInfo;

    public static class LastBillInfo {
        public double lastBillRefundPrice;
        public String serviceEndAt;
        public double deliveryFeeRefund;
        public double productRefundPrice;
        public int serviceDaysTotal;
        public int serviceDaysRemaining;
    }
}