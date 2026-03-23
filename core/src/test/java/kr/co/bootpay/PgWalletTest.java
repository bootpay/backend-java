package kr.co.bootpay;

import kr.co.bootpay.pg.Bootpay;
import kr.co.bootpay.pg.model.request.Item;
import kr.co.bootpay.pg.model.request.User;
import kr.co.bootpay.pg.model.request.WalletPayment;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PG API - Wallet (간편결제 지갑)")
class PgWalletTest {

    private static Bootpay bootpay;

    @BeforeAll
    static void setUp() throws Exception {
        bootpay = TestConfig.createBootpayWithToken();
    }

    @Test
    @DisplayName("getUserWallets - 사용자 지갑 목록 조회")
    void testGetUserWallets() throws Exception {
        String userId = "test_user_001";
        HashMap<String, Object> res = bootpay.getUserWallets(userId, true);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("getUserWallets 응답: " + res);
    }

    @Test
    @DisplayName("getUserWallets - sandbox false로 조회")
    void testGetUserWalletsProduction() throws Exception {
        String userId = "test_user_001";
        HashMap<String, Object> res = bootpay.getUserWallets(userId, false);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("getUserWallets (sandbox=false) 응답: " + res);
    }

    @Test
    @DisplayName("requestWalletPayment - 지갑 결제 요청")
    void testRequestWalletPayment() throws Exception {
        WalletPayment walletPayment = new WalletPayment();
        walletPayment.userId = "test_user_001";
        walletPayment.orderName = "테스트 지갑 결제";
        walletPayment.price = 1000.0;
        walletPayment.taxFree = 0.0;
        walletPayment.orderId = "test_wallet_" + System.currentTimeMillis();
        walletPayment.sandbox = true;

        User user = new User();
        user.username = "테스트유저";
        user.phone = "01012345678";
        walletPayment.user = user;

        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.name = "테스트 상품";
        item.qty = 1;
        item.price = 1000.0;
        items.add(item);
        walletPayment.items = items;

        HashMap<String, Object> res = bootpay.requestWalletPayment(walletPayment);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("requestWalletPayment 응답: " + res);
    }
}
