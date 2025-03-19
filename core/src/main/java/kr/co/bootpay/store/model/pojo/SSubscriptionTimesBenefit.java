package kr.co.bootpay.store.model.pojo;

import java.util.List;

public class SSubscriptionTimesBenefit {
    public String termOfUseId; // ID 필드

    public String seller_id; // 가맹점 ID
    public String project_id; // 프로젝트 ID

    public boolean required = true; // 필수 여부
    public String title; // 이용약관 제목
    public String content; // 이용약관 내용
    public String desc; // 설명
    public String group; // 그룹 (용도 불분명)
    public int termType = 1; // 이용약관 유형 (회원가입/상품/청구서 등)
    public int status = 1; // 사용 상태 (ABLE(1), UNABLE(0))
    public int sort = 0; // 정렬 순서

    public boolean fixed = false; // 고정 노출 여부 (부트페이 기본 약관)

    public String stockKeepingUnit; // SKU (관리코드)
    public int projectStock = 0; // 프로젝트 유일 코드

    public String url; // 외부 이용약관 URL
    public boolean useUrl = false; // 외부 URL 사용 여부 (true일 경우 `content` 필요 없음)

    public List<String> tags; // 태그 목록
}
