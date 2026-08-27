package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.AlimtalkMessage;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkMessageListParams;

/**
 * 알림톡 발송내역·집계 모듈.
 *
 * <p><b>유료</b> 알림톡만 조회된다 (무료 커머스 알림톡은 포함되지 않는다).</p>
 *
 * @since 3.6.0
 */
public class AlimtalkMessageModule {

    private final AlimtalkMessage delegate;

    public AlimtalkMessageModule(BootpayStore bootpay) {
        this.delegate = new AlimtalkMessage(bootpay);
    }

    /**
     * 발송내역 목록 조회 (최근 30일 기본, 최대 92일).
     *
     * @return {@code { list: [...], count, page, per, period: { from, to } }}
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list() throws Exception {
        return CommerceResponses.of(delegate.list());
    }

    /**
     * 발송내역 목록 조회.
     *
     * @param params 조회 필터 — ⚠️ 최대 조회 폭 92일을 넘기면 시작일이 당겨져 잘린다
     * @return 발송내역 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list(AlimtalkMessageListParams params) throws Exception {
        return CommerceResponses.of(delegate.list(params));
    }

    /**
     * 기간 집계 조회 (서버 기본 기간).
     *
     * @return 집계 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse stats() throws Exception {
        return CommerceResponses.of(delegate.stats());
    }

    /**
     * 기간 집계 조회.
     *
     * @param sAt 집계 시작일
     * @param eAt 집계 종료일
     * @return 집계 결과 — ⚠️ {@code billing.unit_price_source} 가 default 면 잠정 단가다
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse stats(String sAt, String eAt) throws Exception {
        return CommerceResponses.of(delegate.stats(sAt, eAt));
    }

    /**
     * 단건 발송 결과 조회.
     *
     * @param receiptId 접수 ID
     * @return 발송 결과 (실패 사유는 {@code error_code} · {@code error_message})
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String receiptId) throws Exception {
        return CommerceResponses.of(delegate.detail(receiptId));
    }
}
