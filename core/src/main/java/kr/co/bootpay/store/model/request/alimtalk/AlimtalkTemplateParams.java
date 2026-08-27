package kr.co.bootpay.store.model.request.alimtalk;

import java.util.List;
import java.util.Map;

/**
 * 자체 알림톡 템플릿의 공통 페이로드.
 *
 * <p>생성은 {@link AlimtalkTemplateCreateParams}, 수정은 {@link AlimtalkTemplateUpdateParams} 를 쓴다.
 * 생성에만 있는 {@code ksp_id} / {@code register} 가 수정 요청에 섞여 나가지 않도록 두 타입을 나눠 두었다.</p>
 *
 * <p>⚠️ 본문 변수는 {@code #{변수명}} 형식이고 템플릿 전체에서 최대 40개다.</p>
 *
 * <p>{@code buttons} / {@code templateItem} / {@code itemHighlight} / {@code examples} 는 서버 스키마를 그대로
 * 통과시키는 자리라 맵으로 둔다 — 서버가 항목을 늘려도 SDK 수정 없이 실어 보낼 수 있다.</p>
 */
public class AlimtalkTemplateParams {
    public String name;
    /** 본문 — 변수는 {@code #{변수명}} 형식 */
    public String content;
    /** 버튼 목록. AD·MI 는 채널추가(AC) 버튼이 필수다 */
    public List<Map<String, Object>> buttons;
    /** BA(기본형) · EX(부가정보형, {@code templateExtra} 필수) · AD(채널추가형) · MI(복합형) */
    public String msgType;
    /** NONE · TEXT(강조표기형) · IMAGE(이미지형) · ITEM_LIST(아이템리스트형) */
    public String emphasizeType;
    /** TEXT 강조표기형 필수 (50자) */
    public String emphasizeTitle;
    /** TEXT 강조표기형 필수 (40자) */
    public String emphasizeSubtitle;
    /** EX(부가정보형) 필수 */
    public String templateExtra;
    /** ITEM_LIST 헤더 */
    public String templateHeader;
    /** ITEM_LIST 하이라이트 — 썸네일은 {@code storage_image_url} 로 넣는다 */
    public Map<String, Object> itemHighlight;
    /** ITEM_LIST 의 {@code list} (2~10개) 필수 */
    public Map<String, Object> templateItem;
    public String imageUrl;
    /**
     * {@code alimtalk.template.image} 로 올린 이미지 URL.
     *
     * <p>⚠️ 수정 시 빈 값으로 보내면 <b>이미지 삭제</b>로 처리되어 벤더에도 전달된다.</p>
     */
    public String storageImageUrl;
    public Boolean securityFlag;
    public String category;
    public List<String> tags;
    /** 변수 예문(표시용). 주면 <b>모든 변수에 예문이 있어야</b> 한다 (없으면 3017) */
    public Map<String, Object> examples;
    public String templateCode;

    /**
     * 서버가 새로 받기 시작한 필드를 SDK 수정 없이 실어 보내는 탈출구 (ruby SDK 의 {@code **attrs}).
     *
     * <p>여기 담은 키는 이름 변환 없이 그대로 전송되며, 같은 이름이 위 필드에도 있으면 이 값이 이긴다.</p>
     */
    public transient Map<String, Object> attrs;
}
