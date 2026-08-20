package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.Webhook;

/**
 * 웹훅 모듈.
 *
 * @since 3.3.0
 */
public class WebhookModule {

    private final Webhook delegate;

    public WebhookModule(BootpayStore bootpay) {
        this.delegate = new Webhook(bootpay);
    }

    /**
     * 웹훅 테스트 발송.
     *
     * @return 발송 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse sendTest() throws Exception {
        return CommerceResponses.of(delegate.sendTest());
    }

    /**
     * 웹훅 테스트 발송.
     *
     * @param headerContentType 전송 Content-Type 구분값
     * @return 발송 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse sendTest(Integer headerContentType) throws Exception {
        return CommerceResponses.of(delegate.sendTest(headerContentType));
    }

    /**
     * 웹훅 테스트 발송.
     *
     * @param headerContentType 전송 Content-Type 구분값
     * @param idempotencyKey    미지정 시 자동 생성 (Idempotency-Key 헤더)
     * @return 발송 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse sendTest(Integer headerContentType, String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.sendTest(headerContentType, idempotencyKey));
    }
}
