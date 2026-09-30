package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.notice.NoticeCreateParams;
import kr.co.bootpay.store.model.request.notice.NoticeListParams;
import kr.co.bootpay.store.model.request.notice.NoticeUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;

/**
 * 공지사항 예제 (/v1/notices)
 *
 * 조회는 고객·운영자 모드 둘 다, 등록·수정·삭제는 supervisor 전용이다.
 */
public class Notice {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload("hxS-Up--5RvT6oU6QJE0JA", "r5zxvDcQJiAP2PBQ0aJjSHQtblNmYFt6uFoEMhti_mg=");
            bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
            list();
            listSupervisor();
//            detail("NOTICE_ID");
//            create();
//            update("NOTICE_ID");
//            delete("NOTICE_ID");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 공지사항 목록 (고객 모드) — page 1 / limit 20 이 기본값
    public static void list() {
        try {
            BootpayStoreResponse res = bootpayStore.notice.list();
            if(res.isSuccess()) {
                System.out.println("notice list success: " + res.getData());
            } else {
                System.out.println("notice list false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 공지사항 목록 (운영자 모드) — view: "all" 이면 비공개 공지까지 조회된다
    public static void listSupervisor() {
        try {
            NoticeListParams params = new NoticeListParams();
            params.keyword = "배송";
            params.view = "all";
            params.supervisor = true;

            BootpayStoreResponse res = bootpayStore.notice.list(params);
            if(res.isSuccess()) {
                System.out.println("notice list supervisor success: " + res.getData());
            } else {
                System.out.println("notice list supervisor false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 공지사항 단건 — 비공개 공지는 supervisor 일 때만 보인다
    public static void detail(String noticeId) {
        try {
            BootpayStoreResponse res = bootpayStore.notice.detail(noticeId, true);
            if(res.isSuccess()) {
                System.out.println("notice detail success: " + res.getData());
            } else {
                System.out.println("notice detail false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 공지사항 등록 (supervisor 전용) — isNotice 는 상단 고정 여부
    public static void create() {
        try {
            NoticeCreateParams params = new NoticeCreateParams();
            params.title = "설 연휴 배송 안내";
            params.content = "연휴 기간에는 배송이 지연될 수 있습니다.";
            params.isNotice = true;
            params.isDisplay = true;

            BootpayStoreResponse res = bootpayStore.notice.create(params);
            if(res.isSuccess()) {
                System.out.println("notice create success: " + res.getData());
            } else {
                System.out.println("notice create false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 공지사항 수정 (supervisor 전용) — 보낸 필드만 바뀐다
    public static void update(String noticeId) {
        try {
            NoticeUpdateParams params = new NoticeUpdateParams();
            params.noticeId = noticeId;
            params.title = "설 연휴 배송 안내 (수정)";
            params.isNotice = false;

            BootpayStoreResponse res = bootpayStore.notice.update(params);
            if(res.isSuccess()) {
                System.out.println("notice update success: " + res.getData());
            } else {
                System.out.println("notice update false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 공지사항 삭제 (supervisor 전용)
    public static void delete(String noticeId) {
        try {
            BootpayStoreResponse res = bootpayStore.notice.delete(noticeId);
            if(res.isSuccess()) {
                System.out.println("notice delete success: " + res.getData());
            } else {
                System.out.println("notice delete false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
