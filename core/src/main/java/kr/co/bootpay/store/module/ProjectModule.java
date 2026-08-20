package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.Project;

/**
 * 프로젝트 모듈.
 *
 * @since 3.3.0
 */
public class ProjectModule {

    private final Project delegate;

    public ProjectModule(BootpayStore bootpay) {
        this.delegate = new Project(bootpay);
    }

    /**
     * 현재 인증 정보에 연결된 프로젝트 정보 조회.
     *
     * @return 프로젝트 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse me() throws Exception {
        return CommerceResponses.of(delegate.me());
    }
}
