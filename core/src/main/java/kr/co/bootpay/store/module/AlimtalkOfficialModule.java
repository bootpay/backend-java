package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.AlimtalkOfficial;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkOfficialListParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkOfficialRecommendParams;

/**
 * 부트페이 공식 알림톡 템플릿 카탈로그 모듈.
 *
 * <p>부트페이가 미리 카카오 승인을 받아 둔 템플릿이라, 그룹키가 등록된 채널이면 검수 없이 즉시 발송된다.</p>
 *
 * @since 3.6.0
 */
public class AlimtalkOfficialModule {

    private final AlimtalkOfficial delegate;

    public AlimtalkOfficialModule(BootpayStore bootpay) {
        this.delegate = new AlimtalkOfficial(bootpay);
    }

    /**
     * 공식 템플릿 전체 조회.
     *
     * @return {@code { list: [...], count, page, per, categories: [...] }}
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list() throws Exception {
        return CommerceResponses.of(delegate.list());
    }

    /**
     * 공식 템플릿 검색.
     *
     * @param params 검색 파라미터 ({@code keyword} 는 서버 정본 키인 {@code q} 로 전송된다)
     * @return 검색 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list(AlimtalkOfficialListParams params) throws Exception {
        return CommerceResponses.of(delegate.list(params));
    }

    /**
     * 보내려는 문구로 공식 템플릿 추천.
     *
     * @param text 보내려는 문구
     * @return 유사도 score(0~1) 내림차순 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse recommend(String text) throws Exception {
        return CommerceResponses.of(delegate.recommend(text));
    }

    /**
     * 보내려는 문구로 공식 템플릿 추천.
     *
     * @param params 추천 파라미터
     * @return 유사도 score(0~1) 내림차순 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse recommend(AlimtalkOfficialRecommendParams params) throws Exception {
        return CommerceResponses.of(delegate.recommend(params));
    }

    /**
     * 공식 템플릿 상세 조회.
     *
     * @param code 서버 채번 코드
     * @return 템플릿 상세
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String code) throws Exception {
        return CommerceResponses.of(delegate.detail(code));
    }

    /**
     * 공식 템플릿 상세 조회.
     *
     * @param code  서버 채번 코드
     * @param kspId 주면 그 채널의 변수 예문 사전으로 variable_examples 를 채워 준다 (표시용)
     * @return 템플릿 상세
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String code, String kspId) throws Exception {
        return CommerceResponses.of(delegate.detail(code, kspId));
    }
}
