package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.SubscriptionSetting;
import kr.co.bootpay.store.model.pojo.SSubscriptionSetting;
import kr.co.bootpay.store.model.request.ListParams;

/**
 * 구독 설정 모듈.
 *
 * <p>기존 {@code BootpayStore} 에는 이 모듈이 배선되어 있지 않아 도달할 수 없었습니다.
 * 신규 표면에서 노출합니다.</p>
 *
 * @since 3.3.0
 */
public class SubscriptionSettingModule {

    private final SubscriptionSetting delegate;

    public SubscriptionSettingModule(BootpayStore bootpay) {
        this.delegate = new SubscriptionSetting(bootpay);
    }

    /**
     * 구독 설정 목록 조회.
     *
     * @param params 조회 조건
     * @return 구독 설정 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list(ListParams params) throws Exception {
        return CommerceResponses.of(delegate.list(params));
    }

    /**
     * 구독 설정 생성.
     *
     * @param subscriptionSetting 생성할 설정
     * @return 생성 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse create(SSubscriptionSetting subscriptionSetting) throws Exception {
        return CommerceResponses.of(delegate.create(subscriptionSetting));
    }

    /**
     * 구독 설정 상세 조회.
     *
     * @param subscriptionSettingId 구독 설정 id
     * @return 구독 설정
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String subscriptionSettingId) throws Exception {
        return CommerceResponses.of(delegate.detail(subscriptionSettingId));
    }

    /**
     * 구독 설정 수정.
     *
     * @param subscriptionSetting 수정할 설정
     * @return 수정 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse update(SSubscriptionSetting subscriptionSetting) throws Exception {
        return CommerceResponses.of(delegate.update(subscriptionSetting));
    }

    /**
     * 구독 설정 삭제.
     *
     * @param subscriptionSettingId 구독 설정 id
     * @return 삭제 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse delete(String subscriptionSettingId) throws Exception {
        return CommerceResponses.of(delegate.delete(subscriptionSettingId));
    }
}
