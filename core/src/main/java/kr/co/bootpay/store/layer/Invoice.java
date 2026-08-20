package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SInvoice;
import kr.co.bootpay.store.model.request.ListParams;
import kr.co.bootpay.store.model.request.invoice.InvoiceListParams;
import kr.co.bootpay.store.service.invoices.SInvoiceService;

import java.util.List;

public class Invoice {
    private final BootpayStore bootpay;

    public Invoice(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    // InvoiceListParams를 넘기면 cs_type / user_id / product_type / css_at / cse_at 필터도 함께 전송한다
    public BootpayStoreResponse list(ListParams params)  throws Exception {
        return SInvoiceService.list(
                bootpay,
                params
        );
    }

    /**
     * 청구서 목록 조회 (파라미터 확장형)
     * GET /v1/invoices — 응답은 { list, count } 구조다 ({ items, total } 아님).
     * limit 미지정시 서버 기본값과 동일한 24 를 보낸다.
     */
    public BootpayStoreResponse list(InvoiceListParams params) throws Exception {
        return SInvoiceService.list(bootpay, params);
    }

    public BootpayStoreResponse create(SInvoice invoice) throws Exception {
        return SInvoiceService.create(bootpay, invoice);
    }

    /**
     * 청구서 생성.
     *
     * @param invoice 청구서 정보
     * @param idempotencyKey 미지정시 자동 생성 (Idempotency-Key 헤더로 전송)
     */
    public BootpayStoreResponse create(SInvoice invoice, String idempotencyKey) throws Exception {
        return SInvoiceService.create(bootpay, invoice, idempotencyKey);
    }

    /**
     * 청구서 알림 재발송 — sendTypes 미전달시 서버가 빈 배열로 처리한다.
     * ⚠️ 실제 고객에게 알림이 발송되므로 테스트 호출 주의.
     */
    public BootpayStoreResponse notify(String invoiceId) throws Exception {
        return SInvoiceService.notify(bootpay, invoiceId, null, null);
    }

    public BootpayStoreResponse notify(String invoiceId, List<Integer> sendTypes) throws Exception {
        return SInvoiceService.notify(bootpay, invoiceId, sendTypes);
    }

    /**
     * 청구서 알림 재발송
     * @param idempotencyKey 미지정시 자동 생성
     */
    public BootpayStoreResponse notify(String invoiceId, List<Integer> sendTypes, String idempotencyKey) throws Exception {
        return SInvoiceService.notify(bootpay, invoiceId, sendTypes, idempotencyKey);
    }

    public BootpayStoreResponse detail(String invoiceId) throws Exception {
        return SInvoiceService.detail(bootpay, invoiceId);
    }

    /**
     * 청구서 상세 조회
     * @param idempotencyKey 미지정시 자동 생성
     */
    public BootpayStoreResponse detail(String invoiceId, String idempotencyKey) throws Exception {
        return SInvoiceService.detail(bootpay, invoiceId, idempotencyKey);
    }
}
