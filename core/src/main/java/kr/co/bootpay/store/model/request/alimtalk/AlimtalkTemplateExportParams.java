package kr.co.bootpay.store.model.request.alimtalk;

/**
 * 알림톡 템플릿 내보내기 파라미터 (GET /v1/alimtalk/templates/export)
 *
 * <p>⚠️ SDK 기본 {@code format} 은 <b>json</b> 이다 — 서버 기본은 csv 지만 csv 본문은 JSON 이 아니라서
 * 공용 파싱 경로를 통과하지 못한다. {@code csv} 를 주면 파싱 없이 원문 문자열
 * ({@code { body, content_type }})을 담아 돌려준다.</p>
 *
 * <p>1회 5,000건을 넘으면 3031 로 거부되므로 채널·상태 필터로 좁힌다.</p>
 */
public class AlimtalkTemplateExportParams {
    /** json(SDK 기본) · csv */
    public String format;
    /** private(기본, 내 채널 자체 템플릿) · official(공식 카탈로그) · all */
    public String scope;
    public String kspId;
    public String status;
    public Boolean includeContent;
}
