package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.AlimtalkWebhook;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkWebhookDeliveriesParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkWebhookUpdateParams;

/**
 * 알림톡 발송결과·검수결과 웹훅 설정 모듈.
 *
 * <p>⚠️ <b>주문·구독 통합 웹훅({@code webhook.sendTest})과 완전히 별개다.</b></p>
 *
 * @since 3.6.0
 */
public class AlimtalkWebhookModule {

    private final AlimtalkWebhook delegate;

    public AlimtalkWebhookModule(BootpayStore bootpay) {
        this.delegate = new AlimtalkWebhook(bootpay);
    }

    /**
     * 웹훅 설정 조회 — 시크릿은 앞 12자만 노출된다.
     *
     * @return 설정값 (미설정이면 {@code { configured: false }})
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail() throws Exception {
        return CommerceResponses.of(delegate.detail());
    }

    /**
     * 웹훅 설정 저장 — url 은 https 만 허용한다.
     *
     * @param url 수신 URL
     * @return 저장 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse update(String url) throws Exception {
        return CommerceResponses.of(delegate.update(url));
    }

    /**
     * 웹훅 설정 저장.
     *
     * @param params url · events · enabled
     * @return 저장 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse update(AlimtalkWebhookUpdateParams params) throws Exception {
        return CommerceResponses.of(delegate.update(params));
    }

    /**
     * 테스트 이벤트 1건 발송 — ⚠️ 설정된 URL 로 실제 HTTP 요청이 나간다.
     *
     * @return {@code { delivery_id, url, queued }}
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse test() throws Exception {
        return CommerceResponses.of(delegate.test());
    }

    /**
     * 서명 시크릿 재발급 — ⚠️ 이 응답에서만 secret 원문을 돌려준다.
     *
     * @return 재발급된 시크릿
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse rotateSecret() throws Exception {
        return CommerceResponses.of(delegate.rotateSecret());
    }

    /**
     * 웹훅 전송 이력 조회.
     *
     * @return {@code { list: [...], count, page, per }}
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse deliveries() throws Exception {
        return CommerceResponses.of(delegate.deliveries());
    }

    /**
     * 웹훅 전송 이력 조회.
     *
     * @param params page · limit
     * @return 전송 이력
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse deliveries(AlimtalkWebhookDeliveriesParams params) throws Exception {
        return CommerceResponses.of(delegate.deliveries(params));
    }
}
