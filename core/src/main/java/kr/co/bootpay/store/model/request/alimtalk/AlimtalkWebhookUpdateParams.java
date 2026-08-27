package kr.co.bootpay.store.model.request.alimtalk;

import java.util.List;

/**
 * 알림톡 웹훅 설정 저장 파라미터 (PUT /v1/alimtalk/webhook)
 *
 * <p>⚠️ <b>주문·구독 통합 웹훅과 완전히 별개다.</b> 알림톡 이벤트를 기존 주문 웹훅 URL 로 태우면 그 수신 서버가
 * 모르는 payload 를 받아 기존 연동이 깨진다. 그래서 수신 URL 을 따로 둔다
 * ({@code webhook.sendTest} 는 주문 웹훅용이다 — {@code alimtalkWebhook.test} 와 혼동하지 말 것).</p>
 */
public class AlimtalkWebhookUpdateParams {
    /** ⚠️ <b>https 만</b> 허용한다 (아니면 3028). 최초 저장 시 서명 시크릿이 자동 발급된다 */
    public String url;
    /**
     * 구독할 이벤트 코드. 목록에 없는 값은 저장 시 조용히 버려진다 (유령 구독 방지).
     *
     * <p>300 발송 접수(기본 미구독) / 301 전달 성공 / 302 전달 실패 / 303 예약 취소 /
     * 304 문자(LMS) 대체발송 전환 / 310 검수 승인 / 311 검수 반려 / 320 수신거부 등록(기본 미구독)</p>
     *
     * <p>비우면 기본 구독셋(301·302·303·304·310·311)이 적용된다.</p>
     */
    public List<Integer> events;
    public Boolean enabled;
}
