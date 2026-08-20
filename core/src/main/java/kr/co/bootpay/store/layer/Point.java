package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.point.PointTransactionsParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.points.SPointService;

public class Point {
    private final BootpayStore bootpay;

    public Point(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /**
     * 적립금 잔액 조회
     */
    public BootpayStoreResponse balance() throws Exception {
        return SPointService.balance(bootpay);
    }

    /**
     * 적립금 내역 조회
     */
    public BootpayStoreResponse transactions(PointTransactionsParams params) throws Exception {
        return SPointService.transactions(bootpay, params);
    }

    public BootpayStoreResponse transactions() throws Exception {
        return SPointService.transactions(bootpay, null);
    }
}
