package kr.co.bootpay.store.model.request.alimtalk;

/**
 * 알림톡 발송내역 목록 조회 파라미터 (GET /v1/alimtalk/messages)
 *
 * <p><b>유료</b> 알림톡만 조회된다 (무료 커머스 알림톡은 포함되지 않는다).
 * 상태는 벤더 결과 동기화로 확정되므로 접수 직후에는 {@code requested} 로 보인다.</p>
 *
 * <p>⚠️ 기간 기본값은 최근 30일이고 최대 조회 폭은 92일이다 — 초과분은 거부되지 않고 시작일이 당겨져 잘린다.
 * 실제 적용된 구간은 응답의 {@code period} 로 확인한다.</p>
 */
public class AlimtalkMessageListParams {
    /** 템플릿 코드 */
    public String templateCode;
    /** requested · success · failed · canceled */
    public String status;
    /** 발송 시 넘긴 멱등키 */
    public String refId;
    /** 수신번호 (하이픈 무관, 정확 매칭) */
    public String to;
    /** 조회 시작일 */
    public String sAt;
    /** 조회 종료일 */
    public String eAt;
    public Integer page;
    /** 서버 기본 20, 최대 100 */
    public Integer limit;
}
