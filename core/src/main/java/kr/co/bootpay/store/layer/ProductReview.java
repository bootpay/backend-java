package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.productReview.ProductReviewCreateParams;
import kr.co.bootpay.store.model.request.productReview.ProductReviewDeleteParams;
import kr.co.bootpay.store.model.request.productReview.ProductReviewDetailParams;
import kr.co.bootpay.store.model.request.productReview.ProductReviewListParams;
import kr.co.bootpay.store.model.request.productReview.ProductReviewUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.boards.SProductReviewService;

/**
 * 상품평 모듈
 * /v1/reviews 계열
 *
 * <p>작성·수정은 회원 전용, 판매자 답글은 supervisor 전용이다.</p>
 */
public class ProductReview {
    private final BootpayStore bootpay;

    public ProductReview(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /** 상품평 목록 — page 1 / limit 20 이 기본값이다 */
    public BootpayStoreResponse list() throws Exception {
        return SProductReviewService.list(bootpay, null);
    }

    /** 상품평 목록 */
    public BootpayStoreResponse list(ProductReviewListParams params) throws Exception {
        return SProductReviewService.list(bootpay, params);
    }

    /** 상품평 단건 */
    public BootpayStoreResponse detail(String productReviewId) throws Exception {
        ProductReviewDetailParams params = new ProductReviewDetailParams();
        params.productReviewId = productReviewId;
        return SProductReviewService.detail(bootpay, params);
    }

    /** 상품평 단건 */
    public BootpayStoreResponse detail(ProductReviewDetailParams params) throws Exception {
        return SProductReviewService.detail(bootpay, params);
    }

    /** 상품평 작성 — 회원 전용 */
    public BootpayStoreResponse create(ProductReviewCreateParams params) throws Exception {
        return SProductReviewService.create(bootpay, params);
    }

    /** 상품평 수정 — 작성 회원 본인, 작성 후 7일 이내 */
    public BootpayStoreResponse update(ProductReviewUpdateParams params) throws Exception {
        return SProductReviewService.update(bootpay, params);
    }

    /** 상품평 삭제 */
    public BootpayStoreResponse delete(String productReviewId) throws Exception {
        ProductReviewDeleteParams params = new ProductReviewDeleteParams();
        params.productReviewId = productReviewId;
        return SProductReviewService.delete(bootpay, params);
    }

    /** 상품평 삭제 — 작성 회원 본인 또는 supervisor */
    public BootpayStoreResponse delete(ProductReviewDeleteParams params) throws Exception {
        return SProductReviewService.delete(bootpay, params);
    }

    /** 상품평 판매자 답글 등록·수정 — supervisor 전용 (10자 이상) */
    public BootpayStoreResponse reply(String productReviewId, String content) throws Exception {
        return SProductReviewService.reply(bootpay, productReviewId, content, null);
    }

    /**
     * 상품평 판매자 답글 등록·수정 — supervisor 전용 (10자 이상)
     * @param idempotencyKey 미지정시 자동 생성
     */
    public BootpayStoreResponse reply(String productReviewId, String content, String idempotencyKey) throws Exception {
        return SProductReviewService.reply(bootpay, productReviewId, content, idempotencyKey);
    }
}
