package kr.co.bootpay.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public enum BankCode {
    산업("산업"),
    기업("기업"),
    국민("국민"),
    하나("하나"),
    외환("외환"),
    케이뱅크("케이뱅크"),
    카카오뱅크("카카오뱅크"),
    토스뱅크("토스뱅크"),
    수협("수협"),
    농협("농협"),
    우리("우리"),
    SC제일("SC제일"),
    시티("시티"),
    대구("대구"),
    부산("부산"),
    광주("광주"),
    제주("제주"),
    전북("전북"),
    강원("강원"),
    경남("경남"),
    새마을금고("새마을금고"),
    우체국("우체국");

    private String title;

    BankCode(String title) {
        this.title = title;
    }


    public static BankCode fromString(String title) {
        for (BankCode b : BankCode.values()) {
            if (b.title.equalsIgnoreCase(title)) {
                return b;
            }
        }
        return null;
    }
}
