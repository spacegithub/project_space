package com.mr.modules.api.model;

import com.mr.data.common.base.model.BaseEntity;
import java.util.Date;
import javax.persistence.*;

@Table(name = "person_info_tmp")
public class PersonInfoTmp extends BaseEntity {
   /* @Id
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
     * 姓名
     */
    @Column(name = "person_name")
    private String personName;

    /**
     * 证件类型:默认为1-身份证
     */
    @Column(name = "person_type")
    private String personType;

    /**
     * 证件号码
     */
    @Column(name = "person_id")
    private String personId;

    /**
     * 手机号码
     */
    @Column(name = "person_phonenum")
    private String personPhonenum;

    /**
     * @return id
     */
    /*public Integer getId() {
        return id;
    }*/

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
     * 获取姓名
     *
     * @return person_name - 姓名
     */
    public String getPersonName() {
        return personName;
    }

    /**
     * 设置姓名
     *
     * @param personName 姓名
     */
    public void setPersonName(String personName) {
        this.personName = personName;
    }

    /**
     * 获取证件类型:默认为1-身份证
     *
     * @return person_type - 证件类型:默认为1-身份证
     */
    public String getPersonType() {
        return personType;
    }

    /**
     * 设置证件类型:默认为1-身份证
     *
     * @param personType 证件类型:默认为1-身份证
     */
    public void setPersonType(String personType) {
        this.personType = personType;
    }

    /**
     * 获取证件号码
     *
     * @return person_id - 证件号码
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * 设置证件号码
     *
     * @param personId 证件号码
     */
    public void setPersonId(String personId) {
        this.personId = personId;
    }

    /**
     * 获取手机号码
     *
     * @return person_phonenum - 手机号码
     */
    public String getPersonPhonenum() {
        return personPhonenum;
    }

    /**
     * 设置手机号码
     *
     * @param personPhonenum 手机号码
     */
    public void setPersonPhonenum(String personPhonenum) {
        this.personPhonenum = personPhonenum;
    }
}