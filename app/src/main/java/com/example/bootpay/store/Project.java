package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.request.TokenPayload;

/**
 * 프로젝트 조회 예제
 * SDK 초기화 시 설정한 application_id와 private_key로 연결된 프로젝트 정보를 확인합니다.
 */
public class Project {
    static BootpayStore bootpay;

    public static void main(String[] args) {
        try {
            // SDK 초기화 - application_id와 private_key 설정
            TokenPayload tokenPayload = new TokenPayload("hxS-Up--5RvT6oU6QJE0JA", "r5zxvDcQJiAP2PBQ0aJjSHQtblNmYFt6uFoEMhti_mg=");
            bootpay = new BootpayStore(tokenPayload, "DEVELOPMENT");

            // 토큰 발급
            getToken();

            // 현재 연결된 프로젝트 정보 조회
            getProjectInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 액세스 토큰 발급
     */
    public static void getToken() {
        try {
            BootpayStoreResponse res = bootpay.getAccessToken();
            if (res.isSuccess()) {
                System.out.println("getToken success: " + res.getData());
            } else {
                System.out.println("getToken false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 현재 연결된 프로젝트 정보 조회
     * SDK가 올바르게 설정되었는지 확인하거나, 프로젝트 정보를 표시할 때 사용합니다.
     *
     * 응답 예시:
     * {
     *   "project_id": "684a6292b0eacea5cd9745ef",
     *   "name": "테스트 쇼핑몰",
     *   "status": 1,
     *   "created_at": "2025-06-12T14:16:02+09:00"
     * }
     */
    public static void getProjectInfo() {
        try {
            BootpayStoreResponse res = bootpay.project.me();
            if (res.isSuccess()) {
                System.out.println("getProjectInfo success: " + res.getData());
                // 프로젝트 정보 확인
                // project_id, name, status 등의 정보를 확인할 수 있습니다.
            } else {
                System.out.println("getProjectInfo false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
