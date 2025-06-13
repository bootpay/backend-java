package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.pojo.SUserGroup;
import kr.co.bootpay.store.model.request.UserGroupListParams;
import kr.co.bootpay.store.service.user_groups.SUserGroupService;

import java.util.HashMap;
import java.util.Optional;

public class UserGroup {
    private final BootpayStore bootpay;

    public UserGroup(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public HashMap<String, Object> create(SUserGroup userGroup) throws Exception {
        return SUserGroupService.create(bootpay, userGroup);
    }

    public HashMap<String, Object> list(UserGroupListParams params)  throws Exception {
        return SUserGroupService.list(
                bootpay,
                params
        );
    }


    public HashMap<String, Object> update(SUserGroup userGroup) throws Exception {
        return SUserGroupService.update(bootpay, userGroup);
    }

    public HashMap<String, Object> detail(String userGroupId) throws Exception {
        return SUserGroupService.detail(bootpay, userGroupId);
    }

    public HashMap<String, Object> addUser(String userGroupId, String userId) throws Exception {
        return SUserGroupService.addUser(bootpay, userGroupId, userId);
    }

    public HashMap<String, Object> removeUser(String userGroupId, String userId) throws Exception {
        return SUserGroupService.removeUser(bootpay, userGroupId, userId);
    }
}
