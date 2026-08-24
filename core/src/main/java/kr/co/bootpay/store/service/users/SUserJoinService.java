package kr.co.bootpay.store.service.users;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.context.RequestContext;
import kr.co.bootpay.store.model.request.user.MallUserJoinParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SUser;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.net.URLEncoder;

public class SUserJoinService {

    // 중복 확인 key 목록 (서버 라우트 GET users/join/:id 의 :id 값)
    public static final String EMAIL_EXIST = "email-exist";
    public static final String ID_EXIST = "id-exist";
    public static final String PHONE_EXIST = "phone-exist";
    public static final String UID_EXIST = "uid-exist";
    public static final String GROUP_BUSINESS_NUMBER_EXIST = "group-business-number-exist";

    /**
     * 회원가입
     * <p>
     * 가맹점에서 관리하는 고유 식별자(ex_uid)를 설정하면, 이후 조회/수정/삭제 시 user_id 대신 사용 가능합니다.
     * 서버에서 id 조회 시 user_id, ex_uid, login_id 순서로 검색합니다.
     * </p>
     *
     * @param bootpay BootpayStoreObject 인스턴스
     * @param user 가입할 사용자 정보 (externalUid 필드에 외부 고유 ID 설정 가능)
     * @return BootpayStoreResponse 가입된 사용자 정보
     */
    static public BootpayStoreResponse join(BootpayStoreObject bootpay, SUser user) throws Exception {
        bootpay.requireCommerceCredentials();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPost post = bootpay.httpPost("users/join", new StringEntity(gson.toJson(user), "UTF-8"));

        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 중복 체크
     * <p>
     * 지원하는 체크 유형:
     * <ul>
     *   <li>id-exist: 로그인 ID 중복 체크</li>
     *   <li>email-exist: 이메일 중복 체크</li>
     *   <li>phone-exist: 전화번호 중복 체크</li>
     *   <li>uid-exist: 외부 고유 ID(ex_uid) 중복 체크</li>
     *   <li>group-business-number-exist: 사업자번호 중복 체크</li>
     * </ul>
     * </p>
     *
     * @param bootpay BootpayStoreObject 인스턴스
     * @param path 체크 유형 (id-exist, email-exist, phone-exist, uid-exist, group-business-number-exist)
     * @param pk 체크할 값
     * @return BootpayStoreResponse { exists: boolean }
     */
    static public BootpayStoreResponse checkExist(BootpayStoreObject bootpay, String path, String pk) throws Exception {
        HttpGet get = checkExistRequest(bootpay, path, pk);
        HttpResponse response = bootpay.execute(get);

        return bootpay.responseToJsonObject(response);
    }

    /**
     * 중복 확인 요청을 구성한다 (전송하지 않는다).
     *
     * <p>URL·쿼리 구성만 떼어내 서버 없이 검증할 수 있게 한 것이다.</p>
     */
    static HttpGet checkExistRequest(BootpayStoreObject bootpay, String path, String pk) throws Exception {
        bootpay.requireCommerceCredentials();
        if (path == null || path.isEmpty()) throw new Exception("path 값이 비어있습니다.");
        if (pk == null || pk.isEmpty()) throw new Exception("pk 값이 비어있습니다.");

        String encodedPk = URLEncoder.encode(pk, "UTF-8");

        // URL 구조: users/join/:path?pk=:pk
        String url = String.format("users/join/%s?pk=%s", path, encodedPk);
        return bootpay.httpGet(url);
    }

    /**
     * 회원가입 (V1 Mall API) — 일반 회원가입용
     * POST /v1/users/join
     * ⚠️ join(user) 과 같은 엔드포인트를 부른다. 중복이 아니라 용도가 다르다 —
     *    이쪽은 password/corporate_type/group 을 쓰는 일반 회원가입, 저쪽은 uid/login_email/login_pw 를 쓰는 외부 uid 연동 가입이다.
     *    서버가 파라미터 조합으로 분기하므로 둘 다 유지한다.
     * @param params 회원가입 파라미터 (corporate_type 미지정시 0, 나머지 null 값은 전송하지 않는다)
     */
    static public BootpayStoreResponse userJoin(BootpayStoreObject bootpay, MallUserJoinParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null) throw new Exception("params 값이 비어있습니다.");
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        JsonObject body = gson.toJsonTree(params).getAsJsonObject();
        if (!body.has("corporate_type")) {
            body.addProperty("corporate_type", 0);
        }

        HttpPost post = bootpay.httpPost("users/join", new StringEntity(gson.toJson(body), "UTF-8"),
                mallContext(params.idempotencyKey));

        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 회원가입 중복 확인 (V1 Mall API) — key 를 인자로 받는 일반형
     * GET /v1/users/join/{type}?pk={pk}
     * ⚠️ uidExist 등 전용형과 기능이 겹치지만 둘 다 유지한다.
     *    일반형은 서버에 새 key 가 생겨도 SDK 수정 없이 쓸 수 있다.
     * @param type email-exist, id-exist, phone-exist, uid-exist, group-business-number-exist
     * @param pk 중복 확인할 값
     * @param idempotencyKey 미지정시 자동 생성
     */
    static public BootpayStoreResponse userJoinCheck(BootpayStoreObject bootpay, String type, String pk, String idempotencyKey) throws Exception {
        bootpay.requireCommerceCredentials();

        String encodedPk = URLEncoder.encode(pk, "UTF-8");
        String url = String.format("users/join/%s?pk=%s", type, encodedPk);
        HttpGet get = bootpay.httpGet(url, mallContext(idempotencyKey));
        HttpResponse response = bootpay.execute(get);

        return bootpay.responseToJsonObject(response);
    }

    /**
     * 외부 uid(ex_uid) 중복 검사
     * GET /v1/users/join/uid-exist?pk={uid}
     * email-exist / id-exist / phone-exist / group-business-number-exist 와 같은 전용형이다.
     * @param uid 중복 확인할 외부 uid
     * @param idempotencyKey 미지정시 자동 생성
     */
    static public BootpayStoreResponse uidExist(BootpayStoreObject bootpay, String uid, String idempotencyKey) throws Exception {
        bootpay.requireCommerceCredentials();

        String encodedUid = URLEncoder.encode(uid, "UTF-8");
        String url = "users/join/uid-exist?pk=" + encodedUid;
        HttpGet get = bootpay.httpGet(url, RequestContext.builder()
                .role("user")
                .idempotencyKey(RequestContext.idempotencyKeyOrGenerate(idempotencyKey))
                .build());
        HttpResponse response = bootpay.execute(get);

        return bootpay.responseToJsonObject(response);
    }

    /**
     * V1 Mall API 요청 컨텍스트 — Idempotency-Key 는 미지정시 매 호출마다 생성된다.
     */
    private static RequestContext mallContext(String idempotencyKey) {
        return RequestContext.builder()
                .idempotencyKey(RequestContext.idempotencyKeyOrGenerate(idempotencyKey))
                .build();
    }
}
