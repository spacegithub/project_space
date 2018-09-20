package com.mr.data.common.constant;

/**
 * @Author: zqzhou
 * @Description:
 * @Date: Created in 2018/9/19 23:13
 */
public class BusinessConstant {
    /**
     * 成功返回码
     */
    public static final String RTN_CODE_SUCCESS="0";
    public static final String RTN_MSG_SUCCESS="success";
    /**
     * 失败返回码
     */
    public static final String RTN_CODE_FAIL="1";
    public static final String RTN_MSG_FAIL="fail";

    /**
     * 查询状态码 0-查询中，1-查询成功，2-查询失败
     * */
    public static final String QRY_STATUS_Q = "0";
    public static final String QRY_STATUS_S = "1";
    public static final String QRY_STATUS_F = "2";

    /**
     * 风险名单（类型）
     * */
    public static final String FRUAD = "个人不良诚信记录"; //欺诈类
    public static final String LFIC = "失信被执行人"; //法院失信
    public static final String disruptTypeName = "有履行能力而拒不履行生效法律文书确定义务"; //违反条例（默认）


    /**
     * 风险名单-原因码值
     * */
    public static final String FRUAD_CODE = "C03BT004";
    public static final String LFIC_CODE = "C06BT020";



}
