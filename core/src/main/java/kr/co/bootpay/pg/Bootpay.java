package kr.co.bootpay.pg;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.pg.model.request.*;
import kr.co.bootpay.pg.model.response.ResDefault;
import kr.co.bootpay.pg.module.AuthModule;
import kr.co.bootpay.pg.module.BillingModule;
import kr.co.bootpay.pg.module.CashModule;
import kr.co.bootpay.pg.module.EscrowModule;
import kr.co.bootpay.pg.module.PaymentModule;
import kr.co.bootpay.pg.module.UserModule;
import kr.co.bootpay.pg.module.WalletModule;
import kr.co.bootpay.pg.service.*;

import java.util.HashMap;

public class Bootpay extends BootpayObject {

    // ========================================
    // 모듈 표면 (3.3.0~) — Commerce 와 동일한 형태로 통일된 신규 API.
    // 기존의 flat 메서드(getReceipt/receiptCancel/...) 는 그대로 유지되며 아무 영향도 받지 않는다.
    // ========================================

    /** 결제 조회 / 승인 / 취소 / 링크 발급. */
    public final PaymentModule payment = new PaymentModule(this);

    /** 빌링키 발급 / 정기결제 / 예약결제. */
    public final BillingModule billing = new BillingModule(this);

    /** 본인인증. */
    public final AuthModule auth = new AuthModule(this);

    /** 현금영수증. */
    public final CashModule cash = new CashModule(this);

    /** 에스크로 배송. */
    public final EscrowModule escrow = new EscrowModule(this);

    /** 간편결제 사용자 토큰. */
    public final UserModule user = new UserModule(this);

    /**
     * 월렛.
     *
     * @deprecated wallet 엔드포인트는 폐기 예정입니다.
     */
    @Deprecated
    public final WalletModule wallet = new WalletModule(this);

    public Bootpay() { }

    public Bootpay(String restApplicationId, String privateKey) {
        super(restApplicationId, privateKey);
    }

    public Bootpay(String restApplicationId, String privateKey, String devMode) {
        super(restApplicationId, privateKey, devMode);
    }

    private Bootpay(String clientKey, String secretKey, String devMode, boolean useClientKey) {
        super(clientKey, secretKey, devMode, useClientKey);
    }

    public static Bootpay withClientKey(String clientKey, String secretKey) {
        return new Bootpay(clientKey, secretKey, "PRODUCTION", true);
    }

    public static Bootpay withClientKey(String clientKey, String secretKey, String devMode) {
        return new Bootpay(clientKey, secretKey, devMode, true);
    }

    /**
     * 생성 빌더를 반환합니다 (3.3.0~).
     *
     * <p>신규 인증과 legacy 인증을 같은 형태로 생성하며, 환경은 {@code BootpayMode} 로 지정합니다.
     * 기존의 {@link #withClientKey(String, String)} 및 생성자는 그대로 사용할 수 있습니다.</p>
     *
     * <pre>{@code
     * Bootpay bootpay = Bootpay.builder()
     *         .clientKey(clientKey)
     *         .secretKey(secretKey)
     *         .mode(BootpayMode.PRODUCTION)
     *         .build();
     * }</pre>
     *
     * @return 빌더
     */
    public static BootpayBuilder builder() {
        return new BootpayBuilder();
    }

    /**
     * 액세스 토큰을 발급합니다 (3.3.0~, 통일 응답).
     *
     * <p>{@link #getAccessToken()} 과 같은 동작이며 반환 타입만 {@link BootpayResponse} 로 통일된
     * 버전입니다. client_key/secret_key 인증에서는 매 요청에 Basic Auth 헤더가 자동으로 붙으므로
     * 호출할 필요가 없습니다.</p>
     *
     * @return 발급 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse issueAccessToken() throws Exception {
        return BootpayResponse.ofPg(TokenService.getAccessToken(this));
    }

    //token
    public HashMap<String, Object> getAccessToken() throws Exception {
        return TokenService.getAccessToken(this);
    }
    public HashMap<String, Object> lookupBillingKey(String receiptId) throws Exception {
        return BillingService.lookupBillingKey(this, receiptId);
    }

    public HashMap<String, Object> lookupBillingKeyByKey(String billingKey) throws Exception {
        return BillingService.lookupBillingKeyByKey(this, billingKey);
    }

    /**
     * 우선순위(순차) 결제 빌링키 조회
     * GET subscribe/sequential_billing_key/{billing_key}?widget_key={widget_key}&user_id={user_id}
     * @param widgetKey 위젯 키
     * @param billingKey 빌링키
     * @param userId 조회 대상 회원 ID (서버가 빌링키 소유자 검증에 사용)
     */
    public HashMap<String, Object> lookupSequentialBillingKey(String widgetKey, String billingKey, String userId) throws Exception {
        return BillingService.lookupSequentialBillingKey(this, widgetKey, billingKey, userId);
    }



    public HashMap<String, Object> lookupPaymentMethods() throws Exception {
        return SellerService.lookupPaymentMethods(this);
    }

    public HashMap<String, Object> lookupOrderId(String orderId) throws Exception {
        return PaymentService.lookupOrderId(this, orderId);
    }

    //billing
    public HashMap<String, Object> getBillingKey(Subscribe subscribeBilling) throws Exception {
        return BillingService.getBillingKey(this, subscribeBilling);
    }
    public HashMap<String, Object> requestSubscribe(SubscribePayload payload) throws Exception {
        return BillingService.requestSubscribe(this, payload);
    }
    public HashMap<String, Object> reserveSubscribe(SubscribePayload payload) throws Exception {
        return BillingService.reserveSubscribe(this, payload);
    }

    public HashMap<String, Object> reserveSubscribeLookup(String reserveId) throws Exception {
        return BillingService.reserveSubscribeLookup(this, reserveId);
    }

    public HashMap<String, Object> reserveCancelSubscribe(String reserveId) throws Exception {
        return BillingService.reserveCancelSubscribe(this, reserveId);
    }
    public HashMap<String, Object> destroyBillingKey(String billingKey) throws Exception {
        return BillingService.destroyBillingKey(this, billingKey);
    }

    //cancel
    public HashMap<String, Object> receiptCancel(Cancel cancel) throws Exception {
        return CancelService.receiptCancel(this, cancel);
    }

    //easy
    public HashMap<String, Object> getUserToken(UserToken userToken) throws Exception {
        return EasyService.getUserToken(this, userToken);
    }

    //link
    public ResDefault<String> requestLink(Payload payload) throws Exception {
        return LinkService.requestLink(this, payload);
    }

    //submit
    public HashMap<String, Object> confirm(String receiptId) throws Exception {
        return ConfirmService.confirm(this, receiptId);
    }


    //veriy
    public HashMap<String, Object> getReceipt(String receiptId) throws Exception {
        return VerificationService.receipt(this, receiptId);
    }
    public HashMap<String, Object> getReceipt(String receiptId, boolean lookupUserData) throws Exception {
        return VerificationService.receipt(this, receiptId, lookupUserData);
    }
    public HashMap<String, Object> certificate(String receiptId) throws Exception {
        return VerificationService.certificate(this, receiptId);
    }

    public HashMap<String, Object> shippingStart(Shipping shipping) throws Exception {
        return EscrowService.shippingStart(this, shipping);
    }


    //cash cancel
    public HashMap<String, Object> requestCashReceipt(CashReceipt cashReceipt) throws Exception {
        return CashService.requestCashReceipt(this, cashReceipt);
    }

    public HashMap<String, Object> requestCashReceiptCancel(Cancel cancel) throws Exception {
        return CashService.requestCashReceiptCancel(this, cancel);
    }


    public HashMap<String, Object> requestCashReceiptByBootpay(CashReceipt cashReceipt) throws Exception {
        return CashService.requestCashReceiptByBootpay(this, cashReceipt);
    }

    public HashMap<String, Object> requestCashReceiptCancelByBootpay(Cancel cancel) throws Exception {
        return CashService.requestCashReceiptCancelByBootpay(this, cancel);
    }

    public HashMap<String, Object> requestAuthentication(Authentication authentication) throws Exception {
        return AuthService.requestAuthentication(this, authentication);
    }

    public HashMap<String, Object> confirmAuthentication(String receiptId, String otp) throws Exception {
        return AuthService.confirmAuthentication(this, receiptId, otp);
    }

    public HashMap<String, Object> realarmAuthentication(String receiptId) throws Exception {
        return AuthService.realarmAuthentication(this, receiptId);
    }

    public HashMap<String, Object> getBillingKeyTransfer(Subscribe subscribe) throws Exception {
        return BillingService.getBillingKeyTransfer(this, subscribe);
    }

    public HashMap<String, Object> publishBillingKeyTransfer(String receiptId) throws Exception {
        return BillingService.publishBillingKeyTransfer(this, receiptId);
    }

    //wallet
    /**
     * @deprecated 다음 메이저 버전에서 제거 예정. wallet 엔드포인트는 폐기 예정이며,
     * 결제는 Request::PaymentController#create 의 wallet_id + user_token 으로 처리됩니다.
     */
    @Deprecated
    public HashMap<String, Object> getUserWallets(String userId, boolean sandbox) throws Exception {
        return WalletService.getUserWallets(this, userId, sandbox);
    }

    /**
     * @deprecated 다음 메이저 버전에서 제거 예정. wallet 엔드포인트는 폐기 예정이며,
     * 결제는 wallet_id + user_token 흐름으로 전환하세요.
     */
    @Deprecated
    public HashMap<String, Object> requestWalletPayment(WalletPayment walletPayment) throws Exception {
        return WalletService.requestWalletPayment(this, walletPayment);
    }
}
