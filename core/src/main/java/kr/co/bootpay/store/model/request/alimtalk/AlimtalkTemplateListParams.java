package kr.co.bootpay.store.model.request.alimtalk;

/**
 * 자체 알림톡 템플릿 목록 조회 파라미터 (GET /v1/alimtalk/templates)
 *
 * <p>⚠️ 페이지네이션이 없다 — 필터에 걸린 템플릿을 한 번에 모두 돌려준다.</p>
 */
public class AlimtalkTemplateListParams {
    /**
     * 검수상태 필터 — 1 REG(등록) / 2 REQ(검수요청) / 3 APR(승인) / 4 KRR(등록거절) / 5 REJ(승인반려).
     *
     * <p>숫자·숫자문자열·벤더 문자열('APR' 등)을 모두 받는다. 해석 못 하는 값은 필터 없음으로 떨어진다.</p>
     */
    public String ins;
    /** latest(기본) · oldest · code */
    public String sort;
    /** 코드·이름·본문·분류 부분일치 */
    public String keyword;
}
