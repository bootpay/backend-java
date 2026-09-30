package kr.co.bootpay.store.model.request.faq;


import java.util.List;

/**
 * FAQ 수정 파라미터 (PUT /v1/faqs/{faq_id}) — supervisor 전용
 *
 * <p>보낸 필드만 바뀐다. {@code images} 는 보내면 목록 전체를 교체한다 (빈 목록이면 모두 삭제).</p>
 */
public class FaqUpdateParams {
    public String faqId;
    public String title;
    public String content;
    /** 보내면 목록 전체를 교체한다 (빈 목록이면 모두 삭제) */
    public List<Object> images;
    public Boolean isDisplay;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더) */
    public String idempotencyKey;
}
