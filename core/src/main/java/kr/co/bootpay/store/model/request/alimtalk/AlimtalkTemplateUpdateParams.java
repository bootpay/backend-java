package kr.co.bootpay.store.model.request.alimtalk;

/**
 * 자체 알림톡 템플릿 수정 파라미터 (PUT /v1/alimtalk/templates/{template_id})
 *
 * <p>⚠️ <b>부분 수정이 아니다.</b> 보내지 않은 필드는 null 로 덮어써지므로 항상 전체 필드를 보낸다.</p>
 * <p>⚠️ 등록된 템플릿을 수정하면 벤더에도 수정 요청이 나간다. 수정 가능 상태는
 * 초안 / REG(등록) / REJ(승인반려) / KRR(등록거절) 뿐이다 — APR·REQ 는 거부된다.</p>
 */
public class AlimtalkTemplateUpdateParams extends AlimtalkTemplateParams {
}
