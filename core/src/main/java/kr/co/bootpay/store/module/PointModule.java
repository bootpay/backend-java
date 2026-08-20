package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.Point;
import kr.co.bootpay.store.model.request.point.PointTransactionsParams;

/**
 * 포인트 모듈.
 *
 * @since 3.3.0
 */
public class PointModule {

    private final Point delegate;

    public PointModule(BootpayStore bootpay) {
        this.delegate = new Point(bootpay);
    }

    /**
     * 포인트 잔액 조회.
     *
     * @return 잔액 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse balance() throws Exception {
        return CommerceResponses.of(delegate.balance());
    }

    /**
     * 포인트 거래 내역 조회.
     *
     * @param params 조회 조건
     * @return 거래 내역
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse transactions(PointTransactionsParams params) throws Exception {
        return CommerceResponses.of(delegate.transactions(params));
    }

    /**
     * 포인트 거래 내역 조회 (조건 없이).
     *
     * @return 거래 내역
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse transactions() throws Exception {
        return CommerceResponses.of(delegate.transactions());
    }
}
