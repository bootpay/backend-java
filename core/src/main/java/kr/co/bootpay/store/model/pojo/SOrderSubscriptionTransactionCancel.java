package kr.co.bootpay.store.model.pojo;

import java.util.List;

public class SOrderSubscriptionTransactionCancel {

    public String orderSubscriptionTransactionCancelId;
    public String sellerId;
    public String projectId;

    public String orderId;
    public String orderSubscriptionId;
    public String orderSubscriptionTransactionId;

    // 결제 취소 관련 정보
    public Double price;
    public Double taxFreePrice;
    public Double fee;

    // 취소 요청 정보
    public String requester;
    public String message;
    public String ip;
    public int unit;

    // 결제 취소 실패 메시지
    public List<String> failedMessage;
    public String revokedAt;
    public String revokedReason;

    // 배송비 변동 금액
    public Double amountDeliveryFee;
    public Double discountPrice;

    // 승인 및 거절 정보
    public String adminName;
    public String rejectMessage;
    public String approvalAt;

    // 상태 정보
    public int status;

}
