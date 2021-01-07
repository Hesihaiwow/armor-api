package com.zhixinhuixue.armor.model.dto.request;

import com.zhixinhuixue.armor.model.pojo.OrgIdField;
import org.aspectj.weaver.ast.Or;

import java.util.Date;
import java.util.List;

/**
 * @author SCH
 * @date 2019/2/22 14:56
 *
 * 分页查询考勤记录
 */
public class SignInReqDTO extends OrgIdField{
    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 用户id(被查询用户的id)
     */
    private Long userId;

    /**
     * 用户ids(登录用户所在组的成员id集合)
     */
    private List<Long> userIds;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

}
