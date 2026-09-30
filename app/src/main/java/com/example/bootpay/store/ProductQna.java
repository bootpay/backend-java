package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.productQna.ProductQnaCreateParams;
import kr.co.bootpay.store.model.request.productQna.ProductQnaDeleteParams;
import kr.co.bootpay.store.model.request.productQna.ProductQnaListParams;
import kr.co.bootpay.store.model.request.productQna.ProductQnaUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;

/**
 * 상품문의 예제 (/v1/product-qnas)
 *
 * 회원(userId · loginId · userJwt) 또는 비회원(guestName + guestPassword)으로 작성하고,
 * 답변은 supervisor 전용이다.
 */
public class ProductQna {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload("hxS-Up--5RvT6oU6QJE0JA", "r5zxvDcQJiAP2PBQ0aJjSHQtblNmYFt6uFoEMhti_mg=");
            bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
            listSupervisor();
//            list("PRODUCT_ID");
//            detail("PRODUCT_QNA_ID");
//            createGuest("PRODUCT_ID");
//            update("PRODUCT_QNA_ID");
//            answer("PRODUCT_QNA_ID");
//            delete("PRODUCT_QNA_ID");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 상품별 문의 목록 (고객 모드) — 고객 모드는 productId 가 필수다
    public static void list(String productId) {
        try {
            BootpayStoreResponse res = bootpayStore.productQna.list(productId);
            if(res.isSuccess()) {
                System.out.println("productQna list success: " + res.getData());
            } else {
                System.out.println("productQna list false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 몰 전체 문의 목록 (운영자 모드) — view: "all" 이면 숨김 글까지 조회된다
    public static void listSupervisor() {
        try {
            ProductQnaListParams params = new ProductQnaListParams();
            params.view = "all";
            params.supervisor = true;

            BootpayStoreResponse res = bootpayStore.productQna.list(params);
            if(res.isSuccess()) {
                System.out.println("productQna list supervisor success: " + res.getData());
            } else {
                System.out.println("productQna list supervisor false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 상품문의 단건 — 다른 사람의 비밀글은 supervisor 일 때만 보인다
    public static void detail(String productQnaId) {
        try {
            BootpayStoreResponse res = bootpayStore.productQna.detail(productQnaId);
            if(res.isSuccess()) {
                System.out.println("productQna detail success: " + res.getData());
            } else {
                System.out.println("productQna detail false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 비회원 상품문의 작성 — 몰이 비회원 작성을 허용할 때만 가능하다
    public static void createGuest(String productId) {
        try {
            ProductQnaCreateParams params = new ProductQnaCreateParams();
            params.productId = productId;
            params.title = "재입고 문의";
            params.content = "품절된 색상 재입고 예정이 있나요?";
            params.isSecret = true;
            params.guestName = "비회원";
            params.guestPassword = "1234";

            BootpayStoreResponse res = bootpayStore.productQna.create(params);
            if(res.isSuccess()) {
                System.out.println("productQna create success: " + res.getData());
            } else {
                System.out.println("productQna create false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 상품문의 수정 (작성자 본인, 답변 전만) — 비회원은 guestPassword 로 본인을 증명한다
    public static void update(String productQnaId) {
        try {
            ProductQnaUpdateParams params = new ProductQnaUpdateParams();
            params.productQnaId = productQnaId;
            params.content = "블랙 색상 재입고 예정이 있나요?";
            params.guestPassword = "1234";

            BootpayStoreResponse res = bootpayStore.productQna.update(params);
            if(res.isSuccess()) {
                System.out.println("productQna update success: " + res.getData());
            } else {
                System.out.println("productQna update false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 상품문의 답변 등록·수정 (supervisor 전용)
    public static void answer(String productQnaId) {
        try {
            BootpayStoreResponse res = bootpayStore.productQna.answer(productQnaId, "다음 주 중 입고 예정입니다.");
            if(res.isSuccess()) {
                System.out.println("productQna answer success: " + res.getData());
            } else {
                System.out.println("productQna answer false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 상품문의 삭제 — guestPassword 는 query 가 아니라 본문으로 전송된다
    public static void delete(String productQnaId) {
        try {
            ProductQnaDeleteParams params = new ProductQnaDeleteParams();
            params.productQnaId = productQnaId;
            params.guestPassword = "1234";

            BootpayStoreResponse res = bootpayStore.productQna.delete(params);
            if(res.isSuccess()) {
                System.out.println("productQna delete success: " + res.getData());
            } else {
                System.out.println("productQna delete false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
