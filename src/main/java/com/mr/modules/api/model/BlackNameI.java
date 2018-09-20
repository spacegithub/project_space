package com.mr.modules.api.model;

import com.mr.data.common.base.model.BaseEntity;
import lombok.ToString;

import java.util.Date;
import javax.persistence.*;

@Table(name = "black_name_i")
@ToString
public class BlackNameI extends BaseEntity {
    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;*/

    /**
     * 本条记录创建时间
     */
    @Column(name = "created_at")
    private Date createdAt;

    /**
     * 本条记录最后更新时间
     */
    @Column(name = "updated_at")
    private Date updatedAt;

    /**
     * 数据来源
     */
    private String source;

    /**
     * 主题
     */
    private String subject;

    /**
     * url
     */
    private String url;

    /**
     * 当事人名称
     */
    @Column(name = "person_name")
    private String personName;

    /**
     * 当事人证件类型:默认为1-身份证
     */
    @Column(name = "person_type")
    private String personType;

    /**
     * 当事人证件号码
     */
    @Column(name = "person_id")
    private String personId;

    /**
     * 当事人手机号码
     */
    @Column(name = "person_phonenum")
    private String personPhonenum;

    /**
     * 处罚类型
     */
    @Column(name = "punish_type")
    private String punishType;

    /**
     * 执行文号
     */
    @Column(name = "judge_no")
    private String judgeNo;

    /**
     * 执行时间
     */
    @Column(name = "judge_date")
    private String judgeDate;

    /**
     * 判决机关
     */
    @Column(name = "judge_auth")
    private String judgeAuth;

    /**
     * 发布日期
     */
    @Column(name = "publish_date")
    private String publishDate;

    /**
     * 当前状态
     */
    private String status;

    /**
     * 处罚事由
     */
    @Column(name = "punish_reason")
    private String punishReason;

    /**
     * 处罚依据
     */
    @Column(name = "punish_according")
    private String punishAccording;

    /**
     * 处罚结果
     */
    @Column(name = "punish_result")
    private String punishResult;

    /**
     * @return id
     */
    /*public Integer getId() {
        return id;
    }
*/
    /**
     * @param id
     */
   /* public void setId(Integer id) {
        this.id = id;
    }
*/
    /**
     * 获取本条记录创建时间
     *
     * @return created_at - 本条记录创建时间
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * 设置本条记录创建时间
     *
     * @param createdAt 本条记录创建时间
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 获取本条记录最后更新时间
     *
     * @return updated_at - 本条记录最后更新时间
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 设置本条记录最后更新时间
     *
     * @param updatedAt 本条记录最后更新时间
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * 获取数据来源
     *
     * @return source - 数据来源
     */
    public String getSource() {
        return source;
    }

    /**
     * 设置数据来源
     *
     * @param source 数据来源
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * 获取主题
     *
     * @return subject - 主题
     */
    public String getSubject() {
        return subject;
    }

    /**
     * 设置主题
     *
     * @param subject 主题
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * 获取url
     *
     * @return url - url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置url
     *
     * @param url url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取当事人名称
     *
     * @return person_name - 当事人名称
     */
    public String getPersonName() {
        return personName;
    }

    /**
     * 设置当事人名称
     *
     * @param personName 当事人名称
     */
    public void setPersonName(String personName) {
        this.personName = personName;
    }

    /**
     * 获取当事人证件类型:默认为1-身份证
     *
     * @return person_type - 当事人证件类型:默认为1-身份证
     */
    public String getPersonType() {
        return personType;
    }

    /**
     * 设置当事人证件类型:默认为1-身份证
     *
     * @param personType 当事人证件类型:默认为1-身份证
     */
    public void setPersonType(String personType) {
        this.personType = personType;
    }

    /**
     * 获取当事人证件号码
     *
     * @return person_id - 当事人证件号码
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * 设置当事人证件号码
     *
     * @param personId 当事人证件号码
     */
    public void setPersonId(String personId) {
        this.personId = personId;
    }

    /**
     * 获取当事人手机号码
     *
     * @return person_phonenum - 当事人手机号码
     */
    public String getPersonPhonenum() {
        return personPhonenum;
    }

    /**
     * 设置当事人手机号码
     *
     * @param personPhonenum 当事人手机号码
     */
    public void setPersonPhonenum(String personPhonenum) {
        this.personPhonenum = personPhonenum;
    }

    /**
     * 获取处罚类型
     *
     * @return punish_type - 处罚类型
     */
    public String getPunishType() {
        return punishType;
    }

    /**
     * 设置处罚类型
     *
     * @param punishType 处罚类型
     */
    public void setPunishType(String punishType) {
        this.punishType = punishType;
    }

    /**
     * 获取执行文号
     *
     * @return judge_no - 执行文号
     */
    public String getJudgeNo() {
        return judgeNo;
    }

    /**
     * 设置执行文号
     *
     * @param judgeNo 执行文号
     */
    public void setJudgeNo(String judgeNo) {
        this.judgeNo = judgeNo;
    }

    /**
     * 获取执行时间
     *
     * @return judge_date - 执行时间
     */
    public String getJudgeDate() {
        return judgeDate;
    }

    /**
     * 设置执行时间
     *
     * @param judgeDate 执行时间
     */
    public void setJudgeDate(String judgeDate) {
        this.judgeDate = judgeDate;
    }

    /**
     * 获取判决机关
     *
     * @return judge_auth - 判决机关
     */
    public String getJudgeAuth() {
        return judgeAuth;
    }

    /**
     * 设置判决机关
     *
     * @param judgeAuth 判决机关
     */
    public void setJudgeAuth(String judgeAuth) {
        this.judgeAuth = judgeAuth;
    }

    /**
     * 获取发布日期
     *
     * @return publish_date - 发布日期
     */
    public String getPublishDate() {
        return publishDate;
    }

    /**
     * 设置发布日期
     *
     * @param publishDate 发布日期
     */
    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    /**
     * 获取当前状态
     *
     * @return status - 当前状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置当前状态
     *
     * @param status 当前状态
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取处罚事由
     *
     * @return punish_reason - 处罚事由
     */
    public String getPunishReason() {
        return punishReason;
    }

    /**
     * 设置处罚事由
     *
     * @param punishReason 处罚事由
     */
    public void setPunishReason(String punishReason) {
        this.punishReason = punishReason;
    }

    /**
     * 获取处罚依据
     *
     * @return punish_according - 处罚依据
     */
    public String getPunishAccording() {
        return punishAccording;
    }

    /**
     * 设置处罚依据
     *
     * @param punishAccording 处罚依据
     */
    public void setPunishAccording(String punishAccording) {
        this.punishAccording = punishAccording;
    }

    /**
     * 获取处罚结果
     *
     * @return punish_result - 处罚结果
     */
    public String getPunishResult() {
        return punishResult;
    }

    /**
     * 设置处罚结果
     *
     * @param punishResult 处罚结果
     */
    public void setPunishResult(String punishResult) {
        this.punishResult = punishResult;
    }
}