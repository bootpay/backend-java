package kr.co.bootpay.store.service.users;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SUser;
import kr.co.bootpay.store.model.request.user.UserListParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 사용자 관리 서비스
 * <p>
 * 모든 사용자 조회/수정/삭제 API에서 id 파라미터는 다음 중 하나를 사용할 수 있습니다:
 * <ul>
 *   <li>user_id: 부트페이 시스템 고유 ID (MongoDB ObjectId 형식)</li>
 *   <li>ex_uid: 가맹점에서 설정한 외부 고유 ID</li>
 *   <li>login_id: 로그인 ID</li>
 * </ul>
 * 서버에서 user_id → ex_uid → login_id 순서로 검색합니다.
 * </p>
 */
public class SUserService {

    /**
     * 고객 목록 조회
     *
     * @param bootpay BootpayStoreObject 인스턴스
     * @param params 검색 파라미터 (keyword로 이름, 전화번호, 이메일, user_id 검색 가능)
     * @return BootpayStoreResponse 고객 목록 { list: [...], count: number }
     */
    static public BootpayStoreResponse list(BootpayStoreObject bootpay, UserListParams params) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");

        HttpClient client = HttpClientBuilder.create().build();

        String url = "users";
        if(params != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            if(params.page != null) nameValuePairList.add(new BasicNameValuePair("page", params.page.toString()));
            if(params.limit != null) nameValuePairList.add(new BasicNameValuePair("limit", params.limit.toString()));
            if(params.keyword != null) nameValuePairList.add(new BasicNameValuePair("keyword", params.keyword));
            if(params.memberType != null) nameValuePairList.add(new BasicNameValuePair("member_type", params.memberType.toString()));
            if(params.type != null) nameValuePairList.add(new BasicNameValuePair("type", params.type));

            HttpGet get = bootpay.httpGet(url, nameValuePairList);
            HttpResponse response = client.execute(get);
            return bootpay.responseToJsonObject(response);
        } else {
            HttpGet get = bootpay.httpGet(url);
            HttpResponse response = client.execute(get);
            return bootpay.responseToJsonObject(response);
        }
    }

    /**
     * 고객 정보 수정
     * <p>
     * user.userId에 user_id, ex_uid, login_id 중 하나를 사용할 수 있습니다.
     * </p>
     *
     * @param bootpay BootpayStoreObject 인스턴스
     * @param user 수정할 고객 정보 (userId 필드 필수)
     * @return BootpayStoreResponse 수정된 고객 정보
     */
    static public BootpayStoreResponse update(BootpayStoreObject bootpay, SUser user) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPut put = bootpay.httpPut("users/" + user.userId, new StringEntity(gson.toJson(user), "UTF-8"));
        HttpResponse response = client.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 고객 상세 조회
     * <p>
     * userId에 user_id, ex_uid, login_id 중 하나를 사용할 수 있습니다.
     * </p>
     *
     * @param bootpay BootpayStoreObject 인스턴스
     * @param userId 고객 식별자 (user_id, ex_uid, login_id 중 하나)
     * @return BootpayStoreResponse 고객 상세 정보
     */
    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String userId) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");

        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = bootpay.httpGet("users/" + userId);
        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 회원 탈퇴
     * <p>
     * userId에 user_id, ex_uid, login_id 중 하나를 사용할 수 있습니다.
     * </p>
     *
     * @param bootpay BootpayStoreObject 인스턴스
     * @param userId 고객 식별자 (user_id, ex_uid, login_id 중 하나)
     * @return BootpayStoreResponse 삭제 결과
     */
    static public BootpayStoreResponse destroy(BootpayStoreObject bootpay, String userId) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");

        HttpClient client = HttpClientBuilder.create().build();

        HttpDelete delete = bootpay.httpDelete("users/" + userId);
        HttpResponse response = client.execute(delete);
        return bootpay.responseToJsonObject(response);
    }

}
