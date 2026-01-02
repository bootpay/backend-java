package kr.co.bootpay.store.service.users;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SUser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.net.URLEncoder;

public class SUserJoinService {

    /**
     * 회원가입
     * <p>
     * 가맹점에서 관리하는 고유 식별자(ex_uid)를 설정하면, 이후 조회/수정/삭제 시 user_id 대신 사용 가능합니다.
     * 서버에서 id 조회 시 user_id, ex_uid, login_id 순서로 검색합니다.
     * </p>
     *
     * @param bootpay BootpayStoreObject 인스턴스
     * @param user 가입할 사용자 정보 (exUid 필드에 외부 고유 ID 설정 가능)
     * @return BootpayStoreResponse 가입된 사용자 정보
     */
    static public BootpayStoreResponse join(BootpayStoreObject bootpay, SUser user) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPost post = bootpay.httpPost("users/join", new StringEntity(gson.toJson(user), "UTF-8"));

        HttpResponse response = client.execute(post);
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
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        String encodedPk = URLEncoder.encode(pk, "UTF-8");

        HttpClient client = HttpClientBuilder.create().build();
        String url = String.format("users/join/%s?pk=%s", path, encodedPk);
        HttpGet get = bootpay.httpGet(url);
        HttpResponse response = client.execute(get);

        return bootpay.responseToJsonObject(response);
    }
}
