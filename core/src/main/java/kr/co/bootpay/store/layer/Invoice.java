package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.service.invoices.SInvoiceService;
import kr.co.bootpay.store.service.products.SProductService;

import java.util.HashMap;
import java.util.Optional;

public class Invoice {
    private final BootpayStore bootpay;

    public Invoice(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public HashMap<String, Object> list( String keyword, Integer page, Integer limit)  throws Exception {
        return SInvoiceService.list(
                bootpay,
                Optional.ofNullable(keyword),
                Optional.ofNullable(page),
                Optional.ofNullable(limit)
        );
    }


}
