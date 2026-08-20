package kr.co.bootpay.pg.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.pg.BootpayObject;
import kr.co.bootpay.pg.model.request.WalletPayment;
import kr.co.bootpay.pg.service.WalletService;

/**
 * 월렛 모듈.
 *
 * @deprecated wallet 엔드포인트는 폐기 예정입니다. 결제는 wallet_id + user_token 흐름으로 전환하세요.
 *             기존 표면의 {@code getUserWallets} / {@code requestWalletPayment} 와 동일한 이유로
 *             신규 표면에서도 deprecated 로 노출합니다.
 * @since 3.3.0
 */
@Deprecated
public class WalletModule {

    private final BootpayObject bootpay;

    public WalletModule(BootpayObject bootpay) {
        this.bootpay = bootpay;
    }

    /**
     * 사용자 월렛 목록 조회.
     *
     * @param userId  사용자 id
     * @param sandbox 샌드박스 여부
     * @return 월렛 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     * @deprecated 다음 메이저 버전에서 제거 예정
     */
    @Deprecated
    public BootpayResponse list(String userId, boolean sandbox) throws Exception {
        return BootpayResponse.ofPg(WalletService.getUserWallets(bootpay, userId, sandbox));
    }

    /**
     * 월렛 결제 요청.
     *
     * @param walletPayment 월렛 결제 요청 정보
     * @return 결제 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     * @deprecated 다음 메이저 버전에서 제거 예정
     */
    @Deprecated
    public BootpayResponse pay(WalletPayment walletPayment) throws Exception {
        return BootpayResponse.ofPg(WalletService.requestWalletPayment(bootpay, walletPayment));
    }
}
