package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.Category;
import kr.co.bootpay.store.model.request.category.CategoryCreateParams;
import kr.co.bootpay.store.model.request.category.CategoryUpdateParams;

/**
 * 카테고리 모듈.
 *
 * @since 3.3.0
 */
public class CategoryModule {

    private final Category delegate;

    public CategoryModule(BootpayStore bootpay) {
        this.delegate = new Category(bootpay);
    }

    /**
     * 카테고리 목록 조회.
     *
     * @return 카테고리 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list() throws Exception {
        return CommerceResponses.of(delegate.list());
    }

    /**
     * 카테고리 상세 조회.
     *
     * @param categoryId 카테고리 id
     * @return 카테고리 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String categoryId) throws Exception {
        return CommerceResponses.of(delegate.detail(categoryId));
    }

    /**
     * 카테고리 생성.
     *
     * @param params 생성 정보
     * @return 생성된 카테고리
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse create(CategoryCreateParams params) throws Exception {
        return CommerceResponses.of(delegate.create(params));
    }

    /**
     * 카테고리 수정.
     *
     * @param params 수정 정보
     * @return 수정된 카테고리
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse update(CategoryUpdateParams params) throws Exception {
        return CommerceResponses.of(delegate.update(params));
    }

    /**
     * 카테고리 삭제.
     *
     * @param categoryId 카테고리 id
     * @return 삭제 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse delete(String categoryId) throws Exception {
        return CommerceResponses.of(delegate.delete(categoryId));
    }
}
