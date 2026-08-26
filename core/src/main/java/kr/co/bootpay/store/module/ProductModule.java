package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.Product;
import kr.co.bootpay.store.model.pojo.SProduct;
import kr.co.bootpay.store.model.request.product.MallProductListParams;
import kr.co.bootpay.store.model.request.product.ProductListParams;
import kr.co.bootpay.store.model.request.product.ProductStatusParams;

import java.net.URL;
import java.util.List;

/**
 * 상품 모듈.
 *
 * <p>관리자용 조회({@code list} / {@code detail})와 쇼핑몰 프론트용 조회({@code mallList} /
 * {@code mallDetail})를 이름으로 구분합니다. 기존 표면의 {@code products} / {@code productDetail} 가
 * 이에 해당합니다.</p>
 *
 * @since 3.3.0
 */
public class ProductModule {

    private final Product delegate;

    public ProductModule(BootpayStore bootpay) {
        this.delegate = new Product(bootpay);
    }

    /**
     * 상품 목록 조회 (관리자).
     *
     * @param params 조회 조건
     * @return 상품 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list(ProductListParams params) throws Exception {
        return CommerceResponses.of(delegate.list(params));
    }

    /**
     * 상품 상세 조회 (관리자).
     *
     * @param productId 상품 id
     * @return 상품 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String productId) throws Exception {
        return CommerceResponses.of(delegate.detail(productId));
    }

    /**
     * 상품 상세 조회 (관리자).
     *
     * @param productId 상품 id
     * @param userJwt   회원 JWT (비회원이면 null)
     * @return 상품 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String productId, String userJwt) throws Exception {
        return CommerceResponses.of(delegate.detail(productId, userJwt));
    }

    /**
     * 상품 상세 조회 (관리자).
     *
     * @param productId      상품 id
     * @param userJwt        회원 JWT (비회원이면 null)
     * @param idempotencyKey 미지정 시 자동 생성 (Idempotency-Key 헤더)
     * @return 상품 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String productId, String userJwt, String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.detail(productId, userJwt, idempotencyKey));
    }

    /**
     * 상품 목록 조회 (쇼핑몰 프론트).
     *
     * @return 상품 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse mallList() throws Exception {
        return CommerceResponses.of(delegate.products());
    }

    /**
     * 상품 목록 조회 (쇼핑몰 프론트).
     *
     * @param params 조회 조건
     * @return 상품 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse mallList(MallProductListParams params) throws Exception {
        return CommerceResponses.of(delegate.products(params));
    }

    /**
     * 상품 상세 조회 (쇼핑몰 프론트).
     *
     * @param productId 상품 id
     * @param userJwt   회원 JWT (비회원이면 null)
     * @return 상품 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse mallDetail(String productId, String userJwt) throws Exception {
        return CommerceResponses.of(delegate.productDetail(productId, userJwt));
    }

    /**
     * 상품 상세 조회 (쇼핑몰 프론트).
     *
     * @param productId      상품 id
     * @param userJwt        회원 JWT (비회원이면 null)
     * @param idempotencyKey 미지정 시 자동 생성 (Idempotency-Key 헤더)
     * @return 상품 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse mallDetail(String productId, String userJwt, String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.productDetail(productId, userJwt, idempotencyKey));
    }

    /**
     * 상품 생성.
     *
     * @param product 생성할 상품
     * @return 생성된 상품
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse create(SProduct product) throws Exception {
        return CommerceResponses.of(delegate.create(product));
    }

    /**
     * 상품 생성 (이미지 포함).
     *
     * @param product    생성할 상품
     * @param imagePaths 첨부할 이미지 경로 목록
     * @return 생성된 상품
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse create(SProduct product, List<URL> imagePaths) throws Exception {
        return CommerceResponses.of(delegate.create(product, imagePaths));
    }

    /**
     * 상품 생성 (이미지 포함).
     *
     * @param product        생성할 상품
     * @param imagePaths     첨부할 이미지 경로 목록
     * @param idempotencyKey 미지정 시 자동 생성 (Idempotency-Key 헤더)
     * @return 생성된 상품
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse create(SProduct product, List<URL> imagePaths, String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.create(product, imagePaths, idempotencyKey));
    }

    /**
     * 상품 수정.
     *
     * @param product 수정할 상품
     * @return 수정된 상품
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse update(SProduct product) throws Exception {
        return CommerceResponses.of(delegate.update(product));
    }

    /**
     * 상품 수정.
     *
     * @param product        수정할 상품
     * @param idempotencyKey 미지정 시 자동 생성 (Idempotency-Key 헤더)
     * @return 수정된 상품
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse update(SProduct product, String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.update(product, idempotencyKey));
    }

    /**
     * 상품 판매 상태 변경.
     *
     * @param params 상태 변경 정보
     * @return 변경 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse status(ProductStatusParams params) throws Exception {
        return CommerceResponses.of(delegate.status(params));
    }

    /**
     * 상품 삭제.
     *
     * @param productId 상품 id
     * @return 삭제 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse delete(String productId) throws Exception {
        return CommerceResponses.of(delegate.delete(productId));
    }

    /**
     * 상품 삭제.
     *
     * @param productId      상품 id
     * @param idempotencyKey 미지정 시 자동 생성 (Idempotency-Key 헤더)
     * @return 삭제 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse delete(String productId, String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.delete(productId, idempotencyKey));
    }
}
