package kr.co.bootpay.store.service.products;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import kr.co.bootpay.BootpayResponse;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.pojo.SInvoice;
import kr.co.bootpay.store.model.pojo.SProduct;
import kr.co.bootpay.store.model.pojo.SUserGroup;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.*;

public class SProductService extends BootpayResponse {

    static public HashMap<String, Object> create(BootpayStoreObject bootpay, SProduct product, List<URL> imagePaths) throws Exception {
        if (bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");

        HttpClient client = HttpClientBuilder.create().build();

        // URL 리스트를 파일 리스트로 변환
        List<File> fileList = new ArrayList<>();
        for (URL imageUrl : imagePaths) {
            File tempFile = new File(imageUrl.toURI());
            if (!tempFile.exists()) throw new Exception("파일 경로가 올바르지 않습니다: " + tempFile.getAbsolutePath());
            fileList.add(tempFile);
        }

        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        String jsonProduct = gson.toJson(product);

        // JSON 문자열을 Map으로 변환
        HashMap<String, String> params = gson.fromJson(jsonProduct, new TypeToken<HashMap<String, String>>(){}.getType());

        // 파일 업로드 요청 (여러 파일)
        HttpPost post = bootpay.httpPostMultipart("products", fileList, params);
        HttpResponse response = client.execute(post);

        // 응답 처리
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        return responseJson(gson, str, response.getStatusLine().getStatusCode());
    }



//    static public HashMap<String, Object> create(BootpayStoreObject bootpay, SProduct product, String imagePath) throws Exception {
//        if (bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
//
//        HttpClient client = HttpClientBuilder.create().build();
//
//        // 이미지 파일 로드
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        InputStream inputStream = classLoader.getResourceAsStream(imagePath);
//        if (inputStream == null) throw new Exception("이미지 파일을 찾을 수 없습니다: " + imagePath);
//
//        // 파일 변환 (InputStream -> File)
//        String extension = imagePath.substring(imagePath.lastIndexOf("."));
//        String fileName = "upload_" + System.currentTimeMillis() + extension;
//        File tempFile = File.createTempFile(fileName, null);
//        try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }
//        } catch (Exception e) {
//            throw new Exception("파일 쓰기 오류: " + e.getMessage(), e);
//        }
//
//        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
//        Gson gson = new Gson();
//        String jsonProduct = gson.toJson(product);
//
//        // JSON 문자열을 Map으로 변환
//        HashMap<String, String> params = gson.fromJson(jsonProduct, new TypeToken<HashMap<String, String>>(){}.getType());
//
//        // 파일 업로드 요청
//        HttpPost post = bootpay.httpPostMultipart("products", tempFile, params);
//        HttpResponse response = client.execute(post);
//
//        // 응답 처리
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        Type resType = new TypeToken<HashMap<String, Object>>() {}.getType();
//        HashMap<String, Object> result = gson.fromJson(str, resType);
//        if (result == null) {
//            result = new HashMap<>();
//        }
//
//        result.put("http_status", response.getStatusLine().getStatusCode());
//
//        // 임시 파일 삭제
//        if (tempFile.exists()) {
//            tempFile.delete();
//        }
//
//        return result;
//    }


    static public HashMap<String, Object> list(BootpayStoreObject bootpay, Optional<String> keyword, Optional<Integer> page, Optional<Integer> limit) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        // 파라미터 맵 초기화
        Map<String, Object> params = new HashMap<>();
        keyword.ifPresent(value -> params.put("keyword", value));
        page.ifPresent(value -> params.put("page", value));
        limit.ifPresent(value -> params.put("limit", value));

        // 파라미터를 URL 쿼리 문자열로 변환
        StringBuilder query = new StringBuilder("products?");
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        // 마지막 '&' 제거
        if (query.charAt(query.length() - 1) == '&') {
            query.deleteCharAt(query.length() - 1);
        }

        // GET 요청 객체 생성
        HttpGet get = bootpay.httpGet(query.toString());

        // HTTP 요청 전송 및 응답 수신
        HttpResponse response = client.execute(get);
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        Type resType = new TypeToken<HashMap<String, Object>>(){}.getType();
        HashMap<String, Object> result = new Gson().fromJson(str, resType);
        if(result == null) {
            result = new HashMap<>();
        }

        result.put("http_status", response.getStatusLine().getStatusCode());
        return result;
    }
}
