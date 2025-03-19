package kr.co.bootpay.store.model.pojo;

public class SBoard {
    public String boardId;
    public String sellerId;
    public String projectId;

    public String name;
    public String domain;
    public String description;
    public Integer type;
    public Integer generatedType;

    public Boolean useAuthMember;
    public Boolean useNonAuthMember;
    public Boolean useAnonymous;
    public Boolean useReply;
    public Boolean useFile;
    public Boolean useListImage;

    public Integer status;
}
