package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.store.SStoreService;

public class Store {
    private final BootpayStore bootpay;

    public Store(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public BootpayStoreResponse info() throws Exception {
        return SStoreService.info(bootpay);
    }

    public BootpayStoreResponse detail() throws Exception {
        return SStoreService.detail(bootpay);
    }
}
