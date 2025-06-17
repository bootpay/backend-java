package kr.co.bootpay.store.context;

/**
 * API 요청 시 사용할 컨텍스트 정보를 담는 클래스
 */
public class RequestContext {
    private String role;
    private String token;
    
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
        
        public Builder role(String role) {
            this.role = role;
            return this;
        }
        
        public Builder token(String token) {
            this.token = token;
            return this;
        }
        
        public RequestContext build() {
            return new RequestContext(role, token);
        }
    }
    
    // Getters
    public String getRole() {
        return role;
    }
    
    public String getToken() {
        return token;
    }
    
    // Setters
    public void setRole(String role) {
        this.role = role;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
} 