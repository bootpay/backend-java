package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.cart.OrderPreviewParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.cart.SCartService;

public class Cart {
    private final BootpayStore bootpay;

    public Cart(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /**
     * 주문 미리보기 (배송비/할인 권위적 계산)
     * POST /v1/cart/order-preview
     *
     * member_mode='guest' (기본): cart_items 필수
     * member_mode='member': 서버 장바구니 사용 (user 토큰 필요)
     */
    public BootpayStoreResponse orderPreview(OrderPreviewParams params) throws Exception {
        return SCartService.orderPreview(bootpay, params);
    }

    public BootpayStoreResponse orderPreview() throws Exception {
        return SCartService.orderPreview(bootpay, null);
    }
}
