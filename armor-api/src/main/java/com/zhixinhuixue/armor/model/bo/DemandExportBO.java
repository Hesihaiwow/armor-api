package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.Demand;

import java.util.List;

/**
 * @author sch
 * @time 2020/1/6 14:01
 */
public class DemandExportBO extends Demand {

    /**
     * 点赞人集合
     */
    private List<String> likesPersons;

    /**
     * 已读人数
     */
    private Integer readNum;

    /**
     * 回复人
     */
    private List<String> replyPersons;

    /**
     * 回复内容
     */
    private List<String> replyContents;

    public List<String> getLikesPersons() {
        return likesPersons;
    }

    public void setLikesPersons(List<String> likesPersons) {
        this.likesPersons = likesPersons;
    }

    public Integer getReadNum() {
        return readNum;
    }

    public void setReadNum(Integer readNum) {
        this.readNum = readNum;
    }

    public List<String> getReplyPersons() {
        return replyPersons;
    }

    public void setReplyPersons(List<String> replyPersons) {
        this.replyPersons = replyPersons;
    }

    public List<String> getReplyContents() {
        return replyContents;
    }

    public void setReplyContents(List<String> replyContents) {
        this.replyContents = replyContents;
    }
}
