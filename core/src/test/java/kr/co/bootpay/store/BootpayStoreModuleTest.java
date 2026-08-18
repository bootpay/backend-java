package kr.co.bootpay.store;

import kr.co.bootpay.store.model.request.TokenPayload;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BootpayStoreModuleTest {

    private BootpayStore bootpay() {
        return new BootpayStore(new TokenPayload("test_client_key", "test_secret_key"), "PRODUCTION");
    }

    @Test
    public void 웹훅_모듈이_초기화된다() {
        assertNotNull(bootpay().webhook);
    }

    @Test
    public void 구독_변경요청_모듈이_초기화된다() {
        BootpayStore bootpay = bootpay();

        assertNotNull(bootpay.orderSubscription.request);
        assertNotNull(bootpay.orderSubscription.requestIng);
    }
}
