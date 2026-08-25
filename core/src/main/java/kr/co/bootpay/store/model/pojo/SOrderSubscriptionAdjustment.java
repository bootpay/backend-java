package kr.co.bootpay.store.model.pojo;


public class SOrderSubscriptionAdjustment {

    public String orderSubscriptionAdjustmentId;
//    public String chosenProductOptionId;
//    public String orderSubscriptionId;
//    public String orderSubscriptionBillId;
//    public String orderSubscriptionHistoryDataId;

    public int duration; // 결제 회차 (-1: 동일한 회차 결제, 양수: 회차별 할인/선결제 포함된 금액)

    public Double price; // 할인 또는 추가 금액 (음수 또는 양수)
    public Double taxFreePrice; // 회차별 비과세 금액
    public String name; // 할인 내용 코멘트
    public int type; // 할인, 선결제 등의 유형 (기본값: Const.SUBSCRIPTION_ADJUSTMENT_TYPE_PERIOD_DISCOUNT)

    /**
     * 범위 지정 시작 회차 (조정항목 생성 전용).
     *
     * <p>회차 지정 방법 3가지 (아래로 갈수록 넓다).</p>
     * <ul>
     *   <li>{@code duration = 5} → 5회차 한 건만</li>
     *   <li>{@code durationFrom = 3, durationTo = 7} → 3~7회차 각각 한 건씩 (총 5건)</li>
     *   <li>{@code durationFrom = 3, isUnlimited = true} → 3회차부터 계약 끝까지 (레코드는 1건, {@code durationTo} 는 무시)</li>
     * </ul>
     * <p>상한은 계약 총회차이며, 총회차가 무제한인 계약은 60회차까지다.
     * 이미 결제가 끝난 회차는 거절된다. 범위 중 한 회차라도 최종 금액이 음수면 전부 거절된다 (부분 반영 없음).</p>
     */
    public Integer durationFrom;
    /** 범위 지정 종료 회차 (조정항목 생성 전용, {@code isUnlimited = true} 이면 무시된다) */
    public Integer durationTo;
    /** {@code durationFrom} 회차부터 계약 끝까지 적용한다 (조정항목 생성 전용) */
    public Boolean isUnlimited;

    public String createdAt; // LocalDateTime → String 변환

    public SOrderSubscriptionAdjustment() {
    }

    public SOrderSubscriptionAdjustment(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public SOrderSubscriptionAdjustment(String name, Double price, Double taxFreePrice) {
        this.name = name;
        this.price = price;
        this.taxFreePrice = taxFreePrice;
    }
}

