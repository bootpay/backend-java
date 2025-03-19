package kr.co.bootpay.store.model.pojo;

import java.util.List;

public class SArticle {
    public String articleId;
    public String sellerId;
    public String projectId;
    public String boardId;
    public String parentArticleId;

    public String title;
    public String contentHtml;
    public List<String> images;

    public Boolean useNotice;
    public Boolean useAnonymous;
    public Boolean useDisplay;

    public String userId;
    public String userName;
    public String password;

    public String memberId;
    public Integer readCount;

    public Boolean needReply;
    public Boolean needRead;

    public List<String> searchTags;
}
