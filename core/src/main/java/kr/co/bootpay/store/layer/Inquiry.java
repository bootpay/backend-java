package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.inquiry.InquiryCreateParams;
import kr.co.bootpay.store.model.request.inquiry.InquiryDeleteParams;
import kr.co.bootpay.store.model.request.inquiry.InquiryDetailParams;
import kr.co.bootpay.store.model.request.inquiry.InquiryListParams;
import kr.co.bootpay.store.model.request.inquiry.InquiryUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.boards.SInquiryService;

/**
 * 1:1 문의 모듈
 * /v1/inquiries 계열
 *
 * <p>작성·수정은 회원 전용, 답변은 supervisor 전용이고, 조회·삭제는 두 모드 모두 가능하다.</p>
 */
public class Inquiry {
    private final BootpayStore bootpay;

    public Inquiry(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /** 1:1 문의 목록 — page 1 / limit 20 이 기본값이다 */
    public BootpayStoreResponse list() throws Exception {
        return SInquiryService.list(bootpay, null);
    }

    /** 1:1 문의 목록 */
    public BootpayStoreResponse list(InquiryListParams params) throws Exception {
        return SInquiryService.list(bootpay, params);
    }

    /** 1:1 문의 단건 */
    public BootpayStoreResponse detail(String inquiryId) throws Exception {
        InquiryDetailParams params = new InquiryDetailParams();
        params.inquiryId = inquiryId;
        return SInquiryService.detail(bootpay, params);
    }

    /** 1:1 문의 단건 */
    public BootpayStoreResponse detail(InquiryDetailParams params) throws Exception {
        return SInquiryService.detail(bootpay, params);
    }

    /** 1:1 문의 작성 — 회원 전용 */
    public BootpayStoreResponse create(InquiryCreateParams params) throws Exception {
        return SInquiryService.create(bootpay, params);
    }

    /** 1:1 문의 수정 — 작성 회원 본인, 답변 전만 */
    public BootpayStoreResponse update(InquiryUpdateParams params) throws Exception {
        return SInquiryService.update(bootpay, params);
    }

    /** 1:1 문의 삭제 — 달린 답변도 함께 삭제된다 */
    public BootpayStoreResponse delete(String inquiryId) throws Exception {
        InquiryDeleteParams params = new InquiryDeleteParams();
        params.inquiryId = inquiryId;
        return SInquiryService.delete(bootpay, params);
    }

    /** 1:1 문의 삭제 — 작성 회원 본인 또는 supervisor */
    public BootpayStoreResponse delete(InquiryDeleteParams params) throws Exception {
        return SInquiryService.delete(bootpay, params);
    }

    /** 1:1 문의 답변 등록·수정 — supervisor 전용 */
    public BootpayStoreResponse answer(String inquiryId, String content) throws Exception {
        return SInquiryService.answer(bootpay, inquiryId, content, null);
    }

    /**
     * 1:1 문의 답변 등록·수정 — supervisor 전용
     * @param idempotencyKey 미지정시 자동 생성
     */
    public BootpayStoreResponse answer(String inquiryId, String content, String idempotencyKey) throws Exception {
        return SInquiryService.answer(bootpay, inquiryId, content, idempotencyKey);
    }
}
