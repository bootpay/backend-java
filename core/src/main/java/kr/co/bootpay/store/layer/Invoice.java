package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SInvoice;
import kr.co.bootpay.store.model.request.ListParams;
import kr.co.bootpay.store.service.invoices.SInvoiceService;

import java.util.List;

public class Invoice {
    private final BootpayStore bootpay;

    public Invoice(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public BootpayStoreResponse list(ListParams params)  throws Exception {
        return SInvoiceService.list(
                bootpay,
                params
        );
    }

    public BootpayStoreResponse create(SInvoice invoice) throws Exception {
        return SInvoiceService.create(bootpay, invoice);
    }

    public BootpayStoreResponse notify(String invoiceId, List<Integer> sendTypes) throws Exception {
        return SInvoiceService.notify(bootpay, invoiceId, sendTypes);
    }

    public BootpayStoreResponse detail(String invoiceId) throws Exception {
        return SInvoiceService.detail(bootpay, invoiceId);
    }
}
