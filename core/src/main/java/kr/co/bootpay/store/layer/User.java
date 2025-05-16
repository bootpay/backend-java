package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.pojo.SUser;
import kr.co.bootpay.store.model.request.UserListParams;
import kr.co.bootpay.store.service.users.SUserAuthenticateService;
import kr.co.bootpay.store.service.users.SUserJoinService;
import kr.co.bootpay.store.service.users.SUserLoginService;
import kr.co.bootpay.store.service.users.SUserService;

import java.util.HashMap;
import java.util.Optional;

public class User {
    private final BootpayStore bootpay;

    public User(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public HashMap<String, Object> token(String userId) throws Exception {
        return SUserLoginService.token(bootpay, userId);
    }

    public HashMap<String, Object> join(SUser user) throws Exception {
        return SUserJoinService.join(bootpay, user);
    }

    public HashMap<String, Object> checkExist(String key, String value) throws Exception {
        return SUserJoinService.checkExist(bootpay, key, value);
    }

//    public HashMap<String, Object> idExist(String pk) throws Exception {
//        return SUserJoinService.idExist(bootpay, pk);
//    }
//
//    public HashMap<String, Object> phoneExist(String pk) throws Exception {
//        return SUserJoinService.phoneExist(bootpay, pk);
//    }
//
//    public HashMap<String, Object> groupBusinessNumberExist(String pk) throws Exception {
//        return SUserJoinService.groupBusinessNumberExist(bootpay, pk);
//    }

    public HashMap<String, Object> authenticationData(String standId) throws Exception {
        return SUserAuthenticateService.authenticationData(bootpay, standId);
    }

    public HashMap<String, Object> login(String loginId, String loginPw) throws Exception {
        return SUserLoginService.login(bootpay, loginId, loginPw);
    }

    public HashMap<String, Object> list(UserListParams params) throws Exception {
        return SUserService.list(
                bootpay,
                params
//                Optional.ofNullable(memberType),
//                Optional.ofNullable(type),
//                Optional.ofNullable(keyword),
//                Optional.ofNullable(page),
//                Optional.ofNullable(limit)
        );
    }

    public HashMap<String, Object> detail(String userId) throws Exception {
        return SUserLoginService.detail(bootpay, userId);
    }

    public HashMap<String, Object> update(SUser user) throws Exception {
        return SUserService.update(bootpay, user);
    }

    public HashMap<String, Object> withdraw(String userId) throws Exception {
        return SUserLoginService.withdraw(bootpay, userId);
    }
}
