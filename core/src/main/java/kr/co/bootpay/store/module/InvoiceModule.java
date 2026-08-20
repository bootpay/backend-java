package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.Invoice;
import kr.co.bootpay.store.model.pojo.SInvoice;
import kr.co.bootpay.store.model.request.ListParams;
import kr.co.bootpay.store.model.request.invoice.InvoiceListParams;

import java.util.List;

/**
 * 청구서 모듈.
 *
 * @since 3.3.0
 */
public class InvoiceModule {

    private final Invoice delegate;

    public InvoiceModule(BootpayStore bootpay) {
        this.delegate = new Invoice(bootpay);
    }

    /**
     * 청구서 목록 조회.
     *
     * @param params 조회 조건
     * @return 청구서 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list(ListParams params) throws Exception {
        return CommerceResponses.of(delegate.list(params));
    }

    /**
     * 청구서 목록 조회.
     *
     * @param params 조회 조건
     * @return 청구서 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list(InvoiceListParams params) throws Exception {
        return CommerceResponses.of(delegate.list(params));
    }

    /**
     * 청구서 생성.
     *
     * @param invoice 생성할 청구서
     * @return 생성된 청구서
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse create(SInvoice invoice) throws Exception {
        return CommerceResponses.of(delegate.create(invoice));
    }

    /**
     * 청구서 생성.
     *
     * @param invoice        생성할 청구서
     * @param idempotencyKey 미지정 시 자동 생성 (Idempotency-Key 헤더)
     * @return 생성된 청구서
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse create(SInvoice invoice, String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.create(invoice, idempotencyKey));
    }

    /**
     * 청구서 발송.
     *
     * @param invoiceId 청구서 id
     * @return 발송 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse notify(String invoiceId) throws Exception {
        return CommerceResponses.of(delegate.notify(invoiceId));
    }

    /**
     * 청구서 발송.
     *
     * @param invoiceId 청구서 id
     * @param sendTypes 발송 수단 (1: SMS, 2: Email 등)
     * @return 발송 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse notify(String invoiceId, List<Integer> sendTypes) throws Exception {
        return CommerceResponses.of(delegate.notify(invoiceId, sendTypes));
    }

    /**
     * 청구서 발송.
     *
     * @param invoiceId      청구서 id
     * @param sendTypes      발송 수단 (1: SMS, 2: Email 등)
     * @param idempotencyKey 미지정 시 자동 생성 (Idempotency-Key 헤더)
     * @return 발송 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse notify(String invoiceId, List<Integer> sendTypes, String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.notify(invoiceId, sendTypes, idempotencyKey));
    }

    /**
     * 청구서 상세 조회.
     *
     * @param invoiceId 청구서 id
     * @return 청구서 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String invoiceId) throws Exception {
        return CommerceResponses.of(delegate.detail(invoiceId));
    }

    /**
     * 청구서 상세 조회.
     *
     * @param invoiceId      청구서 id
     * @param idempotencyKey 미지정 시 자동 생성 (Idempotency-Key 헤더)
     * @return 청구서 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String invoiceId, String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.detail(invoiceId, idempotencyKey));
    }
}
