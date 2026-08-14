package kr.co.bootpay.store.service.mall_setting;

import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.mallSetting.MallSettingUpdateParams;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SMallSettingServiceTest {
    private static final String CLIENT_KEY = "test_client_key";
    private static final String SECRET_KEY = "test_secret_key";

    private BootpayStoreObject bootpay() {
        return new BootpayStoreObject(new TokenPayload(CLIENT_KEY, SECRET_KEY), "PRODUCTION");
    }

    @Test
    public void 몰설정_조회는_supervisor_role로_요청한다() throws Exception {
        HttpGet get = SMallSettingService.getRequest(bootpay(), null);

        assertEquals("https://api.bootapi.com/v1/mall-setting", get.getURI().toString());
        assertEquals("supervisor", get.getFirstHeader("BOOTPAY-ROLE").getValue());
        assertNotNull(get.getFirstHeader("Idempotency-Key").getValue());
    }

    @Test
    public void 전달된_idempotency_key를_헤더로_사용한다() throws Exception {
        HttpGet get = SMallSettingService.getRequest(bootpay(), "my-idempotency-key");

        assertEquals("my-idempotency-key", get.getFirstHeader("Idempotency-Key").getValue());
    }

    @Test
    public void 몰설정_수정은_PUT으로_supervisor_role로_요청한다() throws Exception {
        MallSettingUpdateParams params = new MallSettingUpdateParams();
        params.name = "부트페이 스토어";

        HttpPut put = SMallSettingService.updateRequest(bootpay(), params);

        assertEquals("PUT", put.getMethod());
        assertEquals("https://api.bootapi.com/v1/mall-setting", put.getURI().toString());
        assertEquals("supervisor", put.getFirstHeader("BOOTPAY-ROLE").getValue());
        assertNotNull(put.getFirstHeader("Idempotency-Key").getValue());
    }

    @Test
    public void 수정_요청은_설정된_값만_snake_case로_전송한다() throws Exception {
        MallSettingUpdateParams params = new MallSettingUpdateParams();
        params.idempotencyKey = "my-idempotency-key";
        params.normalWidgetKey = "widget_key_value";
        params.sellerName = "부트페이";
        params.addr1 = "서울시 강남구";
        params.addr2 = "1층";
        params.useAgeAccept19 = true;
        params.useAgeAccept14 = false;
        params.useOrderCancelApproval = true;
        params.pointCalcType1 = 1;
        params.catalogViewTypePc = 2;
        params.restDay = Arrays.asList("saturday", "sunday");

        String body = EntityUtils.toString(SMallSettingService.updateRequest(bootpay(), params).getEntity(), "UTF-8");

        assertTrue(body.contains("\"normal_widget_key\":\"widget_key_value\""));
        assertTrue(body.contains("\"seller_name\":\"부트페이\""));
        assertTrue(body.contains("\"addr_1\":\"서울시 강남구\""));
        assertTrue(body.contains("\"addr_2\":\"1층\""));
        assertTrue(body.contains("\"use_age_accept_19\":true"));
        assertTrue(body.contains("\"use_age_accept_14\":false"));
        assertTrue(body.contains("\"use_oder_cancel_approval\":true"));
        assertTrue(body.contains("\"point_calc_type1\":1"));
        assertTrue(body.contains("\"catalog_view_type_pc\":2"));
        assertTrue(body.contains("\"rest_day\":[\"saturday\",\"sunday\"]"));

        // 값이 설정되지 않은 필드는 전송하지 않는다 (ruby의 compact 동작)
        assertFalse(body.contains("biz_email"));
        assertFalse(body.contains("use_cart"));
        // idempotency key는 헤더로만 전송한다
        assertFalse(body.contains("idempotency"));
    }

    @Test
    public void 인증정보가_없으면_예외를_발생시킨다() {
        BootpayStoreObject bootpay = new BootpayStoreObject(new TokenPayload(), "PRODUCTION");

        assertThrows(Exception.class, () -> SMallSettingService.getRequest(bootpay, null));
        assertThrows(Exception.class, () -> SMallSettingService.updateRequest(bootpay, new MallSettingUpdateParams()));
    }

    @Test
    public void params가_없으면_예외를_발생시킨다() {
        assertThrows(Exception.class, () -> SMallSettingService.updateRequest(bootpay(), null));
    }
}
