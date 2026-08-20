package kr.co.bootpay.store.model.pojo;

/**
 * 청구서 생성 부가 옵션 ({@code extra}).
 *
 * @since 3.4.0
 */
public class SInvoiceExtra {

    /** 결제와 승인을 분리할지 여부 */
    public Boolean separatelyConfirmed;
    /** 청구서 생성과 동시에 주문을 만들지 여부 */
    public Boolean createOrderImmediately;
}
