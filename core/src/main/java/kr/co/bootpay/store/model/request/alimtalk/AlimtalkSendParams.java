package kr.co.bootpay.store.model.request.alimtalk;

import java.util.Map;

/**
 * 알림톡 단건 발송 파라미터 (POST /v1/alimtalk/send)
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
}
