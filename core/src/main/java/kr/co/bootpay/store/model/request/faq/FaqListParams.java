package kr.co.bootpay.store.model.request.faq;


import kr.co.bootpay.store.model.request.ListParams;

/**
 * FAQ 목록 조회 파라미터 (GET /v1/faqs)
 *
 * <p>고객 모드는 몰 FAQ 사용여부가 꺼져 있으면 BOARD_FEATURE_DISABLED 로 거절된다.</p>
 */
public class FaqListParams extends ListParams {
    /** true 면 운영자 모드 — Bootpay-Role 을 supervisor 로 보낸다 */
    public Boolean supervisor;
    /** 'all' 이면 비공개 FAQ 까지 조회한다 (운영자 모드에서만 의미가 있다) */
    public String view;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더로 전송, query 에는 포함되지 않는다) */
    public String idempotencyKey;
}
