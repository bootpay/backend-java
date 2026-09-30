package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.faq.FaqCreateParams;
import kr.co.bootpay.store.model.request.faq.FaqListParams;
import kr.co.bootpay.store.model.request.faq.FaqUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;

import java.util.Arrays;

/**
 * FAQ 예제 (/v1/faqs)
 *
 * 조회는 고객·운영자 모드 둘 다, 등록·수정·삭제는 supervisor 전용이다.
 */
public class Faq {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload("hxS-Up--5RvT6oU6QJE0JA", "r5zxvDcQJiAP2PBQ0aJjSHQtblNmYFt6uFoEMhti_mg=");
            bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
            list();
            listSupervisor();
//            detail("FAQ_ID");
//            create();
//            update("FAQ_ID");
//            delete("FAQ_ID");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // FAQ 목록 (고객 모드) — page 1 / limit 20 이 기본값
    public static void list() {
        try {
            BootpayStoreResponse res = bootpayStore.faq.list();
            if(res.isSuccess()) {
                System.out.println("faq list success: " + res.getData());
            } else {
                System.out.println("faq list false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // FAQ 목록 (운영자 모드) — view: "all" 이면 비공개 FAQ 까지 조회된다
    public static void listSupervisor() {
        try {
            FaqListParams params = new FaqListParams();
            params.page = 1;
            params.limit = 20;
            params.view = "all";
            params.supervisor = true;

            BootpayStoreResponse res = bootpayStore.faq.list(params);
            if(res.isSuccess()) {
                System.out.println("faq list supervisor success: " + res.getData());
            } else {
                System.out.println("faq list supervisor false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // FAQ 단건 — 비공개 FAQ 는 supervisor 일 때만 보인다
    public static void detail(String faqId) {
        try {
            BootpayStoreResponse res = bootpayStore.faq.detail(faqId, true);
            if(res.isSuccess()) {
                System.out.println("faq detail success: " + res.getData());
            } else {
                System.out.println("faq detail false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // FAQ 등록 (supervisor 전용)
    public static void create() {
        try {
            FaqCreateParams params = new FaqCreateParams();
            params.title = "배송은 얼마나 걸리나요?";
            params.content = "결제 확인 후 2~3 영업일 내에 출고됩니다.";
            params.isDisplay = true;

            BootpayStoreResponse res = bootpayStore.faq.create(params);
            if(res.isSuccess()) {
                System.out.println("faq create success: " + res.getData());
            } else {
                System.out.println("faq create false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // FAQ 수정 (supervisor 전용) — 보낸 필드만 바뀐다, images 는 목록 전체를 교체한다
    public static void update(String faqId) {
        try {
            FaqUpdateParams params = new FaqUpdateParams();
            params.faqId = faqId;
            params.content = "결제 확인 후 1~2 영업일 내에 출고됩니다.";
            params.images = Arrays.<Object>asList("https://img.example.com/faq.png");

            BootpayStoreResponse res = bootpayStore.faq.update(params);
            if(res.isSuccess()) {
                System.out.println("faq update success: " + res.getData());
            } else {
                System.out.println("faq update false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // FAQ 삭제 (supervisor 전용)
    public static void delete(String faqId) {
        try {
            BootpayStoreResponse res = bootpayStore.faq.delete(faqId);
            if(res.isSuccess()) {
                System.out.println("faq delete success: " + res.getData());
            } else {
                System.out.println("faq delete false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
