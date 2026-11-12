package kr.co.bootpay.store.model.response.deposit;

import com.google.gson.annotations.SerializedName;

/** 웹훅 서명 비밀값 — 발급 응답에서만 원문을 받습니다. */
public class DepositWebhookSecretResponse {
    @SerializedName("secret")
    public String secret;
}
