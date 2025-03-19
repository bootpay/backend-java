package kr.co.bootpay.store.model.pojo;

public class SOrderSubscriptionAction {
    public String orderSubscriptionActionId;
    public String orderSubscriptionId;
    public String orderSubscriptionBillId;

    public String sellerId;
    public String projectId;
    public String userId;
    public String userGroupId;
    public String message;

    public int type;
    public String approvalByAdminId;
    public String approvalAt; // LocalDateTime → String
    public String approvalMessage;

    // 돈 계산 관련
    public double cancelFee;
    public double cancelFeeTaxFree;
    public double fixedCancelFee;
    public double fixedCancelFeeTaxFree;
    public double extendFee;
    public double extendFeeTaxFree;
    public double returnFee;
    public double returnFeeTaxFree;
    public double purchaseFee;
    public double purchaseFeeTaxFree;

    public int status;

    // 청구 관련
    public String invoiceFixedCancelId;
    public String invoiceExtendId;
    public String invoiceReturnId;
    public String invoicePurchaseId;
}
