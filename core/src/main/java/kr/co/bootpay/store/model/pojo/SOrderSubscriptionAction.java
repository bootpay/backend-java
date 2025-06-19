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
    public Double cancelFee;
    public Double cancelFeeTaxFree;
    public Double fixedCancelFee;
    public Double fixedCancelFeeTaxFree;
    public Double extendFee;
    public Double extendFeeTaxFree;
    public Double returnFee;
    public Double returnFeeTaxFree;
    public Double purchaseFee;
    public Double purchaseFeeTaxFree;

    public int status;

    // 청구 관련
    public String invoiceFixedCancelId;
    public String invoiceExtendId;
    public String invoiceReturnId;
    public String invoicePurchaseId;
}
