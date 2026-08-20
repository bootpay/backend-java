package kr.co.bootpay.store.context;

/**
 * API 요청 시 사용할 컨텍스트 정보를 담는 클래스
 */
public class RequestContext {
    private String role;
    private String token;
    private String idempotencyKey;
    private String userJwt;

    public RequestContext() {}

    public RequestContext(String role) {
        this.role = role;
    }

    public RequestContext(String role, String token) {
        this.role = role;
        this.token = token;
    }

    // Builder 패턴
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String role;
        private String token;
        private String idempotencyKey;
        private String userJwt;

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder idempotencyKey(String idempotencyKey) {
            this.idempotencyKey = idempotencyKey;
            return this;
        }

        public Builder userJwt(String userJwt) {
            this.userJwt = userJwt;
            return this;
        }

        public RequestContext build() {
            RequestContext context = new RequestContext(role, token);
            context.idempotencyKey = idempotencyKey;
            context.userJwt = userJwt;
            return context;
        }
    }

    /**
     * Idempotency-Key 값을 결정한다 — 지정된 값이 있으면 그대로, 없으면 UUID 자동 생성.
     * (nodejs SDK 의 `idempotencyKey || randomUUID()` 와 동일 동작)
     */
    public static String idempotencyKeyOrGenerate(String idempotencyKey) {
        if (idempotencyKey != null && !idempotencyKey.isEmpty()) return idempotencyKey;
        return java.util.UUID.randomUUID().toString();
    }

    // Getters
    public String getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public String getUserJwt() {
        return userJwt;
    }

    // Setters
    public void setRole(String role) {
        this.role = role;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public void setUserJwt(String userJwt) {
        this.userJwt = userJwt;
    }
}