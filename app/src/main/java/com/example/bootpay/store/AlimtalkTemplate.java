package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkTemplateCreateParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkTemplateExportParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkTemplateListParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkTemplateUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 가맹점 자체 알림톡 템플릿 예제.
 *
 * 흐름: (초안 생성 → 확인 → 대행사 등록) → 검수 요청 → 승인(APR) → 발송 가능
 * ⚠️ create 의 register 를 false 로 주지 않으면 생성 즉시 대행사·카카오에 실제 등록된다.
 */
public class AlimtalkTemplate {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload("hxS-Up--5RvT6oU6QJE0JA", "r5zxvDcQJiAP2PBQ0aJjSHQtblNmYFt6uFoEMhti_mg=");
            bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
            list();
//            createDraft();
//            register("TEMPLATE_ID");
//            inspect("TEMPLATE_ID");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 자체 템플릿 목록 (⚠️ 페이지네이션이 없다)
    public static void list() {
        try {
            AlimtalkTemplateListParams params = new AlimtalkTemplateListParams();
            params.ins = "3"; // 3 APR(승인)
            params.sort = "latest";

            BootpayStoreResponse res = bootpayStore.alimtalkTemplate.list(params);
            if(res.isSuccess()) {
                System.out.println("alimtalkTemplate list success: " + res.getData());
            } else {
                System.out.println("alimtalkTemplate list false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 초안 생성 (register=false 로 두고 내용을 확인한 뒤 register 를 부른다)
    public static void createDraft() {
        try {
            Map<String, Object> button = new HashMap<>();
            button.put("name", "주문 확인");
            button.put("type", "WL");
            button.put("url_mobile", "https://example.com/orders");

            List<Map<String, Object>> buttons = new ArrayList<>();
            buttons.add(button);

            Map<String, Object> examples = new HashMap<>();
            examples.put("user_name", "홍길동");

            AlimtalkTemplateCreateParams params = new AlimtalkTemplateCreateParams();
            params.kspId = "KSP_ID";
            params.register = false; // ⚠️ 생략하면 대행사·카카오에 즉시 등록된다
            params.name = "주문 완료 안내";
            params.content = "#{user_name}님, 주문이 완료되었습니다.";
            params.msgType = "BA";
            params.emphasizeType = "NONE";
            params.buttons = buttons;
            params.examples = examples;

            BootpayStoreResponse res = bootpayStore.alimtalkTemplate.create(params);
            if(res.isSuccess()) {
                System.out.println("alimtalkTemplate create success: " + res.getData());
            } else {
                System.out.println("alimtalkTemplate create false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 수정 (⚠️ 부분 수정이 아니다 — 항상 전체 필드를 보낸다)
    public static void update(String templateId) {
        try {
            AlimtalkTemplateUpdateParams params = new AlimtalkTemplateUpdateParams();
            params.name = "주문 완료 안내";
            params.content = "#{user_name}님, 주문이 완료되었습니다. 감사합니다.";
            params.msgType = "BA";
            params.emphasizeType = "NONE";

            BootpayStoreResponse res = bootpayStore.alimtalkTemplate.update(templateId, params);
            if(res.isSuccess()) {
                System.out.println("alimtalkTemplate update success: " + res.getData());
            } else {
                System.out.println("alimtalkTemplate update false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 초안을 대행사에 등록 (⚠️ 실제 등록된다)
    public static void register(String templateId) {
        try {
            BootpayStoreResponse res = bootpayStore.alimtalkTemplate.register(templateId);
            if(res.isSuccess()) {
                System.out.println("alimtalkTemplate register success: " + res.getData());
            } else {
                System.out.println("alimtalkTemplate register false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 검수 요청 (⚠️ 카카오에 검수를 요청하며 취소할 수 없다)
    public static void inspect(String templateId) {
        try {
            BootpayStoreResponse res = bootpayStore.alimtalkTemplate.inspect(templateId);
            if(res.isSuccess()) {
                System.out.println("alimtalkTemplate inspect success: " + res.getData());
            } else {
                System.out.println("alimtalkTemplate inspect false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // csv 로 내보내면 파싱 없이 { body, content_type } 을 돌려준다
    public static void exportCsv() {
        try {
            AlimtalkTemplateExportParams params = new AlimtalkTemplateExportParams();
            params.format = "csv";
            params.scope = "private";

            BootpayStoreResponse res = bootpayStore.alimtalkTemplate.export(params);
            if(res.isSuccess()) {
                System.out.println("alimtalkTemplate export success: " + res.getData().get("body"));
            } else {
                System.out.println("alimtalkTemplate export false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
