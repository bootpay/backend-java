package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.ProductQna;
import kr.co.bootpay.store.model.request.productQna.ProductQnaCreateParams;
import kr.co.bootpay.store.model.request.productQna.ProductQnaDeleteParams;
import kr.co.bootpay.store.model.request.productQna.ProductQnaDetailParams;
import kr.co.bootpay.store.model.request.productQna.ProductQnaListParams;
import kr.co.bootpay.store.model.request.productQna.ProductQnaUpdateParams;

/**
 * 상품문의 모듈.
 *
 * <p>작성은 회원·비회원 모두 가능하고(몰이 비회원 작성을 허용할 때), 답변은 supervisor 전용입니다.</p>
 *
 * @since 3.7.0
 */
public class ProductQnaModule {

    private final ProductQna delegate;

    public ProductQnaModule(BootpayStore bootpay) {
        this.delegate = new ProductQna(bootpay);
    }

    /**
     * 상품문의 목록 — page 1 / limit 20 이 기본값입니다.
     *
     * @param productId 상품 ID (고객 모드에서는 필수)
     * @return 상품문의 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list(String productId) throws Exception {
        return CommerceResponses.of(delegate.list(productId));
    }

    /**
     * 상품문의 목록.
     *
     * @param params productId · view · page · limit · userId · loginId · userJwt · supervisor
     * @return 상품문의 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list(ProductQnaListParams params) throws Exception {
        return CommerceResponses.of(delegate.list(params));
    }

    /**
     * 상품문의 단건.
     *
     * @param productQnaId 상품문의 ID
     * @return 상품문의
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String productQnaId) throws Exception {
        return CommerceResponses.of(delegate.detail(productQnaId));
    }

    /**
     * 상품문의 단건.
     *
     * @param params productQnaId · userId · loginId · userJwt · supervisor
     * @return 상품문의
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(ProductQnaDetailParams params) throws Exception {
        return CommerceResponses.of(delegate.detail(params));
    }

    /**
     * 상품문의 작성 — 회원 또는 비회원.
     *
     * @param params productId · content · title · productOptionId · optionText · isSecret ·
     *               guestName · guestPassword · userId · loginId · userJwt
     * @return 작성된 상품문의
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse create(ProductQnaCreateParams params) throws Exception {
        return CommerceResponses.of(delegate.create(params));
    }

    /**
     * 상품문의 수정 — 작성 회원 본인 또는 비회원 비밀번호, 답변 전만 가능합니다.
     *
     * @param params productQnaId · title · content · isSecret · guestPassword · userId · loginId · userJwt
     * @return 수정된 상품문의
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse update(ProductQnaUpdateParams params) throws Exception {
        return CommerceResponses.of(delegate.update(params));
    }

    /**
     * 상품문의 삭제.
     *
     * @param productQnaId 상품문의 ID
     * @return 삭제 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse delete(String productQnaId) throws Exception {
        return CommerceResponses.of(delegate.delete(productQnaId));
    }

    /**
     * 상품문의 삭제 — 작성 회원 본인 · 비회원 비밀번호 또는 supervisor.
     *
     * @param params productQnaId · guestPassword · userId · loginId · userJwt · supervisor
     * @return 삭제 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse delete(ProductQnaDeleteParams params) throws Exception {
        return CommerceResponses.of(delegate.delete(params));
    }

    /**
     * 상품문의 답변 등록·수정 — supervisor 전용.
     *
     * @param productQnaId 상품문의 ID
     * @param content      답변 내용
     * @return 답변이 반영된 상품문의
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse answer(String productQnaId, String content) throws Exception {
        return CommerceResponses.of(delegate.answer(productQnaId, content));
    }

    /**
     * 상품문의 답변 등록·수정 — supervisor 전용.
     *
     * @param productQnaId   상품문의 ID
     * @param content        답변 내용
     * @param idempotencyKey 미지정 시 자동 생성 (Idempotency-Key 헤더)
     * @return 답변이 반영된 상품문의
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse answer(String productQnaId, String content, String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.answer(productQnaId, content, idempotencyKey));
    }
}
