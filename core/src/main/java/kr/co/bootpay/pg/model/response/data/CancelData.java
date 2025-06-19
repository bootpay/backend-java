package kr.co.bootpay.pg.model.response.data;

public class CancelData {
    public String receipt_id;
    public Double request_cancel_price;
    public Double remain_price;
    public Double remain_tax_free;
    public Double cancelled_price;
    public Double cancelled_tax_free;
    public String revoked_at;
    public String tid;
}