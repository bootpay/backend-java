package kr.co.bootpay.store.model.pojo;

import java.util.List;
import java.util.UUID;

public class SOrderCancellationRequestHistory {
    public int orderRequestType;
    public String cancelId;
    public Double cancelBeforePrice;
    public Double cancelBeforeTaxFreePrice;

    public Double cancelPrice;
    public Double cancelTaxFreePrice;

    public Double cancelAfterPrice; // 결제 취소 후 금액

    public Double cancelAfterTaxFreePrice; // 결제 취소 후 비과세 금액

    public String cancelRequester;

    public String cancelMessage;

    public boolean cancelImmediately;

    public boolean isPartialCancelled;

    public String actionAt;

    public String actionMessage;

    public String lastErroredAt;

    public String withdrawnAt;

    public int status;
}
