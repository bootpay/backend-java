package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkMessageListParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.alimtalk.SAlimtalkMessageService;

/**
 * 알림톡 발송내역·집계 모듈
 * GET /v1/alimtalk/messages 계열
 *
 * <p><b>유료</b> 알림톡만 조회된다 (무료 커머스 알림톡은 포함되지 않는다).</p>
 */
public class AlimtalkMessage {
    private final BootpayStore bootpay;

    public AlimtalkMessage(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /** 발송내역 목록 조회 (최근 30일 기본, 최대 92일) */
    public BootpayStoreResponse list() throws Exception {
        return SAlimtalkMessageService.list(bootpay, null);
    }

    /** 발송내역 목록 조회 */
    public BootpayStoreResponse list(AlimtalkMessageListParams params) throws Exception {
        return SAlimtalkMessageService.list(bootpay, params);
    }

    /** 기간 집계 조회 (서버 기본 기간) */
    public BootpayStoreResponse stats() throws Exception {
        return SAlimtalkMessageService.stats(bootpay, null, null);
    }

    /**
     * 기간 집계 조회
     * @param sAt 집계 시작일
     * @param eAt 집계 종료일
     */
    public BootpayStoreResponse stats(String sAt, String eAt) throws Exception {
        return SAlimtalkMessageService.stats(bootpay, sAt, eAt);
    }

    /** 단건 발송 결과 조회 */
    public BootpayStoreResponse detail(String receiptId) throws Exception {
        return SAlimtalkMessageService.detail(bootpay, receiptId);
    }
}
