package com.mr.modules.api.model;

import com.mr.data.common.base.model.BaseEntity;
import java.util.Date;
import javax.persistence.*;

@Table(name = "ent_info_tmp")
public class EntInfoTmp extends BaseEntity {
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
     * 企业名称
     */
    @Column(name = "enterprise_name")
    private String enterpriseName;

    /**
     * 统一社会信用代码/营业执照注册号/组织机构代码/税务登记号
     */
    @Column(name = "enterprise_code")
    private String enterpriseCode;

    /**
     * @return id
     */
    /*public Integer getId() {
        return id;
    }*/

    /**
     * @param id
     */
    /*public void setId(Integer id) {
        this.id = id;
    }*/

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
     * 获取企业名称
     *
     * @return enterprise_name - 企业名称
     */
    public String getEnterpriseName() {
        return enterpriseName;
    }

    /**
     * 设置企业名称
     *
     * @param enterpriseName 企业名称
     */
    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    /**
     * 获取统一社会信用代码/营业执照注册号/组织机构代码/税务登记号
     *
     * @return enterprise_code - 统一社会信用代码/营业执照注册号/组织机构代码/税务登记号
     */
    public String getEnterpriseCode() {
        return enterpriseCode;
    }

    /**
     * 设置统一社会信用代码/营业执照注册号/组织机构代码/税务登记号
     *
     * @param enterpriseCode 统一社会信用代码/营业执照注册号/组织机构代码/税务登记号
     */
    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }
}