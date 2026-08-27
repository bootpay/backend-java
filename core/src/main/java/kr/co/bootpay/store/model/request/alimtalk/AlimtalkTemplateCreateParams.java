package kr.co.bootpay.store.model.request.alimtalk;

/**
 * 자체 알림톡 템플릿 생성 파라미터 (POST /v1/alimtalk/templates)
 *
 * <p>⚠️ {@code register} 를 명시적으로 {@code false} 로 주지 않으면 <b>생성 즉시 대행사·카카오에 실제 등록</b>된다
 * (되돌리려면 삭제해야 한다). {@code register = false} 로 초안만 만들고 내용을 확인한 뒤
 * {@code alimtalk.template.register} 로 올리는 것을 권장한다.</p>
 */
public class AlimtalkTemplateCreateParams extends AlimtalkTemplateParams {
    /** 템플릿을 붙일 채널의 내부 문서 id */
    public String kspId;
    /** ⚠️ false 로 주지 않으면 대행사·카카오에 <b>실제 등록</b>된다 */
    public Boolean register;
}
