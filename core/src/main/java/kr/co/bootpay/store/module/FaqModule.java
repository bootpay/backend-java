package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.Faq;
import kr.co.bootpay.store.model.request.faq.FaqCreateParams;
import kr.co.bootpay.store.model.request.faq.FaqDetailParams;
import kr.co.bootpay.store.model.request.faq.FaqListParams;
import kr.co.bootpay.store.model.request.faq.FaqUpdateParams;

/**
 * FAQ 모듈.
 *
 * <p>조회는 고객·운영자 모드 둘 다, 등록·수정·삭제는 supervisor 전용입니다.</p>
 *
 * @since 3.7.0
 */
public class FaqModule {

    private final Faq delegate;

    public FaqModule(BootpayStore bootpay) {
        this.delegate = new Faq(bootpay);
    }

    /**
     * FAQ 목록 — page 1 / limit 20 이 기본값입니다.
     *
     * @return FAQ 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list() throws Exception {
        return CommerceResponses.of(delegate.list());
    }

    /**
     * FAQ 목록.
     *
     * @param params page · limit · keyword · view · supervisor
     * @return FAQ 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list(FaqListParams params) throws Exception {
        return CommerceResponses.of(delegate.list(params));
    }

    /**
     * FAQ 단건 — 고객 모드.
     *
     * @param faqId FAQ ID
     * @return FAQ
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String faqId) throws Exception {
        return CommerceResponses.of(delegate.detail(faqId));
    }

    /**
     * FAQ 단건.
     *
     * @param faqId      FAQ ID
     * @param supervisor true 면 운영자 모드로 조회합니다 (비공개 FAQ 포함)
     * @return FAQ
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String faqId, boolean supervisor) throws Exception {
        return CommerceResponses.of(delegate.detail(faqId, supervisor));
    }

    /**
     * FAQ 단건.
     *
     * @param params faqId · supervisor
     * @return FAQ
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(FaqDetailParams params) throws Exception {
        return CommerceResponses.of(delegate.detail(params));
    }

    /**
     * FAQ 등록 — supervisor 전용.
     *
     * @param params title · content · images · isDisplay
     * @return 등록된 FAQ
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse create(FaqCreateParams params) throws Exception {
        return CommerceResponses.of(delegate.create(params));
    }

    /**
     * FAQ 수정 — supervisor 전용, 보낸 필드만 바뀝니다.
     *
     * @param params faqId · title · content · images · isDisplay
     * @return 수정된 FAQ
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse update(FaqUpdateParams params) throws Exception {
        return CommerceResponses.of(delegate.update(params));
    }

    /**
     * FAQ 삭제 — supervisor 전용.
     *
     * @param faqId FAQ ID
     * @return 삭제 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse delete(String faqId) throws Exception {
        return CommerceResponses.of(delegate.delete(faqId));
    }

    /**
     * FAQ 삭제 — supervisor 전용.
     *
     * @param faqId          FAQ ID
     * @param idempotencyKey 미지정 시 자동 생성 (Idempotency-Key 헤더)
     * @return 삭제 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse delete(String faqId, String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.delete(faqId, idempotencyKey));
    }
}
