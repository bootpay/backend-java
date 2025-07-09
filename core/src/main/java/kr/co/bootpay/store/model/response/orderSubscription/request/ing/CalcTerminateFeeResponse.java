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
    public int remainingDuration;
    public double remainingPrice;
    public double terminationFee;
    public double lastBillRefundPrice;
    public double finalFee;
    public String serviceEndAt;
    public LastBillInfo lastBillInfo;

    public static class LastBillInfo {
        public double lastBillRefundPrice;
        public String serviceEndAt;
        public int serviceDaysTotal;
        public int serviceDaysRemaining;
    }
}