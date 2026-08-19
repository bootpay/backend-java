package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SUser;
import kr.co.bootpay.store.model.request.user.MallUserJoinParams;
import kr.co.bootpay.store.model.request.user.UserListParams;
import kr.co.bootpay.store.service.users.SUserAuthenticateService;
import kr.co.bootpay.store.service.users.SUserJoinService;
import kr.co.bootpay.store.service.users.SUserLoginService;
import kr.co.bootpay.store.service.users.SUserService;

/**
 * 사용자 관리 모듈
 * <p>
 * 조회/수정/삭제 API에서 사용자 식별자로 다음 중 하나를 사용할 수 있습니다:
 * <ul>
 *   <li>user_id: 부트페이 시스템 고유 ID (MongoDB ObjectId 형식)</li>
 *   <li>external_uid: 가맹점에서 설정한 외부 고유 ID (회원가입 시 externalUid 필드로 설정)</li>
 *   <li>login_id: 로그인 ID</li>
 * </ul>
 * 서버에서 user_id → ex_uid → login_id 순서로 검색합니다.
 * </p>
 *
 * <pre>{@code
 * // ex_uid를 활용한 가입 예시
 * SUser user = new SUser();
 * user.loginId = "user001";
 * user.externalUid = "my_shop_user_12345"; // 가맹점 고유 ID
 * bootpay.user.join(user);
 *
 * // 이후 ex_uid로 조회/수정/삭제 가능
 * bootpay.user.detail("my_shop_user_12345");
 * bootpay.user.delete("my_shop_user_12345");
 * }</pre>
 */
public class User {
    private final BootpayStore bootpay;

    public User(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /**
     * 사용자 토큰 발급
     * @param userId 사용자 식별자 (user_id, ex_uid, login_id 중 하나)
     */
    public BootpayStoreResponse token(String userId) throws Exception {
        return SUserLoginService.token(bootpay, userId);
    }

    /**
     * 사용자 토큰 발급 (회원 유형 지정)
     * @param userId 사용자 식별자 (user_id, ex_uid, login_id 중 하나)
     * @param corporateType 개인/기업 구분 ("individual" 또는 "corporate")
     * @param membershipType 회원 유형 ("guest" 또는 "member")
     */
    public BootpayStoreResponse token(String userId, String corporateType, String membershipType) throws Exception {
        return SUserLoginService.token(bootpay, userId, corporateType, membershipType);
    }

    /**
     * 회원가입
     * @param user 가입할 사용자 정보 (externalUid 필드로 외부 고유 ID 설정 가능)
     */
    public BootpayStoreResponse join(SUser user) throws Exception {
        return SUserJoinService.join(bootpay, user);
    }

    /**
     * 중복 체크
     * @param key 체크 유형 (id-exist, email-exist, phone-exist, uid-exist, group-business-number-exist)
     * @param value 체크할 값
     */
    public BootpayStoreResponse checkExist(String key, String value) throws Exception {
        return SUserJoinService.checkExist(bootpay, key, value);
    }

    /**
     * 본인인증 데이터 조회
     * @param standId 인증 ID
     */
    public BootpayStoreResponse authenticationData(String standId) throws Exception {
        return SUserAuthenticateService.authenticationData(bootpay, standId);
    }

    /**
     * 로그인
     * @param loginId 로그인 ID
     * @param loginPw 비밀번호
     */
    public BootpayStoreResponse login(String loginId, String loginPw) throws Exception {
        return SUserLoginService.login(bootpay, loginId, loginPw);
    }

    /**
     * 회원 로그인 (V1 Mall API)
     * POST /v1/users/login
     * @param loginId 로그인 ID
     * @param password 비밀번호
     */
    public BootpayStoreResponse userLogin(String loginId, String password) throws Exception {
        return SUserLoginService.userLogin(bootpay, loginId, password, null, null);
    }

    /**
     * 회원 로그인 (V1 Mall API)
     * @param corporateType 0: 개인, 1: 사업자 (미지정시 0 — 서버는 무시하지만 nodejs SDK 와 동일하게 전송)
     */
    public BootpayStoreResponse userLogin(String loginId, String password, Integer corporateType) throws Exception {
        return SUserLoginService.userLogin(bootpay, loginId, password, corporateType, null);
    }

    /**
     * 회원 로그인 (V1 Mall API)
     * @param idempotencyKey 미지정시 자동 생성
     */
    public BootpayStoreResponse userLogin(String loginId, String password, Integer corporateType, String idempotencyKey) throws Exception {
        return SUserLoginService.userLogin(bootpay, loginId, password, corporateType, idempotencyKey);
    }

    /**
     * 회원 세션 조회 (V1 Mall API)
     * GET /v1/users/session
     * @param userJwt 로그인시 발급받은 회원 JWT
     */
    public BootpayStoreResponse userSession(String userJwt) throws Exception {
        return SUserLoginService.userSession(bootpay, userJwt, null);
    }

    /**
     * 회원 세션 조회 (V1 Mall API)
     * @param idempotencyKey 미지정시 자동 생성
     */
    public BootpayStoreResponse userSession(String userJwt, String idempotencyKey) throws Exception {
        return SUserLoginService.userSession(bootpay, userJwt, idempotencyKey);
    }

    /**
     * 회원 로그아웃 (V1 Mall API)
     * DELETE /v1/users/session
     * @param userJwt 로그인시 발급받은 회원 JWT
     */
    public BootpayStoreResponse userLogout(String userJwt) throws Exception {
        return SUserLoginService.userLogout(bootpay, userJwt, null);
    }

    /**
     * 회원 로그아웃 (V1 Mall API)
     * @param idempotencyKey 미지정시 자동 생성
     */
    public BootpayStoreResponse userLogout(String userJwt, String idempotencyKey) throws Exception {
        return SUserLoginService.userLogout(bootpay, userJwt, idempotencyKey);
    }

    /**
     * 회원가입 (V1 Mall API) — 일반 회원가입용
     * POST /v1/users/join
     * ⚠️ join(user) 과 같은 엔드포인트를 부르지만 용도가 다르다 (일반 회원가입 vs 외부 uid 연동 가입).
     * @param params 회원가입 파라미터 (corporateType 미지정시 0, null 값은 전송하지 않는다)
     */
    public BootpayStoreResponse userJoin(MallUserJoinParams params) throws Exception {
        return SUserJoinService.userJoin(bootpay, params);
    }

    /**
     * 회원가입 중복 확인 (V1 Mall API) — key 를 인자로 받는 일반형
     * GET /v1/users/join/{type}?pk={pk}
     * @param type email-exist, id-exist, phone-exist, uid-exist, group-business-number-exist
     * @param pk 중복 확인할 값
     */
    public BootpayStoreResponse userJoinCheck(String type, String pk) throws Exception {
        return SUserJoinService.userJoinCheck(bootpay, type, pk, null);
    }

    /**
     * 회원가입 중복 확인 (V1 Mall API)
     * @param idempotencyKey 미지정시 자동 생성
     */
    public BootpayStoreResponse userJoinCheck(String type, String pk, String idempotencyKey) throws Exception {
        return SUserJoinService.userJoinCheck(bootpay, type, pk, idempotencyKey);
    }

    /**
     * 외부 uid(ex_uid) 중복 검사
     * GET /v1/users/join/uid-exist?pk={uid}
     * @param uid 중복 확인할 외부 uid
     */
    public BootpayStoreResponse uidExist(String uid) throws Exception {
        return SUserJoinService.uidExist(bootpay, uid, null);
    }

    /**
     * 외부 uid(ex_uid) 중복 검사
     * @param idempotencyKey 미지정시 자동 생성
     */
    public BootpayStoreResponse uidExist(String uid, String idempotencyKey) throws Exception {
        return SUserJoinService.uidExist(bootpay, uid, idempotencyKey);
    }

    /**
     * 사용자 목록 조회
     * @param params 검색 파라미터
     */
    public BootpayStoreResponse list(UserListParams params) throws Exception {
        return SUserService.list(bootpay, params);
    }

    /**
     * 사용자 상세 조회
     * @param userId 사용자 식별자 (user_id, ex_uid, login_id 중 하나)
     */
    public BootpayStoreResponse detail(String userId) throws Exception {
        return SUserService.detail(bootpay, userId);
    }

    /**
     * 사용자 정보 수정
     * @param user 수정할 사용자 정보 (userId 필드에 user_id, ex_uid, login_id 중 하나 설정)
     */
    public BootpayStoreResponse update(SUser user) throws Exception {
        return SUserService.update(bootpay, user);
    }

    /**
     * 회원 탈퇴
     * @param userId 사용자 식별자 (user_id, ex_uid, login_id 중 하나)
     */
    public BootpayStoreResponse delete(String userId) throws Exception {
        return SUserService.destroy(bootpay, userId);
    }
}
