package com.mr.data.api.service.impl;

import com.mr.data.api.product.BasicApiQuery;
import com.mr.data.api.service.AntiFraudApiService;
import com.mr.data.common.exception.BusiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.util.Map;

/**
 * @Author: zqzhou
 * @Description:反欺诈服务逻辑实现类
 * @Date: Created in 2018/9/19 22:48
 */
@Service
@Slf4j
public class AntiFraudApiServiceImpl implements AntiFraudApiService {
    @Override
    public BasicApiQuery getApiProduct(String prodCode) {
        String fullProdCode = "P"+prodCode;
        String name = "com.mr.data.api.product." + fullProdCode + "Product";
        try {
            Class c = Class.forName(name);
            Constructor con = c.getConstructor();
            BasicApiQuery apiProduct = (BasicApiQuery) con.newInstance();
            return apiProduct;

        } catch (Exception e) {
            BusiException busiex = new BusiException("系统异常,产品处理class错误:"+fullProdCode + "Product");
            log.error("产品处理class错误", fullProdCode + "Product", busiex);
            throw busiex;
        }
    }

}
