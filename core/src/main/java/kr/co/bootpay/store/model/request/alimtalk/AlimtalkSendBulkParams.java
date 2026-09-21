package kr.co.bootpay.store.model.request.alimtalk;

import kr.co.bootpay.store.model.pojo.SAlimtalkRecipient;

import java.util.List;

/**
 * 알림톡 벌크 발송 파라미터 (POST /alimtalk/send/bulk) — 1요청 = N수신자
 *
 * <p>⚠️ 수신자 수만큼 실제 발송되고 과금된다.</p>
 * <ul>
 *   <li>쿼터를 넘으면 요청 시점에 <b>전체 거부</b>된다(3022) — 일부만 나가지 않는다.</li>
 *   <li>개별 수신자의 실패는 건별 {@code rejected} 로 표시되고 나머지는 정상 발송된다.</li>
 *   <li>수신거부 번호는 {@code skipped} 이며 <b>과금되지 않고 발송 기록도 만들지 않는다</b>.</li>
 *   <li>{@code fallback} 은 요청 단위로 한 번만 판정한다 — 발신번호가 없으면 요청 전체가 3030 으로 거부된다.</li>
 *   <li>{@code webhookUrl} 도 요청 단위 하나다 — 이 요청으로 나간 모든 수신자 건의 결과 웹훅이 그 주소로 간다.
 *       형식이 틀리면(https 아님·2,000자 초과) 요청 전체가 3028 로 거부된다.</li>
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
    /**
     * 이 요청으로 나간 모든 수신자 건의 결과 웹훅을 받을 주소 — 요청 단위 하나다.
     *
     * <p>형식이 틀리면(https 아님·2,000자 초과) 요청 전체가 3028 로 거부된다.
     * 자세한 동작은 {@link AlimtalkSendParams#webhookUrl} 참고.</p>
     *
     * @since 3.6.0
     */
    public String webhookUrl;
}
