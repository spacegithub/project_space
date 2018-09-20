package com.mr.data.api.product;

import com.mr.data.common.constant.BusinessConstant;
import com.mr.data.common.exception.BusiException;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zqzhou
 * @Description:
 * @Date: Created in 2018/9/19 17:30
 */
public abstract class BasicApiQuery {

    private static Map<String,String> REQUIRED_QRY_ARGS=new HashMap<String,String>();

    static{
        REQUIRED_QRY_ARGS.put("service_id","service_id");
        REQUIRED_QRY_ARGS.put("idCard","idCard");
        REQUIRED_QRY_ARGS.put("realName","realName");
        REQUIRED_QRY_ARGS.put("mobile","mobile");
    }

    public void checkArgs(Map args) {
        for(Object key:args.keySet()){
            if (!REQUIRED_QRY_ARGS.keySet().contains(key))//只能是其中的入参
                throw new BusiException("查询参数校验不通过,非法参数:"+key);
        }
        for(String required:REQUIRED_QRY_ARGS.keySet()){
            Object vO=args.get(required);
            boolean isStringNotBlank=vO instanceof String?StringUtils.isNotBlank((String)vO):false;//必须是String类型，且非空
            if (!isStringNotBlank){
                throw new BusiException("查询参数校验不通过,未提供有效查询参数值:"+required);
            }

        }
    }



    /**
     * 查询信息
     * @param map
     */
    public abstract Object query(Map map);
    /**
     * 生成成功应答
     * */
    public Object createSuccessResultObject(String transId,Object result){
        Map<String,Object> map =new HashMap<String,Object>();
        map.put("retCode", BusinessConstant.RTN_CODE_SUCCESS);
        map.put("retMsg", BusinessConstant.RTN_MSG_SUCCESS);
        map.put("transId", transId);
        if(result!=null){
            map.put("result", result);
        }
        return map;
    }


    /**
     * 生成失败应答
     * */
    public Object createFailResultObject(String transId){
        Map<String,Object> map =new HashMap<String,Object>();
        map.put("retCode", BusinessConstant.RTN_CODE_FAIL);
        map.put("retMsg", BusinessConstant.RTN_MSG_FAIL);
        map.put("transId", transId);
        return map;
    }
}
