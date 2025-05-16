package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.pojo.SUser;
import kr.co.bootpay.store.model.pojo.SUserGroup;
import kr.co.bootpay.store.model.request.ListParams;
import kr.co.bootpay.store.service.user_groups.SUserGroupService;
import kr.co.bootpay.store.service.users.SUserAuthenticateService;
import kr.co.bootpay.store.service.users.SUserJoinService;
import kr.co.bootpay.store.service.users.SUserLoginService;
import kr.co.bootpay.store.service.users.SUserService;

import java.util.HashMap;
import java.util.Optional;

public class UserGroup {
    private final BootpayStore bootpay;

    public UserGroup(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public HashMap<String, Object> list(ListParams params)  throws Exception {
        return SUserGroupService.list(
                bootpay,
                params
//                Optional.ofNullable(keyword),
//                Optional.ofNullable(page),
//                Optional.ofNullable(limit)
        );
    }

    public HashMap<String, Object> detail(String userGroupId) throws Exception {
        return SUserGroupService.detail(bootpay, userGroupId);
    }

    public HashMap<String, Object> update(SUserGroup userGroup) throws Exception {
        return SUserGroupService.update(bootpay, userGroup);
    }

}
