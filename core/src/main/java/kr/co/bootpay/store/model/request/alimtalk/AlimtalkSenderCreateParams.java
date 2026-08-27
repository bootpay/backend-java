package kr.co.bootpay.store.model.request.alimtalk;

/**
 * 알림톡 발신프로필 등록 파라미터 (POST /v1/alimtalk/senders)
 *
 * <p>⚠️ 카카오에 발신프로필이 <b>실제 등록</b>된다. 같은 {@code yellowId} 를 다시 등록하면 기존 프로필을 재사용한다(dedup).
 * 등록 성공 시 그룹키 등록까지 서버가 수행하므로 공식 카탈로그 전체를 바로 발송할 수 있다.</p>
 */
public class AlimtalkSenderCreateParams {
    /** {@code alimtalk.sender.otp} 로 받은 인증번호 */
    public String otp;
    /** 카카오채널 검색용 아이디 */
    public String yellowId;
    /** 채널 관리자 휴대폰번호 */
    public String phone;
    /** {@code alimtalk.sender.categories} 로 조회한 카카오 카테고리 코드 */
    public String categoryCode;
}
