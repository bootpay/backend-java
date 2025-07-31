package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.userGroup.UserGroupAggregateTransactionParams;
import kr.co.bootpay.store.model.request.userGroup.UserGroupLimitParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SUserGroup;
import kr.co.bootpay.store.model.request.userGroup.UserGroupListParams;
import kr.co.bootpay.store.service.user_groups.SUserGroupService;

public class UserGroup {
    private final BootpayStore bootpay;

    public UserGroup(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public BootpayStoreResponse create(SUserGroup userGroup) throws Exception {
        return SUserGroupService.create(bootpay, userGroup);
    }

    public BootpayStoreResponse list(UserGroupListParams params)  throws Exception {
        return SUserGroupService.list(
                bootpay,
                params
        );
    }


    public BootpayStoreResponse update(SUserGroup userGroup) throws Exception {
        return SUserGroupService.update(bootpay, userGroup);
    }

    public BootpayStoreResponse detail(String userGroupId) throws Exception {
        return SUserGroupService.detail(bootpay, userGroupId);
    }

    public BootpayStoreResponse userCreate(String userGroupId, String userId) throws Exception {
        return SUserGroupService.userCreate(bootpay, userGroupId, userId);
    }

    public BootpayStoreResponse userDelete(String userGroupId, String userId) throws Exception {
        return SUserGroupService.userDelete(bootpay, userGroupId, userId);
    }

    public BootpayStoreResponse limit(UserGroupLimitParams params) throws Exception {
        return SUserGroupService.limit(bootpay, params);
    }

    public BootpayStoreResponse aggregateTransaction(UserGroupAggregateTransactionParams params) throws Exception {
        return SUserGroupService.aggregateTransaction(bootpay, params);
    }
}
