package kr.co.bootpay.store.model.request.alimtalk;

/**
 * 부트페이 공식 알림톡 템플릿 검색 파라미터 (GET /v1/alimtalk/official)
 *
 * <p>{@code keyword} 는 본문·이름·분류를 부분일치(대소문자 무시)로 훑는다.
 * 서버는 {@code q} 를 먼저 보고 없으면 {@code keyword} 를 보므로, SDK 는 정본 키인 {@code q} 로 보낸다.</p>
 */
public class AlimtalkOfficialListParams {
    /** 검색어 — 서버에는 {@code q} 로 전송된다 */
    public String keyword;
    public String category;
    /** BA(기본형) · EX(부가정보형) 만 존재한다 — 그룹 템플릿이라 AD/MI 는 쓸 수 없다 */
    public String msgType;
    public Integer page;
    /** 서버 기본 20, 최대 100 으로 clamp */
    public Integer per;
    /** 주면 그 채널의 변수 예문 사전으로 variable_examples 를 채워 준다 (표시용) */
    public String kspId;
}
