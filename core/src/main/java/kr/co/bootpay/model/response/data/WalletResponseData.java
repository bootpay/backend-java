package kr.co.bootpay.model.response.data;

import java.util.HashMap;
import java.util.Map;

public class WalletResponseData {
    public String receipt_id;
    public String order_id;
    public double tax_free;
    public double remain_price;
    public double remain_tax_free;
    public double cancelled_price;
    public double cancelled_tax_free;
    public String order_name; // added
    public String company_name; // added

    public String gateway_url;
    public Map<String, Object> metadata = new HashMap<>();

    public boolean sandbox;

    public String pg;
    public String method;

    public String method_symbol;
    public String method_origin;

    public String method_origin_symbol;

    public String currency;

    public String purchased_at;

    public String cancelled_at;
    public String requested_at;
    public String receipt_url;
    public int status;

    public CardData card_data;
}