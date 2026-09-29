package kr.co.bootpay.store.model.request.inquiry;


/**
 * 1:1 문의 목록 조회 파라미터 (GET /v1/inquiries)
 *
 * <p>회원 모드: {@code userId}(Bootpay 회원 _id 또는 외부 회원 ID) · {@code loginId} · {@code userJwt} 중
 * 하나로 회원을 정하고 본인 문의만 본다. {@code supervisor} 면 몰 전체 문의를 보며, 이때 {@code userId} 는
 * 작성 회원 필터(선택)다.</p>
 */
public class InquiryListParams {
    public String userId;
    public String loginId;
    /** 회원 JWT (Bootpay-User-JWT 헤더로 전송) */
    public String userJwt;
    /** true 답변 완료만 / false 미답변만 / null 전체 */
    public Boolean answered;
    /** 미지정시 1 */
    public Integer page;
    /** 미지정시 20 */
    public Integer limit;
    /** true 면 운영자 모드 — Bootpay-Role 을 supervisor 로 보낸다 */
    public Boolean supervisor;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더) */
    public String idempotencyKey;
}
