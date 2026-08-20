package com.example.bootpay.store;

import com.example.bootpay.Config;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.ListParams;
import kr.co.bootpay.store.model.pojo.SInvoice;
import kr.co.bootpay.store.model.pojo.SInvoiceExtra;
import kr.co.bootpay.store.model.pojo.SInvoicePriceAdjustment;
import kr.co.bootpay.store.model.pojo.SInvoicePriceAdjustmentCycle;
import kr.co.bootpay.store.model.pojo.SInvoiceProduct;
import kr.co.bootpay.store.model.pojo.SInvoiceUser;

import java.util.List;


public class Invoice {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload(Config.Commerce.getClientKey(), Config.Commerce.getSecretKey());
            bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
            getToken();
            list();
//            create();
//            _notify();
//            detail();
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

    public static void list() {
        try {
            ListParams params = new ListParams();
//            params.keyword = "테스트";

            BootpayStoreResponse res = bootpayStore.invoice.list(params);
            if(res.isSuccess()) {
                System.out.println("invoice list success: " + res.getData());
            } else {
                System.out.println("invoice list false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void create() {
        try {
            SInvoice invoice = new SInvoice();
//            invoice.orderId = "67e4ead95ec892162491d0f3";
            invoice.price = 1000.0;
            invoice.name = "테스트 상품";
            invoice.selectedUsers = List.of("68527d03b0eacea5cd974821");

            BootpayStoreResponse res = bootpayStore.invoice.create(invoice);
            if(res.isSuccess()) {
                System.out.println("invoice create success: " + res.getData());
            } else {
                System.out.println("invoice create false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 상품·구매자를 지정해 청구서를 만든다 (ruby SDK request_checkout 과 같은 파라미터)
    public static void createWithProducts() {
        try {
            SInvoice invoice = new SInvoice();
            invoice.name = "테스트 청구서";
            invoice.memo = "테스트 청구서 상세 메모";
            invoice.price = 1000.0;
            invoice.redirectUrl = "https://example.com";
            invoice.requestId = "test1";
            invoice.useAutoLogin = true;      // 청구서 링크에서 자동 로그인
            invoice.useNotification = true;   // 생성과 동시에 안내 발송

            // 구매자 — 이미 가입된 회원이면 userId 만으로 충분하다
            SInvoiceUser user = new SInvoiceUser();
            user.membershipType = SInvoiceUser.MEMBERSHIP_TYPE_GUEST;
            user.userId = "test123";
            user.name = "부트페이";
            user.phone = "01095735114";
            invoice.user = user;

            // 등록된 상품을 참조해 청구한다
            SInvoiceProduct product = new SInvoiceProduct();
            product.productId = "66fa14954eac568eab4fc2d0";
            product.productOptionId = "68ede8c675febc5627363fb2";
            product.duration = 24;
            product.quantity = 1;
            invoice.products = List.of(product);

            BootpayStoreResponse res = bootpayStore.invoice.create(invoice);
            if(res.isSuccess()) {
                System.out.println("invoice createWithProducts success: " + res.getData());
            } else {
                System.out.println("invoice createWithProducts false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 구독 상품에 프로모션(가격 조정)을 붙여 청구서를 만든다
    public static void createSubscriptionWithPromotion() {
        try {
            SInvoice invoice = new SInvoice();
            invoice.name = "과금 청구서";
            invoice.price = 1000.0;
            invoice.useAutoLogin = true;

            SInvoiceUser user = new SInvoiceUser();
            user.userId = "gosomi85";
            invoice.user = user;

            // 첫달 20% 할인 (최소 100 ~ 최대 500)
            SInvoicePriceAdjustmentCycle firstMonth = new SInvoicePriceAdjustmentCycle();
            firstMonth.duration = 1;
            firstMonth.adjustmentType = SInvoicePriceAdjustmentCycle.ADJUSTMENT_TYPE_DISCOUNT_PERCENT;
            firstMonth.name = "첫달 할인";
            firstMonth.value = 20.0;
            firstMonth.minValue = 100.0;
            firstMonth.maxValue = 500.0;

            // 도입비 500원
            SInvoicePriceAdjustmentCycle setupFee = new SInvoicePriceAdjustmentCycle();
            setupFee.duration = 1;
            setupFee.adjustmentType = SInvoicePriceAdjustmentCycle.ADJUSTMENT_TYPE_SETUP_FEE;
            setupFee.name = "도입비";
            setupFee.value = 500.0;

            SInvoicePriceAdjustment adjustment = new SInvoicePriceAdjustment();
            adjustment.priceAdjustmentId = "test1";
            adjustment.name = "첫 구매 할인 프로모션";
            adjustment.startAt = "2025-09-20 00:00:00";
            adjustment.endAt = "2025-12-30 23:59:59";
            adjustment.cycles = List.of(firstMonth, setupFee);

            SInvoiceProduct product = new SInvoiceProduct();
            product.productId = "66fa14954eac568eab4fc2d0";
            product.productOptionId = "68ede8c675febc5627363fb2";
            product.duration = 24;
            product.quantity = 1;
            product.priceAdjustments = List.of(adjustment);
            invoice.products = List.of(product);

            SInvoiceExtra extra = new SInvoiceExtra();
            extra.separatelyConfirmed = false;
            extra.createOrderImmediately = true;
            invoice.extra = extra;

            BootpayStoreResponse res = bootpayStore.invoice.create(invoice);
            if(res.isSuccess()) {
                System.out.println("invoice createSubscriptionWithPromotion success: " + res.getData());
            } else {
                System.out.println("invoice createSubscriptionWithPromotion false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 사용량 기반 과금 청구서 — usageApiUrl 로 사용량을 조회한다
    public static void createUsageBased() {
        try {
            SInvoice invoice = new SInvoice();
            invoice.name = "과금 청구서";
            invoice.memo = "과금 청구서입니다";
            invoice.price = 1000.0;
            invoice.redirectUrl = "https://example.com";
            invoice.usageApiUrl = "https://dev-api.bootapi.com/v1/billing/usage";
            invoice.useAutoLogin = true;
            invoice.requestId = "test1";

            SInvoiceUser user = new SInvoiceUser();
            user.userId = "gosomi85";
            invoice.user = user;

            SInvoiceProduct product = new SInvoiceProduct();
            product.productId = "68dcee4c5614185fea14a0b7";
            product.quantity = 1;
            invoice.products = List.of(product);

            BootpayStoreResponse res = bootpayStore.invoice.create(invoice);
            if(res.isSuccess()) {
                System.out.println("invoice createUsageBased success: " + res.getData());
            } else {
                System.out.println("invoice createUsageBased false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void _notify() {
        try {
            List<Integer> sendTypes = List.of(
                    SInvoice.SEND_TYPE_KAKAO,
                    SInvoice.SEND_TYPE_EMAIL
            );

            BootpayStoreResponse res = bootpayStore.invoice.notify("6853a79bb0eacea5cd9748da", sendTypes);
            if(res.isSuccess()) {
                System.out.println("invoice notify success: " + res.getData());
            } else {
                System.out.println("invoice notify false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void detail() {
        try {
            BootpayStoreResponse res = bootpayStore.invoice.detail("67e4e9ecd01640bb9859c64d");
            if(res.isSuccess()) {
                System.out.println("invoice detail success: " + res.getData());
            } else {
                System.out.println("invoice detail false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

