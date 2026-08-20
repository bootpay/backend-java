package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.orderSubscriptionBill.OrderSubscriptionBillListParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SOrderSubscriptionBill;
import kr.co.bootpay.store.model.request.ListParams;
import kr.co.bootpay.store.service.order_subscription_bill.SOrderSubscriptionBillService;

public class OrderSubscriptionBill {
    private final BootpayStore bootpay;

    public OrderSubscriptionBill(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /**
     * 구독 청구서 목록 조회
     * <p>
     * params에서 사용 가능한 ID 필드:
     * <ul>
     *   <li>orderSubscriptionId: 부트페이 구독 ID</li>
     *   <li>exUid, externalUid, uid: 가맹점 구독 고유 ID (ex_uid)</li>
     * </ul>
     * </p>
     * @param params 조회 조건 (OrderSubscriptionBillListParams)
     * @return BootpayStoreResponse
     */
    public BootpayStoreResponse list(OrderSubscriptionBillListParams params)  throws Exception {
        return SOrderSubscriptionBillService.list(
                bootpay,
                params
        );
    }

    /**
     * 구독 청구서 상세 조회
     * @param orderSubscriptionBillId 청구서 ID
     * @return BootpayStoreResponse
     */
    public BootpayStoreResponse detail(String orderSubscriptionBillId) throws Exception {
        return SOrderSubscriptionBillService.detail(bootpay, orderSubscriptionBillId);
    }

    /**
     * 구독 청구서 수정
     * @param orderSubscriptionBill 수정할 청구서 정보
     * @return BootpayStoreResponse
     */
    public BootpayStoreResponse update(SOrderSubscriptionBill orderSubscriptionBill) throws Exception {
        return SOrderSubscriptionBillService.update(bootpay, orderSubscriptionBill);
    }
}
