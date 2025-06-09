package kr.co.bootpay.store.model.pojo;

import java.util.List;
import java.util.UUID;

public class SOrderCancellationRequestHistory {
    public int orderRequestType;
    public String cancelId;
    public double cancelBeforePrice;
    public double cancelBeforeTaxFreePrice;

    public double cancelPrice;
    public double cancelTaxFreePrice;

    public double cancelAfterPrice; // 결제 취소 후 금액

    public double cancelAfterTaxFreePrice; // 결제 취소 후 비과세 금액

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
