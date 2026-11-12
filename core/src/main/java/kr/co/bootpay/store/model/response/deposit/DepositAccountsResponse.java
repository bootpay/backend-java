package kr.co.bootpay.store.model.response.deposit;

import com.google.gson.annotations.SerializedName;

/** 판매자 입금 계좌 목록 응답. */
public class DepositAccountsResponse {
    @SerializedName("accounts")
    public java.util.List<DepositAccount> accounts;
}
