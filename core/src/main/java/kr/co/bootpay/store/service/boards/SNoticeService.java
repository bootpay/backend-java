package kr.co.bootpay.store.service.boards;

import com.google.gson.Gson;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.notice.NoticeCreateParams;
import kr.co.bootpay.store.model.request.notice.NoticeDetailParams;
import kr.co.bootpay.store.model.request.notice.NoticeListParams;
import kr.co.bootpay.store.model.request.notice.NoticeUpdateParams;
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
 * 공지사항 — /v1/notices 계열
 *
 * <p>조회는 고객·운영자 모드 둘 다, 등록·수정·삭제는 supervisor 전용이다.</p>
 */
public class SNoticeService {

    /**
     * 공지사항 목록
     * GET /v1/notices
     *
     * <p>{@code supervisor} 면 운영자 모드로 나가 {@code view: "all"} 로 비공개 공지까지 조회할 수 있다.
     * 고객 모드는 몰 공지사항 사용여부가 꺼져 있으면 BOARD_FEATURE_DISABLED 로 거절된다.</p>
     */
    static public BootpayStoreResponse list(BootpayStoreObject bootpay, NoticeListParams params) throws Exception {
        bootpay.requireCommerceCredentials();

        List<NameValuePair> pairs = new ArrayList<>();
        SBoardSupport.put(pairs, "page", SBoardSupport.pageOrDefault(params == null ? null : params.page));
        SBoardSupport.put(pairs, "limit", SBoardSupport.limitOrDefault(params == null ? null : params.limit));
        if (params != null) {
            SBoardSupport.put(pairs, "keyword", params.keyword);
            SBoardSupport.put(pairs, "view", params.view);
        }

        HttpGet get = bootpay.httpGet("notices", pairs,
                SBoardSupport.context(params == null ? null : params.supervisor, null, params == null ? null : params.idempotencyKey));
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 공지사항 단건
     * GET /v1/notices/{notice_id}
     *
     * <p>비공개 공지는 {@code supervisor} 일 때만 보인다 (아니면 404 POST_NOT_FOUND).</p>
     */
    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, NoticeDetailParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null) throw new Exception("params 값이 비어있습니다.");
        SBoardSupport.requireValue(params.noticeId, "noticeId");

        HttpGet get = bootpay.httpGet("notices/" + params.noticeId,
                SBoardSupport.context(params.supervisor, null, params.idempotencyKey));
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 공지사항 등록
     * POST /v1/notices — supervisor 전용
     */
    static public BootpayStoreResponse create(BootpayStoreObject bootpay, NoticeCreateParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null) throw new Exception("params 값이 비어있습니다.");
        SBoardSupport.requireValue(params.title, "title");
        SBoardSupport.requireValue(params.content, "content");

        Gson gson = SBoardSupport.gson();

        Map<String, Object> body = new LinkedHashMap<>();
        SBoardSupport.put(body, "title", params.title);
        SBoardSupport.put(body, "content", params.content);
        SBoardSupport.put(body, "images", params.images);
        SBoardSupport.put(body, "is_notice", params.isNotice);
        SBoardSupport.put(body, "is_display", params.isDisplay);

        HttpPost post = bootpay.httpPost("notices", new StringEntity(gson.toJson(body), "UTF-8"),
                SBoardSupport.supervisorContext(params.idempotencyKey));
        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 공지사항 수정
     * PUT /v1/notices/{notice_id} — supervisor 전용
     *
     * <p>보낸 필드만 바뀐다. {@code images} 는 보내면 목록 전체를 교체한다 (빈 목록이면 모두 삭제).</p>
     */
    static public BootpayStoreResponse update(BootpayStoreObject bootpay, NoticeUpdateParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null) throw new Exception("params 값이 비어있습니다.");
        SBoardSupport.requireValue(params.noticeId, "noticeId");

        Gson gson = SBoardSupport.gson();

        Map<String, Object> body = new LinkedHashMap<>();
        SBoardSupport.put(body, "title", params.title);
        SBoardSupport.put(body, "content", params.content);
        SBoardSupport.put(body, "images", params.images);
        SBoardSupport.put(body, "is_notice", params.isNotice);
        SBoardSupport.put(body, "is_display", params.isDisplay);

        HttpPut put = bootpay.httpPut("notices/" + params.noticeId, new StringEntity(gson.toJson(body), "UTF-8"),
                SBoardSupport.supervisorContext(params.idempotencyKey));
        HttpResponse response = bootpay.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 공지사항 삭제
     * DELETE /v1/notices/{notice_id} — supervisor 전용
     */
    static public BootpayStoreResponse delete(BootpayStoreObject bootpay, String noticeId, String idempotencyKey) throws Exception {
        bootpay.requireCommerceCredentials();
        SBoardSupport.requireValue(noticeId, "noticeId");

        HttpDelete delete = bootpay.httpDelete("notices/" + noticeId, SBoardSupport.supervisorContext(idempotencyKey));
        HttpResponse response = bootpay.execute(delete);
        return bootpay.responseToJsonObject(response);
    }
}
