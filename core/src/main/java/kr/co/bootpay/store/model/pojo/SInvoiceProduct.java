package kr.co.bootpay.store.model.pojo;

import java.util.List;

/**
 * 청구서에 담는 상품 ({@code products[]}).
 *
 * <p>{@code invoice_items} 가 이름·금액을 직접 적어 넣는 방식이라면, 이쪽은 이미 등록된 상품을
 * 참조하는 방식이다. 구독 상품이면 {@link #duration} 으로 계약 기간을,
 * {@link #priceAdjustments} 로 프로모션을 지정한다.</p>
 *
 * @since 3.3.0
 */
public class SInvoiceProduct {

    /** 상품 id */
    public String productId;
    /** 상품 옵션 id */
    public String productOptionId;
    /** 계약 기간 (개월 수) */
    public Integer duration;
    /** 수량 */
    public Integer quantity;
    /** 가격 조정 (프로모션) */
    public List<SInvoicePriceAdjustment> priceAdjustments;
}
