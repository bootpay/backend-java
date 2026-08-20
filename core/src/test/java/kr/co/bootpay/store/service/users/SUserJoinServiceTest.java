package kr.co.bootpay.store.service.users;

import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.TokenPayload;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SUserJoinServiceTest {
    private static final String CLIENT_KEY = "test_client_key";
    private static final String SECRET_KEY = "test_secret_key";

    private BootpayStoreObject bootpay() {
        BootpayStoreObject bootpay = new BootpayStoreObject(new TokenPayload(CLIENT_KEY, SECRET_KEY), "PRODUCTION");
        bootpay.setTokenFromAPI("test_token");
        return bootpay;
    }

    @Test
    public void uid_중복확인은_GET_users_join_uid_exist로_요청한다() throws Exception {
        HttpGet get = SUserJoinService.checkExistRequest(bootpay(), SUserJoinService.UID_EXIST, "ex_uid_1234");

        assertEquals("GET", get.getMethod());
        assertEquals("/v1/users/join/uid-exist", get.getURI().getPath());
        assertEquals("pk=ex_uid_1234", get.getURI().getQuery());
    }

    @Test
    public void 중복확인_key는_users_join_경로에_그대로_사용한다() throws Exception {
        assertEquals("/v1/users/join/email-exist",
                SUserJoinService.checkExistRequest(bootpay(), SUserJoinService.EMAIL_EXIST, "test@bootpay.co.kr").getURI().getPath());
        assertEquals("/v1/users/join/id-exist",
                SUserJoinService.checkExistRequest(bootpay(), SUserJoinService.ID_EXIST, "ehowlsla").getURI().getPath());
        assertEquals("/v1/users/join/phone-exist",
                SUserJoinService.checkExistRequest(bootpay(), SUserJoinService.PHONE_EXIST, "01000000000").getURI().getPath());
        assertEquals("/v1/users/join/group-business-number-exist",
                SUserJoinService.checkExistRequest(bootpay(), SUserJoinService.GROUP_BUSINESS_NUMBER_EXIST, "1088603663").getURI().getPath());
    }

    @Test
    public void pk는_URL_인코딩해서_전송한다() throws Exception {
        HttpGet get = SUserJoinService.checkExistRequest(bootpay(), SUserJoinService.EMAIL_EXIST, "test+1@bootpay.co.kr");

        assertEquals("pk=test+1@bootpay.co.kr", get.getURI().getQuery());
        assertEquals("pk=test%2B1%40bootpay.co.kr", get.getURI().getRawQuery());
    }

    @Test
    public void 토큰이_없으면_예외를_발생시킨다() {
        BootpayStoreObject bootpay = new BootpayStoreObject(new TokenPayload(CLIENT_KEY, SECRET_KEY), "PRODUCTION");

        assertThrows(Exception.class, () -> SUserJoinService.checkExistRequest(bootpay, SUserJoinService.UID_EXIST, "ex_uid_1234"));
    }
}
