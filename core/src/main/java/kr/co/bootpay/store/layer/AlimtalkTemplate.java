package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkTemplateCreateParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkTemplateExportParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkTemplateListParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkTemplateUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.alimtalk.SAlimtalkTemplateService;

import java.io.File;

/**
 * 가맹점 자체 알림톡 템플릿 모듈
 * /v1/alimtalk/templates 계열
 *
 * <p>흐름: (초안 생성 → 확인 → 대행사 등록) → 검수 요청 → 승인(APR) → 발송 가능.</p>
 * <p>⚠️ {@code register} 를 명시적으로 false 로 주지 않으면 <b>생성 즉시 대행사·카카오에 실제 등록</b>된다.</p>
 */
public class AlimtalkTemplate {
    private final BootpayStore bootpay;

    public AlimtalkTemplate(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /** 자체 템플릿 전체 조회 — ⚠️ 페이지네이션이 없다 */
    public BootpayStoreResponse list() throws Exception {
        return SAlimtalkTemplateService.list(bootpay, null);
    }

    /** 자체 템플릿 목록 조회 */
    public BootpayStoreResponse list(AlimtalkTemplateListParams params) throws Exception {
        return SAlimtalkTemplateService.list(bootpay, params);
    }

    /** 자체 템플릿 생성 — ⚠️ register 를 false 로 주지 않으면 대행사·카카오에 실제 등록된다 */
    public BootpayStoreResponse create(AlimtalkTemplateCreateParams params) throws Exception {
        return SAlimtalkTemplateService.create(bootpay, params);
    }

    /** 자체 템플릿 상세 조회 — ⚠️ 서버 기본 sync 가 true 라 조회만 해도 벤더 동기화가 일어난다 */
    public BootpayStoreResponse detail(String templateId) throws Exception {
        return SAlimtalkTemplateService.detail(bootpay, templateId, null);
    }

    /**
     * 자체 템플릿 상세 조회
     * @param sync false 를 주면 벤더 동기화 없이 자체 DB 만 본다 (초안 조회 권장)
     */
    public BootpayStoreResponse detail(String templateId, Boolean sync) throws Exception {
        return SAlimtalkTemplateService.detail(bootpay, templateId, sync);
    }

    /** 자체 템플릿 수정 — ⚠️ 부분 수정이 아니다. 항상 전체 필드를 보낸다 */
    public BootpayStoreResponse update(String templateId, AlimtalkTemplateUpdateParams params) throws Exception {
        return SAlimtalkTemplateService.update(bootpay, templateId, params);
    }

    /** 자체 템플릿 삭제 — ⚠️ 승인(APR) 템플릿은 카카오가 거부한다 */
    public BootpayStoreResponse delete(String templateId) throws Exception {
        return SAlimtalkTemplateService.delete(bootpay, templateId);
    }

    /** 초안을 대행사에 등록 — ⚠️ 대행사·카카오에 실제 등록된다 */
    public BootpayStoreResponse register(String templateId) throws Exception {
        return SAlimtalkTemplateService.register(bootpay, templateId);
    }

    /** 검수 요청 — ⚠️ 카카오에 검수를 요청하며 취소할 수 없다 */
    public BootpayStoreResponse inspect(String templateId) throws Exception {
        return SAlimtalkTemplateService.inspect(bootpay, templateId);
    }

    /** 템플릿 목록 내보내기 (json) */
    public BootpayStoreResponse export() throws Exception {
        return SAlimtalkTemplateService.export(bootpay, null);
    }

    /** 템플릿 목록 내보내기 — format 이 csv 면 파싱 없이 { body, content_type } 을 돌려준다 */
    public BootpayStoreResponse export(AlimtalkTemplateExportParams params) throws Exception {
        return SAlimtalkTemplateService.export(bootpay, params);
    }

    /** 이미지형 템플릿의 원본 이미지 업로드 (jpg/png · 500KB 이하 · 가로 500px 이상 · 2:1) */
    public BootpayStoreResponse image(File image) throws Exception {
        return SAlimtalkTemplateService.image(bootpay, image, null);
    }

    /**
     * 이미지형 템플릿의 원본 이미지 업로드
     * @param replaceUrl 업로드 성공 후 지울 기존 파일 URL
     */
    public BootpayStoreResponse image(File image, String replaceUrl) throws Exception {
        return SAlimtalkTemplateService.image(bootpay, image, replaceUrl);
    }

    /** 이미지형 템플릿의 원본 이미지 업로드 (파일 경로) */
    public BootpayStoreResponse image(String imagePath) throws Exception {
        return SAlimtalkTemplateService.image(bootpay, imagePath == null ? null : new File(imagePath), null);
    }

    /** 아이템리스트형의 하이라이트 썸네일 업로드 (jpg/png · 500KB 이하 · 가로 108px 이상 · 1:1) */
    public BootpayStoreResponse highlightImage(File image) throws Exception {
        return SAlimtalkTemplateService.highlightImage(bootpay, image, null);
    }

    /**
     * 아이템리스트형의 하이라이트 썸네일 업로드
     * @param replaceUrl 업로드 성공 후 지울 기존 파일 URL
     */
    public BootpayStoreResponse highlightImage(File image, String replaceUrl) throws Exception {
        return SAlimtalkTemplateService.highlightImage(bootpay, image, replaceUrl);
    }

    /** 아이템리스트형의 하이라이트 썸네일 업로드 (파일 경로) */
    public BootpayStoreResponse highlightImage(String imagePath) throws Exception {
        return SAlimtalkTemplateService.highlightImage(bootpay, imagePath == null ? null : new File(imagePath), null);
    }
}
