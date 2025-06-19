package kr.co.bootpay.pg.model.response.data;

import java.util.HashMap;

public class VerificationData {
    public String receipt_id;
    public String order_id;
    public String name;
    public String item_name;
    public Double price;
    public Double tax_free;
    public Double remain_price;
    public Double remain_tax_free;
    public Double cancelled_price;
    public Double cancelled_tax_free;

    public String receipt_url;
    public String unit;

    public String pg;
    public String method;
    public String pg_name;
    public String method_name;

    public HashMap<String, Object> params;
    public HashMap<String, Object> payment_data;


    public String requested_at;
    public String purchased_at;
    public String revoked_at;

    public int escrow_status;
    public String escrow_status_en;
    public String escrow_status_ko;

    public int status;
    public String status_en;
    public String status_ko;


//    public int retry_count;
//    public int status;
}