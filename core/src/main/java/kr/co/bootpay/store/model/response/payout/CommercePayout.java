package kr.co.bootpay.store.model.response.payout;

import com.google.gson.annotations.SerializedName;

/** 입점사 지급 항목 — 등록·조회·목록 응답이 같은 형태입니다. */
public class CommercePayout {
    @SerializedName("id")
    public String id;
    @SerializedName("merchant_payout_id")
    public String merchantPayoutId;
    @SerializedName("sub_seller_id")
    public String subSellerId;
    @SerializedName("amount")
    public Long amount;
    @SerializedName("status")
    public Status status;
    /** 지급 예정일 (YYYY-MM-DD) */
    @SerializedName("payout_date")
    public String payoutDate;
    @SerializedName("order_number")
    public String orderNumber;
    @SerializedName("last_code")
    public String lastCode;
    @SerializedName("last_message")
    public String lastMessage;
    @SerializedName("created_at")
    public String createdAt;
    @SerializedName("updated_at")
    public String updatedAt;

    /** 지급 상태 — 서버 응답은 숫자가 아닌 문자열입니다. */
    public enum Status {
        @SerializedName("ready") READY("ready"),
        @SerializedName("held") HELD("held"),
        @SerializedName("sending") SENDING("sending"),
        @SerializedName("requested") REQUESTED("requested"),
        @SerializedName("processing") PROCESSING("processing"),
        @SerializedName("succeeded") SUCCEEDED("succeeded"),
        @SerializedName("failed") FAILED("failed"),
        @SerializedName("cancelled") CANCELLED("cancelled"),
        @SerializedName("unknown") UNKNOWN("unknown"),
        @SerializedName("void") VOID("void");

        private final String wireValue;

        Status(String wireValue) {
            this.wireValue = wireValue;
        }

        @Override
        public String toString() {
            return wireValue;
        }
    }
}
