package kr.co.bootpay.store.model.pojo;


public class SOrderSubscriptionAdjustment {

    public String orderSubscriptionAdjustmentId;
    public String chosenProductOptionId;
    public String orderSubscriptionId;
    public String orderSubscriptionBillId;
    public String orderSubscriptionHistoryDataId;

    public int duration; // 결제 회차 (-1: 동일한 회차 결제, 양수: 회차별 할인/선결제 포함된 금액)

    public double price; // 할인 또는 추가 금액 (음수 또는 양수)
    public double taxFreePrice; // 회차별 비과세 금액
    public String name; // 할인 내용 코멘트
    public int type; // 할인, 선결제 등의 유형 (기본값: Const.SUBSCRIPTION_ADJUSTMENT_TYPE_PERIOD_DISCOUNT)

    public String createdAt; // LocalDateTime → String 변환


}

