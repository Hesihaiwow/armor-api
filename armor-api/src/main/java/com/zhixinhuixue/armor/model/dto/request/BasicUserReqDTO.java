package com.zhixinhuixue.armor.model.dto.request;

/**
 * Created by Akuma on 2018/4/17.
 */
public class BasicUserReqDTO {

    /**
     * IMS系统学校ID
     */
    private Integer schoolId;

    /**
     * 业务库学校ID
     */
    private Integer businessSchoolId;

    /**
     * 助教账号
     */
    private String coachAccount;

    /**
     * 助教ID
     */
    private Integer coachId;

    /**
     * 助教名称
     */
    private String coachName;

    public BasicUserReqDTO(Integer businessSchoolId, String coachAccount){
        this.businessSchoolId = businessSchoolId;
        this.coachAccount = coachAccount;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Integer getCoachId() {
        return coachId;
    }

    public void setCoachId(Integer coachId) {
        this.coachId = coachId;
    }

    public String getCoachAccount() {
        return coachAccount;
    }

    public void setCoachAccount(String coachAccount) {
        this.coachAccount = coachAccount;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public Integer getBusinessSchoolId() {
        return businessSchoolId;
    }

    public void setBusinessSchoolId(Integer businessSchoolId) {
        this.businessSchoolId = businessSchoolId;
    }
}
