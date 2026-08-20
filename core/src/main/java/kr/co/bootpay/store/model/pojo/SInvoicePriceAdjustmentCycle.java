package kr.co.bootpay.store.model.pojo;

/**
 * 청구서 상품의 가격 조정 주기 ({@code products[].price_adjustments[].cycles[]}).
 *
 * <p>구독 상품에서 "첫 달 20% 할인, 둘째 달 100원 할인, 도입비 500원" 같은 규칙을
 * 주기 단위로 기술한다.</p>
 *
 * @since 3.4.0
 */
public class SInvoicePriceAdjustmentCycle {

    public static final String ADJUSTMENT_TYPE_DISCOUNT_PERCENT = "discount_percent";
    public static final String ADJUSTMENT_TYPE_DISCOUNT_PRICE = "discount_price";
    public static final String ADJUSTMENT_TYPE_SETUP_FEE = "setup_fee";

    /** 이 조정이 적용될 기간 (개월 수) */
    public Integer duration;
    /** 조정 유형 — "discount_percent" / "discount_price" / "setup_fee" */
    public String adjustmentType;
    /** 조정 명칭 (예: "첫달 할인") */
    public String name;
    /** 조정 값 — percent 면 비율, 그 외에는 금액 */
    public Double value;
    /** 조정 금액 하한 */
    public Double minValue;
    /** 조정 금액 상한 */
    public Double maxValue;
}
