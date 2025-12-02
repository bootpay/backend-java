package kr.co.bootpay.pg.service;

import kr.co.bootpay.pg.BootpayObject;
import kr.co.bootpay.pg.model.request.Subscribe;
import kr.co.bootpay.pg.model.request.SubscribePayload;

import java.util.HashMap;

public class BillingService {

    private static void validateToken(BootpayObject bootpay) throws Exception {
        if (bootpay.token == null || bootpay.token.isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
    }

    static public HashMap<String, Object> getBillingKey(BootpayObject bootpay, Subscribe subscribe) throws Exception {
        validateToken(bootpay);
        if (subscribe.orderName == null || subscribe.orderName.isEmpty()) throw new Exception("order_name 값을 입력해주세요.");
        if (subscribe.subscriptionId == null || subscribe.subscriptionId.isEmpty()) throw new Exception("order_id 주문번호를 설정해주세요.");
        if (subscribe.pg == null || subscribe.pg.isEmpty()) throw new Exception("결제하고자 하는 pg alias를 입력해주세요.");

        return bootpay.doPost("request/subscribe", subscribe);
    }

    // 빌링키 조회
    static public HashMap<String, Object> lookupBillingKey(BootpayObject bootpay, String receiptId) throws Exception {
        validateToken(bootpay);
        if (receiptId == null || receiptId.isEmpty()) throw new Exception("receiptId 값이 비어있습니다.");

        return bootpay.doGet("subscribe/billing_key/" + receiptId);
    }

    // 빌링키 조회 (by key)
    static public HashMap<String, Object> lookupBillingKeyByKey(BootpayObject bootpay, String billingKey) throws Exception {
        validateToken(bootpay);
        if (billingKey == null || billingKey.isEmpty()) throw new Exception("billingKey 값이 비어있습니다.");

        return bootpay.doGet("billing_key/" + billingKey);
    }

    static public HashMap<String, Object> destroyBillingKey(BootpayObject bootpay, String billingKey) throws Exception {
        validateToken(bootpay);
        if (billingKey == null || billingKey.isEmpty()) throw new Exception("billingKey 값이 비어있습니다.");

        return bootpay.doDelete("subscribe/billing_key/" + billingKey);
    }

    static public HashMap<String, Object> requestSubscribe(BootpayObject bootpay, SubscribePayload payload) throws Exception {
        validateToken(bootpay);
        if (payload.billingKey == null || payload.billingKey.isEmpty()) throw new Exception("billing_key 값을 입력해주세요.");
        if (payload.orderName == null || payload.orderName.isEmpty()) throw new Exception("order_name 값을 입력해주세요.");
        if (payload.price <= 0) throw new Exception("price 금액을 설정을 해주세요.");
        if (payload.orderId == null || payload.orderId.isEmpty()) throw new Exception("order_id 주문번호를 설정해주세요.");

        return bootpay.doPost("subscribe/payment", payload);
    }

    static public HashMap<String, Object> reserveSubscribe(BootpayObject bootpay, SubscribePayload reserve) throws Exception {
        validateToken(bootpay);
        if (reserve.billingKey == null || reserve.billingKey.isEmpty()) throw new Exception("billing_key 값을 입력해주세요.");
        if (reserve.orderName == null || reserve.orderName.isEmpty()) throw new Exception("order_name 값을 입력해주세요.");
        if (reserve.price <= 0) throw new Exception("price 금액을 설정을 해주세요.");
        if (reserve.orderId == null || reserve.orderId.isEmpty()) throw new Exception("order_id 주문번호를 설정해주세요.");
        if (reserve.reserveExecuteAt == null || reserve.reserveExecuteAt.isEmpty()) throw new Exception("execute_at 실행 시간을 설정해주세요.");

        return bootpay.doPost("subscribe/payment/reserve", reserve);
    }

    static public HashMap<String, Object> reserveSubscribeLookup(BootpayObject bootpay, String reserveId) throws Exception {
        validateToken(bootpay);
        if (reserveId == null || reserveId.isEmpty()) throw new Exception("reserveId 값이 비어있습니다.");

        return bootpay.doGet("subscribe/payment/reserve/" + reserveId);
    }

    static public HashMap<String, Object> reserveCancelSubscribe(BootpayObject bootpay, String reserve_id) throws Exception {
        validateToken(bootpay);
        if (reserve_id == null || reserve_id.isEmpty()) throw new Exception("reserve_id 값이 비어있습니다.");

        return bootpay.doDelete("subscribe/payment/reserve/" + reserve_id);
    }

    // 계좌 빌링키 발급 요청하기
    static public HashMap<String, Object> getBillingKeyTransfer(BootpayObject bootpay, Subscribe subscribe) throws Exception {
        validateToken(bootpay);
        if (subscribe.orderName == null || subscribe.orderName.isEmpty()) throw new Exception("order_name 값을 입력해주세요.");
        if (subscribe.subscriptionId == null || subscribe.subscriptionId.isEmpty()) throw new Exception("order_id 주문번호를 설정해주세요.");
        if (subscribe.pg == null || subscribe.pg.isEmpty()) throw new Exception("결제하고자 하는 pg alias를 입력해주세요.");
        if (subscribe.bankName == null || subscribe.bankName.isEmpty()) throw new Exception("계좌 은행명을 입력해주세요.");
        if (subscribe.bankAccount == null || subscribe.bankAccount.isEmpty()) throw new Exception("계좌 번호를 입력해주세요.");
        if (subscribe.username == null || subscribe.username.isEmpty()) throw new Exception("계좌주 이름을 입력해주세요.");
        if (subscribe.identityNo == null || subscribe.identityNo.isEmpty()) throw new Exception("계좌주의 생년월일 6자리 또는 사업자번호 10자리를 입력해주세요.");

        return bootpay.doPost("request/subscribe/automatic-transfer", subscribe);
    }

    // 계좌 출금 동의 확인
    static public HashMap<String, Object> publishBillingKeyTransfer(BootpayObject bootpay, String receiptId) throws Exception {
        validateToken(bootpay);
        if (receiptId == null || receiptId.isEmpty()) throw new Exception("receiptId 값을 입력해주세요.");

        SubscribePayload payload = new SubscribePayload();
        payload.receiptId = receiptId;

        return bootpay.doPost("request/subscribe/automatic-transfer/publish", payload);
    }
}
