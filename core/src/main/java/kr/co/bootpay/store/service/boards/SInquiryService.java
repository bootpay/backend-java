package kr.co.bootpay.store.service.boards;

import com.google.gson.Gson;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.inquiry.InquiryCreateParams;
import kr.co.bootpay.store.model.request.inquiry.InquiryDeleteParams;
import kr.co.bootpay.store.model.request.inquiry.InquiryDetailParams;
import kr.co.bootpay.store.model.request.inquiry.InquiryListParams;
import kr.co.bootpay.store.model.request.inquiry.InquiryUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 1:1 문의 — /v1/inquiries 계열
 *
 * <p>작성·수정은 회원 전용, 답변은 supervisor 전용이고, 조회·삭제는 두 모드 모두 가능하다.</p>
 */
public class SInquiryService {

    /**
     * 1:1 문의 목록
     * GET /v1/inquiries
     *
     * <p>회원 모드는 {@code userId}(Bootpay 회원 _id 또는 외부 회원 ID) · {@code loginId} · {@code userJwt} 중
     * 하나로 회원을 정하고 본인 문의만 본다. {@code supervisor} 면 몰 전체 문의를 보며, 이때 {@code userId} 는
     * 작성 회원 필터(선택)다.</p>
     */
    static public BootpayStoreResponse list(BootpayStoreObject bootpay, InquiryListParams params) throws Exception {
        bootpay.requireCommerceCredentials();

        List<NameValuePair> pairs = new ArrayList<>();
        if (params != null) {
            SBoardSupport.put(pairs, "user_id", params.userId);
            SBoardSupport.put(pairs, "login_id", params.loginId);
            SBoardSupport.put(pairs, "answered", params.answered);
        }
        SBoardSupport.put(pairs, "page", SBoardSupport.pageOrDefault(params == null ? null : params.page));
        SBoardSupport.put(pairs, "limit", SBoardSupport.limitOrDefault(params == null ? null : params.limit));

        HttpGet get = bootpay.httpGet("inquiries", pairs,
                SBoardSupport.context(params == null ? null : params.supervisor,
                        params == null ? null : params.userJwt,
                        params == null ? null : params.idempotencyKey));
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 1:1 문의 단건
     * GET /v1/inquiries/{inquiry_id}
     *
     * <p>회원 모드는 작성 회원 본인 문의만 본다 (다른 회원 문의는 404). {@code supervisor} 면 몰의 모든 문의.</p>
     */
    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, InquiryDetailParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null) throw new Exception("params 값이 비어있습니다.");
        SBoardSupport.requireValue(params.inquiryId, "inquiryId");

        List<NameValuePair> pairs = new ArrayList<>();
        SBoardSupport.put(pairs, "user_id", params.userId);
        SBoardSupport.put(pairs, "login_id", params.loginId);

        HttpGet get = bootpay.httpGet("inquiries/" + params.inquiryId, pairs,
                SBoardSupport.context(params.supervisor, params.userJwt, params.idempotencyKey));
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 1:1 문의 작성
     * POST /v1/inquiries — 회원 전용
     *
     * <p>{@code productId} 는 이 몰의 상품, {@code orderId} 는 작성 회원의 주문이어야 한다.
     * {@code option} 은 상품 옵션 문구로 {@code productId} 가 함께 필요하다.</p>
     */
    static public BootpayStoreResponse create(BootpayStoreObject bootpay, InquiryCreateParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null) throw new Exception("params 값이 비어있습니다.");
        SBoardSupport.requireValue(params.content, "content");

        Gson gson = SBoardSupport.gson();

        Map<String, Object> body = new LinkedHashMap<>();
        SBoardSupport.put(body, "user_id", params.userId);
        SBoardSupport.put(body, "login_id", params.loginId);
        SBoardSupport.put(body, "title", params.title);
        SBoardSupport.put(body, "content", params.content);
        SBoardSupport.put(body, "product_id", params.productId);
        SBoardSupport.put(body, "option", params.option);
        SBoardSupport.put(body, "order_id", params.orderId);

        HttpPost post = bootpay.httpPost("inquiries", new StringEntity(gson.toJson(body), "UTF-8"),
                SBoardSupport.userContext(params.userJwt, params.idempotencyKey));
        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 1:1 문의 수정
     * PUT /v1/inquiries/{inquiry_id} — 작성 회원 본인, 답변 전만
     *
     * <p>답변이 달린 문의는 409 INQUIRY_ALREADY_ANSWERED. {@code title} 에 빈 문자열을 보내면 제목을 지운다.</p>
     */
    static public BootpayStoreResponse update(BootpayStoreObject bootpay, InquiryUpdateParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null) throw new Exception("params 값이 비어있습니다.");
        SBoardSupport.requireValue(params.inquiryId, "inquiryId");

        Gson gson = SBoardSupport.gson();

        Map<String, Object> body = new LinkedHashMap<>();
        SBoardSupport.put(body, "user_id", params.userId);
        SBoardSupport.put(body, "login_id", params.loginId);
        SBoardSupport.put(body, "title", params.title);
        SBoardSupport.put(body, "content", params.content);

        HttpPut put = bootpay.httpPut("inquiries/" + params.inquiryId, new StringEntity(gson.toJson(body), "UTF-8"),
                SBoardSupport.userContext(params.userJwt, params.idempotencyKey));
        HttpResponse response = bootpay.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 1:1 문의 삭제
     * DELETE /v1/inquiries/{inquiry_id} — 작성 회원 본인 또는 supervisor
     *
     * <p>달린 답변도 함께 삭제된다.</p>
     */
    static public BootpayStoreResponse delete(BootpayStoreObject bootpay, InquiryDeleteParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null) throw new Exception("params 값이 비어있습니다.");
        SBoardSupport.requireValue(params.inquiryId, "inquiryId");

        List<NameValuePair> pairs = new ArrayList<>();
        SBoardSupport.put(pairs, "user_id", params.userId);
        SBoardSupport.put(pairs, "login_id", params.loginId);

        HttpDelete delete = bootpay.httpDelete(SBoardSupport.withQuery("inquiries/" + params.inquiryId, pairs),
                SBoardSupport.context(params.supervisor, params.userJwt, params.idempotencyKey));
        HttpResponse response = bootpay.execute(delete);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 1:1 문의 답변 등록·수정
     * PUT /v1/inquiries/{inquiry_id}/answer — supervisor 전용
     *
     * <p>답변이 없으면 만들고, 있으면 내용을 바꾼다. 응답은 답변이 반영된 문의 객체다.</p>
     */
    static public BootpayStoreResponse answer(BootpayStoreObject bootpay, String inquiryId, String content, String idempotencyKey) throws Exception {
        bootpay.requireCommerceCredentials();
        SBoardSupport.requireValue(inquiryId, "inquiryId");
        SBoardSupport.requireValue(content, "content");

        Gson gson = SBoardSupport.gson();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("content", content);

        HttpPut put = bootpay.httpPut("inquiries/" + inquiryId + "/answer", new StringEntity(gson.toJson(body), "UTF-8"),
                SBoardSupport.supervisorContext(idempotencyKey));
        HttpResponse response = bootpay.execute(put);
        return bootpay.responseToJsonObject(response);
    }
}
