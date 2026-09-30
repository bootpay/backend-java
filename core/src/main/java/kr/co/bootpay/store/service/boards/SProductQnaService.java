package kr.co.bootpay.store.service.boards;

import com.google.gson.Gson;
import kr.co.bootpay.http.HttpDeleteWithBody;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.productQna.ProductQnaCreateParams;
import kr.co.bootpay.store.model.request.productQna.ProductQnaDeleteParams;
import kr.co.bootpay.store.model.request.productQna.ProductQnaDetailParams;
import kr.co.bootpay.store.model.request.productQna.ProductQnaListParams;
import kr.co.bootpay.store.model.request.productQna.ProductQnaUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 상품문의 — /v1/product-qnas 계열
 *
 * <p>작성은 회원·비회원 모두 가능하고(몰이 비회원 작성을 허용할 때), 답변은 supervisor 전용이다.</p>
 */
public class SProductQnaService {

    /**
     * 상품문의 목록
     * GET /v1/product-qnas
     *
     * <p>고객 모드는 {@code productId} 가 필수다. 다른 사람의 비밀글은 {@code is_viewable: false} 로 본문 없이 오고,
     * 회원({@code userId} · {@code loginId} · {@code userJwt})을 보내면 그 회원이 쓴 비밀글 본문이 보인다.
     * {@code supervisor} + {@code view: "all"} 이면 몰 전체(숨김 포함) — 이때 {@code productId} 는 선택 필터다.</p>
     */
    static public BootpayStoreResponse list(BootpayStoreObject bootpay, ProductQnaListParams params) throws Exception {
        bootpay.requireCommerceCredentials();

        List<NameValuePair> pairs = new ArrayList<>();
        if (params != null) {
            SBoardSupport.put(pairs, "product_id", params.productId);
            SBoardSupport.put(pairs, "view", params.view);
        }
        SBoardSupport.put(pairs, "page", SBoardSupport.pageOrDefault(params == null ? null : params.page));
        SBoardSupport.put(pairs, "limit", SBoardSupport.limitOrDefault(params == null ? null : params.limit));
        if (params != null) {
            SBoardSupport.put(pairs, "user_id", params.userId);
            SBoardSupport.put(pairs, "login_id", params.loginId);
        }

        HttpGet get = bootpay.httpGet("product-qnas", pairs,
                SBoardSupport.context(params == null ? null : params.supervisor,
                        params == null ? null : params.userJwt,
                        params == null ? null : params.idempotencyKey));
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 상품문의 단건
     * GET /v1/product-qnas/{product_qna_id}
     *
     * <p>다른 사람의 비밀글은 403 PRODUCT_QNA_SECRET_FORBIDDEN. {@code supervisor} 면 비밀글·숨김 글도 본다.</p>
     */
    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, ProductQnaDetailParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null) throw new Exception("params 값이 비어있습니다.");
        SBoardSupport.requireValue(params.productQnaId, "productQnaId");

        List<NameValuePair> pairs = new ArrayList<>();
        SBoardSupport.put(pairs, "user_id", params.userId);
        SBoardSupport.put(pairs, "login_id", params.loginId);

        HttpGet get = bootpay.httpGet("product-qnas/" + params.productQnaId, pairs,
                SBoardSupport.context(params.supervisor, params.userJwt, params.idempotencyKey));
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 상품문의 작성
     * POST /v1/product-qnas
     *
     * <p>회원({@code userId} · {@code loginId} · {@code userJwt}) 또는 비회원({@code guestName} +
     * {@code guestPassword}, 몰이 비회원 작성을 허용할 때). 회원 문의면 {@code guest*} 는 무시된다.</p>
     */
    static public BootpayStoreResponse create(BootpayStoreObject bootpay, ProductQnaCreateParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null) throw new Exception("params 값이 비어있습니다.");
        SBoardSupport.requireValue(params.productId, "productId");
        SBoardSupport.requireValue(params.content, "content");

        Gson gson = SBoardSupport.gson();

        Map<String, Object> body = new LinkedHashMap<>();
        SBoardSupport.put(body, "product_id", params.productId);
        SBoardSupport.put(body, "title", params.title);
        SBoardSupport.put(body, "content", params.content);
        SBoardSupport.put(body, "product_option_id", params.productOptionId);
        SBoardSupport.put(body, "option_text", params.optionText);
        SBoardSupport.put(body, "is_secret", params.isSecret);
        SBoardSupport.put(body, "guest_name", params.guestName);
        SBoardSupport.put(body, "guest_password", params.guestPassword);
        SBoardSupport.put(body, "user_id", params.userId);
        SBoardSupport.put(body, "login_id", params.loginId);

        HttpPost post = bootpay.httpPost("product-qnas", new StringEntity(gson.toJson(body), "UTF-8"),
                SBoardSupport.userContext(params.userJwt, params.idempotencyKey));
        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 상품문의 수정
     * PUT /v1/product-qnas/{product_qna_id} — 작성 회원 본인 또는 비회원 비밀번호, 답변 전만
     *
     * <p>답변이 달린 문의는 409 PRODUCT_QNA_ALREADY_ANSWERED, 비회원 비밀번호가 틀리면
     * 403 PRODUCT_QNA_GUEST_PASSWORD_INVALID.</p>
     */
    static public BootpayStoreResponse update(BootpayStoreObject bootpay, ProductQnaUpdateParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null) throw new Exception("params 값이 비어있습니다.");
        SBoardSupport.requireValue(params.productQnaId, "productQnaId");

        Gson gson = SBoardSupport.gson();

        Map<String, Object> body = new LinkedHashMap<>();
        SBoardSupport.put(body, "title", params.title);
        SBoardSupport.put(body, "content", params.content);
        SBoardSupport.put(body, "is_secret", params.isSecret);
        SBoardSupport.put(body, "guest_password", params.guestPassword);
        SBoardSupport.put(body, "user_id", params.userId);
        SBoardSupport.put(body, "login_id", params.loginId);

        HttpPut put = bootpay.httpPut("product-qnas/" + params.productQnaId, new StringEntity(gson.toJson(body), "UTF-8"),
                SBoardSupport.userContext(params.userJwt, params.idempotencyKey));
        HttpResponse response = bootpay.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 상품문의 삭제
     * DELETE /v1/product-qnas/{product_qna_id} — 작성 회원 본인 · 비회원 비밀번호 또는 supervisor
     *
     * <p>{@code guestPassword} 는 URL 쿼리가 아닌 본문으로 보낸다 (서버는 둘 다 받는다).</p>
     */
    static public BootpayStoreResponse delete(BootpayStoreObject bootpay, ProductQnaDeleteParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null) throw new Exception("params 값이 비어있습니다.");
        SBoardSupport.requireValue(params.productQnaId, "productQnaId");

        Gson gson = SBoardSupport.gson();

        Map<String, Object> body = new LinkedHashMap<>();
        SBoardSupport.put(body, "guest_password", params.guestPassword);
        SBoardSupport.put(body, "user_id", params.userId);
        SBoardSupport.put(body, "login_id", params.loginId);

        HttpDeleteWithBody delete = bootpay.httpDeleteWithBody("product-qnas/" + params.productQnaId,
                new StringEntity(gson.toJson(body), "UTF-8"),
                SBoardSupport.context(params.supervisor, params.userJwt, params.idempotencyKey));
        HttpResponse response = bootpay.execute(delete);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 상품문의 답변 등록·수정
     * PUT /v1/product-qnas/{product_qna_id}/answer — supervisor 전용
     *
     * <p>답변이 없으면 만들고, 있으면 내용을 바꾼다. 응답은 답변이 반영된 상품문의 객체다.</p>
     */
    static public BootpayStoreResponse answer(BootpayStoreObject bootpay, String productQnaId, String content, String idempotencyKey) throws Exception {
        bootpay.requireCommerceCredentials();
        SBoardSupport.requireValue(productQnaId, "productQnaId");
        SBoardSupport.requireValue(content, "content");

        Gson gson = SBoardSupport.gson();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("content", content);

        HttpPut put = bootpay.httpPut("product-qnas/" + productQnaId + "/answer", new StringEntity(gson.toJson(body), "UTF-8"),
                SBoardSupport.supervisorContext(idempotencyKey));
        HttpResponse response = bootpay.execute(put);
        return bootpay.responseToJsonObject(response);
    }
}
