package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.Cart;
import kr.co.bootpay.store.model.request.cart.OrderPreviewParams;

/**
 * 장바구니 모듈.
 *
 * @since 3.3.0
 */
public class CartModule {

    private final Cart delegate;

    public CartModule(BootpayStore bootpay) {
        this.delegate = new Cart(bootpay);
    }

    /**
     * 주문 미리보기.
     *
     * @param params 미리보기 조건
     * @return 미리보기 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse orderPreview(OrderPreviewParams params) throws Exception {
        return CommerceResponses.of(delegate.orderPreview(params));
    }

    /**
     * 주문 미리보기 (조건 없이).
     *
     * @return 미리보기 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse orderPreview() throws Exception {
        return CommerceResponses.of(delegate.orderPreview());
    }
}
