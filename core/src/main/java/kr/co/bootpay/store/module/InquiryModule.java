package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.Inquiry;
import kr.co.bootpay.store.model.request.inquiry.InquiryCreateParams;
import kr.co.bootpay.store.model.request.inquiry.InquiryDeleteParams;
import kr.co.bootpay.store.model.request.inquiry.InquiryDetailParams;
import kr.co.bootpay.store.model.request.inquiry.InquiryListParams;
import kr.co.bootpay.store.model.request.inquiry.InquiryUpdateParams;

/**
 * 1:1 문의 모듈.
 *
 * <p>작성·수정은 회원 전용, 답변은 supervisor 전용이고, 조회·삭제는 두 모드 모두 가능합니다.</p>
 *
 * @since 3.7.0
 */
public class InquiryModule {

    private final Inquiry delegate;

    public InquiryModule(BootpayStore bootpay) {
        this.delegate = new Inquiry(bootpay);
    }

    /**
     * 1:1 문의 목록 — page 1 / limit 20 이 기본값입니다.
     *
     * @return 문의 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list() throws Exception {
        return CommerceResponses.of(delegate.list());
    }

    /**
     * 1:1 문의 목록.
     *
     * @param params userId · loginId · userJwt · answered · page · limit · supervisor
     * @return 문의 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list(InquiryListParams params) throws Exception {
        return CommerceResponses.of(delegate.list(params));
    }

    /**
     * 1:1 문의 단건.
     *
     * @param inquiryId 문의 ID
     * @return 문의
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String inquiryId) throws Exception {
        return CommerceResponses.of(delegate.detail(inquiryId));
    }

    /**
     * 1:1 문의 단건.
     *
     * @param params inquiryId · userId · loginId · userJwt · supervisor
     * @return 문의
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(InquiryDetailParams params) throws Exception {
        return CommerceResponses.of(delegate.detail(params));
    }

    /**
     * 1:1 문의 작성 — 회원 전용.
     *
     * @param params content · userId · loginId · userJwt · title · productId · option · orderId
     * @return 작성된 문의
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse create(InquiryCreateParams params) throws Exception {
        return CommerceResponses.of(delegate.create(params));
    }

    /**
     * 1:1 문의 수정 — 작성 회원 본인, 답변 전만 가능합니다.
     *
     * @param params inquiryId · userId · loginId · userJwt · title · content
     * @return 수정된 문의
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse update(InquiryUpdateParams params) throws Exception {
        return CommerceResponses.of(delegate.update(params));
    }

    /**
     * 1:1 문의 삭제 — 달린 답변도 함께 삭제됩니다.
     *
     * @param inquiryId 문의 ID
     * @return 삭제 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse delete(String inquiryId) throws Exception {
        return CommerceResponses.of(delegate.delete(inquiryId));
    }

    /**
     * 1:1 문의 삭제 — 작성 회원 본인 또는 supervisor.
     *
     * @param params inquiryId · userId · loginId · userJwt · supervisor
     * @return 삭제 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse delete(InquiryDeleteParams params) throws Exception {
        return CommerceResponses.of(delegate.delete(params));
    }

    /**
     * 1:1 문의 답변 등록·수정 — supervisor 전용.
     *
     * @param inquiryId 문의 ID
     * @param content   답변 내용
     * @return 답변이 반영된 문의
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse answer(String inquiryId, String content) throws Exception {
        return CommerceResponses.of(delegate.answer(inquiryId, content));
    }

    /**
     * 1:1 문의 답변 등록·수정 — supervisor 전용.
     *
     * @param inquiryId      문의 ID
     * @param content        답변 내용
     * @param idempotencyKey 미지정 시 자동 생성 (Idempotency-Key 헤더)
     * @return 답변이 반영된 문의
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse answer(String inquiryId, String content, String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.answer(inquiryId, content, idempotencyKey));
    }
}
