package com.mr.data.api.ctrl;

import com.mr.data.api.product.BasicApiQuery;
import com.mr.data.api.service.AntiFraudApiService;
import com.mr.data.common.exception.BusiException;
import com.mr.data.common.util.ParamUtil;
import com.mr.data.common.util.ProdCheckUtil;
import com.mr.data.common.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.mr.data.common.util.ProdCheckUtil.prodCode;

/**
 * @Author: zqzhou
 * @Description:迈容反欺诈服务内部接口
 * @Date: Created in 2018/9/19 17:15
 */
@RestController
@RequestMapping("/antifraudapi")
@Slf4j
public class AntiFraudCtrl {
    @Autowired
    private AntiFraudApiService antiFraudApiService;

    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public Object query(HttpServletRequest request, HttpServletResponse response){
        Object rtnObj = null;
        try{
            Map paraMap = ParamUtil.getParamMap(request);
            String service_id = (String)paraMap.get("service_id");
            //检查产品是否存在
            if(!ProdCheckUtil.checkIsExist(service_id)){
                throw new BusiException("产品校验不通过,无该产品:"+service_id);
            }
            //检查产品的查询参数是否有误
            BasicApiQuery apiProduct = antiFraudApiService
                    .getApiProduct(prodCode);
            apiProduct.checkArgs(paraMap);

            String tranId = UUIDUtil.getUUID();
            Object result = apiProduct.query(paraMap);
            rtnObj = apiProduct.createResultObject(tranId,result);
        }catch (Exception e){
            log.error("黑名单查询发生异常");
            try {
                throw e;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        return rtnObj;
    }
}
