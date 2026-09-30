package kr.co.bootpay.store.service.boards;

import com.google.gson.Gson;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.productReview.ProductReviewCreateParams;
import kr.co.bootpay.store.model.request.productReview.ProductReviewDeleteParams;
import kr.co.bootpay.store.model.request.productReview.ProductReviewDetailParams;
import kr.co.bootpay.store.model.request.productReview.ProductReviewListParams;
import kr.co.bootpay.store.model.request.productReview.ProductReviewUpdateParams;
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
 * 상품평 — /v1/reviews 계열
 *
 * <p>작성·수정은 회원 전용, 판매자 답글은 supervisor 전용이다.
 * 상품 상세의 공개 상품평 목록은 별도 경로(GET /v1/products/{product_id}/reviews)를 쓴다.</p>
 */
public class SProductReviewService {

    /**
     * 상품평 목록
     * GET /v1/reviews
     *
     * <p>회원 모드는 {@code userId} · {@code loginId} · {@code userJwt} 로 정한 회원이 쓴 상품평만 본다.
     * {@code supervisor} 면 몰 전체 상품평을 보며, 이때 {@code userId} 는 작성 회원 필터(선택)다.</p>
     */
    static public BootpayStoreResponse list(BootpayStoreObject bootpay, ProductReviewListParams params) throws Exception {
        bootpay.requireCommerceCredentials();

        List<NameValuePair> pairs = new ArrayList<>();
        SBoardSupport.put(pairs, "page", SBoardSupport.pageOrDefault(params == null ? null : params.page));
        SBoardSupport.put(pairs, "limit", SBoardSupport.limitOrDefault(params == null ? null : params.limit));
        if (params != null) {
            SBoardSupport.put(pairs, "product_id", params.productId);
            SBoardSupport.put(pairs, "view", params.view);
            SBoardSupport.put(pairs, "user_id", params.userId);
            SBoardSupport.put(pairs, "login_id", params.loginId);
        }

        HttpGet get = bootpay.httpGet("reviews", pairs,
                SBoardSupport.context(params == null ? null : params.supervisor,
                        params == null ? null : params.userJwt,
                        params == null ? null : params.idempotencyKey));
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 상품평 단건
     * GET /v1/reviews/{product_review_id}
     *
     * <p>공개 상품평은 누구나, 숨김 상품평은 작성 회원 본인 또는 supervisor 만 본다.</p>
     */
    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, ProductReviewDetailParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null) throw new Exception("params 값이 비어있습니다.");
        SBoardSupport.requireValue(params.productReviewId, "productReviewId");

        List<NameValuePair> pairs = new ArrayList<>();
        SBoardSupport.put(pairs, "user_id", params.userId);
        SBoardSupport.put(pairs, "login_id", params.loginId);

        HttpGet get = bootpay.httpGet("reviews/" + params.productReviewId, pairs,
                SBoardSupport.context(params.supervisor, params.userJwt, params.idempotencyKey));
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 상품평 작성
     * POST /v1/reviews — 회원 전용
     *
     * <p>{@code orderId} 는 회원의 구매확정 주문, {@code productId}(·{@code productOptionId})는 그 주문에 담긴
     * 상품이어야 한다. {@code images} 는 사진 URL 최대 5개이며 파일 업로드는 지원하지 않는다.</p>
     */
    static public BootpayStoreResponse create(BootpayStoreObject bootpay, ProductReviewCreateParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null) throw new Exception("params 값이 비어있습니다.");
        SBoardSupport.requireValue(params.orderId, "orderId");
        SBoardSupport.requireValue(params.productId, "productId");
        SBoardSupport.requireValue(params.rating, "rating");
        SBoardSupport.requireValue(params.content, "content");

        Gson gson = SBoardSupport.gson();

        Map<String, Object> body = new LinkedHashMap<>();
        SBoardSupport.put(body, "user_id", params.userId);
        SBoardSupport.put(body, "login_id", params.loginId);
        SBoardSupport.put(body, "order_id", params.orderId);
        SBoardSupport.put(body, "product_id", params.productId);
        SBoardSupport.put(body, "product_option_id", params.productOptionId);
        SBoardSupport.put(body, "rating", params.rating);
        SBoardSupport.put(body, "content", params.content);
        SBoardSupport.put(body, "images", params.images);

        HttpPost post = bootpay.httpPost("reviews", new StringEntity(gson.toJson(body), "UTF-8"),
                SBoardSupport.userContext(params.userJwt, params.idempotencyKey));
        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 상품평 수정
     * PUT /v1/reviews/{product_review_id} — 작성 회원 본인, 작성 후 7일 이내
     *
     * <p>{@code images} 는 보내면 통째로 교체한다 (빈 목록이면 모두 삭제). 7일이 지나면 REVIEW_EDIT_TIME_EXPIRED.</p>
     */
    static public BootpayStoreResponse update(BootpayStoreObject bootpay, ProductReviewUpdateParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null) throw new Exception("params 값이 비어있습니다.");
        SBoardSupport.requireValue(params.productReviewId, "productReviewId");

        Gson gson = SBoardSupport.gson();

        Map<String, Object> body = new LinkedHashMap<>();
        SBoardSupport.put(body, "user_id", params.userId);
        SBoardSupport.put(body, "login_id", params.loginId);
        SBoardSupport.put(body, "rating", params.rating);
        SBoardSupport.put(body, "content", params.content);
        SBoardSupport.put(body, "images", params.images);

        HttpPut put = bootpay.httpPut("reviews/" + params.productReviewId, new StringEntity(gson.toJson(body), "UTF-8"),
                SBoardSupport.userContext(params.userJwt, params.idempotencyKey));
        HttpResponse response = bootpay.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 상품평 삭제
     * DELETE /v1/reviews/{product_review_id} — 작성 회원 본인 또는 supervisor
     *
     * <p>지급된 적립금 회수·상품 통계 차감이 함께 일어난다. {@code reason} 은 운영자 모드의 삭제 사유다.</p>
     */
    static public BootpayStoreResponse delete(BootpayStoreObject bootpay, ProductReviewDeleteParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null) throw new Exception("params 값이 비어있습니다.");
        SBoardSupport.requireValue(params.productReviewId, "productReviewId");

        List<NameValuePair> pairs = new ArrayList<>();
        SBoardSupport.put(pairs, "user_id", params.userId);
        SBoardSupport.put(pairs, "login_id", params.loginId);
        SBoardSupport.put(pairs, "reason", params.reason);

        HttpDelete delete = bootpay.httpDelete(SBoardSupport.withQuery("reviews/" + params.productReviewId, pairs),
                SBoardSupport.context(params.supervisor, params.userJwt, params.idempotencyKey));
        HttpResponse response = bootpay.execute(delete);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 상품평 판매자 답글 등록·수정
     * PUT /v1/reviews/{product_review_id}/reply — supervisor 전용
     *
     * <p>답글이 없으면 만들고, 있으면 내용을 바꾼다 (10자 이상). 응답은 답글이 반영된 상품평 객체다.</p>
     */
    static public BootpayStoreResponse reply(BootpayStoreObject bootpay, String productReviewId, String content, String idempotencyKey) throws Exception {
        bootpay.requireCommerceCredentials();
        SBoardSupport.requireValue(productReviewId, "productReviewId");
        SBoardSupport.requireValue(content, "content");

        Gson gson = SBoardSupport.gson();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("content", content);

        HttpPut put = bootpay.httpPut("reviews/" + productReviewId + "/reply", new StringEntity(gson.toJson(body), "UTF-8"),
                SBoardSupport.supervisorContext(idempotencyKey));
        HttpResponse response = bootpay.execute(put);
        return bootpay.responseToJsonObject(response);
    }
}
