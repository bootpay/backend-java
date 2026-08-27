package kr.co.bootpay.store.model.request.alimtalk;

/**
 * 알림톡 수신거부 목록 조회 파라미터 (GET /v1/alimtalk/optouts)
 *
 * <p>{@code phone} 은 숫자만 남겨 <b>부분일치</b>로 찾는다 (정확 매칭이 아니다). 50건 단위로 페이징된다.</p>
 */
public class AlimtalkOptoutListParams {
    public String phone;
    public Integer page;
}
