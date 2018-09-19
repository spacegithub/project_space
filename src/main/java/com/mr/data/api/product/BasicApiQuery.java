package com.mr.data.api.product;

import com.mr.data.common.constant.SystemConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zqzhou
 * @Description:
 * @Date: Created in 2018/9/19 17:30
 */
public abstract class BasicApiQuery {
    /**
     * 检查业务查询输入
     * @param args
     */
    public abstract void checkArgs(Map args);
    /**
     * 查询信息
     * @param map
     */
    public abstract Object query(Map map);
    /**
     * 生成应答
     * */
    public Object createResultObject(String transId,Object result){
        Map<String,Object> map =new HashMap<String,Object>();
        map.put("retCode", SystemConstant.RTN_CODE_SUCCESS);
        map.put("retMsg", "成功");
        map.put("transId", transId);
        if(result!=null){
            map.put("result", result);
        }
        return map;
    }
}
