package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.projects.SProjectService;

/**
 * 프로젝트 관련 API를 제공하는 레이어
 * SDK 초기화 시 설정한 프로젝트 정보를 조회할 수 있습니다.
 */
public class Project {
    private final BootpayStore bootpay;

    public Project(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /**
     * 현재 연결된 프로젝트 정보를 조회합니다.
     * SDK가 올바르게 설정되었는지 확인하거나, 프로젝트 정보를 표시할 때 사용합니다.
     *
     * @return BootpayStoreResponse 프로젝트 정보
     * @throws Exception 토큰이 없거나 API 호출 실패 시
     */
    public BootpayStoreResponse me() throws Exception {
        return SProjectService.me(bootpay);
    }
}
