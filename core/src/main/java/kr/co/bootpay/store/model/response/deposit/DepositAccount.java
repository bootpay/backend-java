package kr.co.bootpay.store.model.response.deposit;

import com.google.gson.annotations.SerializedName;

/** 판매자 입금 계좌 — 계좌 목록의 첫 항목이 대표 계좌(primary: true)입니다. */
public class DepositAccount {
    @SerializedName("id")
    public String id;
    @SerializedName("bank_name")
    public String bankName;
    @SerializedName("account_no")
    public String accountNo;
    @SerializedName("holder")
    public String holder;
    /** 대표 계좌 여부 — 계좌 목록(accounts()) 응답에만 실린다. 주문에 붙은 계좌 스냅샷에는 없다. */
    @SerializedName("primary")
    public Boolean primary;
}
