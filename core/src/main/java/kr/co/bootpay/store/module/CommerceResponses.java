package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;

import java.util.Map;

/**
 * Commerce 기존 응답({@link BootpayStoreResponse})을 통일 응답({@link BootpayResponse})으로 옮기는 헬퍼.
 *
 * <p>기존 응답 객체는 전혀 수정하지 않고 값만 읽어 옮깁니다.</p>
 *
 * @since 3.3.0
 */
public final class CommerceResponses {

    private CommerceResponses() {
    }

    /**
     * @param res 기존 Commerce 응답
     * @return 통일 응답
     */
    @SuppressWarnings("deprecation")
    public static BootpayResponse of(BootpayStoreResponse res) {
        if (res == null) {
            return BootpayResponse.of(false, null, null, "응답이 비어있습니다.", null);
        }
        Map<String, Object> data = res.getData();

        Integer errorCode = data == null ? null : BootpayResponse.toInteger(data.get("error_code"));
        String message = data == null ? null : BootpayResponse.toString(data.get("message"));
        if (message == null) message = res.getError();

        return BootpayResponse.of(res.isSuccess(), data, errorCode, message, res.toHashMap());
    }
}
