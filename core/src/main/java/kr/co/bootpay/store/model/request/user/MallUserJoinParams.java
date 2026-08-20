package kr.co.bootpay.store.model.request.user;

import java.util.Map;

/**
 * 회원가입 파라미터 (V1 Mall API)
 * POST /v1/users/join
 * null 값은 전송하지 않는다. corporate_type 미지정시 0 으로 전송된다.
 */
public class MallUserJoinParams {
    public String loginId;
    public String password;
    public String name;
    public String email;
    public String phone;
    public String nickname;
    public Integer gender;
    public String birth;
    /** 0: 개인, 1: 사업자 (미지정시 0) */
    public Integer corporateType;
    public Map<String, Object> group;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더로 전송, body 에는 포함되지 않는다) */
    public transient String idempotencyKey;
}
