package kr.co.bootpay.pg.model;


public enum ShippingCompanyCode {

    CJ대한통운("CJ대한통운"),
    한진("한진"),
    롯데("롯데"),
    KGB("KGB"),
    동부("동부"),
    우체국("우체국"),
    로젠("로젠"),
    옐로우캡("옐로우캡"),
    경동("경동"),
    대신("대신"),
    사가와("사가와"),
    일양로지스("일양로지스"),
    호남("호남"),
    합동("합동"),
    천일("천일"),
    편의점("편의점"),
    직접배달("직접배달"),
    퀵서비스("퀵서비스");
    private String title;

    ShippingCompanyCode(String title) {
        this.title = title;
    }


    public static ShippingCompanyCode fromString(String title) {
        for (ShippingCompanyCode b : ShippingCompanyCode.values()) {
            if (b.title.equalsIgnoreCase(title)) {
                return b;
            }
        }
        return null;
    }
}
