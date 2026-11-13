package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.Order;
import kr.co.bootpay.store.model.request.order.OrderListParams;
import kr.co.bootpay.store.model.request.order.OrderPurchasesUpdateParams;

/**
 * 주문 모듈.
 *
 * @since 3.3.0
 */
public class OrderModule {

    private final Order delegate;

    public OrderModule(BootpayStore bootpay) {
        this.delegate = new Order(bootpay);
    }

    /**
     * 주문 목록 조회.
     *
     * @param params 조회 조건
     * @return 주문 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list(OrderListParams params) throws Exception {
        return CommerceResponses.of(delegate.list(params));
    }

    /**
     * 주문 상세 조회.
     *
     * @param orderId 주문 id
     * @return 주문 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String orderId) throws Exception {
        return CommerceResponses.of(delegate.detail(orderId));
    }

    /**
     * 월별 주문 집계 조회.
     *
     * @param userGroupId 사용자 그룹 id
     * @param searchDate  조회 년월 (YYYY-MM)
     * @return 집계 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse month(String userGroupId, String searchDate) throws Exception {
        return CommerceResponses.of(delegate.month(userGroupId, searchDate));
    }

    /**
     * 발송 정보 조회 — 발주(배송) 단위 운송장·택배사·배송추적 상태·이력.
     * GET /v1/orders/{orderNumber}/purchases. BOOTPAY-ROLE 은 user 로 고정한다.
     *
     * @param orderNumber 주문 번호
     * @return 발송 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse purchases(String orderNumber) throws Exception {
        return CommerceResponses.of(delegate.purchases(orderNumber));
    }

    /**
     * 발송 정보 조회 — 발주(배송) 단위 운송장·택배사·배송추적 상태·이력.
     * GET /v1/orders/{orderNumber}/purchases. BOOTPAY-ROLE 은 user 로 고정한다.
     *
     * @param orderNumber    주문 번호
     * @param idempotencyKey 미지정시 자동 생성
     * @return 발송 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse purchases(String orderNumber, String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.purchases(orderNumber, idempotencyKey));
    }

    /**
     * 발송처리 — 발주 단위 상태·운송장 갱신.
     * PUT /v1/orders/{orderNumber}/purchases. BOOTPAY-ROLE 은 user 로 고정한다.
     * 경로 주문에 딸리지 않은 orderPurchaseNumber 가 섞이면 요청 전체가 ORDER_PURCHASE_NOT_FOUND 로 거절된다.
     * 한 번에 최대 50건.
     *
     * @param params 주문 번호와 갱신할 발주 목록
     * @return 처리 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse updatePurchases(OrderPurchasesUpdateParams params) throws Exception {
        return CommerceResponses.of(delegate.updatePurchases(params));
    }
}
