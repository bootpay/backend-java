package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkWebhookDeliveriesParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkWebhookUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.alimtalk.SAlimtalkWebhookService;

/**
 * 알림톡 발송결과·검수결과 웹훅 설정 모듈
 * /v1/alimtalk/webhook 계열
 *
 * <p>⚠️ <b>주문·구독 통합 웹훅({@code webhook.sendTest})과 완전히 별개다.</b>
 * 알림톡 이벤트를 기존 주문 웹훅 URL 로 태우면 그 수신 서버가 모르는 payload 를 받아 기존 연동이 깨진다.</p>
 */
public class AlimtalkWebhook {
    private final BootpayStore bootpay;

    public AlimtalkWebhook(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /** 웹훅 설정 조회 — 시크릿은 앞 12자만 노출된다 */
    public BootpayStoreResponse detail() throws Exception {
        return SAlimtalkWebhookService.detail(bootpay);
    }

    /** 웹훅 설정 저장 — url 은 https 만 허용한다 */
    public BootpayStoreResponse update(AlimtalkWebhookUpdateParams params) throws Exception {
        return SAlimtalkWebhookService.update(bootpay, params);
    }

    /** 웹훅 설정 저장 */
    public BootpayStoreResponse update(String url) throws Exception {
        AlimtalkWebhookUpdateParams params = new AlimtalkWebhookUpdateParams();
        params.url = url;
        return SAlimtalkWebhookService.update(bootpay, params);
    }

    /** 테스트 이벤트 1건 발송 — ⚠️ 설정된 URL 로 실제 HTTP 요청이 나간다 */
    public BootpayStoreResponse test() throws Exception {
        return SAlimtalkWebhookService.test(bootpay);
    }

    /** 서명 시크릿 재발급 — ⚠️ 이 응답에서만 secret 원문을 돌려준다 */
    public BootpayStoreResponse rotateSecret() throws Exception {
        return SAlimtalkWebhookService.rotateSecret(bootpay);
    }

    /** 웹훅 전송 이력 조회 */
    public BootpayStoreResponse deliveries() throws Exception {
        return SAlimtalkWebhookService.deliveries(bootpay, null);
    }

    /** 웹훅 전송 이력 조회 */
    public BootpayStoreResponse deliveries(AlimtalkWebhookDeliveriesParams params) throws Exception {
        return SAlimtalkWebhookService.deliveries(bootpay, params);
    }
}
