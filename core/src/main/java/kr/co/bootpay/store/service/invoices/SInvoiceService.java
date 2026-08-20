package kr.co.bootpay.store.service.invoices;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.context.RequestContext;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SInvoice;
import kr.co.bootpay.store.model.request.ListParams;
import kr.co.bootpay.store.model.request.invoice.InvoiceListParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SInvoiceService {

    static public BootpayStoreResponse create(BootpayStoreObject bootpay, SInvoice invoice) throws Exception {
        return create(bootpay, invoice, null);
    }

    /**
     * 청구서 생성 (POST invoices).
     *
     * @param invoice 청구서 정보. name 과 price 외에 user / products / deliveryPrice /
     *                useNotification / useAutoLogin / usageApiUrl / extra 를 지정할 수 있다.
     * @param idempotencyKey 미지정시 자동 생성 (Idempotency-Key 헤더로 전송)
     */
    static public BootpayStoreResponse create(BootpayStoreObject bootpay, SInvoice invoice, String idempotencyKey) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPost post = bootpay.httpPost("invoices", new StringEntity(gson.toJson(invoice), "UTF-8"),
                invoiceCreateContext(idempotencyKey));

        HttpResponse response = client.execute(post);
        return bootpay.responseToJsonObject(response);
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//
//        return responseJson(gson, str, response.getStatusLine().getStatusCode());
    }

    // 청구서 목록 조회 (GET invoices)
    // InvoiceListParams를 넘기면 cs_type / user_id / product_type / css_at / cse_at 필터도 함께 전송한다
    static public BootpayStoreResponse list(BootpayStoreObject bootpay, ListParams params) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = listRequest(bootpay, params);
        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    static HttpGet listRequest(BootpayStoreObject bootpay, ListParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        String url = "invoices";
        if (params == null) return bootpay.httpGet(url);

        // 값이 설정되지 않은 필드는 전송하지 않는다 (ruby의 compact 동작)
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        if (params.keyword != null) nameValuePairList.add(new BasicNameValuePair("keyword", params.keyword));
        if (params.page != null) nameValuePairList.add(new BasicNameValuePair("page", params.page.toString()));
        if (params.limit != null) nameValuePairList.add(new BasicNameValuePair("limit", params.limit.toString()));

        if (params instanceof InvoiceListParams) {
            InvoiceListParams invoiceParams = (InvoiceListParams) params;
            if (invoiceParams.type != null) nameValuePairList.add(new BasicNameValuePair("type", invoiceParams.type.toString()));
            if (invoiceParams.csType != null) nameValuePairList.add(new BasicNameValuePair("cs_type", invoiceParams.csType));
            if (invoiceParams.userId != null) nameValuePairList.add(new BasicNameValuePair("user_id", invoiceParams.userId));
            if (invoiceParams.productType != null) nameValuePairList.add(new BasicNameValuePair("product_type", invoiceParams.productType.toString()));
            if (invoiceParams.cssAt != null) nameValuePairList.add(new BasicNameValuePair("css_at", invoiceParams.cssAt));
            if (invoiceParams.cseAt != null) nameValuePairList.add(new BasicNameValuePair("cse_at", invoiceParams.cseAt));
        }

        return bootpay.httpGet(url, nameValuePairList);
    }

    /**
     * 청구서 목록 조회 (파라미터 확장형)
     * GET /v1/invoices
     * 응답은 { list: [...], count: N } 구조다 ({ items, total } 아님).
     * limit 미지정시 서버 기본값과 동일한 24 를 보낸다.
     */
    static public BootpayStoreResponse list(BootpayStoreObject bootpay, InvoiceListParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();

        String idempotencyKey = params != null ? params.idempotencyKey : null;

        List<NameValuePair> nameValuePairList = new ArrayList<>();
        nameValuePairList.add(new BasicNameValuePair("page", params != null && params.page != null ? params.page.toString() : "1"));
        nameValuePairList.add(new BasicNameValuePair("limit", params != null && params.limit != null ? params.limit.toString() : "24"));
        if (params != null) {
            if (params.keyword != null) nameValuePairList.add(new BasicNameValuePair("keyword", params.keyword));
            if (params.csType != null) nameValuePairList.add(new BasicNameValuePair("cs_type", params.csType));
            if (params.userId != null) nameValuePairList.add(new BasicNameValuePair("user_id", params.userId));
            if (params.productType != null) nameValuePairList.add(new BasicNameValuePair("product_type", params.productType.toString()));
            if (params.cssAt != null) nameValuePairList.add(new BasicNameValuePair("css_at", params.cssAt));
            if (params.cseAt != null) nameValuePairList.add(new BasicNameValuePair("cse_at", params.cseAt));
        }

        HttpGet get = bootpay.httpGet("invoices", nameValuePairList, invoiceContext(idempotencyKey));
        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse notify(BootpayStoreObject bootpay, String invoiceId, List<Integer> sendTypes) throws Exception {
        return notify(bootpay, invoiceId, sendTypes, null);
    }

    /**
     * 청구서 알림 재발송
     * POST /v1/invoices/{invoice_id}/notify
     * sendTypes 미전달(null)시 서버가 빈 배열로 처리한다.
     * ⚠️ 실제 고객에게 알림이 발송되므로 테스트 호출 주의.
     * @param idempotencyKey 미지정시 자동 생성
     */
    static public BootpayStoreResponse notify(BootpayStoreObject bootpay, String invoiceId, List<Integer> sendTypes, String idempotencyKey) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        SInvoice invoice = new SInvoice();
        invoice.sendTypes = sendTypes;
//        invoice.invoiceId = invoiceId;

        HttpPost post = bootpay.httpPost("invoices/" + invoiceId + "/notify" , new StringEntity(gson.toJson(invoice), "UTF-8"),
                invoiceContext(idempotencyKey));

        HttpResponse response = client.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String invoiceId) throws Exception {
        return detail(bootpay, invoiceId, null);
    }

    /**
     * 청구서 상세 조회
     * GET /v1/invoices/{invoice_id}
     * @param idempotencyKey 미지정시 자동 생성
     */
    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String invoiceId, String idempotencyKey) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = bootpay.httpGet("invoices/" + invoiceId, invoiceContext(idempotencyKey));

        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 청구서 API 요청 컨텍스트 — Idempotency-Key 는 미지정시 매 호출마다 생성된다.
     */
    private static RequestContext invoiceContext(String idempotencyKey) {
        return RequestContext.builder()
                .role("user")
                .idempotencyKey(RequestContext.idempotencyKeyOrGenerate(idempotencyKey))
                .build();
    }

    /**
     * 청구서 생성 요청 컨텍스트 — Idempotency-Key 만 싣고 role 은 지정하지 않는다.
     *
     * <p>role 을 고정하면 {@code setRole("supervisor")} 로 지정해 둔 호출자가 조용히 user 로
     * 강등된다. role 미지정 시 인스턴스 role 이 쓰이고, 그마저 없으면 "user" 가 기본값이다.</p>
     */
    private static RequestContext invoiceCreateContext(String idempotencyKey) {
        return RequestContext.builder()
                .idempotencyKey(RequestContext.idempotencyKeyOrGenerate(idempotencyKey))
                .build();
    }
}
