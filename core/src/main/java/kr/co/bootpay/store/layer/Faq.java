package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.faq.FaqCreateParams;
import kr.co.bootpay.store.model.request.faq.FaqDetailParams;
import kr.co.bootpay.store.model.request.faq.FaqListParams;
import kr.co.bootpay.store.model.request.faq.FaqUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.boards.SFaqService;

/**
 * FAQ 모듈
 * /v1/faqs 계열
 *
 * <p>조회는 고객·운영자 모드 둘 다, 등록·수정·삭제는 supervisor 전용이다.</p>
 */
public class Faq {
    private final BootpayStore bootpay;

    public Faq(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /** FAQ 목록 — page 1 / limit 20 이 기본값이다 */
    public BootpayStoreResponse list() throws Exception {
        return SFaqService.list(bootpay, null);
    }

    /** FAQ 목록 */
    public BootpayStoreResponse list(FaqListParams params) throws Exception {
        return SFaqService.list(bootpay, params);
    }

    /** FAQ 단건 — 고객 모드 */
    public BootpayStoreResponse detail(String faqId) throws Exception {
        return detail(faqId, false);
    }

    /**
     * FAQ 단건
     * @param supervisor true 면 운영자 모드로 조회한다 (비공개 FAQ 포함)
     */
    public BootpayStoreResponse detail(String faqId, boolean supervisor) throws Exception {
        FaqDetailParams params = new FaqDetailParams();
        params.faqId = faqId;
        params.supervisor = supervisor;
        return SFaqService.detail(bootpay, params);
    }

    /** FAQ 단건 */
    public BootpayStoreResponse detail(FaqDetailParams params) throws Exception {
        return SFaqService.detail(bootpay, params);
    }

    /** FAQ 등록 — supervisor 전용 */
    public BootpayStoreResponse create(FaqCreateParams params) throws Exception {
        return SFaqService.create(bootpay, params);
    }

    /** FAQ 수정 — supervisor 전용, 보낸 필드만 바뀐다 */
    public BootpayStoreResponse update(FaqUpdateParams params) throws Exception {
        return SFaqService.update(bootpay, params);
    }

    /** FAQ 삭제 — supervisor 전용 */
    public BootpayStoreResponse delete(String faqId) throws Exception {
        return SFaqService.delete(bootpay, faqId, null);
    }

    /**
     * FAQ 삭제 — supervisor 전용
     * @param idempotencyKey 미지정시 자동 생성
     */
    public BootpayStoreResponse delete(String faqId, String idempotencyKey) throws Exception {
        return SFaqService.delete(bootpay, faqId, idempotencyKey);
    }
}
