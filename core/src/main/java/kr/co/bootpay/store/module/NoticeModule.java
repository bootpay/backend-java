package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.Notice;
import kr.co.bootpay.store.model.request.notice.NoticeCreateParams;
import kr.co.bootpay.store.model.request.notice.NoticeDetailParams;
import kr.co.bootpay.store.model.request.notice.NoticeListParams;
import kr.co.bootpay.store.model.request.notice.NoticeUpdateParams;

/**
 * 공지사항 모듈.
 *
 * <p>조회는 고객·운영자 모드 둘 다, 등록·수정·삭제는 supervisor 전용입니다.</p>
 *
 * @since 3.7.0
 */
public class NoticeModule {

    private final Notice delegate;

    public NoticeModule(BootpayStore bootpay) {
        this.delegate = new Notice(bootpay);
    }

    /**
     * 공지사항 목록 — page 1 / limit 20 이 기본값입니다.
     *
     * @return 공지사항 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list() throws Exception {
        return CommerceResponses.of(delegate.list());
    }

    /**
     * 공지사항 목록.
     *
     * @param params page · limit · keyword · view · supervisor
     * @return 공지사항 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list(NoticeListParams params) throws Exception {
        return CommerceResponses.of(delegate.list(params));
    }

    /**
     * 공지사항 단건 — 고객 모드.
     *
     * @param noticeId 공지사항 ID
     * @return 공지사항
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String noticeId) throws Exception {
        return CommerceResponses.of(delegate.detail(noticeId));
    }

    /**
     * 공지사항 단건.
     *
     * @param noticeId   공지사항 ID
     * @param supervisor true 면 운영자 모드로 조회합니다 (비공개 공지 포함)
     * @return 공지사항
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String noticeId, boolean supervisor) throws Exception {
        return CommerceResponses.of(delegate.detail(noticeId, supervisor));
    }

    /**
     * 공지사항 단건.
     *
     * @param params noticeId · supervisor
     * @return 공지사항
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(NoticeDetailParams params) throws Exception {
        return CommerceResponses.of(delegate.detail(params));
    }

    /**
     * 공지사항 등록 — supervisor 전용.
     *
     * @param params title · content · images · isNotice · isDisplay
     * @return 등록된 공지사항
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse create(NoticeCreateParams params) throws Exception {
        return CommerceResponses.of(delegate.create(params));
    }

    /**
     * 공지사항 수정 — supervisor 전용, 보낸 필드만 바뀝니다.
     *
     * @param params noticeId · title · content · images · isNotice · isDisplay
     * @return 수정된 공지사항
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse update(NoticeUpdateParams params) throws Exception {
        return CommerceResponses.of(delegate.update(params));
    }

    /**
     * 공지사항 삭제 — supervisor 전용.
     *
     * @param noticeId 공지사항 ID
     * @return 삭제 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse delete(String noticeId) throws Exception {
        return CommerceResponses.of(delegate.delete(noticeId));
    }

    /**
     * 공지사항 삭제 — supervisor 전용.
     *
     * @param noticeId       공지사항 ID
     * @param idempotencyKey 미지정 시 자동 생성 (Idempotency-Key 헤더)
     * @return 삭제 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse delete(String noticeId, String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.delete(noticeId, idempotencyKey));
    }
}
