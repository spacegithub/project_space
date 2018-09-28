package com.mr.data.common.test;

import com.mr.data.common.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zqzhou
 * @Description:风险名单测试类
 * @Date: Created in 2018/9/28 17:03
 */
@Slf4j
public class P301ProductClientTest {

    @Test
    public void test(){
        Map<String,String> paraMap = new HashMap();
        paraMap.put("service_id","301");
        paraMap.put("idCard","422801195910310212");
        paraMap.put("realName","王进");
        paraMap.put("mobile","18521301109");

        String url = "http://localhost:8083/bigData/antifraudapi/query";

        try {
            String result = HttpClientUtil.sendMapByPost(paraMap,url);
            log.info("结果为：");
            log.info(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
