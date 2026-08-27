package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkSenderCreateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.alimtalk.SAlimtalkSenderService;

import java.util.Map;

/**
 * 알림톡 발신프로필(카카오채널) 모듈
 * GET /v1/alimtalk/categories · /senders 계열
 *
 * <p>카테고리 조회 → OTP 발송 → 발신프로필 등록 → 목록/상세 → 연동 해지 순으로 쓴다.</p>
 * <p>⚠️ {@link #otp} 는 채널 관리자 휴대폰으로 <b>문자를 실제 발송</b>하고,
 * {@link #create} 는 카카오에 발신프로필을 <b>실제 등록</b>한다. 샌드박스가 없다.</p>
 */
public class AlimtalkSender {
    private final BootpayStore bootpay;

    public AlimtalkSender(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /** 카카오 카테고리 목록 조회 — 발신프로필 등록에 필요한 category_code 후보 */
    public BootpayStoreResponse categories() throws Exception {
        return SAlimtalkSenderService.categories(bootpay);
    }

    /** 채널 관리자폰으로 OTP 발송 — ⚠️ 실제로 문자가 나간다 */
    public BootpayStoreResponse otp(String yellowId, String phone) throws Exception {
        return SAlimtalkSenderService.otp(bootpay, yellowId, phone);
    }

    /** 발신프로필 등록 — ⚠️ 카카오에 실제 등록된다 */
    public BootpayStoreResponse create(AlimtalkSenderCreateParams params) throws Exception {
        return SAlimtalkSenderService.create(bootpay, params);
    }

    /** 연동한 채널 목록 조회 */
    public BootpayStoreResponse list() throws Exception {
        return SAlimtalkSenderService.list(bootpay);
    }

    /** 채널 상세 조회 (자체 DB) */
    public BootpayStoreResponse detail(String kspId) throws Exception {
        return SAlimtalkSenderService.detail(bootpay, kspId, null);
    }

    /**
     * 채널 상세 조회
     * @param sync true 면 벤더에서 채널 상태를 다시 읽어 반영한다 (느리다)
     */
    public BootpayStoreResponse detail(String kspId, Boolean sync) throws Exception {
        return SAlimtalkSenderService.detail(bootpay, kspId, sync);
    }

    /** 채널 연동 해지 — 이 프로젝트와의 연동만 끊는다 (채널 모델·템플릿은 보존) */
    public BootpayStoreResponse release(String kspId) throws Exception {
        return SAlimtalkSenderService.release(bootpay, kspId);
    }

    /**
     * 채널 변수 예문 사전 갱신 (표시용, 부분 갱신)
     *
     * <p>⚠️ 발송값이 아니다 — 벤더로 전송되지 않으므로 검수 상태와 무관하다.</p>
     */
    public BootpayStoreResponse variableExamples(String kspId, Map<String, Object> examples) throws Exception {
        return SAlimtalkSenderService.variableExamples(bootpay, kspId, examples);
    }
}
