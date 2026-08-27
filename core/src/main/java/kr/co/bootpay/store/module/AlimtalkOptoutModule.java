package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.AlimtalkOptout;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkOptoutListParams;

import java.util.List;

/**
 * 알림톡 수신거부 모듈 (가맹점 CRM 수신거부 동기화용).
 *
 * <p>발송 판정과 같은 기준으로 다룬다 — 부트페이 전역(global) + 내 프로젝트.
 * ⚠️ 전역 건은 조회는 되지만 해제할 수 없다.</p>
 *
 * @since 3.6.0
 */
public class AlimtalkOptoutModule {

    private final AlimtalkOptout delegate;

    public AlimtalkOptoutModule(BootpayStore bootpay) {
        this.delegate = new AlimtalkOptout(bootpay);
    }

    /**
     * 수신거부 목록 조회.
     *
     * @return {@code { list: [...], count, page }}
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list() throws Exception {
        return CommerceResponses.of(delegate.list());
    }

    /**
     * 수신거부 목록 조회.
     *
     * @param params {@code phone} 은 숫자만 남겨 부분일치로 찾는다
     * @return 수신거부 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list(AlimtalkOptoutListParams params) throws Exception {
        return CommerceResponses.of(delegate.list(params));
    }

    /**
     * 수신거부 등록 (멱등).
     *
     * @param phone 수신거부할 번호
     * @return 등록 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse create(String phone) throws Exception {
        return CommerceResponses.of(delegate.create(phone));
    }

    /**
     * 수신거부 등록 (멱등).
     *
     * @param phone  수신거부할 번호
     * @param reason 사유
     * @return 등록 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse create(String phone, String reason) throws Exception {
        return CommerceResponses.of(delegate.create(phone, reason));
    }

    /**
     * 발송 전 수신거부 사전 확인 (단건).
     *
     * @param phone 확인할 번호
     * @return {@code { list: [...], count, opted_out_count }}
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse check(String phone) throws Exception {
        return CommerceResponses.of(delegate.check(phone));
    }

    /**
     * 발송 전 수신거부 사전 확인 (다건) — ⚠️ 1회 최대 1,000건.
     *
     * @param phones 확인할 번호 목록
     * @return 확인 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse check(List<String> phones) throws Exception {
        return CommerceResponses.of(delegate.check(phones));
    }

    /**
     * 수신거부 해제 (멱등) — ⚠️ 전역 차단은 해제되지 않고 {@code global_blocked: true} 로 알려 준다.
     *
     * @param phone 해제할 번호
     * @return {@code { phone, released, global_blocked }}
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse release(String phone) throws Exception {
        return CommerceResponses.of(delegate.release(phone));
    }
}
