package kr.co.bootpay.store.model.pojo;

import java.util.List;

public class SSubscriptionSetting {
    public String subscriptionSettingId;

    public String sellerId;  // seller_id
    public String projectId;  // project_id
    public String productId; // product_id
    public String productSnapshotIds; // product_snapshot_ids

    public int type = 1; // Const::SUBSCRIPTION_TYPE_REGULAR

    public boolean useSetupFee = false;
    public double setupFeeValue = 0;
    public int setupFeeType = 0; // Const::UNIT_TYPE_FIXED_PRICE
    public String setupFeeName;
    public String setupFeeText = "초기비용";

    public int subscriptionFeePayType = 1; // Const::FEE_PAY_TYPE_PREPAID

    public boolean useFirstDiscount = false;
    public int firstDiscountType = 0; // Const::UNIT_TYPE_FIXED_PRICE
    public double firstDiscountValue = 0;

    public boolean useApplyCoupon = false;
    public boolean useApplyPoint = false;
    public boolean useBackupPayment = false;

    public boolean useChangeProductOption = true;

    public boolean usePaymentDate = false;
    public int paymentDateType = 1; // Const::PAYMENT_DATE_TYPE_DATE
    public List<Integer> paymentDateValue;

    public List<Integer> paymentCycleTimes;
    public int paymentCycleValue = 1;
    public int paymentCycleType = 4; // Const::CYCLE_TYPE_1_MONTH

    public boolean useNonPayment = false;
    public boolean useAutoUnsubscribe = false;
    public int autoUnsubscribeDay = 3;
    public int autoUnsubscribeRetryCount = 3;

    public boolean useLateFee = false;
    public int lateFeeType = 1; // Const::LATE_FEE_TYPE_SINGLE
    public double lateFeeValue = 0;
    public int lateFeeValueType = 0;
    public int lateFeeDate = 1;
    public String lateFeeText = "연체료";

    public boolean useCancel = true;
    public boolean useCancelApproval = false;
    public boolean useCancelCollectReason = true;

    public boolean useCancelCondition = true;
    public int cancelConditionType = 1;
    public int cancelConditionValue = 0;

    public boolean useDepreciation = false;
    public float depreciationRate = 0.0f;

    public boolean useLastBillRefund = false;

    public boolean useTerminationFee = false;
    public String terminationFeeLabel = "중도해지 비용";
    public int terminationFeeType = 1;

    public boolean useFreeTrial = false;
    public int freeTrialDay = 0;

    public boolean useDeliveryChangeAddress = false;
    public int deliveryAfter = 2;
    public boolean useDeliveryChangePaymentValue = true;
    public boolean useDeliveryChangeSubscriptionValue = true;
    public boolean useDeliveryChangeDay = true;
    public boolean useDeliveryChangeTimesDelay = false;
    public boolean useDeliveryChangeTimesUrgent = false;
    public boolean useDeliveryRest = false;
    public List<String> deliveryRest;

    public boolean useExpired = false;

    public boolean useExpiredReturn = false;
    public boolean useExpiredReturnFee = false;
    public boolean useExpiredReturnFeeAuto = false;
    public int expiredReturnFeeType = 0;
    public double expiredReturnFeeValue = 0;

    public boolean useExpiredPurchase = false;
    public boolean useExpiredPurchaseFee = false;
    public boolean useExpiredPurchaseFeeAuto = false;
    public int expiredPurchaseFeeType = 0;
    public double expiredPurchaseFeeValue = 0;

    public boolean useExpiredExtend = false;
    public boolean useExpiredExtendFee = false;
    public boolean useExpiredExtendFeeAuto = false;
    public int expiredExtendFeeType = 0;
    public double expiredExtendFeeValue = 0;

    public boolean useExpiredExtendSale = false;
    public int expiredExtendSaleType = 0;
    public double expiredExtendSaleValue = 0;

    public String templateName;
    public int status = 1;
}
