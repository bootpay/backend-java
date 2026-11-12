package kr.co.bootpay.store.model.response.deposit;

import com.google.gson.annotations.SerializedName;

/** 주문에 연결된 입금 알림 — 웹훅 본문에만 실립니다. */
public class DepositNotice {
    @SerializedName("key")
    public String key;
    @SerializedName("bank_name")
    public String bankName;
    @SerializedName("account_tail")
    public String accountTail;
    @SerializedName("depositor")
    public String depositor;
    @SerializedName("amount")
    public Number amount;
    @SerializedName("received_at")
    public String receivedAt;
}
