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

    // 중복 확인 key 목록 (서버 라우트 GET users/join/:id 의 :id 값)
    public static final String EMAIL_EXIST = "email-exist";
    public static final String ID_EXIST = "id-exist";
    public static final String PHONE_EXIST = "phone-exist";
    public static final String UID_EXIST = "uid-exist";
    public static final String GROUP_BUSINESS_NUMBER_EXIST = "group-business-number-exist";

    static public BootpayStoreResponse join(BootpayStoreObject bootpay, SUser user) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");
//        if(user.group == null) throw new Exception("group 값이 비었습니다.");


        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPost post = bootpay.httpPost("users/join", new StringEntity(gson.toJson(user), "UTF-8"));

        HttpResponse response = client.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    // 회원가입 중복 확인 (GET users/join/:path?pk=:pk)
    // path: email-exist, id-exist, phone-exist, uid-exist, group-business-number-exist
    static public BootpayStoreResponse checkExist(BootpayStoreObject bootpay, String path, String pk) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = checkExistRequest(bootpay, path, pk);
        HttpResponse response = client.execute(get);

        return bootpay.responseToJsonObject(response);
    }

    // 외부 uid(ex_uid) 중복 확인 (GET users/join/uid-exist)
    // email-exist / id-exist / phone-exist / group-business-number-exist 와 같은 패턴이다
    static public BootpayStoreResponse uidExist(BootpayStoreObject bootpay, String uid) throws Exception {
        return checkExist(bootpay, UID_EXIST, uid);
    }

    static HttpGet checkExistRequest(BootpayStoreObject bootpay, String path, String pk) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        if (path == null || path.isEmpty()) throw new Exception("path 값이 비어있습니다.");
        if (pk == null || pk.isEmpty()) throw new Exception("pk 값이 비어있습니다.");

        // URL 인코딩 처리
        // String encodedPk = URLEncoder.encode(pk, StandardCharsets.UTF_8);
        String encodedPk = URLEncoder.encode(pk, "UTF-8");

        // URL 구조: users/join/:path?pk=:pk
        String url = String.format("users/join/%s?pk=%s", path, encodedPk);
        return bootpay.httpGet(url);
    }
}
