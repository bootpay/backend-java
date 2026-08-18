package kr.co.bootpay.store.service.order_subscriptions.request;

import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.orderSubscription.request.OrderSubscriptionRequestListParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.OrderSubscriptionRequestUpdateParams;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SOrderSubscriptionRequestServiceTest {
    private static final String CLIENT_KEY = "test_client_key";
    private static final String SECRET_KEY = "test_secret_key";

    private BootpayStoreObject bootpay() {
        return new BootpayStoreObject(new TokenPayload(CLIENT_KEY, SECRET_KEY), "PRODUCTION");
    }

    @Test
    public void 목록은_하이픈_경로로_요청한다() throws Exception {
        HttpGet get = SOrderSubscriptionRequestService.listRequest(bootpay(), null);

        assertEquals("https://api.bootapi.com/v1/order-subscription-requests", get.getURI().toString());
        assertEquals("user", get.getFirstHeader("BOOTPAY-ROLE").getValue());
    }

    @Test
    public void 목록은_설정된_값만_snake_case_쿼리로_전송한다() throws Exception {
        OrderSubscriptionRequestListParams params = new OrderSubscriptionRequestListParams();
        params.orderSubscriptionId = "686dc2f2b0eacea5cd974ca2";
        params.page = 2;
        params.limit = 20;
        params.sAt = "2026-08-01";
        params.eAt = "2026-08-31";
        params.status = 1;
        params.requestType = 3;
        params.userGroupId = "group_id";

        String query = SOrderSubscriptionRequestService.listRequest(bootpay(), params).getURI().getQuery();

        assertTrue(query.contains("order_subscription_id=686dc2f2b0eacea5cd974ca2"));
        assertTrue(query.contains("page=2"));
        assertTrue(query.contains("limit=20"));
        assertTrue(query.contains("s_at=2026-08-01"));
        assertTrue(query.contains("e_at=2026-08-31"));
        assertTrue(query.contains("status=1"));
        assertTrue(query.contains("request_type=3"));
        assertTrue(query.contains("user_group_id=group_id"));

        // 값이 설정되지 않은 필드는 전송하지 않는다 (ruby의 compact 동작)
        assertFalse(query.contains("project_id"));
        assertFalse(query.contains("keyword"));
        assertFalse(query.contains("user_id="));
    }

    @Test
    public void project_id가_있으면_supervisor_role로_요청한다() throws Exception {
        OrderSubscriptionRequestListParams params = new OrderSubscriptionRequestListParams();
        params.projectId = "project_id";

        HttpGet get = SOrderSubscriptionRequestService.listRequest(bootpay(), params);

        assertEquals("supervisor", get.getFirstHeader("BOOTPAY-ROLE").getValue());
        assertTrue(get.getURI().getQuery().contains("project_id=project_id"));
    }

    @Test
    public void 상세는_project_id가_없으면_user_role로_요청한다() throws Exception {
        HttpGet get = SOrderSubscriptionRequestService.detailRequest(bootpay(), "request_history_id", null);

        assertEquals("https://api.bootapi.com/v1/order-subscription-requests/request_history_id", get.getURI().toString());
        assertEquals("user", get.getFirstHeader("BOOTPAY-ROLE").getValue());
    }

    @Test
    public void 상세는_project_id가_있으면_supervisor_role로_요청한다() throws Exception {
        HttpGet get = SOrderSubscriptionRequestService.detailRequest(bootpay(), "request_history_id", "project_id");

        assertEquals("supervisor", get.getFirstHeader("BOOTPAY-ROLE").getValue());
        assertTrue(get.getURI().getQuery().contains("project_id=project_id"));
    }

    @Test
    public void 승인반려는_PUT으로_supervisor_role로_요청한다() throws Exception {
        OrderSubscriptionRequestUpdateParams params = new OrderSubscriptionRequestUpdateParams();
        params.requestHistoryId = "request_history_id";
        params.approval = OrderSubscriptionRequestUpdateParams.APPROVAL_APPROVE;
        params.reason = "승인 처리";
        params.terminationFee = 1000.0;

        HttpPut put = SOrderSubscriptionRequestService.updateRequest(bootpay(), params);

        assertEquals("PUT", put.getMethod());
        assertEquals("https://api.bootapi.com/v1/order-subscription-requests/request_history_id", put.getURI().toString());
        assertEquals("supervisor", put.getFirstHeader("BOOTPAY-ROLE").getValue());

        String body = EntityUtils.toString(put.getEntity(), "UTF-8");
        assertTrue(body.contains("\"approval\":\"approve\""));
        assertTrue(body.contains("\"reason\":\"승인 처리\""));
        assertTrue(body.contains("\"termination_fee\":1000"));

        // request_history_id는 URL path로만 사용하며 body에는 담지 않는다
        assertFalse(body.contains("request_history_id"));
    }

    @Test
    public void 승인반려는_필수값이_없으면_예외를_발생시킨다() {
        OrderSubscriptionRequestUpdateParams noId = new OrderSubscriptionRequestUpdateParams();
        noId.approval = OrderSubscriptionRequestUpdateParams.APPROVAL_REJECT;

        OrderSubscriptionRequestUpdateParams noApproval = new OrderSubscriptionRequestUpdateParams();
        noApproval.requestHistoryId = "request_history_id";

        assertThrows(Exception.class, () -> SOrderSubscriptionRequestService.updateRequest(bootpay(), null));
        assertThrows(Exception.class, () -> SOrderSubscriptionRequestService.updateRequest(bootpay(), noId));
        assertThrows(Exception.class, () -> SOrderSubscriptionRequestService.updateRequest(bootpay(), noApproval));
    }

    @Test
    public void 인증정보가_없으면_예외를_발생시킨다() {
        BootpayStoreObject bootpay = new BootpayStoreObject(new TokenPayload(), "PRODUCTION");

        assertThrows(Exception.class, () -> SOrderSubscriptionRequestService.listRequest(bootpay, null));
        assertThrows(Exception.class, () -> SOrderSubscriptionRequestService.detailRequest(bootpay, "request_history_id", null));
    }
}
