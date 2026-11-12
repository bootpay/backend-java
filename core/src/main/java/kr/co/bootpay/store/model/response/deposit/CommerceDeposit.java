package kr.co.bootpay.store.model.response.deposit;

import com.google.gson.annotations.SerializedName;

/** 입금대기 주문 — 등록·조회·취소 응답 및 입금 웹훅의 data. */
public class CommerceDeposit {
    @SerializedName("merchant_order_id")
    public String merchantOrderId;
    @SerializedName("order_number")
    public String orderNumber;
    @SerializedName("order_name")
    public String orderName;
    @SerializedName("amount")
    public Number amount;
    @SerializedName("status")
    public Status status;
    @SerializedName("buyer_name")
    public String buyerName;
    @SerializedName("depositor")
    public String depositor;
    @SerializedName("account")
    public DepositAccount account;
    @SerializedName("deadline")
    public String deadline;
    @SerializedName("deadline_label")
    public String deadlineLabel;
    @SerializedName("paid_at")
    public String paidAt;
    @SerializedName("notify")
    public Notify notify;
    @SerializedName("deposit")
    public DepositNotice deposit;

    public enum Status {
        @SerializedName("awaiting") AWAITING,
        @SerializedName("paid") PAID,
        @SerializedName("expired") EXPIRED,
        @SerializedName("cancelled") CANCELLED
    }

    /** 구매자 알림톡 설정 응답. */
    public static class Notify {
        @SerializedName("deposit_guide")
        public Boolean depositGuide;
        @SerializedName("deposit_paid")
        public Boolean depositPaid;
    }
}
