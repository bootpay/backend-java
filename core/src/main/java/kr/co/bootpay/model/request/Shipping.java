package kr.co.bootpay.model.request;

public class Shipping {
    public String receiptId; // 부트페이에서 발급한 영수증 id
    public String deliveryCorp; // 택배 회사명
    public String trackingNumber; // 송장번호
    public boolean shippingPrepayment;
    public boolean shippingDay;
    public ShippingUser user;
    public ShippingCompany company;
}
