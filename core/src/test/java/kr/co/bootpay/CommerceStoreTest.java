package kr.co.bootpay;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.pojo.SUserGroup;
import kr.co.bootpay.store.model.request.userGroup.UserGroupListParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Commerce API - Store & UserGroup")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommerceStoreTest {

    private static BootpayStore store;
    private static String createdUserGroupId;

    @BeforeAll
    static void setUp() throws Exception {
        store = TestConfig.createBootpayStoreWithToken();
    }

    // ══════════════════════════════════════════════════════════
    // Store (가맹점 정보)
    // ══════════════════════════════════════════════════════════

    @Test
    @Order(1)
    @DisplayName("store.info - 가맹점 기본 정보 조회")
    void testStoreInfo() throws Exception {
        BootpayStoreResponse res = store.store.info();
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("store.info 응답: " + res);
    }

    @Test
    @Order(2)
    @DisplayName("store.detail - 가맹점 상세 정보 조회")
    void testStoreDetail() throws Exception {
        BootpayStoreResponse res = store.store.detail();
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("store.detail 응답: " + res);
    }

    // ══════════════════════════════════════════════════════════
    // UserGroup (사용자 그룹)
    // ══════════════════════════════════════════════════════════

    @Test
    @Order(3)
    @DisplayName("userGroup.create - 사용자 그룹 생성")
    void testUserGroupCreate() throws Exception {
        SUserGroup userGroup = new SUserGroup();
        userGroup.companyName = "Java SDK 테스트 그룹 " + System.currentTimeMillis();
        userGroup.corporateType = SUserGroup.CORPORATE_TYPE_INDIVIDUAL;
        userGroup.ceoName = "테스트대표";
        userGroup.email = "group_test@example.com";
        userGroup.phone = "01012345678";

        BootpayStoreResponse res = store.userGroup.create(userGroup);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("userGroup.create 응답: " + res);

        if (res.isSuccess() && res.getData() != null && res.getData().containsKey("user_group_id")) {
            createdUserGroupId = (String) res.getData().get("user_group_id");
            System.out.println("생성된 user_group_id: " + createdUserGroupId);
        }
    }

    @Test
    @Order(4)
    @DisplayName("userGroup.list - 사용자 그룹 목록 조회")
    void testUserGroupList() throws Exception {
        UserGroupListParams params = new UserGroupListParams();
        params.page = 1;
        params.limit = 10;

        BootpayStoreResponse res = store.userGroup.list(params);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("userGroup.list 응답: " + res);
    }

    @Test
    @Order(5)
    @DisplayName("userGroup.list - 키워드 검색")
    void testUserGroupListWithKeyword() throws Exception {
        UserGroupListParams params = new UserGroupListParams();
        params.page = 1;
        params.limit = 10;
        params.keyword = "테스트";

        BootpayStoreResponse res = store.userGroup.list(params);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("userGroup.list (keyword) 응답: " + res);
    }

    @Test
    @Order(6)
    @DisplayName("userGroup.detail - 사용자 그룹 상세 조회")
    void testUserGroupDetail() throws Exception {
        String userGroupId = (createdUserGroupId != null) ? createdUserGroupId : "test_user_group_id";
        BootpayStoreResponse res = store.userGroup.detail(userGroupId);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("userGroup.detail 응답: " + res);
    }

    @Test
    @Order(7)
    @DisplayName("userGroup.update - 사용자 그룹 수정")
    void testUserGroupUpdate() throws Exception {
        String userGroupId = (createdUserGroupId != null) ? createdUserGroupId : "test_user_group_id";

        SUserGroup userGroup = new SUserGroup();
        userGroup.userGroupId = userGroupId;
        userGroup.companyName = "수정된 Java SDK 테스트 그룹";

        BootpayStoreResponse res = store.userGroup.update(userGroup);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("userGroup.update 응답: " + res);
    }

    @Test
    @Order(8)
    @DisplayName("userGroup.userCreate - 그룹에 사용자 추가")
    void testUserGroupUserCreate() throws Exception {
        String userGroupId = (createdUserGroupId != null) ? createdUserGroupId : "test_user_group_id";
        BootpayStoreResponse res = store.userGroup.userCreate(userGroupId, "test_user_001");
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("userGroup.userCreate 응답: " + res);
    }

    @Test
    @Order(9)
    @DisplayName("userGroup.userDelete - 그룹에서 사용자 제거")
    void testUserGroupUserDelete() throws Exception {
        String userGroupId = (createdUserGroupId != null) ? createdUserGroupId : "test_user_group_id";
        BootpayStoreResponse res = store.userGroup.userDelete(userGroupId, "test_user_001");
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("userGroup.userDelete 응답: " + res);
    }

    // ══════════════════════════════════════════════════════════
    // Project (프로젝트)
    // ══════════════════════════════════════════════════════════

    // Project 레이어는 현재 메서드가 없으므로 별도 테스트 불필요
}
