package kr.co.bootpay.store.model.pojo;

import java.util.List;

public class SProductOption {

    public String productOptionId;
    public String projectId;
    public String sellerId;
    public String productId;
    public String productSnapshotId;

    public int type; // 선택 or 추가상품
    public String name; // 옵션명
    public boolean useStock; // 재고관리 여부
    public int stock; // 전체 재고
    public List<String> keys; // 옵션 키값 목록

    public List<String> images; // 이미지 리스트
    public double price; // 추가금액
    public double taxFreePrice; // 면세 가격
    public String sku; // Stock Keeping Unit (관리 코드)
    public int status; // 사용 여부 (사용자 체크)

    public List<String> subscriptionPeriods; // 구독 기간

    public String createdAt;
    public String updatedAt;

}
