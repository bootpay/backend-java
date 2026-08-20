package kr.co.bootpay.pg.model.response;

import com.google.gson.Gson;

/**
 * Created by ehowlsla on 2018. 5. 29..
 */
public class ResDefault<T> {
    public int status;
    public int error_code;
    public String message;
    /**
     * @deprecated HTTP status code 노출 필드. 성공 여부는 {@code status}/{@code error_code} 로 판단하세요.
     *             다음 메이저 버전에서 제거 예정 — 기존 사용 코드는 그대로 동작합니다.
     */
    @Deprecated
    public int http_status;
    public T data;

    public String toJson() {
        return new Gson().toJson(this);
    }
}
