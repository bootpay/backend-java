package kr.co.bootpay.model.response.data;

import java.util.HashMap;

public class CertificateData {
    public String receipt_id;
    public String order_id;
    public String pg;
    public String method;
    public String pg_name;
    public String method_name;

    public HashMap<String, Object> certificate;
    public HashMap<String, Object> payment_data;
}