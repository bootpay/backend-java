package kr.co.bootpay.store.model.request.inquiry;


/**
 * 1:1 문의 수정 파라미터 (PUT /v1/inquiries/{inquiry_id}) — 작성 회원 본인, 답변 전만
 *
 * <p>답변이 달린 문의는 409 INQUIRY_ALREADY_ANSWERED. {@code title} 에 빈 문자열을 보내면 제목을 지운다.</p>
 */
public class InquiryUpdateParams {
    public String inquiryId;
    public String userId;
    public String loginId;
    /** 회원 JWT (Bootpay-User-JWT 헤더로 전송) */
    public String userJwt;
    /** 빈 문자열을 보내면 제목을 지운다 */
    public String title;
    public String content;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더) */
    public String idempotencyKey;
}
