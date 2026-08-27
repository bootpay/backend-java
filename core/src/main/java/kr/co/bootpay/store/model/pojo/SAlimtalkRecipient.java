package kr.co.bootpay.store.model.pojo;

import java.util.Map;

/**
 * 알림톡 벌크 발송의 수신자 한 건 (POST /v1/alimtalk/send/bulk 의 {@code recipients} 원소)
 */
public class SAlimtalkRecipient {
    /** 수신번호 */
    public String to;
    /** 이 수신자의 발송 식별자 — 건별 <b>멱등 키</b>다 */
    public String refId;
    /** 이 수신자에게 적용할 치환값 */
    public Map<String, Object> variables;

    public SAlimtalkRecipient() {}

    public SAlimtalkRecipient(String to) {
        this.to = to;
    }

    public SAlimtalkRecipient(String to, String refId, Map<String, Object> variables) {
        this.to = to;
        this.refId = refId;
        this.variables = variables;
    }
}
