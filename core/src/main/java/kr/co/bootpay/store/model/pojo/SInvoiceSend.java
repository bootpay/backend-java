package kr.co.bootpay.store.model.pojo;

import java.util.Map;

public class SInvoiceSend {
    public String invoiceSendId;
    public String invoiceId;
    public String projectId;
    public String sellerId;

    public Integer type;
    public Integer sentType;
    public Integer status;

    public String executeAt;
    public Map<String, Object> response;
    public Map<String, Object> resultData;
    public Map<String, Object> revokeData;
}