package kr.co.bootpay.store.model.request.alimtalk;

import kr.co.bootpay.store.model.pojo.SAlimtalkRecipient;

import java.util.List;

/**
 * 알림톡 벌크 발송 파라미터 (POST /v1/alimtalk/send/bulk) — 1요청 = N수신자
 *
 * <p>⚠️ 수신자 수만큼 실제 발송되고 과금된다.</p>
 * <ul>
 *   <li>쿼터를 넘으면 요청 시점에 <b>전체 거부</b>된다(3022) — 일부만 나가지 않는다.</li>
 *   <li>개별 수신자의 실패는 건별 {@code rejected} 로 표시되고 나머지는 정상 발송된다.</li>
 *   <li>수신거부 번호는 {@code skipped} 이며 <b>과금되지 않고 발송 기록도 만들지 않는다</b>.</li>
 *   <li>{@code fallback} 은 요청 단위로 한 번만 판정한다 — 발신번호가 없으면 요청 전체가 3030 으로 거부된다.</li>
 * </ul>
 */
public class AlimtalkSendBulkParams {
    public String templateCode;
    /** 수신자 목록 */
    public List<SAlimtalkRecipient> recipients;
    /** ⚠️ 미지정(null)과 false 는 다르다 — {@link AlimtalkSendParams#fallback} 참고 */
    public Boolean fallback;
    public String reservedAt;
    public String senderKey;
    public String userId;
}
