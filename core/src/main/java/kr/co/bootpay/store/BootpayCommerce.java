package kr.co.bootpay.store;

import kr.co.bootpay.common.BootpayMode;
import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.common.BootpayRole;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.module.CartModule;
import kr.co.bootpay.store.module.CategoryModule;
import kr.co.bootpay.store.module.CommerceResponses;
import kr.co.bootpay.store.module.CouponModule;
import kr.co.bootpay.store.module.InvoiceModule;
import kr.co.bootpay.store.module.MallSettingModule;
import kr.co.bootpay.store.module.OrderCancelModule;
import kr.co.bootpay.store.module.OrderModule;
import kr.co.bootpay.store.module.OrderSubscriptionAdjustmentModule;
import kr.co.bootpay.store.module.OrderSubscriptionBillModule;
import kr.co.bootpay.store.module.OrderSubscriptionModule;
import kr.co.bootpay.store.module.OrderSubscriptionRequestModule;
import kr.co.bootpay.store.module.PointModule;
import kr.co.bootpay.store.module.ProductModule;
import kr.co.bootpay.store.module.ProjectModule;
import kr.co.bootpay.store.module.StoreModule;
import kr.co.bootpay.store.module.SubscriptionSettingModule;
import kr.co.bootpay.store.module.UserGroupModule;
import kr.co.bootpay.store.module.UserModule;
import kr.co.bootpay.store.module.WebhookModule;

/**
 * Commerce API 진입점 (3.3.0~).
 *
 * <p>PG 의 {@code Bootpay} 와 같은 형태 — 빌더로 생성하고, 모듈로 호출하고,
 * {@link BootpayResponse} 로 응답받습니다.</p>
 *
 * <pre>{@code
 * BootpayCommerce bootpay = BootpayCommerce.builder()
 *         .clientKey(clientKey)
 *         .secretKey(secretKey)
 *         .mode(BootpayMode.PRODUCTION)
 *         .role(BootpayRole.USER)
 *         .build();
 *
 * BootpayResponse res = bootpay.user.list(params);
 * if (res.isSuccess()) {
 *     System.out.println(res.getData());
 * }
 * }</pre>
 *
 * <p>Commerce API 요청은 client_key/secret_key Basic 인증을 사용한다. {@link #issueAccessToken()}은
 * 기존 호출 흐름과 토큰 조회 호환성을 위해 유지하지만 일반 요청의 인증 방식은 바꾸지 않는다.</p>
 *
 * <p>기존 {@link BootpayStore} 는 아무 영향 없이 그대로 사용할 수 있습니다. 이 클래스는 상속이 아니라
 * 위임으로 구현되어 있어, 기존 클래스의 동작을 어떤 방식으로도 바꾸지 않습니다.</p>
 *
 * @since 3.3.0
 */
public class BootpayCommerce {

    private final BootpayStore delegate;

    /** 가맹점 정보. */
    public final StoreModule store;

    /** 프로젝트. */
    public final ProjectModule project;

    /** 사용자. */
    public final UserModule user;

    /** 사용자 그룹. */
    public final UserGroupModule userGroup;

    /** 상품. */
    public final ProductModule product;

    /** 청구서. */
    public final InvoiceModule invoice;

    /** 주문. */
    public final OrderModule order;

    /** 주문 취소. */
    public final OrderCancelModule orderCancel;

    /** 정기구독. */
    public final OrderSubscriptionModule orderSubscription;

    /** 정기구독 청구. */
    public final OrderSubscriptionBillModule orderSubscriptionBill;

    /** 정기구독 조정. */
    public final OrderSubscriptionAdjustmentModule orderSubscriptionAdjustment;

    /** 정기구독 요청 이력. */
    public final OrderSubscriptionRequestModule orderSubscriptionRequest;

    /** 구독 설정. */
    public final SubscriptionSettingModule subscriptionSetting;

    /** 카테고리. */
    public final CategoryModule category;

    /** 쿠폰. */
    public final CouponModule coupon;

    /** 포인트. */
    public final PointModule point;

    /** 장바구니. */
    public final CartModule cart;

    /** 몰 설정. */
    public final MallSettingModule mallSetting;

    /** 웹훅. */
    public final WebhookModule webhook;

    BootpayCommerce(String clientKey, String secretKey, BootpayMode mode, BootpayRole role) {
        this.delegate = new BootpayStore(new TokenPayload(clientKey, secretKey), mode.value());
        this.delegate.setRole(role.value());

        this.store = new StoreModule(delegate);
        this.project = new ProjectModule(delegate);
        this.user = new UserModule(delegate);
        this.userGroup = new UserGroupModule(delegate);
        this.product = new ProductModule(delegate);
        this.invoice = new InvoiceModule(delegate);
        this.order = new OrderModule(delegate);
        this.orderCancel = new OrderCancelModule(delegate);
        this.orderSubscription = new OrderSubscriptionModule(delegate);
        this.orderSubscriptionBill = new OrderSubscriptionBillModule(delegate);
        this.orderSubscriptionAdjustment = new OrderSubscriptionAdjustmentModule(delegate);
        this.orderSubscriptionRequest = new OrderSubscriptionRequestModule(delegate);
        this.subscriptionSetting = new SubscriptionSettingModule(delegate);
        this.category = new CategoryModule(delegate);
        this.coupon = new CouponModule(delegate);
        this.point = new PointModule(delegate);
        this.cart = new CartModule(delegate);
        this.mallSetting = new MallSettingModule(delegate);
        this.webhook = new WebhookModule(delegate);
    }

    /**
     * 생성 빌더를 반환합니다.
     *
     * @return 빌더
     */
    public static BootpayCommerceBuilder builder() {
        return new BootpayCommerceBuilder();
    }

    /**
     * 액세스 토큰을 발급하고 인스턴스에 설정합니다.
     *
     * <p>기존 토큰 조회 흐름과의 호환성을 위해 유지한다. 일반 Commerce 요청은 이미
     * client_key/secret_key Basic 인증을 사용한다.</p>
     *
     * @return 발급 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse issueAccessToken() throws Exception {
        return CommerceResponses.of(delegate.getAccessToken());
    }

    /**
     * 발급된 토큰이 있는지 확인합니다. 일반 Commerce 요청의 인증 방식에는 영향을 주지 않습니다.
     *
     * @return 토큰 보유 여부
     */
    public boolean hasToken() {
        String token = delegate.getToken();
        return token != null && !token.isEmpty();
    }

    /**
     * {@code BOOTPAY-ROLE} 헤더 값을 변경합니다.
     *
     * @param role 변경할 role
     * @return this (메서드 체이닝 지원)
     */
    public BootpayCommerce role(BootpayRole role) {
        delegate.setRole((role == null ? BootpayRole.USER : role).value());
        return this;
    }

    /**
     * 현재 설정된 role 을 반환합니다.
     *
     * @return 현재 role
     */
    public BootpayRole role() {
        return BootpayRole.of(delegate.getRole());
    }

    /**
     * 내부에서 사용 중인 기존 {@link BootpayStore} 인스턴스를 반환합니다.
     *
     * <p>신규 표면에 아직 없는 기존 메서드를 써야 할 때의 탈출구입니다. 같은 인스턴스이므로
     * 토큰과 role 이 공유됩니다.</p>
     *
     * @return 내부 인스턴스
     */
    public BootpayStore unwrap() {
        return delegate;
    }
}
