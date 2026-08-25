package kr.co.bootpay.store.service.order_subscriptions;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.http.HttpDeleteWithBody;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.context.RequestContext;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionListParams;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionUpdateParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorChargeParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorChargeRevokeParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorPauseParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorResumeParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorTerminateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;


public class SOrderSubscriptionService {
    static public BootpayStoreResponse list(BootpayStoreObject bootpay, OrderSubscriptionListParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        HttpGet get = listRequest(bootpay, params);
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 목록 조회 요청을 구성한다 (전송하지 않는다).
     *
     * <p>URL·쿼리 구성만 떼어내 서버 없이 검증할 수 있게 한 것이다.</p>
     */
    static HttpGet listRequest(BootpayStoreObject bootpay, OrderSubscriptionListParams params) throws Exception {
        bootpay.requireCommerceCredentials();

        String url = "order_subscriptions";
        if(params == null) return bootpay.httpGet(url);

        List<NameValuePair> nameValuePairList = new ArrayList<>();
        if(params.sAt != null) nameValuePairList.add(new BasicNameValuePair("s_at", params.sAt));
        if(params.eAt != null) nameValuePairList.add(new BasicNameValuePair("e_at", params.eAt));
        if(params.searchDateFrom != null) nameValuePairList.add(new BasicNameValuePair("search_date_from", params.searchDateFrom));
        if(params.searchDateTo != null) nameValuePairList.add(new BasicNameValuePair("search_date_to", params.searchDateTo));

        if(params.requestType != null) nameValuePairList.add(new BasicNameValuePair("request_type", params.requestType.toString()));
        if(params.status != null) nameValuePairList.add(new BasicNameValuePair("status", params.status.toString()));

        // user_group_id 또는 ex_uid 지원
        if(params.userGroupId != null) nameValuePairList.add(new BasicNameValuePair("user_group_id", params.userGroupId));
        if(params.userGroupExUid != null) nameValuePairList.add(new BasicNameValuePair("user_group_ex_uid", params.userGroupExUid));
        if(params.userGroupExternalUid != null) nameValuePairList.add(new BasicNameValuePair("user_group_external_uid", params.userGroupExternalUid));
        if(params.userGroupUid != null) nameValuePairList.add(new BasicNameValuePair("user_group_uid", params.userGroupUid));

        // user_id 또는 ex_uid 지원
        if(params.userId != null) nameValuePairList.add(new BasicNameValuePair("user_id", params.userId));
        if(params.userExUid != null) nameValuePairList.add(new BasicNameValuePair("user_ex_uid", params.userExUid));
        if(params.userExternalUid != null) nameValuePairList.add(new BasicNameValuePair("user_external_uid", params.userExternalUid));
        if(params.userUid != null) nameValuePairList.add(new BasicNameValuePair("user_uid", params.userUid));

        if(params.keyword != null) nameValuePairList.add(new BasicNameValuePair("keyword", params.keyword));
        if(params.page != null) nameValuePairList.add(new BasicNameValuePair("page", params.page.toString()));
        if(params.limit != null) nameValuePairList.add(new BasicNameValuePair("limit", params.limit.toString()));

        return bootpay.httpGet(url, nameValuePairList);
    }


    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String orderSubscriptionId) throws Exception {
        bootpay.requireCommerceCredentials();

        HttpGet get = bootpay.httpGet("order_subscriptions/" + orderSubscriptionId);

        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 구독 계약 내용 변경 (supervisor 전용)
     * PUT /v1/order_subscriptions/{order_subscription_id}
     *
     * <p>바뀐 값만 채우면 된다 (나머지는 서버가 그대로 유지한다).</p>
     *
     * <p>{@code price} 는 회차별 결제 금액의 <b>기준금액</b>이다. 바꾸면 결제예정(READY) 회차의 청구액이
     * 즉시 다시 계산되고, 이후 회차도 이 금액으로 만들어진다. 이미 결제된 회차는 그대로다. 0 이하는 받지 않는다.
     * 특정 회차만 가감하려면 조정항목({@code orderSubscriptionAdjustment.create})을 쓴다.</p>
     */
    static public BootpayStoreResponse update(BootpayStoreObject bootpay, OrderSubscriptionUpdateParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if(params == null) {
            throw new Exception("params 값이 비어있습니다");
        }
        if(params.orderSubscriptionId == null || params.orderSubscriptionId.isEmpty()) {
            throw new Exception("order_subscription_id 값이 비어있습니다");
        }

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        HttpPut put = bootpay.httpPut("order_subscriptions/" + params.orderSubscriptionId, new StringEntity(gson.toJson(params), "UTF-8"),
                supervisorContext(params.idempotencyKey));

        HttpResponse response = bootpay.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 수시결제(온디맨드) charge_key 즉시 결제 (supervisor 전용)
     * POST /v1/order_subscriptions/charge
     * ⚠️ charge_key 는 body 로만 전송한다 (URL/query 금지 — 액세스 로그 노출 방지)
     */
    static public BootpayStoreResponse supervisorCharge(BootpayStoreObject bootpay, SupervisorChargeParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if(params == null || params.chargeKey == null || params.chargeKey.isEmpty()) {
            throw new Exception("charge_key 값이 비어있습니다");
        }

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPost post = bootpay.httpPost("order_subscriptions/charge", new StringEntity(gson.toJson(params), "UTF-8"),
                supervisorContext(params.idempotencyKey));

        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 수시결제(온디맨드) charge_key 해지 (supervisor 전용)
     * DELETE /v1/order_subscriptions/charge
     * 해지 이후 해당 키로의 재결제는 불가능하다. charge_key 는 body 로만 전송한다.
     */
    static public BootpayStoreResponse supervisorChargeRevoke(BootpayStoreObject bootpay, SupervisorChargeRevokeParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if(params == null || params.chargeKey == null || params.chargeKey.isEmpty()) {
            throw new Exception("charge_key 값이 비어있습니다");
        }

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpDeleteWithBody delete = bootpay.httpDeleteWithBody("order_subscriptions/charge", new StringEntity(gson.toJson(params), "UTF-8"),
                supervisorContext(params.idempotencyKey));

        HttpResponse response = bootpay.execute(delete);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * supervisor 전용 요청 컨텍스트 — Idempotency-Key 는 미지정시 매 호출마다 생성된다.
     */
    private static RequestContext supervisorContext(String idempotencyKey) {
        return RequestContext.builder()
                .role("supervisor")
                .idempotencyKey(RequestContext.idempotencyKeyOrGenerate(idempotencyKey))
                .build();
    }

    /**
     * 구독 승인
     * @param bootpay BootpayStoreObject
     * @param orderSubscriptionId 구독 ID 또는 external_uid
     * @param reason 승인 사유 (선택)
     * @return BootpayStoreResponse
     */
    static public BootpayStoreResponse approve(BootpayStoreObject bootpay, String orderSubscriptionId, String reason) throws Exception {
        bootpay.requireCommerceCredentials();
        if(orderSubscriptionId == null || orderSubscriptionId.isEmpty()) {
            throw new Exception("order_subscription_id 값이 비어있습니다");
        }

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        java.util.Map<String, String> params = new java.util.HashMap<>();
        if(reason != null && !reason.isEmpty()) {
            params.put("reason", reason);
        }

        HttpPut put = bootpay.httpPut("order_subscriptions/" + orderSubscriptionId + "/approve", new StringEntity(gson.toJson(params), "UTF-8"),
                supervisorContext(null));

        HttpResponse response = bootpay.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 구독 거절
     * @param bootpay BootpayStoreObject
     * @param orderSubscriptionId 구독 ID 또는 external_uid
     * @param reason 거절 사유 (필수)
     * @return BootpayStoreResponse
     */
    static public BootpayStoreResponse reject(BootpayStoreObject bootpay, String orderSubscriptionId, String reason) throws Exception {
        bootpay.requireCommerceCredentials();
        if(orderSubscriptionId == null || orderSubscriptionId.isEmpty()) {
            throw new Exception("order_subscription_id 값이 비어있습니다");
        }
        if(reason == null || reason.isEmpty()) {
            throw new Exception("reason 값이 비어있습니다 (거절 사유 필수)");
        }

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        java.util.Map<String, String> params = new java.util.HashMap<>();
        params.put("reason", reason);

        HttpPut put = bootpay.httpPut("order_subscriptions/" + orderSubscriptionId + "/reject", new StringEntity(gson.toJson(params), "UTF-8"),
                supervisorContext(null));

        HttpResponse response = bootpay.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 관리자 구독 해지 (supervisor 권한 필요)
     * - 검증 최소화, 즉시 해지 처리
     * @param bootpay BootpayStoreObject
     * @param orderSubscriptionId 구독 ID 또는 external_uid
     * @param reason 해지 사유 (선택)
     * @return BootpayStoreResponse
     */
    static public BootpayStoreResponse terminate(BootpayStoreObject bootpay, String orderSubscriptionId, String reason) throws Exception {
        bootpay.requireCommerceCredentials();
        if(orderSubscriptionId == null || orderSubscriptionId.isEmpty()) {
            throw new Exception("order_subscription_id 값이 비어있습니다");
        }

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        java.util.Map<String, String> params = new java.util.HashMap<>();
        if(reason != null && !reason.isEmpty()) {
            params.put("reason", reason);
        }

        HttpPut put = bootpay.httpPut("order_subscriptions/" + orderSubscriptionId + "/terminate", new StringEntity(gson.toJson(params), "UTF-8"),
                supervisorContext(null));

        HttpResponse response = bootpay.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 관리자 구독 일시정지 (supervisor 권한 필요)
     * @param bootpay BootpayStoreObject
     * @param orderSubscriptionId 구독 ID 또는 external_uid
     * @param params 일시정지 파라미터 (pausedAt 필수, reason/expectedResumeAt 선택)
     * @return BootpayStoreResponse
     */
    static public BootpayStoreResponse supervisorPause(BootpayStoreObject bootpay, String orderSubscriptionId, SupervisorPauseParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if(orderSubscriptionId == null || orderSubscriptionId.isEmpty()) {
            throw new Exception("order_subscription_id 값이 비어있습니다");
        }

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        String json = (params != null) ? gson.toJson(params) : "{}";
        HttpPut put = bootpay.httpPut("order_subscriptions/" + orderSubscriptionId + "/pause", new StringEntity(json, "UTF-8"),
                supervisorContext(null));

        HttpResponse response = bootpay.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 관리자 구독 재개 (supervisor 권한 필요)
     * @param bootpay BootpayStoreObject
     * @param orderSubscriptionId 구독 ID 또는 external_uid
     * @param params 재개 파라미터 (reason 선택)
     * @return BootpayStoreResponse
     */
    static public BootpayStoreResponse supervisorResume(BootpayStoreObject bootpay, String orderSubscriptionId, SupervisorResumeParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if(orderSubscriptionId == null || orderSubscriptionId.isEmpty()) {
            throw new Exception("order_subscription_id 값이 비어있습니다");
        }

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        String json = (params != null) ? gson.toJson(params) : "{}";
        HttpPut put = bootpay.httpPut("order_subscriptions/" + orderSubscriptionId + "/resume", new StringEntity(json, "UTF-8"),
                supervisorContext(null));

        HttpResponse response = bootpay.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 관리자 구독 해지 (supervisor 권한 필요)
     *
     * <p>{@code terminate(...)} 와 같은 엔드포인트지만 위약금·환불액·서비스 종료일 등 정산 항목을 함께
     * 전달할 수 있다. 지정하지 않은 항목은 서버 기본 처리에 맡긴다.</p>
     *
     * @param bootpay BootpayStoreObject
     * @param orderSubscriptionId 구독 ID 또는 external_uid
     * @param params 해지 파라미터 (전부 선택)
     * @return BootpayStoreResponse
     */
    static public BootpayStoreResponse supervisorTerminate(BootpayStoreObject bootpay, String orderSubscriptionId, SupervisorTerminateParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if(orderSubscriptionId == null || orderSubscriptionId.isEmpty()) {
            throw new Exception("order_subscription_id 값이 비어있습니다");
        }

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        String json = (params != null) ? gson.toJson(params) : "{}";
        HttpPut put = bootpay.httpPut("order_subscriptions/" + orderSubscriptionId + "/terminate", new StringEntity(json, "UTF-8"),
                supervisorContext(null));

        HttpResponse response = bootpay.execute(put);
        return bootpay.responseToJsonObject(response);
    }

}
