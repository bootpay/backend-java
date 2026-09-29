package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.productReview.ProductReviewCreateParams;
import kr.co.bootpay.store.model.request.productReview.ProductReviewDeleteParams;
import kr.co.bootpay.store.model.request.productReview.ProductReviewListParams;
import kr.co.bootpay.store.model.request.productReview.ProductReviewUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;

import java.util.Arrays;

/**
 * 상품평 예제 (/v1/reviews)
 *
 * 작성·수정은 회원 전용, 판매자 답글은 supervisor 전용이다.
 * 상품 상세의 공개 상품평 목록은 별도 경로(GET /v1/products/{product_id}/reviews)를 쓴다.
 */
public class ProductReview {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload("hxS-Up--5RvT6oU6QJE0JA", "r5zxvDcQJiAP2PBQ0aJjSHQtblNmYFt6uFoEMhti_mg=");
            bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
            listSupervisor();
//            list("USER_ID");
//            detail("REVIEW_ID");
//            create("ORDER_ID", "PRODUCT_ID", "USER_ID");
//            update("REVIEW_ID", "USER_ID");
//            reply("REVIEW_ID");
//            delete("REVIEW_ID");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 회원이 쓴 상품평 목록
    public static void list(String userId) {
        try {
            ProductReviewListParams params = new ProductReviewListParams();
            params.userId = userId;

            BootpayStoreResponse res = bootpayStore.productReview.list(params);
            if(res.isSuccess()) {
                System.out.println("productReview list success: " + res.getData());
            } else {
                System.out.println("productReview list false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 몰 전체 상품평 목록 (운영자 모드) — view: "all" 이면 숨김 상품평까지 조회된다
    public static void listSupervisor() {
        try {
            ProductReviewListParams params = new ProductReviewListParams();
            params.view = "all";
            params.supervisor = true;

            BootpayStoreResponse res = bootpayStore.productReview.list(params);
            if(res.isSuccess()) {
                System.out.println("productReview list supervisor success: " + res.getData());
            } else {
                System.out.println("productReview list supervisor false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 상품평 단건 — 숨김 상품평은 작성 회원 본인 또는 supervisor 만 볼 수 있다
    public static void detail(String productReviewId) {
        try {
            BootpayStoreResponse res = bootpayStore.productReview.detail(productReviewId);
            if(res.isSuccess()) {
                System.out.println("productReview detail success: " + res.getData());
            } else {
                System.out.println("productReview detail false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 상품평 작성 (회원 전용) — orderId 는 구매확정 주문, images 는 사진 URL 최대 5개
    public static void create(String orderId, String productId, String userId) {
        try {
            ProductReviewCreateParams params = new ProductReviewCreateParams();
            params.orderId = orderId;
            params.productId = productId;
            params.userId = userId;
            params.rating = 5;
            params.content = "배송도 빠르고 품질도 좋습니다.";
            params.images = Arrays.<Object>asList("https://img.example.com/review.png");

            BootpayStoreResponse res = bootpayStore.productReview.create(params);
            if(res.isSuccess()) {
                System.out.println("productReview create success: " + res.getData());
            } else {
                System.out.println("productReview create false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 상품평 수정 (작성 회원 본인, 작성 후 7일 이내) — images 는 통째로 교체된다
    public static void update(String productReviewId, String userId) {
        try {
            ProductReviewUpdateParams params = new ProductReviewUpdateParams();
            params.productReviewId = productReviewId;
            params.userId = userId;
            params.rating = 4;
            params.content = "재구매 의사 있습니다.";

            BootpayStoreResponse res = bootpayStore.productReview.update(params);
            if(res.isSuccess()) {
                System.out.println("productReview update success: " + res.getData());
            } else {
                System.out.println("productReview update false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 판매자 답글 등록·수정 (supervisor 전용, 10자 이상)
    public static void reply(String productReviewId) {
        try {
            BootpayStoreResponse res = bootpayStore.productReview.reply(productReviewId, "소중한 후기 감사합니다.");
            if(res.isSuccess()) {
                System.out.println("productReview reply success: " + res.getData());
            } else {
                System.out.println("productReview reply false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 상품평 삭제 — 지급된 적립금 회수·상품 통계 차감이 함께 일어난다
    public static void delete(String productReviewId) {
        try {
            ProductReviewDeleteParams params = new ProductReviewDeleteParams();
            params.productReviewId = productReviewId;
            params.reason = "광고성 리뷰";
            params.supervisor = true;

            BootpayStoreResponse res = bootpayStore.productReview.delete(params);
            if(res.isSuccess()) {
                System.out.println("productReview delete success: " + res.getData());
            } else {
                System.out.println("productReview delete false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
