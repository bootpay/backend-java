package kr.co.bootpay.pg.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.pg.BootpayObject;
import kr.co.bootpay.pg.model.request.Authentication;
import kr.co.bootpay.pg.service.AuthService;
import kr.co.bootpay.pg.service.VerificationService;

/**
 * 본인인증 모듈.
 *
 * <pre>{@code
 * bootpay.auth.request(authentication);
 * bootpay.auth.confirm(receiptId, otp);
 * bootpay.auth.certificate(receiptId);
 * }</pre>
 *
 * @since 3.3.0
 */
public class AuthModule {

    private final BootpayObject bootpay;

    public AuthModule(BootpayObject bootpay) {
        this.bootpay = bootpay;
    }

    /**
     * 본인인증 요청.
     *
     * @param authentication 본인인증 요청 정보
     * @return 인증 요청 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse request(Authentication authentication) throws Exception {
        return BootpayResponse.ofPg(AuthService.requestAuthentication(bootpay, authentication));
    }

    /**
     * 본인인증 승인 (SMS 인증 시 OTP 검증).
     *
     * @param receiptId 인증 요청 시 받은 영수증 id
     * @param otp       SMS 로 전달된 인증번호
     * @return 인증 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse confirm(String receiptId, String otp) throws Exception {
        return BootpayResponse.ofPg(AuthService.confirmAuthentication(bootpay, receiptId, otp));
    }

    /**
     * 본인인증 SMS 재발송.
     *
     * @param receiptId 인증 요청 시 받은 영수증 id
     * @return 재발송 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse realarm(String receiptId) throws Exception {
        return BootpayResponse.ofPg(AuthService.realarmAuthentication(bootpay, receiptId));
    }

    /**
     * 본인인증 결과 조회.
     *
     * @param receiptId 인증 영수증 id
     * @return 인증 정보 (이름 / CI / 생년월일 등)
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse certificate(String receiptId) throws Exception {
        return BootpayResponse.ofPg(VerificationService.certificate(bootpay, receiptId));
    }
}
