package kr.co.bootpay.store.model.request.orderSubscriptionBill;

import kr.co.bootpay.store.model.request.ListParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 구독 청구서 목록 조회 파라미터
 * <p>
 * orderSubscriptionId 필드는 다음 중 하나로 대체 가능합니다:
 * <ul>
 *   <li>orderSubscriptionId: 부트페이 구독 ID</li>
 *   <li>exUid, externalUid, uid: 가맹점에서 설정한 구독 외부 고유 ID</li>
 * </ul>
 * </p>
 */
public class OrderSubscriptionBillListParams extends ListParams {
    // 기본 ID 필드
    public String orderSubscriptionId;
    public List<Integer> status;

    // ex_uid 지원 필드 (order_subscription)
    public String exUid;
    public String externalUid;
    public String uid;

    public OrderSubscriptionBillListParams() {
        this.status = new ArrayList<>();
    }
}
