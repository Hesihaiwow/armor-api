package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.dto.response.PlatformResDTO;
import com.zhixinhuixue.armor.model.pojo.Platform;
import com.zhixinhuixue.armor.model.pojo.User;

import java.util.Date;
import java.util.List;

/**
 * @author sch
 * @DATE 2019/6/11 9:52
 */
public class WeekPublishTaskBO {

    /**
     * 计划id
     */
    private Long wppId;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 负责人(创建人)
     */
    private Long createBy;
    private String createByName;

    /**
     * 参与人员id
     */
    private List<Long> userIds;
    private List<User> userList;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 设计截止时间(开发开始时间)
     */
    private Date beginTime;

    /**
     * 开发截止时间(测试开始时间)
     */
    private Date testTime;

    /**
     * 截止时间
     */
    private Date endTime;

    /**
     * 需要发布的平台
     */
    private List<Platform> platforms;

    /**
     * 是否可以上线
     */
    private Integer canOnline;

    /**
     * 任务发布情况
     */
    private String condition;


    /**
     * 是否涉及组卷
     */
    private String zujuan;

    /**
     * 阅卷
     */
    private String yuejuan;

    /**
     * 扫描上传
     */
    private String saomiao;

    /**
     * 学业报告
     */
    private String xueyebaogao;

    /**
     * 产品
     */
    private String chanpin;

    /**
     * 时间测试时间
     */
    private String realTestTime;

    /**
     * 预估上线时间
     */
    private String onlineTime;

    /**
     * 时间上线时间
     */
    private String realOnlineTime;

    /**
     * 专项测试时间
     */
    private String specialTestTime;

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public String getZujuan() {
        return zujuan;
    }

    public void setZujuan(String zujuan) {
        this.zujuan = zujuan;
    }

    public String getYuejuan() {
        return yuejuan;
    }

    public void setYuejuan(String yuejuan) {
        this.yuejuan = yuejuan;
    }

    public String getSaomiao() {
        return saomiao;
    }

    public void setSaomiao(String saomiao) {
        this.saomiao = saomiao;
    }

    public String getXueyebaogao() {
        return xueyebaogao;
    }

    public void setXueyebaogao(String xueyebaogao) {
        this.xueyebaogao = xueyebaogao;
    }

    public String getChanpin() {
        return chanpin;
    }

    public void setChanpin(String chanpin) {
        this.chanpin = chanpin;
    }

    public String getRealTestTime() {
        return realTestTime;
    }

    public void setRealTestTime(String realTestTime) {
        this.realTestTime = realTestTime;
    }

    public String getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(String onlineTime) {
        this.onlineTime = onlineTime;
    }

    public String getRealOnlineTime() {
        return realOnlineTime;
    }

    public void setRealOnlineTime(String realOnlineTime) {
        this.realOnlineTime = realOnlineTime;
    }

    public String getSpecialTestTime() {
        return specialTestTime;
    }

    public void setSpecialTestTime(String specialTestTime) {
        this.specialTestTime = specialTestTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getTestTime() {
        return testTime;
    }

    public void setTestTime(Date testTime) {
        this.testTime = testTime;
    }

    public Long getWppId() {
        return wppId;
    }

    public void setWppId(Long wppId) {
        this.wppId = wppId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }

    public Integer getCanOnline() {
        return canOnline;
    }

    public void setCanOnline(Integer canOnline) {
        this.canOnline = canOnline;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
