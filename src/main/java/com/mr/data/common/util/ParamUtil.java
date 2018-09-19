package com.mr.data.common.util;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: zqzhou
 * @Description: 将request参数存到map里
 * @Date: Created in 2018/9/19 15:36
 */
public class ParamUtil {

    public static Map<String,Object> getParamMap(HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, String[]> tempMap = request.getParameterMap();
        Set<String> keys = tempMap.keySet();
        for (String key : keys) {
            byte source [] = request.getParameter(key).getBytes("iso8859-1");
            String modelname = new String (source,"UTF-8");
            resultMap.put(key,modelname);
        }
        return resultMap;
    }
}
