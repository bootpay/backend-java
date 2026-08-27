package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.AlimtalkSend;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkSendBulkParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkSendParams;

import java.util.Map;

/**
 * 알림톡 발송 모듈.
 *
 * <p>⚠️ <b>실제로 카카오톡이 발송되고 과금된다. 샌드박스가 없다.</b></p>
 *
 * @since 3.6.0
 */
public class AlimtalkSendModule {

    private final AlimtalkSend delegate;

    public AlimtalkSendModule(BootpayStore bootpay) {
        this.delegate = new AlimtalkSend(bootpay);
    }

    /**
     * 단건 발송.
     *
     * @param params 발송 파라미터
     * @return 접수 결과 — {@code { receipt_id, ref_id, to, status }}
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse send(AlimtalkSendParams params) throws Exception {
        return CommerceResponses.of(delegate.send(params));
    }

    /**
     * 단건 발송 (치환값 없는 템플릿).
     *
     * @param templateCode 템플릿 코드
     * @param to           수신번호
     * @return 접수 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse send(String templateCode, String to) throws Exception {
        return CommerceResponses.of(delegate.send(templateCode, to));
    }

    /**
     * 단건 발송.
     *
     * @param templateCode 템플릿 코드
     * @param to           수신번호
     * @param variables    치환값 — 템플릿의 required_variables 를 모두 채워야 한다
     * @return 접수 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse send(String templateCode, String to, Map<String, Object> variables) throws Exception {
        return CommerceResponses.of(delegate.send(templateCode, to, variables));
    }

    /**
     * 벌크 발송 — 1요청 = N수신자. ⚠️ 수신자 수만큼 실제 발송되고 과금된다.
     *
     * @param params 벌크 발송 파라미터
     * @return {@code { count, requested, skipped, rejected, receipts: [...] }}
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse sendBulk(AlimtalkSendBulkParams params) throws Exception {
        return CommerceResponses.of(delegate.sendBulk(params));
    }

    /**
     * 예약 발송 취소 — 접수(READY) 상태의 예약 건만 취소할 수 있다.
     *
     * @param receiptId 접수 ID
     * @return 취소 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse cancel(String receiptId) throws Exception {
        return CommerceResponses.of(delegate.cancel(receiptId));
    }
}
