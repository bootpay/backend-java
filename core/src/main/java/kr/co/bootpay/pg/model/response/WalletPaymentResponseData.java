package kr.co.bootpay.pg.model.response;

import kr.co.bootpay.pg.model.response.data.CardData;
import kr.co.bootpay.pg.model.response.data.WalletData;

import java.util.Map;

public class WalletPaymentResponseData {
    public Double cancelled_price;
    public WalletData wallet_data;
    public Map<String, Object> metadata;
    public Double cancelled_tax_free;
    public String method;
    public CardData card_data;
    public boolean sandbox;
    public String receipt_id;
    public String method_origin;
    public String order_name;
    public String method_origin_symbol;
    public String receipt_url;
    public String method_symbol;
    public String purchased_at;
    public Double tax_free;
    public Double price;
    public String company_name;
    public String pg;
    public String status_locale;
    public String currency;
    public int http_status;
    public long order_id;
    public String requested_at;
    public int status;
}