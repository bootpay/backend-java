package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkOptoutListParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.alimtalk.SAlimtalkOptoutService;

import java.util.List;

/**
 * 알림톡 수신거부 모듈 (가맹점 CRM 수신거부 동기화용)
 * /v1/alimtalk/optouts 계열
 *
 * <p>발송 판정과 <b>같은 기준</b>으로 다룬다 — 부트페이 전역(global) + 내 프로젝트.
 * ⚠️ 전역 건은 <b>조회는 되지만 해제할 수 없다</b>({@code releasable: false}).</p>
 */
public class AlimtalkOptout {
    private final BootpayStore bootpay;

    public AlimtalkOptout(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /** 수신거부 목록 조회 */
    public BootpayStoreResponse list() throws Exception {
        return SAlimtalkOptoutService.list(bootpay, null);
    }

    /** 수신거부 목록 조회 — phone 은 숫자만 남겨 부분일치로 찾는다 */
    public BootpayStoreResponse list(AlimtalkOptoutListParams params) throws Exception {
        return SAlimtalkOptoutService.list(bootpay, params);
    }

    /** 수신거부 등록 (멱등) */
    public BootpayStoreResponse create(String phone) throws Exception {
        return SAlimtalkOptoutService.create(bootpay, phone, null);
    }

    /** 수신거부 등록 (멱등) */
    public BootpayStoreResponse create(String phone, String reason) throws Exception {
        return SAlimtalkOptoutService.create(bootpay, phone, reason);
    }

    /** 발송 전 수신거부 사전 확인 (단건) */
    public BootpayStoreResponse check(String phone) throws Exception {
        return SAlimtalkOptoutService.check(bootpay, null, phone);
    }

    /** 발송 전 수신거부 사전 확인 (다건) — ⚠️ 1회 최대 1,000건 */
    public BootpayStoreResponse check(List<String> phones) throws Exception {
        return SAlimtalkOptoutService.check(bootpay, phones, null);
    }

    /**
     * 수신거부 해제 (멱등)
     *
     * <p>⚠️ 전역 차단은 해제되지 않고 {@code global_blocked: true} 로 알려 준다.</p>
     */
    public BootpayStoreResponse release(String phone) throws Exception {
        return SAlimtalkOptoutService.release(bootpay, phone);
    }
}
