package kr.co.bootpay.store.model.request.alimtalk;

/**
 * 공식 템플릿 추천 파라미터 (POST /v1/alimtalk/official/recommend)
 *
 * <p>보내려는 문구와 유사한 공식 템플릿을 {@code score}(0~1) 내림차순으로 돌려준다.</p>
 */
public class AlimtalkOfficialRecommendParams {
    /** 보내려는 문구 */
    public String text;
    public String category;
    /** 서버 기본 5 */
    public Integer limit;
    public String kspId;
}
