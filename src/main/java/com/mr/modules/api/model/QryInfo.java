package com.mr.modules.api.model;


import lombok.ToString;

import java.util.Date;
import javax.persistence.*;

@Table(name = "qry_info")
@ToString
public class QryInfo {

    @Id
    @Column(name = "qry_id")
    private String qryId;

    /**
     * 本条记录创建时间
     */
    @Column(name = "created_at")
    private Date createdAt;

    /**
     * 本条记录更新时间
     */
    @Column(name = "updated_at")
    private String updatedAt;

    /**
     * 查询参数-json格式
     */
    @Column(name = "QRY_PARAM")
    private String qryParam;

    /**
     * 查询结果-json格式
     */
    @Column(name = "QRY_RST")
    private String qryRst;

    /**
     * 查询状态：0-查询中，1-查询成功，2-查询失败
     */
    private String status;

    /**
     * @return qry_id
     */
    public String getQryId() {
        return qryId;
    }

    /**
     * @param qryId
     */
    public void setQryId(String qryId) {
        this.qryId = qryId;
    }

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
     * 获取本条记录更新时间
     *
     * @return updated_at - 本条记录更新时间
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 设置本条记录更新时间
     *
     * @param updatedAt 本条记录更新时间
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * 获取查询参数-json格式
     *
     * @return QRY_PARAM - 查询参数-json格式
     */
    public String getQryParam() {
        return qryParam;
    }

    /**
     * 设置查询参数-json格式
     *
     * @param qryParam 查询参数-json格式
     */
    public void setQryParam(String qryParam) {
        this.qryParam = qryParam;
    }

    /**
     * 获取查询结果-json格式
     *
     * @return QRY_RST - 查询结果-json格式
     */
    public String getQryRst() {
        return qryRst;
    }

    /**
     * 设置查询结果-json格式
     *
     * @param qryRst 查询结果-json格式
     */
    public void setQryRst(String qryRst) {
        this.qryRst = qryRst;
    }

    /**
     * 获取查询状态：0-查询中，1-查询成功，2-查询失败
     *
     * @return status - 查询状态：0-查询中，1-查询成功，2-查询失败
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置查询状态：0-查询中，1-查询成功，2-查询失败
     *
     * @param status 查询状态：0-查询中，1-查询成功，2-查询失败
     */
    public void setStatus(String status) {
        this.status = status;
    }
}