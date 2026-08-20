package kr.co.bootpay.store.model.pojo;

/**
 * 청구서 생성 시 지정하는 구매자 정보 ({@code user}).
 *
 * <p>이미 가입된 회원이면 {@link #userId} 만으로 충분하고, 비회원 청구서라면
 * {@link #membershipType} 을 {@code "guest"} 로 두고 이름·연락처를 함께 넘긴다.</p>
 *
 * @since 3.3.0
 */
public class SInvoiceUser {

    public static final String MEMBERSHIP_TYPE_GUEST = "guest";
    public static final String MEMBERSHIP_TYPE_MEMBER = "member";

    /** 회원 식별자 (user_id, ex_uid, login_id 중 하나) */
    public String userId;
    /** 회원 유형 — "guest" 또는 "member" */
    public String membershipType;
    /** 구매자 이름 */
    public String name;
    /** 구매자 연락처 */
    public String phone;
    /** 구매자 이메일 */
    public String email;
}
