package kr.co.bootpay.store.model.request.user;


import kr.co.bootpay.store.model.request.ListParams;

public class UserListParams extends ListParams {
    /**
     * 회원등급 필터. 서버(v1/users_controller#index)가 읽는 정식 키는 {@code membership_type} 이다.
     */
    public Integer membershipType;
    /**
     * @deprecated 서버가 읽지 않는 이름이었다. {@link #membershipType} 을 쓸 것 —
     *             값을 채우면 {@code membership_type} 으로 대신 전송된다(하위호환 별칭).
     */
    @Deprecated
    public Integer memberType;
    public String type;
}
