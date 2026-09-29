package kr.co.bootpay.store.model.request.notice;


import java.util.List;

/**
 * 공지사항 등록 파라미터 (POST /v1/notices) — supervisor 전용
 */
public class NoticeCreateParams {
    public String title;
    public String content;
    /** 이미지 목록 — 문자열(URL) 또는 { url: ... } 형태를 모두 받는다 */
    public List<Object> images;
    /** 상단 고정 공지 여부 */
    public Boolean isNotice;
    public Boolean isDisplay;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더) */
    public String idempotencyKey;
}
