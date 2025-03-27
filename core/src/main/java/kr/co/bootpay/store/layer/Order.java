package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.pojo.SInvoice;
import kr.co.bootpay.store.service.invoices.SInvoiceService;
import kr.co.bootpay.store.service.orders.SOrdersService;

import java.util.HashMap;
import java.util.Optional;

public class Order {
    private final BootpayStore bootpay;

    public Order(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public HashMap<String, Object> list( String keyword, Integer page, Integer limit)  throws Exception {
        return SOrdersService.list(
                bootpay,
                Optional.ofNullable(keyword),
                Optional.ofNullable(page),
                Optional.ofNullable(limit)
        );
    }
    public HashMap<String, Object> detail(String invoiceId) throws Exception {
        return SOrdersService.detail(bootpay, invoiceId);
    }
}
