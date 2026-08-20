package kr.co.bootpay.pg.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.pg.BootpayObject;
import kr.co.bootpay.pg.model.request.UserToken;
import kr.co.bootpay.pg.service.EasyService;

/**
 * 구매자(간편결제 사용자) 모듈.
 *
 * <pre>{@code
 * bootpay.user.token(userToken);
 * }</pre>
 *
 * @since 3.3.0
 */
public class UserModule {

    private final BootpayObject bootpay;

    public UserModule(BootpayObject bootpay) {
        this.bootpay = bootpay;
    }

    /**
     * 간편결제용 사용자 토큰 발급.
     *
     * @param userToken 사용자 정보 (user_id 필수)
     * @return 발급된 사용자 토큰
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse token(UserToken userToken) throws Exception {
        return BootpayResponse.ofPg(EasyService.getUserToken(bootpay, userToken));
    }
}
