package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.AlimtalkTemplate;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkTemplateCreateParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkTemplateExportParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkTemplateListParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkTemplateUpdateParams;

import java.io.File;

/**
 * 가맹점 자체 알림톡 템플릿 모듈.
 *
 * <p>흐름: (초안 생성 → 확인 → 대행사 등록) → 검수 요청 → 승인(APR) → 발송 가능.</p>
 * <p>⚠️ {@code register} 를 명시적으로 false 로 주지 않으면 생성 즉시 대행사·카카오에 실제 등록된다.</p>
 *
 * @since 3.6.0
 */
public class AlimtalkTemplateModule {

    private final AlimtalkTemplate delegate;

    public AlimtalkTemplateModule(BootpayStore bootpay) {
        this.delegate = new AlimtalkTemplate(bootpay);
    }

    /**
     * 자체 템플릿 전체 조회 — ⚠️ 페이지네이션이 없다.
     *
     * @return 템플릿 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list() throws Exception {
        return CommerceResponses.of(delegate.list());
    }

    /**
     * 자체 템플릿 목록 조회.
     *
     * @param params 검수상태·정렬·검색어 필터
     * @return 템플릿 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list(AlimtalkTemplateListParams params) throws Exception {
        return CommerceResponses.of(delegate.list(params));
    }

    /**
     * 자체 템플릿 생성 — ⚠️ {@code register} 를 false 로 주지 않으면 대행사·카카오에 실제 등록된다.
     *
     * @param params 생성 파라미터
     * @return 생성 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse create(AlimtalkTemplateCreateParams params) throws Exception {
        return CommerceResponses.of(delegate.create(params));
    }

    /**
     * 자체 템플릿 상세 조회 — ⚠️ 서버 기본 {@code sync} 가 true 라 조회만 해도 벤더 동기화가 일어난다.
     *
     * @param templateId 문서 id (ObjectId 형식이 아니면 템플릿 코드로 해석한다)
     * @return 템플릿 상세
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String templateId) throws Exception {
        return CommerceResponses.of(delegate.detail(templateId));
    }

    /**
     * 자체 템플릿 상세 조회.
     *
     * @param templateId 문서 id 또는 템플릿 코드
     * @param sync       false 를 주면 벤더 동기화 없이 자체 DB 만 본다 (초안 조회 권장)
     * @return 템플릿 상세
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String templateId, Boolean sync) throws Exception {
        return CommerceResponses.of(delegate.detail(templateId, sync));
    }

    /**
     * 자체 템플릿 수정 — ⚠️ 부분 수정이 아니다. 항상 전체 필드를 보낸다.
     *
     * @param templateId 문서 id 또는 템플릿 코드
     * @param params     수정 파라미터
     * @return 수정 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse update(String templateId, AlimtalkTemplateUpdateParams params) throws Exception {
        return CommerceResponses.of(delegate.update(templateId, params));
    }

    /**
     * 자체 템플릿 삭제 — ⚠️ 승인(APR) 템플릿은 카카오가 거부한다.
     *
     * @param templateId 문서 id 또는 템플릿 코드
     * @return 삭제 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse delete(String templateId) throws Exception {
        return CommerceResponses.of(delegate.delete(templateId));
    }

    /**
     * 초안을 대행사에 등록 — ⚠️ 대행사·카카오에 실제 등록된다.
     *
     * @param templateId 문서 id 또는 템플릿 코드
     * @return 등록 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse register(String templateId) throws Exception {
        return CommerceResponses.of(delegate.register(templateId));
    }

    /**
     * 검수 요청 — ⚠️ 카카오에 검수를 요청하며 취소할 수 없다.
     *
     * @param templateId 문서 id 또는 템플릿 코드
     * @return 검수 요청 결과 (반려 사유는 {@code comments})
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse inspect(String templateId) throws Exception {
        return CommerceResponses.of(delegate.inspect(templateId));
    }

    /**
     * 템플릿 목록 내보내기 (json).
     *
     * @return 내보내기 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse export() throws Exception {
        return CommerceResponses.of(delegate.export());
    }

    /**
     * 템플릿 목록 내보내기.
     *
     * @param params {@code format} 이 csv 면 파싱 없이 {@code { body, content_type }} 을 돌려준다
     * @return 내보내기 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse export(AlimtalkTemplateExportParams params) throws Exception {
        return CommerceResponses.of(delegate.export(params));
    }

    /**
     * 이미지형 템플릿의 원본 이미지 업로드 (jpg/png · 500KB 이하 · 가로 500px 이상 · 2:1).
     *
     * @param image 업로드할 파일
     * @return {@code image_url} 을 담은 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse image(File image) throws Exception {
        return CommerceResponses.of(delegate.image(image));
    }

    /**
     * 이미지형 템플릿의 원본 이미지 업로드.
     *
     * @param image      업로드할 파일
     * @param replaceUrl 업로드 성공 후 지울 기존 파일 URL
     * @return {@code image_url} 을 담은 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse image(File image, String replaceUrl) throws Exception {
        return CommerceResponses.of(delegate.image(image, replaceUrl));
    }

    /**
     * 아이템리스트형의 하이라이트 썸네일 업로드 (jpg/png · 500KB 이하 · 가로 108px 이상 · 1:1).
     *
     * @param image 업로드할 파일
     * @return {@code image_url} 을 담은 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse highlightImage(File image) throws Exception {
        return CommerceResponses.of(delegate.highlightImage(image));
    }

    /**
     * 아이템리스트형의 하이라이트 썸네일 업로드.
     *
     * @param image      업로드할 파일
     * @param replaceUrl 업로드 성공 후 지울 기존 파일 URL
     * @return {@code image_url} 을 담은 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse highlightImage(File image, String replaceUrl) throws Exception {
        return CommerceResponses.of(delegate.highlightImage(image, replaceUrl));
    }
}
