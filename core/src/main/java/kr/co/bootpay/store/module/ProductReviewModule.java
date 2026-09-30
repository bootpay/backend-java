package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.ProductReview;
import kr.co.bootpay.store.model.request.productReview.ProductReviewCreateParams;
import kr.co.bootpay.store.model.request.productReview.ProductReviewDeleteParams;
import kr.co.bootpay.store.model.request.productReview.ProductReviewDetailParams;
import kr.co.bootpay.store.model.request.productReview.ProductReviewListParams;
import kr.co.bootpay.store.model.request.productReview.ProductReviewUpdateParams;

/**
 * 상품평 모듈.
 *
 * <p>작성·수정은 회원 전용, 판매자 답글은 supervisor 전용입니다.</p>
 *
 * @since 3.7.0
 */
public class ProductReviewModule {

    private final ProductReview delegate;

    public ProductReviewModule(BootpayStore bootpay) {
        this.delegate = new ProductReview(bootpay);
    }

    /**
     * 상품평 목록 — page 1 / limit 20 이 기본값입니다.
     *
     * @return 상품평 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list() throws Exception {
        return CommerceResponses.of(delegate.list());
    }

    /**
     * 상품평 목록.
     *
     * @param params page · limit · productId · view · userId · loginId · userJwt · supervisor
     * @return 상품평 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list(ProductReviewListParams params) throws Exception {
        return CommerceResponses.of(delegate.list(params));
    }

    /**
     * 상품평 단건.
     *
     * @param productReviewId 상품평 ID
     * @return 상품평
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String productReviewId) throws Exception {
        return CommerceResponses.of(delegate.detail(productReviewId));
    }

    /**
     * 상품평 단건.
     *
     * @param params productReviewId · userId · loginId · userJwt · supervisor
     * @return 상품평
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(ProductReviewDetailParams params) throws Exception {
        return CommerceResponses.of(delegate.detail(params));
    }

    /**
     * 상품평 작성 — 회원 전용.
     *
     * @param params orderId · productId · rating · content · productOptionId · images ·
     *               userId · loginId · userJwt
     * @return 작성된 상품평
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse create(ProductReviewCreateParams params) throws Exception {
        return CommerceResponses.of(delegate.create(params));
    }

    /**
     * 상품평 수정 — 작성 회원 본인, 작성 후 7일 이내.
     *
     * @param params productReviewId · rating · content · images · userId · loginId · userJwt
     * @return 수정된 상품평
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse update(ProductReviewUpdateParams params) throws Exception {
        return CommerceResponses.of(delegate.update(params));
    }

    /**
     * 상품평 삭제.
     *
     * @param productReviewId 상품평 ID
     * @return 삭제 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse delete(String productReviewId) throws Exception {
        return CommerceResponses.of(delegate.delete(productReviewId));
    }

    /**
     * 상품평 삭제 — 작성 회원 본인 또는 supervisor. 지급된 적립금 회수·상품 통계 차감이 함께 일어납니다.
     *
     * @param params productReviewId · reason · userId · loginId · userJwt · supervisor
     * @return 삭제 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse delete(ProductReviewDeleteParams params) throws Exception {
        return CommerceResponses.of(delegate.delete(params));
    }

    /**
     * 상품평 판매자 답글 등록·수정 — supervisor 전용 (10자 이상).
     *
     * @param productReviewId 상품평 ID
     * @param content         답글 내용
     * @return 답글이 반영된 상품평
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse reply(String productReviewId, String content) throws Exception {
        return CommerceResponses.of(delegate.reply(productReviewId, content));
    }

    /**
     * 상품평 판매자 답글 등록·수정 — supervisor 전용 (10자 이상).
     *
     * @param productReviewId 상품평 ID
     * @param content         답글 내용
     * @param idempotencyKey  미지정 시 자동 생성 (Idempotency-Key 헤더)
     * @return 답글이 반영된 상품평
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse reply(String productReviewId, String content, String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.reply(productReviewId, content, idempotencyKey));
    }
}
