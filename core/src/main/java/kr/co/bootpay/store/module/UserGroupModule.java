package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.UserGroup;
import kr.co.bootpay.store.model.pojo.SUserGroup;
import kr.co.bootpay.store.model.request.userGroup.UserGroupAggregateTransactionParams;
import kr.co.bootpay.store.model.request.userGroup.UserGroupLimitParams;
import kr.co.bootpay.store.model.request.userGroup.UserGroupListParams;

/**
 * 사용자 그룹 모듈.
 *
 * @since 3.3.0
 */
public class UserGroupModule {

    private final UserGroup delegate;

    public UserGroupModule(BootpayStore bootpay) {
        this.delegate = new UserGroup(bootpay);
    }

    /**
     * 사용자 그룹 생성.
     *
     * @param userGroup 생성할 그룹
     * @return 생성된 그룹
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse create(SUserGroup userGroup) throws Exception {
        return CommerceResponses.of(delegate.create(userGroup));
    }

    /**
     * 사용자 그룹 목록 조회.
     *
     * @param params 조회 조건
     * @return 그룹 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list(UserGroupListParams params) throws Exception {
        return CommerceResponses.of(delegate.list(params));
    }

    /**
     * 사용자 그룹 수정.
     *
     * @param userGroup 수정할 그룹
     * @return 수정된 그룹
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse update(SUserGroup userGroup) throws Exception {
        return CommerceResponses.of(delegate.update(userGroup));
    }

    /**
     * 사용자 그룹 상세 조회.
     *
     * @param userGroupId 그룹 id
     * @return 그룹 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String userGroupId) throws Exception {
        return CommerceResponses.of(delegate.detail(userGroupId));
    }

    /**
     * 그룹에 사용자 추가.
     *
     * @param userGroupId 그룹 id
     * @param userId      사용자 id
     * @return 추가 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse userCreate(String userGroupId, String userId) throws Exception {
        return CommerceResponses.of(delegate.userCreate(userGroupId, userId));
    }

    /**
     * 그룹에서 사용자 제거.
     *
     * @param userGroupId 그룹 id
     * @param userId      사용자 id
     * @return 제거 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse userDelete(String userGroupId, String userId) throws Exception {
        return CommerceResponses.of(delegate.userDelete(userGroupId, userId));
    }

    /**
     * 그룹 한도 설정.
     *
     * @param params 한도 정보
     * @return 설정 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse limit(UserGroupLimitParams params) throws Exception {
        return CommerceResponses.of(delegate.limit(params));
    }

    /**
     * 그룹 거래 합산 설정.
     *
     * @param params 합산 설정 정보
     * @return 설정 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse aggregateTransaction(UserGroupAggregateTransactionParams params) throws Exception {
        return CommerceResponses.of(delegate.aggregateTransaction(params));
    }
}
