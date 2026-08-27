package kr.co.bootpay.store.model.request.alimtalk;

/**
 * 알림톡 웹훅 전송 이력 조회 파라미터 (GET /v1/alimtalk/webhook/deliveries)
 */
public class AlimtalkWebhookDeliveriesParams {
    public Integer page;
    /** 서버 기본 20, 최대 100 */
    public Integer limit;
}
