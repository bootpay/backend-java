package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.webhook.SWebhookService;

/**
 * 웹훅 모듈
 * POST /v1/webhook/test — 등록된 웹훅 URL 로 테스트 페이로드 발송
 */
public class Webhook {
    private final BootpayStore bootpay;

    public Webhook(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /**
     * 테스트 웹훅 발송 (서버 기본 Content-Type)
     */
    public BootpayStoreResponse sendTest() throws Exception {
        return SWebhookService.sendTest(bootpay, null, null);
    }

    /**
     * 테스트 웹훅 발송
     * @param headerContentType 웹훅 본문 Content-Type
     */
    public BootpayStoreResponse sendTest(Integer headerContentType) throws Exception {
        return SWebhookService.sendTest(bootpay, headerContentType, null);
    }

    /**
     * 테스트 웹훅 발송
     * @param headerContentType 웹훅 본문 Content-Type
     * @param idempotencyKey 미지정시 자동 생성
     */
    public BootpayStoreResponse sendTest(Integer headerContentType, String idempotencyKey) throws Exception {
        return SWebhookService.sendTest(bootpay, headerContentType, idempotencyKey);
    }
}
