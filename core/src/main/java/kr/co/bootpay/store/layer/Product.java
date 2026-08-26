package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SProduct;
import kr.co.bootpay.store.model.request.product.MallProductListParams;
import kr.co.bootpay.store.model.request.product.ProductListParams;
import kr.co.bootpay.store.model.request.product.ProductStatusParams;
import kr.co.bootpay.store.service.products.SProductService;

import java.net.URL;
import java.util.List;

public class Product {
    private final BootpayStore bootpay;

    public Product(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public BootpayStoreResponse list(ProductListParams params)  throws Exception {
        return SProductService.list(
                bootpay,
                params
        );
    }

    /**
     * 상품 목록 조회 (V1 Mall API)
     * GET /v1/products — page/limit 미지정시 각각 1 / 20 적용
     */
    public BootpayStoreResponse products() throws Exception {
        return SProductService.products(bootpay, null);
    }

    /**
     * 상품 목록 조회 (V1 Mall API)
     * @param params 조회 파라미터 (userJwt/idempotencyKey 는 헤더로 전송)
     */
    public BootpayStoreResponse products(MallProductListParams params) throws Exception {
        return SProductService.products(bootpay, params);
    }

    /**
     * 상품 생성 — 이미지 없이 JSON 으로 전송
     */
    public BootpayStoreResponse create(SProduct product) throws Exception {
        return SProductService.create(bootpay, product, null, null);
    }

    public BootpayStoreResponse create(SProduct product, List<URL> imagePaths)  throws Exception {
        return SProductService.create(
                bootpay,
                product,
                imagePaths
        );
    }

    /**
     * 상품 생성
     * @param idempotencyKey 미지정시 자동 생성
     */
    public BootpayStoreResponse create(SProduct product, List<URL> imagePaths, String idempotencyKey) throws Exception {
        return SProductService.create(bootpay, product, imagePaths, idempotencyKey);
    }

    public BootpayStoreResponse update(SProduct product)  throws Exception {
        return SProductService.update(
                bootpay,
                product
        );
    }

    /**
     * 상품 수정
     * @param idempotencyKey 미지정시 자동 생성
     */
    public BootpayStoreResponse update(SProduct product, String idempotencyKey) throws Exception {
        return SProductService.update(bootpay, product, idempotencyKey);
    }

    public BootpayStoreResponse detail(String productId) throws Exception {
        return SProductService.detail(
                bootpay,
                productId
        );
    }

    /**
     * 상품 상세 조회
     * GET /v1/products/{product_id} — {@code productDetail} 과 동작이 같다.
     * @param userJwt 회원 JWT (선택)
     */
    public BootpayStoreResponse detail(String productId, String userJwt) throws Exception {
        return SProductService.detail(bootpay, productId, userJwt, null);
    }

    /**
     * 상품 상세 조회
     * @param idempotencyKey 미지정시 자동 생성
     */
    public BootpayStoreResponse detail(String productId, String userJwt, String idempotencyKey) throws Exception {
        return SProductService.detail(bootpay, productId, userJwt, idempotencyKey);
    }

    /**
     * 상품 상세 조회 (V1 Mall API)
     * GET /v1/products/{product_id}
     * @param userJwt 회원 JWT (선택)
     */
    public BootpayStoreResponse productDetail(String productId, String userJwt) throws Exception {
        return SProductService.productDetail(bootpay, productId, userJwt, null);
    }

    /**
     * 상품 상세 조회 (V1 Mall API)
     * @param idempotencyKey 미지정시 자동 생성
     */
    public BootpayStoreResponse productDetail(String productId, String userJwt, String idempotencyKey) throws Exception {
        return SProductService.productDetail(bootpay, productId, userJwt, idempotencyKey);
    }

    public BootpayStoreResponse status(ProductStatusParams params) throws Exception {
        return SProductService.status(
                bootpay,
                params
        );
    }

    public BootpayStoreResponse delete(String productId) throws Exception {
        return SProductService.delete(
                bootpay,
                productId
        );
    }

    /**
     * 상품 삭제
     * @param idempotencyKey 미지정시 자동 생성
     */
    public BootpayStoreResponse delete(String productId, String idempotencyKey) throws Exception {
        return SProductService.delete(bootpay, productId, idempotencyKey);
    }


}
