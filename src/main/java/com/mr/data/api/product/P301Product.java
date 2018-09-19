package com.mr.data.api.product;

import com.mr.data.common.exception.BusiException;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zqzhou
 * @Description: 风险名单
 * @Date: Created in 2018/9/19 17:27
 */
public class P301Product extends BasicApiQuery{

    private static Map<String,String> REQUIRED_QRY_ARGS=new HashMap<String,String>();

    static{
        REQUIRED_QRY_ARGS.put("service_id","service_id");
        REQUIRED_QRY_ARGS.put("idCard","idCard");
        REQUIRED_QRY_ARGS.put("name","name");
        REQUIRED_QRY_ARGS.put("mobile","mobile");
    }

    @Override
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

    @Override
    public Object query(Map map) {
        // 增加处理逻辑
        return null;
    }
}
