package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.store.SStoreService;

/**
 * 가맹점(스토어) 정보 조회 모듈
 * 현재 인증된 가맹점의 기본/상세 정보를 조회할 수 있습니다.
 */
public class Store {
    private final BootpayStore bootpay;

    public Store(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /**
     * 가맹점 기본 정보를 조회합니다.
     *
     * @return BootpayStoreResponse 가맹점 기본 정보
     * @throws Exception 토큰이 없거나 API 호출 실패 시
     */
    public BootpayStoreResponse info() throws Exception {
        return SStoreService.info(bootpay);
    }

    /**
     * 가맹점 상세 정보를 조회합니다.
     *
     * @return BootpayStoreResponse 가맹점 상세 정보
     * @throws Exception 토큰이 없거나 API 호출 실패 시
     */
    public BootpayStoreResponse detail() throws Exception {
        return SStoreService.detail(bootpay);
    }
}
