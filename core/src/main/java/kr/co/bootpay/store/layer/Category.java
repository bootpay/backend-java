package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.category.CategoryCreateParams;
import kr.co.bootpay.store.model.request.category.CategoryUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.categories.SCategoryService;

public class Category {
    private final BootpayStore bootpay;

    public Category(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /**
     * 카테고리 트리 조회
     */
    public BootpayStoreResponse list() throws Exception {
        return SCategoryService.list(bootpay);
    }

    /**
     * 카테고리 단건 조회
     */
    public BootpayStoreResponse detail(String categoryId) throws Exception {
        return SCategoryService.detail(bootpay, categoryId);
    }

    /**
     * 카테고리 생성
     */
    public BootpayStoreResponse create(CategoryCreateParams params) throws Exception {
        return SCategoryService.create(bootpay, params);
    }

    /**
     * 카테고리 수정
     */
    public BootpayStoreResponse update(CategoryUpdateParams params) throws Exception {
        return SCategoryService.update(bootpay, params);
    }

    /**
     * 카테고리 삭제
     */
    public BootpayStoreResponse delete(String categoryId) throws Exception {
        return SCategoryService.delete(bootpay, categoryId);
    }
}
