package kr.co.bootpay.store.model.request.alimtalk;

import java.util.Map;

/**
 * 알림톡 단건 발송 파라미터 (POST /alimtalk/send)
 *
 * <p>⚠️ <b>실제로 카카오톡이 발송되고 과금된다. 샌드박스가 없다.</b></p>
 */
public class AlimtalkSendParams {
    /** 템플릿 코드 */
    public String templateCode;
    /** 수신번호 */
    public String to;
    /**
     * 치환값 — {@code { "company_name": "부트페이몰", "user_name": "홍길동" }}
     *
     * <p>템플릿 응답의 {@code required_variables} 를 모두 채워야 한다. 하나라도 비면 3017 로 거부된다.</p>
     */
    public Map<String, Object> variables;
    /** 가맹점 발송 식별자 — <b>멱등 키</b>로 쓰인다 */
    public String refId;
    /**
     * 알림톡 실패 시 문자(LMS) 대체발송 여부.
     *
     * <p>⚠️ <b>미지정(null)과 false 는 다르다</b> — null 이면 프로젝트 기본값을 따르고, false 는 명시적으로 끈다.
     * 켜면 발신번호가 등록돼 있어야 하며 없으면 3030 으로 거부된다.</p>
     */
    public Boolean fallback;
    /** 예약 발송 시각(ISO8601). 미지정시 즉시 발송 */
    public String reservedAt;
    /** 채널 지정 (공개키). 미지정시 프로젝트 연동 채널로 해석하며, 연동 채널이 둘 이상일 때만 필수다 */
    public String senderKey;
    public String userId;
    /**
     * 이 건의 결과 웹훅을 받을 주소.
     *
     * <p>주면 발송 성공·실패·문자 대체발송·예약취소 웹훅이 <b>이 주소로만</b> 간다(프로젝트 웹훅 설정은 쓰이지 않는다).
     * https 만 허용하며 2,000자를 넘으면 3028 로 거부된다. 서명은 프로젝트 시크릿으로 하고,
     * 시크릿만 필요하면 {@code alimtalkWebhook.rotateSecret()} 으로 설정 없이 발급받을 수 있다.</p>
     *
     * <p>⚠️ 같은 {@code refId} 로 이미 접수·성공한 건을 다시 요청하면 기존 접수가 그대로 돌아와 새 주소는 무시된다.</p>
     *
     * @since 3.6.0
     */
    public String webhookUrl;
}
