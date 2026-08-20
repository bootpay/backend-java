package kr.co.bootpay.store.model.pojo;

import java.util.List;

/**
 * 청구서 상품의 가격 조정 ({@code products[].price_adjustments[]}).
 *
 * <p>프로모션 단위로 묶이며, 실제 할인/부과 규칙은 {@link #cycles} 에 주기별로 담는다.</p>
 *
 * @since 3.3.0
 */
public class SInvoicePriceAdjustment {

    /** 가맹점이 관리하는 조정 식별자 */
    public String priceAdjustmentId;
    /** 조정 적용 시작 일시 */
    public String startAt;
    /** 조정 적용 종료 일시 */
    public String endAt;
    /** 프로모션 명칭 */
    public String name;
    /** 주기별 조정 규칙 */
    public List<SInvoicePriceAdjustmentCycle> cycles;
}
