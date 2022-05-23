package kr.co.bootpay;

import kr.co.bootpay.model.request.*;
import kr.co.bootpay.model.response.ResDefault;
import kr.co.bootpay.service.*;

import java.util.HashMap;

public class Bootpay extends BootpayObject {
    public Bootpay() { }

    public Bootpay(String rest_application_id, String private_key) {
        super(rest_application_id, private_key);
    }

    public Bootpay(String rest_application_id, String private_key, String devMode) {
        super(rest_application_id, private_key, devMode);
    }

    //token
    public HashMap<String, Object> getAccessToken() throws Exception {
        return TokenService.getAccessToken(this);
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
    public HashMap<String, Object> reserveCancelSubscribe(String reserve_id) throws Exception {
        return BillingService.reserveCancelSubscribe(this, reserve_id);
    }
    public HashMap<String, Object> destroyBillingKey(String billing_key) throws Exception {
        return BillingService.destroyBillingKey(this, billing_key);
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
}
