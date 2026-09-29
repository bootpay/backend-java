package kr.co.bootpay.store.model.request.faq;


import java.util.List;

/**
 * FAQ 등록 파라미터 (POST /v1/faqs) — supervisor 전용
 */
public class FaqCreateParams {
    public String title;
    public String content;
    /** 이미지 목록 — 문자열(URL) 또는 { url: ... } 형태를 모두 받는다 */
    public List<Object> images;
    public Boolean isDisplay;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더) */
    public String idempotencyKey;
}
