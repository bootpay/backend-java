package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkSendBulkParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkSendParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.alimtalk.SAlimtalkSendService;

import java.util.Map;

/**
 * 알림톡 발송 모듈
 * POST /v1/alimtalk/send · /send/bulk · DELETE /send/{receipt_id}
 *
 * <p>⚠️ <b>실제로 카카오톡이 발송되고 과금된다. 샌드박스가 없다.</b></p>
 */
public class AlimtalkSend {
    private final BootpayStore bootpay;

    public AlimtalkSend(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /** 단건 발송 */
    public BootpayStoreResponse send(AlimtalkSendParams params) throws Exception {
        return SAlimtalkSendService.send(bootpay, params);
    }

    /** 단건 발송 (치환값 없는 템플릿) */
    public BootpayStoreResponse send(String templateCode, String to) throws Exception {
        return send(templateCode, to, null);
    }

    /**
     * 단건 발송
     * @param variables 치환값 — 템플릿의 required_variables 를 모두 채워야 한다
     */
    public BootpayStoreResponse send(String templateCode, String to, Map<String, Object> variables) throws Exception {
        AlimtalkSendParams params = new AlimtalkSendParams();
        params.templateCode = templateCode;
        params.to = to;
        params.variables = variables;
        return SAlimtalkSendService.send(bootpay, params);
    }

    /** 벌크 발송 — 1요청 = N수신자, ⚠️ 수신자 수만큼 실제 발송되고 과금된다 */
    public BootpayStoreResponse sendBulk(AlimtalkSendBulkParams params) throws Exception {
        return SAlimtalkSendService.sendBulk(bootpay, params);
    }

    /** 예약 발송 취소 — 접수(READY) 상태의 예약 건만 취소할 수 있다 */
    public BootpayStoreResponse cancel(String receiptId) throws Exception {
        return SAlimtalkSendService.cancel(bootpay, receiptId);
    }
}
