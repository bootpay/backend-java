package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.AlimtalkSender;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkSenderCreateParams;

import java.util.Map;

/**
 * 알림톡 발신프로필(카카오채널) 모듈.
 *
 * <p>⚠️ {@link #otp} 는 채널 관리자 휴대폰으로 문자를 실제 발송하고, {@link #create} 는 카카오에
 * 발신프로필을 실제 등록한다. 샌드박스가 없다.</p>
 *
 * @since 3.6.0
 */
public class AlimtalkSenderModule {

    private final AlimtalkSender delegate;

    public AlimtalkSenderModule(BootpayStore bootpay) {
        this.delegate = new AlimtalkSender(bootpay);
    }

    /**
     * 카카오 카테고리 목록 조회 — 발신프로필 등록에 필요한 category_code 후보.
     *
     * @return 카테고리 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse categories() throws Exception {
        return CommerceResponses.of(delegate.categories());
    }

    /**
     * 채널 관리자폰으로 OTP 발송 — ⚠️ 실제로 문자가 나간다.
     *
     * @param yellowId 카카오채널 검색용 아이디
     * @param phone    채널 관리자 휴대폰번호
     * @return 발송 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse otp(String yellowId, String phone) throws Exception {
        return CommerceResponses.of(delegate.otp(yellowId, phone));
    }

    /**
     * 발신프로필 등록 — ⚠️ 카카오에 실제 등록된다.
     *
     * @param params 등록 파라미터
     * @return 등록 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse create(AlimtalkSenderCreateParams params) throws Exception {
        return CommerceResponses.of(delegate.create(params));
    }

    /**
     * 연동한 채널 목록 조회.
     *
     * @return {@code { list: [...], count: N }}
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list() throws Exception {
        return CommerceResponses.of(delegate.list());
    }

    /**
     * 채널 상세 조회 (자체 DB).
     *
     * @param kspId 채널 문서 id
     * @return 채널 상세
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String kspId) throws Exception {
        return CommerceResponses.of(delegate.detail(kspId));
    }

    /**
     * 채널 상세 조회.
     *
     * @param kspId 채널 문서 id
     * @param sync  true 면 벤더에서 채널 상태를 다시 읽어 반영한다 (느리다)
     * @return 채널 상세
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String kspId, Boolean sync) throws Exception {
        return CommerceResponses.of(delegate.detail(kspId, sync));
    }

    /**
     * 채널 연동 해지 — 이 프로젝트와의 연동만 끊는다 (채널 모델·템플릿은 보존).
     *
     * @param kspId 채널 문서 id
     * @return 해지 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse release(String kspId) throws Exception {
        return CommerceResponses.of(delegate.release(kspId));
    }

    /**
     * 채널 변수 예문 사전 갱신 (표시용, 부분 갱신). ⚠️ 발송값이 아니다.
     *
     * @param kspId    채널 문서 id
     * @param examples 변수 예문 — 키에 '.' 이나 선행 '$' 는 쓸 수 없다
     * @return 갱신 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse variableExamples(String kspId, Map<String, Object> examples) throws Exception {
        return CommerceResponses.of(delegate.variableExamples(kspId, examples));
    }
}
