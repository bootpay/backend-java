package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.inquiry.InquiryCreateParams;
import kr.co.bootpay.store.model.request.inquiry.InquiryDeleteParams;
import kr.co.bootpay.store.model.request.inquiry.InquiryListParams;
import kr.co.bootpay.store.model.request.inquiry.InquiryUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;

/**
 * 1:1 문의 예제 (/v1/inquiries)
 *
 * 작성·수정은 회원 전용, 답변은 supervisor 전용이고, 조회·삭제는 두 모드 모두 가능하다.
 * 회원은 userId(회원 _id 또는 외부 회원 ID) · loginId · userJwt 중 하나로 지정한다.
 */
public class Inquiry {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload("hxS-Up--5RvT6oU6QJE0JA", "r5zxvDcQJiAP2PBQ0aJjSHQtblNmYFt6uFoEMhti_mg=");
            bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
            listSupervisor();
//            list("USER_ID");
//            detail("INQUIRY_ID");
//            create("USER_ID");
//            update("INQUIRY_ID", "USER_ID");
//            answer("INQUIRY_ID");
//            delete("INQUIRY_ID");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 회원 본인 문의 목록 — answered: true 답변 완료만 / false 미답변만 / 미지정 전체
    public static void list(String userId) {
        try {
            InquiryListParams params = new InquiryListParams();
            params.userId = userId;
            params.answered = false;

            BootpayStoreResponse res = bootpayStore.inquiry.list(params);
            if(res.isSuccess()) {
                System.out.println("inquiry list success: " + res.getData());
            } else {
                System.out.println("inquiry list false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 몰 전체 문의 목록 (운영자 모드)
    public static void listSupervisor() {
        try {
            InquiryListParams params = new InquiryListParams();
            params.supervisor = true;

            BootpayStoreResponse res = bootpayStore.inquiry.list(params);
            if(res.isSuccess()) {
                System.out.println("inquiry list supervisor success: " + res.getData());
            } else {
                System.out.println("inquiry list supervisor false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 문의 단건
    public static void detail(String inquiryId) {
        try {
            BootpayStoreResponse res = bootpayStore.inquiry.detail(inquiryId);
            if(res.isSuccess()) {
                System.out.println("inquiry detail success: " + res.getData());
            } else {
                System.out.println("inquiry detail false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 문의 작성 (회원 전용) — productId 는 이 몰의 상품, orderId 는 작성 회원의 주문이어야 한다
    public static void create(String userId) {
        try {
            InquiryCreateParams params = new InquiryCreateParams();
            params.userId = userId;
            params.title = "배송 문의";
            params.content = "주문한 상품이 언제 도착하나요?";

            BootpayStoreResponse res = bootpayStore.inquiry.create(params);
            if(res.isSuccess()) {
                System.out.println("inquiry create success: " + res.getData());
            } else {
                System.out.println("inquiry create false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 문의 수정 (작성 회원 본인, 답변 전만) — title 에 "" 를 보내면 제목이 지워진다
    public static void update(String inquiryId, String userId) {
        try {
            InquiryUpdateParams params = new InquiryUpdateParams();
            params.inquiryId = inquiryId;
            params.userId = userId;
            params.content = "주소를 변경하고 싶습니다.";

            BootpayStoreResponse res = bootpayStore.inquiry.update(params);
            if(res.isSuccess()) {
                System.out.println("inquiry update success: " + res.getData());
            } else {
                System.out.println("inquiry update false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 문의 답변 등록·수정 (supervisor 전용) — 없으면 만들고 있으면 내용을 바꾼다
    public static void answer(String inquiryId) {
        try {
            BootpayStoreResponse res = bootpayStore.inquiry.answer(inquiryId, "2~3 영업일 내에 발송됩니다.");
            if(res.isSuccess()) {
                System.out.println("inquiry answer success: " + res.getData());
            } else {
                System.out.println("inquiry answer false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 문의 삭제 (작성 회원 본인 또는 supervisor) — 달린 답변도 함께 삭제된다
    public static void delete(String inquiryId) {
        try {
            InquiryDeleteParams params = new InquiryDeleteParams();
            params.inquiryId = inquiryId;
            params.supervisor = true;

            BootpayStoreResponse res = bootpayStore.inquiry.delete(params);
            if(res.isSuccess()) {
                System.out.println("inquiry delete success: " + res.getData());
            } else {
                System.out.println("inquiry delete false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
