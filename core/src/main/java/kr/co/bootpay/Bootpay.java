package kr.co.bootpay;

import kr.co.bootpay.model.request.*;
import kr.co.bootpay.model.response.ResDefault;
import kr.co.bootpay.service.*;

import java.util.HashMap;

public class Bootpay extends BootpayObject {
    public Bootpay() { }

    public Bootpay(String restApplicationId, String privateKey) {
        super(restApplicationId, privateKey);
    }

    public Bootpay(String restApplicationId, String privateKey, String devMode) {
        super(restApplicationId, privateKey, devMode);
    }

    //token
    public HashMap<String, Object> getAccessToken() throws Exception {
        return TokenService.getAccessToken(this);
    }
    public HashMap<String, Object> lookupBillingKey(String receiptId) throws Exception {
        return BillingService.lookupBillingKey(this, receiptId);
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
}
