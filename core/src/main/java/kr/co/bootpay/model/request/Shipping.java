package kr.co.bootpay.model.request;

public class Shipping {
    public String receiptId; // 부트페이에서 발급한 영수증 id
    public String deliveryCorp; // 택배 회사명
    public String trackingNumber; // 송장번호
    public boolean shippingPrepayment;
    public boolean shippingDay;
    public ShippingUser user;
    public ShippingCompany company;

    public String redirectUrl; // 구매확정, 구매취소시 해당 주소로 {"receipt_id": 값} 이 전달됨, 이니시스만 가능
}
