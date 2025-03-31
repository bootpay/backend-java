package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.pojo.SInvoice;
import kr.co.bootpay.store.service.invoices.SInvoiceService;
import kr.co.bootpay.store.service.products.SProductService;

import java.util.HashMap;
import java.util.List;
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

    public HashMap<String, Object> create(SInvoice invoice) throws Exception {
        return SInvoiceService.create(bootpay, invoice);
    }

    public HashMap<String, Object> notify(String invoiceId, List<Integer> sendTypes) throws Exception {
        return SInvoiceService.notify(bootpay, invoiceId, sendTypes);
    }

    public HashMap<String, Object> detail(String invoiceId) throws Exception {
        return SInvoiceService.detail(bootpay, invoiceId);
    }
}
