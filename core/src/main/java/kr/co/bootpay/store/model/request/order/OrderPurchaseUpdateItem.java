package kr.co.bootpay.store.model.request.order;

public class OrderPurchaseUpdateItem {
    public String orderPurchaseNumber; // 발주 번호 (필수)
    public Integer status; // 발주 상태
    public String chosenProductOptionId; // 선택한 상품 옵션 id
    public Integer deliveryType; // 배송 방식
    public String deliveryCompanyCode; // 택배사 코드
    public String trackingNumber; // 운송장 번호
}
