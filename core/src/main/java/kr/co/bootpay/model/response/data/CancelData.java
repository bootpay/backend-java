package kr.co.bootpay.model.response.data;

public class CancelData {
    public String receipt_id;
    public double request_cancel_price;
    public double remain_price;
    public double remain_tax_free;
    public double cancelled_price;
    public double cancelled_tax_free;
    public String revoked_at;
    public String tid;
}