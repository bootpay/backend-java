package kr.co.bootpay.store.model.pojo;

public class SUserGroupRole {
    public String userGroupRoleId; // ID 필드
    public String userGroup_id; // 사용자 그룹 ID (ug_id)
    public String user_id; // 사용자 ID (u_id)

    public int level = 1; // 관리자 권한 (기본값: MEMBER_LV_ADMIN)
    public int status = 1; // 상태 (ABLE)
}
