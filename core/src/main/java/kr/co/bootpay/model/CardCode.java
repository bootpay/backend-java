package kr.co.bootpay.model;


public enum CardCode {
    BC("BC"),
    국민("국민"),
    하나("하나"),
    삼성("삼성"),
    신한("신한"),
    현대("현대"),
    롯데("롯데"),
    신세계한미("신세계한미"),
    시티("시티"),
    농협("농협"),
    수협("수협"),
    신협("신협"),
    우리("우리"),
    광주("광주"),
    제주("제주"),
    전북("전북"),
    기업("기업"),
    VISA("VISA"),
    마스터("마스터"),
    다이너스("다이너스"),
    AMX("AMX"),
    JCB("JCB"),
    DISCOVER("DISCOVER"),
    우체국("우체국"),
    새마을금고("새마을금고"),
    은련("은련"),
    카카오뱅크("카카오뱅크"),
    케이뱅크("케이뱅크"),
    페이코("페이코"),
    저축은행("저축은행");


    private String title;

    CardCode(String title) {
        this.title = title;
    }


    public static CardCode fromString(String title) {
        for (CardCode b : CardCode.values()) {
            if (b.title.equalsIgnoreCase(title)) {
                return b;
            }
        }
        return null;
    }
}
