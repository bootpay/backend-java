package kr.co.bootpay.store.service.invoices;

import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.ListParams;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.invoice.InvoiceListParams;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SInvoiceServiceTest {
    private BootpayStoreObject bootpay() throws Exception {
        BootpayStoreObject bootpay = new BootpayStoreObject(new TokenPayload("test_client_key", "test_secret_key"), "PRODUCTION");
        bootpay.setTokenFromAPI("test_access_token");
        return bootpay;
    }

    @Test
    public void 목록은_GET_invoices로_요청한다() throws Exception {
        HttpGet get = SInvoiceService.listRequest(bootpay(), null);

        assertEquals("https://api.bootapi.com/v1/invoices", get.getURI().toString());
        assertNull(get.getURI().getQuery());
    }

    @Test
    public void 기본_ListParams도_그대로_사용할_수_있다() throws Exception {
        ListParams params = new ListParams();
        params.keyword = "청구서";
        params.page = 1;
        params.limit = 24;

        String query = SInvoiceService.listRequest(bootpay(), params).getURI().getQuery();

        assertTrue(query.contains("keyword=청구서"));
        assertTrue(query.contains("page=1"));
        assertTrue(query.contains("limit=24"));
    }

    @Test
    public void InvoiceListParams는_추가_필터도_snake_case로_전송한다() throws Exception {
        InvoiceListParams params = new InvoiceListParams();
        params.page = 2;
        params.limit = 24;
        params.csType = "cancel";
        params.userId = "user_id";
        params.productType = 1;
        params.cssAt = "2026-08-01";
        params.cseAt = "2026-08-31";

        String query = SInvoiceService.listRequest(bootpay(), params).getURI().getQuery();

        assertTrue(query.contains("page=2"));
        assertTrue(query.contains("limit=24"));
        assertTrue(query.contains("cs_type=cancel"));
        assertTrue(query.contains("user_id=user_id"));
        assertTrue(query.contains("product_type=1"));
        assertTrue(query.contains("css_at=2026-08-01"));
        assertTrue(query.contains("cse_at=2026-08-31"));

        // 값이 설정되지 않은 필드는 전송하지 않는다 (ruby의 compact 동작)
        assertFalse(query.contains("keyword"));
        assertFalse(query.contains("&type="));
    }

    @Test
    public void 토큰이_없으면_예외를_발생시킨다() {
        BootpayStoreObject bootpay = new BootpayStoreObject(new TokenPayload("test_client_key", "test_secret_key"), "PRODUCTION");

        assertThrows(Exception.class, () -> SInvoiceService.listRequest(bootpay, new InvoiceListParams()));
    }
}
