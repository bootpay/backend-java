package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SUser;
import kr.co.bootpay.store.model.request.user.UserListParams;
import kr.co.bootpay.store.service.users.SUserAuthenticateService;
import kr.co.bootpay.store.service.users.SUserJoinService;
import kr.co.bootpay.store.service.users.SUserLoginService;
import kr.co.bootpay.store.service.users.SUserService;

public class User {
    private final BootpayStore bootpay;

    public User(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public BootpayStoreResponse token(String userId) throws Exception {
        return SUserLoginService.token(bootpay, userId);
    }

    public BootpayStoreResponse token(String userId, String corporateType, String membershipType) throws Exception {
        return SUserLoginService.token(bootpay, userId, corporateType, membershipType);
    }

    public BootpayStoreResponse join(SUser user) throws Exception {
        return SUserJoinService.join(bootpay, user);
    }

    public BootpayStoreResponse checkExist(String key, String value) throws Exception {
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

    public BootpayStoreResponse authenticationData(String standId) throws Exception {
        return SUserAuthenticateService.authenticationData(bootpay, standId);
    }

    public BootpayStoreResponse login(String loginId, String loginPw) throws Exception {
        return SUserLoginService.login(bootpay, loginId, loginPw);
    }

    public BootpayStoreResponse list(UserListParams params) throws Exception {
        return SUserService.list(bootpay, params);
    }

    public BootpayStoreResponse detail(String userId) throws Exception {
        return SUserService.detail(bootpay, userId);
    }

    public BootpayStoreResponse update(SUser user) throws Exception {
        return SUserService.update(bootpay, user);
    }

    public BootpayStoreResponse delete(String userId) throws Exception {
        return SUserService.destroy(bootpay, userId);
    }
}
