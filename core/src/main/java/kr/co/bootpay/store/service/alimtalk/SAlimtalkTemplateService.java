package kr.co.bootpay.store.service.alimtalk;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkTemplateCreateParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkTemplateExportParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkTemplateListParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkTemplateParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkTemplateUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 가맹점 자체 알림톡 템플릿 CRUD·등록·검수 — /v1/alimtalk/templates 계열
 *
 * <p>흐름: (초안 생성 → 확인 → 대행사 등록) → 검수 요청 → 승인(APR) → 발송 가능.
 * {@code create(register = false)} 로 초안만 만들고, 내용을 확인한 뒤 {@link #register} 로 올리는 것을 권장한다.</p>
 *
 * <p>⚠️ {@code register} 를 명시적으로 false 로 주지 않으면 <b>생성 즉시 대행사·카카오에 실제 등록</b>된다.</p>
 * <p>⚠️ 본문 변수는 {@code #{변수명}} 형식이고 템플릿 전체에서 최대 40개다.</p>
 */
public class SAlimtalkTemplateService {

    /**
     * 자체 템플릿 목록 조회
     * GET /v1/alimtalk/templates
     *
     * <p>⚠️ 페이지네이션이 없다 — 필터에 걸린 템플릿을 한 번에 모두 돌려준다.</p>
     */
    static public BootpayStoreResponse list(BootpayStoreObject bootpay, AlimtalkTemplateListParams params) throws Exception {
        bootpay.requireCommerceCredentials();

        List<NameValuePair> pairs = new ArrayList<>();
        if (params != null) {
            SAlimtalkSupport.put(pairs, "ins", params.ins);
            SAlimtalkSupport.put(pairs, "sort", params.sort);
            SAlimtalkSupport.put(pairs, "keyword", params.keyword);
        }

        HttpGet get = bootpay.httpGet("alimtalk/templates", pairs, SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 자체 템플릿 생성
     * POST /v1/alimtalk/templates
     *
     * <p>⚠️ {@code register} 를 false 로 주지 않으면 대행사·카카오에 <b>실제 등록</b>된다 (되돌리려면 삭제해야 한다).</p>
     *
     * <ul>
     *   <li>{@code emphasizeType} TEXT 는 {@code emphasizeTitle}·{@code emphasizeSubtitle} 둘 다 필수 (각 50자·40자)</li>
     *   <li>IMAGE 는 이미지 필수 — {@link #image} 로 올린 URL 을 {@code storageImageUrl} 로 넘긴다</li>
     *   <li>ITEM_LIST 는 {@code templateItem.list}(2~10개) 필수 + {@code templateHeader}·{@code itemHighlight}·이미지 중 하나 이상</li>
     *   <li>{@code msgType} AD·MI 는 채널추가(AC) 버튼이 필수다</li>
     * </ul>
     */
    static public BootpayStoreResponse create(BootpayStoreObject bootpay, AlimtalkTemplateCreateParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null) throw new Exception("params 값이 비어있습니다.");
        if (params.kspId == null || params.kspId.isEmpty()) throw new Exception("kspId 값이 비어있습니다.");

        Map<String, Object> body = new LinkedHashMap<>();
        SAlimtalkSupport.put(body, "ksp_id", params.kspId);
        SAlimtalkSupport.put(body, "register", params.register);
        payload(body, params);

        HttpPost post = bootpay.httpPost("alimtalk/templates",
                new StringEntity(toJson(body, params.attrs), "UTF-8"), SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 자체 템플릿 상세 조회
     * GET /v1/alimtalk/templates/{template_id}
     *
     * <p>{@code templateId} 는 문서 id 이고, ObjectId 형식이 아니면 <b>템플릿 코드</b>로 해석한다.</p>
     * <p>⚠️ {@code sync} 는 서버 기본값이 <b>true</b> 라 조회만 해도 벤더 상태 동기화가 일어난다.
     * 초안(등록 전)을 조회할 때는 {@code sync = false} 를 권장한다.</p>
     */
    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String templateId, Boolean sync) throws Exception {
        bootpay.requireCommerceCredentials();
        if (templateId == null || templateId.isEmpty()) throw new Exception("templateId 값이 비어있습니다.");

        List<NameValuePair> pairs = new ArrayList<>();
        SAlimtalkSupport.put(pairs, "sync", sync);

        HttpGet get = bootpay.httpGet("alimtalk/templates/" + templateId, pairs, SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 자체 템플릿 수정
     * PUT /v1/alimtalk/templates/{template_id}
     *
     * <p>⚠️ <b>부분 수정이 아니다.</b> 보내지 않은 필드는 null 로 덮어써지므로 항상 전체 필드를 보낸다.</p>
     * <p>⚠️ 등록된 템플릿을 수정하면 벤더에도 수정 요청이 나간다. 수정 가능 상태는
     * 초안 / REG(등록) / REJ(승인반려) / KRR(등록거절) 뿐이다 — APR·REQ 는 거부된다.</p>
     * <p>{@code storageImageUrl} 을 빈 값으로 보내면 <b>이미지 삭제</b>로 처리되어 벤더에도 전달된다.</p>
     */
    static public BootpayStoreResponse update(BootpayStoreObject bootpay, String templateId, AlimtalkTemplateUpdateParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (templateId == null || templateId.isEmpty()) throw new Exception("templateId 값이 비어있습니다.");
        if (params == null) throw new Exception("params 값이 비어있습니다.");

        Map<String, Object> body = new LinkedHashMap<>();
        payload(body, params);

        HttpPut put = bootpay.httpPut("alimtalk/templates/" + templateId,
                new StringEntity(toJson(body, params.attrs), "UTF-8"), SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 자체 템플릿 삭제
     * DELETE /v1/alimtalk/templates/{template_id}
     *
     * <p>초안(등록 전)은 대행사 거부와 무관하게 로컬에서 삭제된다.</p>
     * <p>⚠️ 등록분은 <b>대행사 삭제가 성공해야</b> 삭제된다 — 승인(APR) 템플릿은 카카오가 거부하므로
     * 500(3013)이 오고 템플릿은 남는다. 같은 코드가 대행사에 선점된 채 로컬만 사라지는 것을 막기 위함이다.</p>
     */
    static public BootpayStoreResponse delete(BootpayStoreObject bootpay, String templateId) throws Exception {
        bootpay.requireCommerceCredentials();
        if (templateId == null || templateId.isEmpty()) throw new Exception("templateId 값이 비어있습니다.");

        HttpDelete delete = bootpay.httpDelete("alimtalk/templates/" + templateId, SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(delete);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 초안을 대행사에 등록
     * POST /v1/alimtalk/templates/{template_id}/register
     *
     * <p>⚠️ 대행사·카카오에 실제 등록된다. 등록 전(초안) 상태에서만 호출할 수 있다.</p>
     */
    static public BootpayStoreResponse register(BootpayStoreObject bootpay, String templateId) throws Exception {
        bootpay.requireCommerceCredentials();
        if (templateId == null || templateId.isEmpty()) throw new Exception("templateId 값이 비어있습니다.");

        HttpPost post = bootpay.httpPost("alimtalk/templates/" + templateId + "/register",
                new StringEntity("{}", "UTF-8"), SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 검수 요청
     * POST /v1/alimtalk/templates/{template_id}/inspect
     *
     * <p>⚠️ <b>카카오에 검수를 요청하며 취소할 수 없다.</b></p>
     * <p>대행사 등록이 끝난 대기(R) + REG(등록) 상태에서만 호출할 수 있다 — 초안은 먼저 {@link #register} 를 부른다.
     * 반려(REJ/KRR)된 건은 재요청이 아니라 <b>수정 후 재요청</b>이다. 반려 사유는 응답의 {@code comments} 에 담긴다.</p>
     */
    static public BootpayStoreResponse inspect(BootpayStoreObject bootpay, String templateId) throws Exception {
        bootpay.requireCommerceCredentials();
        if (templateId == null || templateId.isEmpty()) throw new Exception("templateId 값이 비어있습니다.");

        HttpPost post = bootpay.httpPost("alimtalk/templates/" + templateId + "/inspect",
                new StringEntity("{}", "UTF-8"), SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 템플릿 목록 내보내기
     * GET /v1/alimtalk/templates/export
     *
     * <p>⚠️ SDK 기본 {@code format} 을 <b>json 으로 둔다</b> — 서버 기본은 csv 지만, csv 본문은 JSON 이 아니라서
     * 공용 응답 파싱을 통과하지 못한다. {@code csv} 를 주면 파싱 없이 원문 문자열
     * ({@code { body, content_type }})을 담아 돌려준다.</p>
     * <p>1회 5,000건을 넘으면 3031 로 거부되므로 채널·상태 필터로 좁힌다.</p>
     */
    static public BootpayStoreResponse export(BootpayStoreObject bootpay, AlimtalkTemplateExportParams params) throws Exception {
        bootpay.requireCommerceCredentials();

        String format = (params == null || params.format == null || params.format.isEmpty()) ? "json" : params.format;

        List<NameValuePair> pairs = new ArrayList<>();
        SAlimtalkSupport.put(pairs, "format", format);
        if (params != null) {
            SAlimtalkSupport.put(pairs, "scope", params.scope);
            SAlimtalkSupport.put(pairs, "ksp_id", params.kspId);
            SAlimtalkSupport.put(pairs, "status", params.status);
            SAlimtalkSupport.put(pairs, "include_content", params.includeContent);
        }

        if ("csv".equalsIgnoreCase(format)) {
            HttpGet raw = bootpay.httpGetRaw("alimtalk/templates/export", pairs, SAlimtalkSupport.context());
            HttpResponse rawResponse = bootpay.execute(raw);
            return bootpay.responseToRawObject(rawResponse);
        }

        HttpGet get = bootpay.httpGet("alimtalk/templates/export", pairs, SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 이미지형 템플릿의 원본 이미지 업로드
     * POST /v1/alimtalk/templates/image
     *
     * <p>돌려받은 {@code image_url} 을 템플릿 생성/수정의 {@code storageImageUrl} 로 넘긴다.</p>
     * <p>규격을 업로드 <b>전에</b> 서버가 검사한다 — jpg/png · 500KB 이하 · 가로 500px 이상 · 2:1.</p>
     * <p>{@code replaceUrl} 을 주면 업로드 성공 후에 기존 파일을 지운다.</p>
     */
    static public BootpayStoreResponse image(BootpayStoreObject bootpay, File image, String replaceUrl) throws Exception {
        return upload(bootpay, "alimtalk/templates/image", image, replaceUrl);
    }

    /**
     * 아이템리스트형의 하이라이트 썸네일 업로드
     * POST /v1/alimtalk/templates/highlight_image
     *
     * <p>⚠️ 본문 이미지와 <b>규격이 다르다</b> — jpg/png · 500KB 이하 · 가로 <b>108px</b> 이상 · <b>1:1</b>.
     * 본문 이미지 엔드포인트로 올리면 거부된다.</p>
     * <p>돌려받은 {@code image_url} 은 {@code itemHighlight.storage_image_url} 로 넘긴다.</p>
     * <p>⚠️ 썸네일을 붙이면 하이라이트 글자 한도가 줄어든다 (타이틀 30→21, 설명 19→13).</p>
     */
    static public BootpayStoreResponse highlightImage(BootpayStoreObject bootpay, File image, String replaceUrl) throws Exception {
        return upload(bootpay, "alimtalk/templates/highlight_image", image, replaceUrl);
    }

    private static BootpayStoreResponse upload(BootpayStoreObject bootpay, String uri, File image, String replaceUrl) throws Exception {
        bootpay.requireCommerceCredentials();
        if (image == null) throw new Exception("image 값이 비어있습니다.");
        if (!image.exists()) throw new Exception("파일 경로가 올바르지 않습니다: " + image.getAbsolutePath());

        HashMap<String, String> form = new HashMap<>();
        if (replaceUrl != null && !replaceUrl.isEmpty()) form.put("replace_url", replaceUrl);

        HttpPost post = bootpay.httpPostMultipartFile(uri, "image", image, form, SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    /** 생성·수정이 공유하는 템플릿 본문 필드 — 지정된 값(non-null)만 담는다. */
    private static void payload(Map<String, Object> body, AlimtalkTemplateParams params) {
        SAlimtalkSupport.put(body, "name", params.name);
        SAlimtalkSupport.put(body, "content", params.content);
        SAlimtalkSupport.put(body, "buttons", params.buttons);
        SAlimtalkSupport.put(body, "msg_type", params.msgType);
        SAlimtalkSupport.put(body, "emphasize_type", params.emphasizeType);
        SAlimtalkSupport.put(body, "emphasize_title", params.emphasizeTitle);
        SAlimtalkSupport.put(body, "emphasize_subtitle", params.emphasizeSubtitle);
        SAlimtalkSupport.put(body, "template_extra", params.templateExtra);
        SAlimtalkSupport.put(body, "template_header", params.templateHeader);
        SAlimtalkSupport.put(body, "item_highlight", params.itemHighlight);
        SAlimtalkSupport.put(body, "template_item", params.templateItem);
        SAlimtalkSupport.put(body, "image_url", params.imageUrl);
        SAlimtalkSupport.put(body, "storage_image_url", params.storageImageUrl);
        SAlimtalkSupport.put(body, "security_flag", params.securityFlag);
        SAlimtalkSupport.put(body, "category", params.category);
        SAlimtalkSupport.put(body, "tags", params.tags);
        SAlimtalkSupport.put(body, "examples", params.examples);
        SAlimtalkSupport.put(body, "template_code", params.templateCode);
    }

    /**
     * 본문을 JSON 으로 만들고 {@code attrs} 를 덮어쓴다 (ruby SDK 의 {@code .merge(attrs).compact} 와 같은 순서).
     */
    private static String toJson(Map<String, Object> body, Map<String, Object> attrs) {
        Gson gson = SAlimtalkSupport.gson();
        JsonObject json = gson.toJsonTree(body).getAsJsonObject();
        if (attrs != null) {
            for (Map.Entry<String, Object> entry : attrs.entrySet()) {
                if (entry.getValue() == null) continue;
                JsonElement value = gson.toJsonTree(entry.getValue());
                json.add(entry.getKey(), value);
            }
        }
        return gson.toJson(json);
    }
}
