package kr.co.bootpay.store.model.request.orderSubscription;

import kr.co.bootpay.store.model.request.ListParams;

/**
 * 구독 목록 조회 파라미터
 * <p>
 * userId, userGroupId 필드는 다음 중 하나로 대체 가능합니다:
 * <ul>
 *   <li>userId: user_id (부트페이 ID) 또는 userExUid, userExternalUid, userUid (가맹점 고유 ID)</li>
 *   <li>userGroupId: user_group_id (부트페이 ID) 또는 userGroupExUid, userGroupExternalUid, userGroupUid (가맹점 고유 ID)</li>
 * </ul>
 * </p>
 */
public class OrderSubscriptionListParams extends ListParams {
    public String sAt;
    public String eAt;
    public Integer requestType;

    // 기본 ID 필드
    public String userGroupId;
    public Integer status;
    public String userId;

    // ex_uid 지원 필드 (user)
    public String userExUid;
    public String userExternalUid;
    public String userUid;

    // ex_uid 지원 필드 (user_group)
    public String userGroupExUid;
    public String userGroupExternalUid;
    public String userGroupUid;
}
