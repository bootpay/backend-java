package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.pojo.SAlimtalkRecipient;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkMessageListParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkOfficialListParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkSendBulkParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkSendParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkWebhookUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 알림톡 v1 API 예제.
 *
 * ⚠️ 발송 계열(send / sendBulk / sender.otp / sender.create)은 실제로 카카오톡·문자가 나가고 과금된다.
 *    샌드박스가 없으므로 main 에서는 조회 계열만 켜 두었다.
 */
public class Alimtalk {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload("hxS-Up--5RvT6oU6QJE0JA", "r5zxvDcQJiAP2PBQ0aJjSHQtblNmYFt6uFoEMhti_mg=");
            bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
            senderList();
            officialList();
            messageList();
//            send();
//            sendBulk();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 연동한 카카오채널 목록 조회
    public static void senderList() {
        try {
            BootpayStoreResponse res = bootpayStore.alimtalkSender.list();
            if(res.isSuccess()) {
                System.out.println("alimtalkSender list success: " + res.getData());
            } else {
                System.out.println("alimtalkSender list false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 공식 템플릿 검색 (검수 없이 즉시 발송 가능한 카탈로그)
    public static void officialList() {
        try {
            AlimtalkOfficialListParams params = new AlimtalkOfficialListParams();
            params.keyword = "주문";
            params.per = 10;

            BootpayStoreResponse res = bootpayStore.alimtalkOfficial.list(params);
            if(res.isSuccess()) {
                System.out.println("alimtalkOfficial list success: " + res.getData());
            } else {
                System.out.println("alimtalkOfficial list false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 발송내역 조회 (기본 최근 30일, 최대 92일)
    public static void messageList() {
        try {
            AlimtalkMessageListParams params = new AlimtalkMessageListParams();
            params.status = "success";
            params.limit = 20;

            BootpayStoreResponse res = bootpayStore.alimtalkMessage.list(params);
            if(res.isSuccess()) {
                System.out.println("alimtalkMessage list success: " + res.getData());
            } else {
                System.out.println("alimtalkMessage list false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 단건 발송 — ⚠️ 실제로 카카오톡이 발송되고 과금된다
    public static void send() {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("company_name", "부트페이몰");
            variables.put("user_name", "홍길동");

            AlimtalkSendParams params = new AlimtalkSendParams();
            params.templateCode = "TEMPLATE_CODE";
            params.to = "01000000000";
            params.variables = variables;
            params.refId = "order-20260827-0001"; // 멱등 키
            params.fallback = false;              // 미지정(null)이면 프로젝트 기본값을 따른다

            BootpayStoreResponse res = bootpayStore.alimtalkSend.send(params);
            if(res.isSuccess()) {
                System.out.println("alimtalkSend send success: " + res.getData());
            } else {
                System.out.println("alimtalkSend send false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 벌크 발송 — ⚠️ 수신자 수만큼 실제 발송되고 과금된다
    public static void sendBulk() {
        try {
            List<SAlimtalkRecipient> recipients = new ArrayList<>();
            Map<String, Object> variables = new HashMap<>();
            variables.put("user_name", "홍길동");
            recipients.add(new SAlimtalkRecipient("01000000000", "bulk-0001", variables));

            AlimtalkSendBulkParams params = new AlimtalkSendBulkParams();
            params.templateCode = "TEMPLATE_CODE";
            params.recipients = recipients;

            BootpayStoreResponse res = bootpayStore.alimtalkSend.sendBulk(params);
            if(res.isSuccess()) {
                System.out.println("alimtalkSend sendBulk success: " + res.getData());
            } else {
                System.out.println("alimtalkSend sendBulk false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 발송 전 수신거부 사전 확인 (⚠️ 1회 최대 1,000건)
    public static void optoutCheck() {
        try {
            BootpayStoreResponse res = bootpayStore.alimtalkOptout.check(Arrays.asList("01000000000", "01011112222"));
            if(res.isSuccess()) {
                System.out.println("alimtalkOptout check success: " + res.getData());
            } else {
                System.out.println("alimtalkOptout check false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 알림톡 전용 웹훅 설정 (주문·구독 웹훅과 별개 — https 만 허용)
    public static void webhookUpdate() {
        try {
            AlimtalkWebhookUpdateParams params = new AlimtalkWebhookUpdateParams();
            params.url = "https://example.com/hooks/alimtalk";
            params.events = Arrays.asList(301, 302, 310, 311);
            params.enabled = true;

            BootpayStoreResponse res = bootpayStore.alimtalkWebhook.update(params);
            if(res.isSuccess()) {
                System.out.println("alimtalkWebhook update success: " + res.getData());
            } else {
                System.out.println("alimtalkWebhook update false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
