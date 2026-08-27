package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkOfficialListParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkOfficialRecommendParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.alimtalk.SAlimtalkOfficialService;

/**
 * 부트페이 공식 알림톡 템플릿 카탈로그 모듈
 * /v1/alimtalk/official 계열
 *
 * <p>부트페이가 미리 카카오 승인을 받아 둔 템플릿이라, 그룹키가 등록된 채널이면 <b>검수 없이 즉시 발송</b>된다.
 * {@code alimtalkSender.create} 로 채널을 등록하면 그룹 등록이 함께 끝나므로 따로 채택할 것이 없다.</p>
 */
public class AlimtalkOfficial {
    private final BootpayStore bootpay;

    public AlimtalkOfficial(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /** 공식 템플릿 전체 조회 */
    public BootpayStoreResponse list() throws Exception {
        return SAlimtalkOfficialService.list(bootpay, null);
    }

    /** 공식 템플릿 검색 */
    public BootpayStoreResponse list(AlimtalkOfficialListParams params) throws Exception {
        return SAlimtalkOfficialService.list(bootpay, params);
    }

    /** 보내려는 문구로 공식 템플릿 추천 */
    public BootpayStoreResponse recommend(AlimtalkOfficialRecommendParams params) throws Exception {
        return SAlimtalkOfficialService.recommend(bootpay, params);
    }

    /** 보내려는 문구로 공식 템플릿 추천 */
    public BootpayStoreResponse recommend(String text) throws Exception {
        AlimtalkOfficialRecommendParams params = new AlimtalkOfficialRecommendParams();
        params.text = text;
        return SAlimtalkOfficialService.recommend(bootpay, params);
    }

    /** 공식 템플릿 상세 조회 */
    public BootpayStoreResponse detail(String code) throws Exception {
        return SAlimtalkOfficialService.detail(bootpay, code, null);
    }

    /**
     * 공식 템플릿 상세 조회
     * @param kspId 주면 그 채널의 변수 예문 사전으로 variable_examples 를 채워 준다 (표시용)
     */
    public BootpayStoreResponse detail(String code, String kspId) throws Exception {
        return SAlimtalkOfficialService.detail(bootpay, code, kspId);
    }
}
