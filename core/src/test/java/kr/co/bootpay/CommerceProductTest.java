package kr.co.bootpay;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.pojo.SProduct;
import kr.co.bootpay.store.model.request.product.ProductListParams;
import kr.co.bootpay.store.model.request.product.ProductStatusParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Commerce API - Product")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommerceProductTest {

    private static BootpayStore store;
    private static String createdProductId;

    @BeforeAll
    static void setUp() throws Exception {
        store = TestConfig.createBootpayStoreWithToken();
    }

    // ── 상품 생성 ─────────────────────────────────────────────

    @Test
    @Order(1)
    @DisplayName("product.create - 상품 생성")
    void testProductCreate() throws Exception {
        SProduct product = new SProduct();
        product.name = "Java SDK 테스트 상품 " + System.currentTimeMillis();
        product.description = "Java SDK 통합테스트로 생성된 상품입니다.";
        product.displayPrice = 10000.0;
        product.taxFreePrice = 0.0;
        product.type = 1;
        product.taxType = 1;
        product.statusSale = true;
        product.statusDisplay = true;

        // 이미지 없이 생성
        BootpayStoreResponse res = store.product.create(product, new ArrayList<>());
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("product.create 응답: " + res);

        if (res.isSuccess() && res.getData() != null && res.getData().containsKey("product_id")) {
            createdProductId = (String) res.getData().get("product_id");
            System.out.println("생성된 product_id: " + createdProductId);
        }
    }

    // ── 상품 목록 조회 ────────────────────────────────────────

    @Test
    @Order(2)
    @DisplayName("product.list - 상품 목록 조회")
    void testProductList() throws Exception {
        ProductListParams params = new ProductListParams();
        params.page = 1;
        params.limit = 10;

        BootpayStoreResponse res = store.product.list(params);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("product.list 응답: " + res);
    }

    @Test
    @Order(3)
    @DisplayName("product.list - 키워드 검색")
    void testProductListWithKeyword() throws Exception {
        ProductListParams params = new ProductListParams();
        params.page = 1;
        params.limit = 5;
        params.keyword = "테스트";

        BootpayStoreResponse res = store.product.list(params);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("product.list (keyword) 응답: " + res);
    }

    @Test
    @Order(4)
    @DisplayName("product.list - 상품 유형 필터")
    void testProductListByType() throws Exception {
        ProductListParams params = new ProductListParams();
        params.page = 1;
        params.limit = 10;
        params.type = 1;

        BootpayStoreResponse res = store.product.list(params);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("product.list (type) 응답: " + res);
    }

    // ── 상품 상세 조회 ────────────────────────────────────────

    @Test
    @Order(5)
    @DisplayName("product.detail - 상품 상세 조회")
    void testProductDetail() throws Exception {
        String productId = (createdProductId != null) ? createdProductId : "test_product_id";
        BootpayStoreResponse res = store.product.detail(productId);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("product.detail 응답: " + res);
    }

    // ── 상품 수정 ─────────────────────────────────────────────

    @Test
    @Order(6)
    @DisplayName("product.update - 상품 수정")
    void testProductUpdate() throws Exception {
        String productId = (createdProductId != null) ? createdProductId : "test_product_id";

        SProduct product = new SProduct();
        product.productId = productId;
        product.name = "수정된 Java SDK 테스트 상품";
        product.description = "수정된 설명입니다.";
        product.displayPrice = 15000.0;

        BootpayStoreResponse res = store.product.update(product);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("product.update 응답: " + res);
    }

    // ── 상품 상태 변경 ────────────────────────────────────────

    @Test
    @Order(7)
    @DisplayName("product.status - 상품 상태 변경")
    void testProductStatus() throws Exception {
        String productId = (createdProductId != null) ? createdProductId : "test_product_id";

        ProductStatusParams params = new ProductStatusParams();
        params.productId = productId;
        params.statusSale = true;
        params.statusDisplay = true;

        BootpayStoreResponse res = store.product.status(params);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("product.status 응답: " + res);
    }

    // ── 상품 삭제 ─────────────────────────────────────────────

    @Test
    @Order(8)
    @DisplayName("product.delete - 상품 삭제")
    void testProductDelete() throws Exception {
        String productId = (createdProductId != null) ? createdProductId : "test_product_id";
        BootpayStoreResponse res = store.product.delete(productId);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("product.delete 응답: " + res);
    }
}
