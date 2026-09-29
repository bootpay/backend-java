package kr.co.bootpay.store.model.request.faq;


/**
 * FAQ 단건 조회 파라미터 (GET /v1/faqs/{faq_id})
 *
 * <p>비공개 FAQ 는 {@code supervisor} 일 때만 보인다 (아니면 404 POST_NOT_FOUND).</p>
 */
public class FaqDetailParams {
    public String faqId;
    /** true 면 운영자 모드 — Bootpay-Role 을 supervisor 로 보낸다 */
    public Boolean supervisor;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더) */
    public String idempotencyKey;
}
