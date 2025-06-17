package kr.co.bootpay.store.example;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.TokenPayload;

import java.util.HashMap;

/**
 * Role 관리 사용 예제
 */
public class RoleUsageExample {
    
    public static void main(String[] args) {
        try {
            // 기본 설정
            TokenPayload tokenPayload = new TokenPayload();
            tokenPayload.clientKey = "your_client_key";
            tokenPayload.secretKey = "your_secret_key";
            
            BootpayStore bootpay = new BootpayStore(tokenPayload, "PRODUCTION");
            
            // 기본값 확인 (user)
            System.out.println("기본 role: " + bootpay.getCurrentRole()); // "user"
            
            // 1. 토큰 발급
            bootpay.withToken(); // API를 통해 토큰 발급
            System.out.println("토큰 발급 완료: " + bootpay.hasToken()); // true
            
            // 2. 기본값(user)으로 API 호출 (role을 명시하지 않음)
            HashMap<String, Object> defaultResult = bootpay
                .user.login("user123", "password123"); // 기본값 "user"로 호출
            
            // 3. 5가지 role로 API 호출
            // 일반 사용자
            HashMap<String, Object> userResult = bootpay
                .asUser()
                .user.login("user123", "password123");
            
            // 매니저
            HashMap<String, Object> managerResult = bootpay
                .asManager()
                .order.list(null);
                
            // 파트너
            HashMap<String, Object> partnerResult = bootpay
                .asPartner()
                .product.list(null);
                
            // 벤더
            HashMap<String, Object> vendorResult = bootpay
                .asVendor()
                .invoice.list(null);
                
            // 슈퍼바이저
            HashMap<String, Object> supervisorResult = bootpay
                .asSupervisor()
                .user.list(null);
            
            // 4. 커스텀 role 사용
            HashMap<String, Object> customResult = bootpay
                .withRole("custom_role")
                .product.list(null);
            
            // 5. role 초기화 후 기본값으로 API 호출
            bootpay.clearRole();
            HashMap<String, Object> afterClearResult = bootpay
                .user.detail("user123"); // 기본값 "user"로 호출
            
            System.out.println("초기화 후 role: " + bootpay.getCurrentRole()); // "user"
            System.out.println("모든 API 호출이 성공적으로 완료되었습니다.");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 토큰 발급 및 사용 예제
     */
    public static void demonstrateTokenUsage(BootpayStore bootpay) throws Exception {
        
        // 1. 토큰 발급
        bootpay.withToken();
        System.out.println("토큰 발급 완료: " + bootpay.hasToken());
        
        // 2. 토큰이 발급된 후 API 호출
        HashMap<String, Object> userResult = bootpay
            .asUser()
            .user.detail("user123");
        
        // 3. 토큰 재발급 (만료된 경우)
        bootpay.withToken();
        
        // 4. 다른 role로 API 호출
        HashMap<String, Object> managerResult = bootpay
            .asManager()
            .order.list(null);
    }
    
    /**
     * 5가지 role로 API를 호출하는 예제
     */
    public static void demonstrateRoleUsage(BootpayStore bootpay) throws Exception {
        
        // 토큰 발급
        bootpay.withToken();
        
        // 기본값(user)으로 API 호출
        HashMap<String, Object> defaultResult = bootpay
            .user.list(null); // role을 명시하지 않으면 기본값 "user" 사용
        
        // 1. 일반 사용자 권한
        HashMap<String, Object> userResult = bootpay
            .asUser()
            .user.detail("user123");
        
        // 2. 매니저 권한
        HashMap<String, Object> managerResult = bootpay
            .asManager()
            .order.list(null);
        
        // 3. 파트너 권한
        HashMap<String, Object> partnerResult = bootpay
            .asPartner()
            .product.list(null);
        
        // 4. 벤더 권한
        HashMap<String, Object> vendorResult = bootpay
            .asVendor()
            .invoice.list(null);
        
        // 5. 슈퍼바이저 권한
        HashMap<String, Object> supervisorResult = bootpay
            .asSupervisor()
            .user.list(null);
    }
    
    /**
     * 동일한 인스턴스에서 여러 role을 순차적으로 사용하는 예제
     */
    public static void demonstrateSequentialRoleUsage(BootpayStore bootpay) throws Exception {
        
        // 토큰 발급
        bootpay.withToken();
        
        // 1. 기본값(user)으로 로그인
        HashMap<String, Object> defaultLogin = bootpay
            .user.login("user123", "password123"); // 기본값 "user"로 호출
        
        // 2. 매니저로 주문 관리
        HashMap<String, Object> managerOrder = bootpay
            .asManager()
            .order.list(null);
        
        // 3. 파트너로 상품 관리
        HashMap<String, Object> partnerProduct = bootpay
            .asPartner()
            .product.list(null);
        
        // 4. 벤더로 인보이스 관리
        HashMap<String, Object> vendorInvoice = bootpay
            .asVendor()
            .invoice.list(null);
        
        // 5. 슈퍼바이저로 사용자 관리
        HashMap<String, Object> supervisorUser = bootpay
            .asSupervisor()
            .user.list(null);
        
        // 6. role 초기화 후 기본값으로 API 호출
        bootpay.clearRole();
        HashMap<String, Object> afterClearResult = bootpay
            .user.detail("user123"); // 기본값 "user"로 호출
        
        System.out.println("현재 role: " + bootpay.getCurrentRole()); // "user"
    }
} 