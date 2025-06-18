package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SUserGroup;
import kr.co.bootpay.store.model.request.UserGroupListParams;
import kr.co.bootpay.store.service.user_groups.SUserGroupService;

import java.util.HashMap;

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
}
