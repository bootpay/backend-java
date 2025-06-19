package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.order.cancel.OrderCancelActionParams;
import kr.co.bootpay.store.model.request.order.cancel.OrderCancelListParams;
import kr.co.bootpay.store.model.request.order.cancel.OrderCancelParams;


public class OrderCancel {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload("4T4tlQq2xpPHioq216K-RQ", "szucYyZ9NtrmUtCu6gtUEm6aMOnhFQqCiSE9AK9I6fo=");
            bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
            getToken();
            list();
    //        request();
    //        detail();
    //        orderCancel();
    //        withdraw();
    //        approve();
    //        reject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getToken() {
        try {
            BootpayStoreResponse res = bootpayStore.getAccessToken();
            if(res.isSuccess()) {
                System.out.println("goGetToken success: " + res.getData());
            } else {
                System.out.println("goGetToken false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    {count=8.0, http_status=200, list=[{name=상급 휴대폰 케이스, desc=null, content_type=3.0, content=, content_images=[], images=[], use_stock=false, stock=0.0, stock_safe=0.0, type=0.0, object_id=null, tax_type=1.0, status_visible=true, status_frozen=false, save_by=3.0, status_review=3.0, status_display=true, display_start_at=null, display_end_at=null, status_sale=true, sale_start_at=null, sale_end_at=null, cost_price=0.0, display_price=2500.0, tax_free_price=0.0, use_discount=false, discount_price=0.0, discount_price_type=2.0, use_discount_period=false, discount_start_at=null, discount_end_at=null, count_sale=0.0, count_qna=0.0, count_like=0.0, count_review=0.0, sku=null, barcode=null, search_tags=[], event_tags=[], target_user_tags=[], delivery_tags=[], emotion_tags=[], tags=[상급 휴대폰 케이스], use_cancel=false, use_minor=false, use_accumulation=false, use_like=true, use_review=true, use_display_stock=false, use_able_refund=true, use_able_cart=true, use_seo=false, seo_page_title=null, seo_meta_description=null, seo_meta_tags=[], use_limitation_person=null, limitation_person=null, use_bulk_purchase_discount=false, bulk_purchase_discount=null, use_review_point=false, review_point_hash=null, free_gift=null, use_option=false, use_product_option=false, use_additional_option=false, _id=67c95e64d01640bb9859c629, p_id=67c92fb8d01640bb9859c613, s_id=67c92fb8d01640bb9859c610, updated_at=2025-03-06T17:35:48+09:00, created_at=2025-03-06T17:35:48+09:00, category_id=null, subscription_setting_id=null, use_delivery_shipping=false, use_delivery_shipping_bundle=false, use_display_period=false, use_subscription=false, use_sale_period=false, use_subscription_times=false, option_count=0.0, category=null, subscription=null, subscription_default_period=null}, {name=중급 휴대폰 케이스, desc=null, content_type=3.0, content=, content_images=[], images=[], use_stock=false, stock=0.0, stock_safe=0.0, type=0.0, object_id=null, tax_type=1.0, status_visible=true, status_frozen=false, save_by=3.0, status_review=3.0, status_display=true, display_start_at=null, display_end_at=null, status_sale=true, sale_start_at=null, sale_end_at=null, cost_price=0.0, display_price=2000.0, tax_free_price=0.0, use_discount=false, discount_price=0.0, discount_price_type=2.0, use_discount_period=false, discount_start_at=null, discount_end_at=null, count_sale=0.0, count_qna=0.0, count_like=0.0, count_review=0.0, sku=null, barcode=null, search_tags=[], event_tags=[], target_user_tags=[], delivery_tags=[], emotion_tags=[], tags=[중급 휴대폰 케이스], use_cancel=false, use_minor=false, use_accumulation=false, use_like=true, use_review=true, use_display_stock=false, use_able_refund=true, use_able_cart=true, use_seo=false, seo_page_title=null, seo_meta_description=null, seo_meta_tags=[], use_limitation_person=null, limitation_person=null, use_bulk_purchase_discount=false, bulk_purchase_discount=null, use_review_point=false, review_point_hash=null, free_gift=null, use_option=false, use_product_option=false, use_additional_option=false, _id=67c95e5cd01640bb9859c627, p_id=67c92fb8d01640bb9859c613, s_id=67c92fb8d01640bb9859c610, updated_at=2025-03-06T17:35:40+09:00, created_at=2025-03-06T17:35:40+09:00, category_id=null, subscription_setting_id=null, use_delivery_shipping=false, use_delivery_shipping_bundle=false, use_display_period=false, use_subscription=false, use_sale_period=false, use_subscription_times=false, option_count=0.0, category=null, subscription=null, subscription_default_period=null}, {name=초급 휴대폰 케이스, desc=null, content_type=3.0, content=, content_images=[], images=[], use_stock=false, stock=0.0, stock_safe=0.0, type=0.0, object_id=null, tax_type=1.0, status_visible=true, status_frozen=false, save_by=3.0, status_review=3.0, status_display=true, display_start_at=null, display_end_at=null, status_sale=true, sale_start_at=null, sale_end_at=null, cost_price=0.0, display_price=1500.0, tax_free_price=0.0, use_discount=false, discount_price=0.0, discount_price_type=2.0, use_discount_period=false, discount_start_at=null, discount_end_at=null, count_sale=0.0, count_qna=0.0, count_like=0.0, count_review=0.0, sku=null, barcode=null, search_tags=[], event_tags=[], target_user_tags=[], delivery_tags=[], emotion_tags=[], tags=[초급 휴대폰 케이스], use_cancel=false, use_minor=false, use_accumulation=false, use_like=true, use_review=true, use_display_stock=false, use_able_refund=true, use_able_cart=true, use_seo=false, seo_page_title=null, seo_meta_description=null, seo_meta_tags=[], use_limitation_person=null, limitation_person=null, use_bulk_purchase_discount=false, bulk_purchase_discount=null, use_review_point=false, review_point_hash=null, free_gift=null, use_option=false, use_product_option=false, use_additional_option=false, _id=67c95e53d01640bb9859c625, p_id=67c92fb8d01640bb9859c613, s_id=67c92fb8d01640bb9859c610, updated_at=2025-03-06T17:35:31+09:00, created_at=2025-03-06T17:35:31+09:00, category_id=null, subscription_setting_id=null, use_delivery_shipping=false, use_delivery_shipping_bundle=false, use_display_period=false, use_subscription=false, use_sale_period=false, use_subscription_times=false, option_count=0.0, category=null, subscription=null, subscription_default_period=null}, {name=휴대폰 케이스, desc=null, content_type=3.0, content=, content_images=[], images=[], use_stock=false, stock=0.0, stock_safe=0.0, type=0.0, object_id=null, tax_type=1.0, status_visible=true, status_frozen=false, save_by=3.0, status_review=3.0, status_display=true, display_start_at=null, display_end_at=null, status_sale=true, sale_start_at=null, sale_end_at=null, cost_price=0.0, display_price=1000.0, tax_free_price=0.0, use_discount=false, discount_price=0.0, discount_price_type=2.0, use_discount_period=false, discount_start_at=null, discount_end_at=null, count_sale=0.0, count_qna=0.0, count_like=0.0, count_review=0.0, sku=null, barcode=null, search_tags=[], event_tags=[], target_user_tags=[], delivery_tags=[], emotion_tags=[], tags=[휴대폰 케이스], use_cancel=false, use_minor=false, use_accumulation=false, use_like=true, use_review=true, use_display_stock=false, use_able_refund=true, use_able_cart=true, use_seo=false, seo_page_title=null, seo_meta_description=null, seo_meta_tags=[], use_limitation_person=null, limitation_person=null, use_bulk_purchase_discount=false, bulk_purchase_discount=null, use_review_point=false, review_point_hash=null, free_gift=null, use_option=false, use_product_option=false, use_additional_option=false, _id=67c95e41d01640bb9859c623, p_id=67c92fb8d01640bb9859c613, s_id=67c92fb8d01640bb9859c610, updated_at=2025-03-06T17:35:13+09:00, created_at=2025-03-06T17:35:13+09:00, category_id=null, subscription_setting_id=null, use_delivery_shipping=false, use_delivery_shipping_bundle=false, use_display_period=false, use_subscription=false, use_sale_period=false, use_subscription_times=false, option_count=0.0, category=null, subscription=null, subscription_default_period=null}, {name=상급 머그컵, desc=null, content_type=3.0, content=, content_images=[], images=[], use_stock=false, stock=0.0, stock_safe=0.0, type=0.0, object_id=null, tax_type=1.0, status_visible=true, status_frozen=false, save_by=3.0, status_review=3.0, status_display=true, display_start_at=null, display_end_at=null, status_sale=true, sale_start_at=null, sale_end_at=null, cost_price=0.0, display_price=2000.0, tax_free_price=0.0, use_discount=false, discount_price=0.0, discount_price_type=2.0, use_discount_period=false, discount_start_at=null, discount_end_at=null, count_sale=0.0, count_qna=0.0, count_like=0.0, count_review=0.0, sku=null, barcode=null, search_tags=[], event_tags=[], target_user_tags=[], delivery_tags=[], emotion_tags=[], tags=[상급 머그컵], use_cancel=false, use_minor=false, use_accumulation=false, use_like=true, use_review=true, use_display_stock=false, use_able_refund=true, use_able_cart=true, use_seo=false, seo_page_title=null, seo_meta_description=null, seo_meta_tags=[], use_limitation_person=null, limitation_person=null, use_bulk_purchase_discount=false, bulk_purchase_discount=null, use_review_point=false, review_point_hash=null, free_gift=null, use_option=false, use_product_option=false, use_additional_option=false, _id=67c95a05d01640bb9859c621, p_id=67c92fb8d01640bb9859c613, s_id=67c92fb8d01640bb9859c610, updated_at=2025-03-06T17:17:09+09:00, created_at=2025-03-06T17:17:09+09:00, category_id=null, subscription_setting_id=null, use_delivery_shipping=false, use_delivery_shipping_bundle=false, use_display_period=false, use_subscription=false, use_sale_period=false, use_subscription_times=false, option_count=0.0, category=null, subscription=null, subscription_default_period=null}, {name=중급 머그컵, desc=null, content_type=3.0, content=, content_images=[], images=[], use_stock=false, stock=0.0, stock_safe=0.0, type=0.0, object_id=null, tax_type=1.0, status_visible=true, status_frozen=false, save_by=3.0, status_review=3.0, status_display=true, display_start_at=null, display_end_at=null, status_sale=true, sale_start_at=null, sale_end_at=null, cost_price=0.0, display_price=2000.0, tax_free_price=0.0, use_discount=false, discount_price=0.0, discount_price_type=2.0, use_discount_period=false, discount_start_at=null, discount_end_at=null, count_sale=0.0, count_qna=0.0, count_like=0.0, count_review=0.0, sku=null, barcode=null, search_tags=[], event_tags=[], target_user_tags=[], delivery_tags=[], emotion_tags=[], tags=[중급 머그컵], use_cancel=false, use_minor=false, use_accumulation=false, use_like=true, use_review=true, use_display_stock=false, use_able_refund=true, use_able_cart=true, use_seo=false, seo_page_title=null, seo_meta_description=null, seo_meta_tags=[], use_limitation_person=null, limitation_person=null, use_bulk_purchase_discount=false, bulk_purchase_discount=null, use_review_point=false, review_point_hash=null, free_gift=null, use_option=false, use_product_option=false, use_additional_option=false, _id=67c959f9d01640bb9859c61f, p_id=67c92fb8d01640bb9859c613, s_id=67c92fb8d01640bb9859c610, updated_at=2025-03-06T17:16:57+09:00, created_at=2025-03-06T17:16:57+09:00, category_id=null, subscription_setting_id=null, use_delivery_shipping=false, use_delivery_shipping_bundle=false, use_display_period=false, use_subscription=false, use_sale_period=false, use_subscription_times=false, option_count=0.0, category=null, subscription=null, subscription_default_period=null}, {name=초급 머그컵, desc=null, content_type=3.0, content=, content_images=[], images=[], use_stock=false, stock=0.0, stock_safe=0.0, type=0.0, object_id=null, tax_type=1.0, status_visible=true, status_frozen=false, save_by=3.0, status_review=3.0, status_display=true, display_start_at=null, display_end_at=null, status_sale=true, sale_start_at=null, sale_end_at=null, cost_price=0.0, display_price=1500.0, tax_free_price=0.0, use_discount=false, discount_price=0.0, discount_price_type=2.0, use_discount_period=false, discount_start_at=null, discount_end_at=null, count_sale=0.0, count_qna=0.0, count_like=0.0, count_review=0.0, sku=null, barcode=null, search_tags=[], event_tags=[], target_user_tags=[], delivery_tags=[], emotion_tags=[], tags=[초급 머그컵], use_cancel=false, use_minor=false, use_accumulation=false, use_like=true, use_review=true, use_display_stock=false, use_able_refund=true, use_able_cart=true, use_seo=false, seo_page_title=null, seo_meta_description=null, seo_meta_tags=[], use_limitation_person=null, limitation_person=null, use_bulk_purchase_discount=false, bulk_purchase_discount=null, use_review_point=false, review_point_hash=null, free_gift=null, use_option=false, use_product_option=false, use_additional_option=false, _id=67c959edd01640bb9859c61d, p_id=67c92fb8d01640bb9859c613, s_id=67c92fb8d01640bb9859c610, updated_at=2025-03-06T17:16:45+09:00, created_at=2025-03-06T17:16:45+09:00, category_id=null, subscription_setting_id=null, use_delivery_shipping=false, use_delivery_shipping_bundle=false, use_display_period=false, use_subscription=false, use_sale_period=false, use_subscription_times=false, option_count=0.0, category=null, subscription=null, subscription_default_period=null}, {name=머그컵, desc=null, content_type=3.0, content=, content_images=[], images=[], use_stock=false, stock=0.0, stock_safe=0.0, type=0.0, object_id=null, tax_type=1.0, status_visible=true, status_frozen=false, save_by=3.0, status_review=3.0, status_display=true, display_start_at=null, display_end_at=null, status_sale=true, sale_start_at=null, sale_end_at=null, cost_price=0.0, display_price=1000.0, tax_free_price=0.0, use_discount=false, discount_price=0.0, discount_price_type=2.0, use_discount_period=false, discount_start_at=null, discount_end_at=null, count_sale=0.0, count_qna=0.0, count_like=0.0, count_review=0.0, sku=null, barcode=null, search_tags=[], event_tags=[], target_user_tags=[], delivery_tags=[], emotion_tags=[], tags=[머그컵], use_cancel=false, use_minor=false, use_accumulation=false, use_like=true, use_review=true, use_display_stock=false, use_able_refund=true, use_able_cart=true, use_seo=false, seo_page_title=null, seo_meta_description=null, seo_meta_tags=[], use_limitation_person=null, limitation_person=null, use_bulk_purchase_discount=false, bulk_purchase_discount=null, use_review_point=false, review_point_hash=null, free_gift=null, use_option=false, use_product_option=false, use_additional_option=false, _id=67c93752d01640bb9859c619, p_id=67c92fb8d01640bb9859c613, s_id=67c92fb8d01640bb9859c610, updated_at=2025-03-06T14:49:06+09:00, created_at=2025-03-06T14:49:06+09:00, category_id=null, subscription_setting_id=null, use_delivery_shipping=false, use_delivery_shipping_bundle=false, use_display_period=false, use_subscription=false, use_sale_period=false, use_subscription_times=false, option_count=0.0, category=null, subscription=null, subscription_default_period=null}]}
    public static void list() {
        try {
            OrderCancelListParams params = new OrderCancelListParams();
            params.orderNumber = "25061041373033253116";
    //          params.orderId = "67c9428f7b47af25bee631e7"; // 고유 주문번호로 조회

            BootpayStoreResponse res = bootpayStore.orderCancel.list(params);
            if(res.isSuccess()) {
                System.out.println("order cancel list: " + res.getData());
            } else {
                System.out.println("order cancel false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void request() {
        OrderCancelParams params = new OrderCancelParams();
        params.orderNumber = "25060971162205013115";
        params.requestCancelParameters.cancelPrice = 300.0;

        try {
            BootpayStoreResponse res = bootpayStore.orderCancel.request(params);
            if(res.isSuccess()) {
                System.out.println("order cancel request: " + res.getData());
            } else {
                System.out.println("order cancel false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void withdraw() {
        try {
            BootpayStoreResponse res = bootpayStore.orderCancel.withdraw("6847844b008fa2aeebcce8b9");
            if(res.isSuccess()) {
                System.out.println("order withdraw request: " + res.getData());
            } else {
                System.out.println("order withdraw false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void approve() {
        try {
            OrderCancelActionParams params = new OrderCancelActionParams();
            params.orderCancelRequestHistoryId = "68468927008fa2aeebcce89c";
            params.message = "관리자 승인";

            BootpayStoreResponse res = bootpayStore.orderCancel.approve(params);
            if(res.isSuccess()) {
                System.out.println("order cancel approve: " + res.getData());
            } else {
                System.out.println("order cancel approve: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reject() {
        try {
            OrderCancelActionParams params = new OrderCancelActionParams();
            params.orderCancelRequestHistoryId = "684687e6008fa2aeebcce893";
            params.message = "관리자 승인 거절";

            BootpayStoreResponse res = bootpayStore.orderCancel.reject(params);
            if(res.isSuccess()) {
                System.out.println("order cancel approve: " + res.getData());
            } else {
                System.out.println("order cancel approve: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

