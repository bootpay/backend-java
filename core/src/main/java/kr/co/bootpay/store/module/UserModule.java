package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.User;
import kr.co.bootpay.store.model.pojo.SUser;
import kr.co.bootpay.store.model.request.user.MallUserJoinParams;
import kr.co.bootpay.store.model.request.user.UserListParams;

/**
 * 사용자 모듈.
 *
 * <p>조회/수정/삭제 시 사용자 식별자로 user_id, external_uid, login_id 중 하나를 쓸 수 있습니다
 * (서버가 user_id → ex_uid → login_id 순서로 검색).</p>
 *
 * <p>관리자용 API 와 쇼핑몰 프론트(회원 JWT 기반) API 를 이름으로 구분합니다. 기존 표면의
 * {@code userLogin} / {@code userJoin} / {@code userSession} / {@code userLogout} /
 * {@code userJoinCheck} 가 후자에 해당하며, 신규 표면에서는 {@code mall} 접두사로 노출합니다.</p>
 *
 * @since 3.3.0
 */
public class UserModule {

    private final User delegate;

    public UserModule(BootpayStore bootpay) {
        this.delegate = new User(bootpay);
    }

    /**
     * 사용자 토큰 발급.
     *
     * @param userId 사용자 식별자 (user_id, ex_uid, login_id 중 하나)
     * @return 발급된 토큰
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse token(String userId) throws Exception {
        return CommerceResponses.of(delegate.token(userId));
    }

    /**
     * 사용자 토큰 발급 (회원 유형 지정).
     *
     * @param userId         사용자 식별자
     * @param corporateType  개인/기업 구분 ("individual" 또는 "corporate")
     * @param membershipType 회원 유형 ("guest" 또는 "member")
     * @return 발급된 토큰
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse token(String userId, String corporateType, String membershipType) throws Exception {
        return CommerceResponses.of(delegate.token(userId, corporateType, membershipType));
    }

    /**
     * 회원가입 (관리자).
     *
     * @param user 가입할 사용자 정보
     * @return 가입 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse join(SUser user) throws Exception {
        return CommerceResponses.of(delegate.join(user));
    }

    /**
     * 중복 체크.
     *
     * @param key   체크 유형 (id-exist, email-exist, phone-exist, uid-exist, group-business-number-exist)
     * @param value 체크할 값
     * @return 중복 여부
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse checkExist(String key, String value) throws Exception {
        return CommerceResponses.of(delegate.checkExist(key, value));
    }

    /**
     * 본인인증 데이터 조회.
     *
     * @param standId 인증 기준 id
     * @return 인증 데이터
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse authenticationData(String standId) throws Exception {
        return CommerceResponses.of(delegate.authenticationData(standId));
    }

    /**
     * 로그인 (관리자).
     *
     * @param loginId 로그인 id
     * @param loginPw 로그인 비밀번호
     * @return 로그인 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse login(String loginId, String loginPw) throws Exception {
        return CommerceResponses.of(delegate.login(loginId, loginPw));
    }

    /**
     * 사용자 목록 조회.
     *
     * @param params 조회 조건
     * @return 사용자 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list(UserListParams params) throws Exception {
        return CommerceResponses.of(delegate.list(params));
    }

    /**
     * 사용자 상세 조회.
     *
     * @param userId 사용자 식별자
     * @return 사용자 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String userId) throws Exception {
        return CommerceResponses.of(delegate.detail(userId));
    }

    /**
     * 사용자 수정.
     *
     * @param user 수정할 사용자 정보
     * @return 수정 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse update(SUser user) throws Exception {
        return CommerceResponses.of(delegate.update(user));
    }

    /**
     * 사용자 삭제.
     *
     * @param userId 사용자 식별자
     * @return 삭제 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse delete(String userId) throws Exception {
        return CommerceResponses.of(delegate.delete(userId));
    }

    // ========================================
    // 쇼핑몰 프론트 (회원 JWT 기반)
    // ========================================

    /**
     * 몰 회원 로그인.
     *
     * @param loginId  로그인 id
     * @param password 비밀번호
     * @return 로그인 결과 (회원 JWT 포함)
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse mallLogin(String loginId, String password) throws Exception {
        return CommerceResponses.of(delegate.userLogin(loginId, password));
    }

    /**
     * 몰 회원 로그인.
     *
     * @param loginId       로그인 id
     * @param password      비밀번호
     * @param corporateType 개인/기업 구분
     * @return 로그인 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse mallLogin(String loginId, String password, Integer corporateType) throws Exception {
        return CommerceResponses.of(delegate.userLogin(loginId, password, corporateType));
    }

    /**
     * 몰 회원 로그인.
     *
     * @param loginId        로그인 id
     * @param password       비밀번호
     * @param corporateType  개인/기업 구분
     * @param idempotencyKey 미지정 시 자동 생성 (Idempotency-Key 헤더)
     * @return 로그인 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse mallLogin(String loginId, String password, Integer corporateType, String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.userLogin(loginId, password, corporateType, idempotencyKey));
    }

    /**
     * 몰 회원 세션 조회.
     *
     * @param userJwt 회원 JWT
     * @return 세션 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse mallSession(String userJwt) throws Exception {
        return CommerceResponses.of(delegate.userSession(userJwt));
    }

    /**
     * 몰 회원 세션 조회.
     *
     * @param userJwt        회원 JWT
     * @param idempotencyKey 미지정 시 자동 생성 (Idempotency-Key 헤더)
     * @return 세션 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse mallSession(String userJwt, String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.userSession(userJwt, idempotencyKey));
    }

    /**
     * 몰 회원 로그아웃.
     *
     * @param userJwt 회원 JWT
     * @return 로그아웃 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse mallLogout(String userJwt) throws Exception {
        return CommerceResponses.of(delegate.userLogout(userJwt));
    }

    /**
     * 몰 회원 로그아웃.
     *
     * @param userJwt        회원 JWT
     * @param idempotencyKey 미지정 시 자동 생성 (Idempotency-Key 헤더)
     * @return 로그아웃 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse mallLogout(String userJwt, String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.userLogout(userJwt, idempotencyKey));
    }

    /**
     * 몰 회원가입.
     *
     * @param params 가입 정보
     * @return 가입 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse mallJoin(MallUserJoinParams params) throws Exception {
        return CommerceResponses.of(delegate.userJoin(params));
    }

    /**
     * 몰 회원가입 중복 체크.
     *
     * @param type 체크 유형
     * @param pk   체크할 값
     * @return 중복 여부
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse mallJoinCheck(String type, String pk) throws Exception {
        return CommerceResponses.of(delegate.userJoinCheck(type, pk));
    }

    /**
     * 몰 회원가입 중복 체크.
     *
     * @param type           체크 유형
     * @param pk             체크할 값
     * @param idempotencyKey 미지정 시 자동 생성 (Idempotency-Key 헤더)
     * @return 중복 여부
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse mallJoinCheck(String type, String pk, String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.userJoinCheck(type, pk, idempotencyKey));
    }

    /**
     * 외부 고유 ID(ex_uid) 중복 체크.
     *
     * @param uid 외부 고유 ID
     * @return 중복 여부
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse uidExist(String uid) throws Exception {
        return CommerceResponses.of(delegate.uidExist(uid));
    }

    /**
     * 외부 고유 ID(ex_uid) 중복 체크.
     *
     * @param uid            외부 고유 ID
     * @param idempotencyKey 미지정 시 자동 생성 (Idempotency-Key 헤더)
     * @return 중복 여부
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse uidExist(String uid, String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.uidExist(uid, idempotencyKey));
    }
}
