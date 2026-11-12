package kr.co.bootpay.store.model.request.deposit;

/** 입금대기 등록 파라미터 (POST /v1/sdk/deposits) — supervisor 전용. */
public class DepositCreateParams {
    /** 가맹점 주문번호 (100자 이하, 필수) */
    public String merchantOrderId;
    public Number price;
    /** 은행 입금 알림과 맞춰 볼 입금자명 (필수, 50자까지 저장) */
    public String depositor;
    /** 미지정시 무통장 입금 */
    public String orderName;
    public String memo;
    /** 미지정시 입금자명 */
    public String buyerName;
    public String buyerPhone;
    /** 주면 입금 안내·확인 메일 (무료). 잘못된 형식은 18136. */
    public String buyerEmail;
    /** accounts() 의 id — 미지정시 대표 계좌 */
    public String accountId;
    /** 웹훅 서명 비밀값을 먼저 발급받아야 합니다 (18131). */
    public String webhookUrl;
    public DepositNotifyParams notify;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더) */
    public String idempotencyKey;
}
