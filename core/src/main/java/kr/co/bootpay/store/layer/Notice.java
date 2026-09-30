package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.notice.NoticeCreateParams;
import kr.co.bootpay.store.model.request.notice.NoticeDetailParams;
import kr.co.bootpay.store.model.request.notice.NoticeListParams;
import kr.co.bootpay.store.model.request.notice.NoticeUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.boards.SNoticeService;

/**
 * 공지사항 모듈
 * /v1/notices 계열
 *
 * <p>조회는 고객·운영자 모드 둘 다, 등록·수정·삭제는 supervisor 전용이다.</p>
 */
public class Notice {
    private final BootpayStore bootpay;

    public Notice(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /** 공지사항 목록 — page 1 / limit 20 이 기본값이다 */
    public BootpayStoreResponse list() throws Exception {
        return SNoticeService.list(bootpay, null);
    }

    /** 공지사항 목록 */
    public BootpayStoreResponse list(NoticeListParams params) throws Exception {
        return SNoticeService.list(bootpay, params);
    }

    /** 공지사항 단건 — 고객 모드 */
    public BootpayStoreResponse detail(String noticeId) throws Exception {
        return detail(noticeId, false);
    }

    /**
     * 공지사항 단건
     * @param supervisor true 면 운영자 모드로 조회한다 (비공개 공지 포함)
     */
    public BootpayStoreResponse detail(String noticeId, boolean supervisor) throws Exception {
        NoticeDetailParams params = new NoticeDetailParams();
        params.noticeId = noticeId;
        params.supervisor = supervisor;
        return SNoticeService.detail(bootpay, params);
    }

    /** 공지사항 단건 */
    public BootpayStoreResponse detail(NoticeDetailParams params) throws Exception {
        return SNoticeService.detail(bootpay, params);
    }

    /** 공지사항 등록 — supervisor 전용 */
    public BootpayStoreResponse create(NoticeCreateParams params) throws Exception {
        return SNoticeService.create(bootpay, params);
    }

    /** 공지사항 수정 — supervisor 전용, 보낸 필드만 바뀐다 */
    public BootpayStoreResponse update(NoticeUpdateParams params) throws Exception {
        return SNoticeService.update(bootpay, params);
    }

    /** 공지사항 삭제 — supervisor 전용 */
    public BootpayStoreResponse delete(String noticeId) throws Exception {
        return SNoticeService.delete(bootpay, noticeId, null);
    }

    /**
     * 공지사항 삭제 — supervisor 전용
     * @param idempotencyKey 미지정시 자동 생성
     */
    public BootpayStoreResponse delete(String noticeId, String idempotencyKey) throws Exception {
        return SNoticeService.delete(bootpay, noticeId, idempotencyKey);
    }
}
