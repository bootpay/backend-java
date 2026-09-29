package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.productQna.ProductQnaCreateParams;
import kr.co.bootpay.store.model.request.productQna.ProductQnaDeleteParams;
import kr.co.bootpay.store.model.request.productQna.ProductQnaDetailParams;
import kr.co.bootpay.store.model.request.productQna.ProductQnaListParams;
import kr.co.bootpay.store.model.request.productQna.ProductQnaUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.boards.SProductQnaService;

/**
 * 상품문의 모듈
 * /v1/product-qnas 계열
 *
 * <p>작성은 회원·비회원 모두 가능하고(몰이 비회원 작성을 허용할 때), 답변은 supervisor 전용이다.</p>
 */
public class ProductQna {
    private final BootpayStore bootpay;

    public ProductQna(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /**
     * 상품문의 목록 — page 1 / limit 20 이 기본값이다
     * @param productId 고객 모드에서는 필수
     */
    public BootpayStoreResponse list(String productId) throws Exception {
        ProductQnaListParams params = new ProductQnaListParams();
        params.productId = productId;
        return SProductQnaService.list(bootpay, params);
    }

    /** 상품문의 목록 */
    public BootpayStoreResponse list(ProductQnaListParams params) throws Exception {
        return SProductQnaService.list(bootpay, params);
    }

    /** 상품문의 단건 */
    public BootpayStoreResponse detail(String productQnaId) throws Exception {
        ProductQnaDetailParams params = new ProductQnaDetailParams();
        params.productQnaId = productQnaId;
        return SProductQnaService.detail(bootpay, params);
    }

    /** 상품문의 단건 */
    public BootpayStoreResponse detail(ProductQnaDetailParams params) throws Exception {
        return SProductQnaService.detail(bootpay, params);
    }

    /** 상품문의 작성 — 회원 또는 비회원 */
    public BootpayStoreResponse create(ProductQnaCreateParams params) throws Exception {
        return SProductQnaService.create(bootpay, params);
    }

    /** 상품문의 수정 — 작성 회원 본인 또는 비회원 비밀번호, 답변 전만 */
    public BootpayStoreResponse update(ProductQnaUpdateParams params) throws Exception {
        return SProductQnaService.update(bootpay, params);
    }

    /** 상품문의 삭제 */
    public BootpayStoreResponse delete(String productQnaId) throws Exception {
        ProductQnaDeleteParams params = new ProductQnaDeleteParams();
        params.productQnaId = productQnaId;
        return SProductQnaService.delete(bootpay, params);
    }

    /** 상품문의 삭제 — 작성 회원 본인 · 비회원 비밀번호 또는 supervisor */
    public BootpayStoreResponse delete(ProductQnaDeleteParams params) throws Exception {
        return SProductQnaService.delete(bootpay, params);
    }

    /** 상품문의 답변 등록·수정 — supervisor 전용 */
    public BootpayStoreResponse answer(String productQnaId, String content) throws Exception {
        return SProductQnaService.answer(bootpay, productQnaId, content, null);
    }

    /**
     * 상품문의 답변 등록·수정 — supervisor 전용
     * @param idempotencyKey 미지정시 자동 생성
     */
    public BootpayStoreResponse answer(String productQnaId, String content, String idempotencyKey) throws Exception {
        return SProductQnaService.answer(bootpay, productQnaId, content, idempotencyKey);
    }
}
