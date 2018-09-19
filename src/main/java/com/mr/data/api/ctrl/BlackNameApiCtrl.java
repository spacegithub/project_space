package com.mr.data.api.ctrl;

import com.mr.data.api.service.BlackNameServie;
import com.mr.data.common.util.ParamUtil;
import com.mr.data.common.util.ProdCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author: zqzhou
 * @Description:迈容黑名单查询内部接口
 * @Date: Created in 2018/9/19 15:27
 */
@RestController
@RequestMapping("/blackNameApi")
@Slf4j
public class BlackNameApiCtrl {
    @Autowired
    public BlackNameServie blackNameServie;

    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public Object query(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object object = null;
        try{
            Map paraMap = ParamUtil.getParamMap(request);
            object = blackNameServie.query(paraMap);
            System.out.println(ProdCheckUtil.checkIsExist(request.getParameter("prod")));
        }catch (Exception e){
            log.error("黑名单查询发生异常");
            throw e;
        }
        return object;
    }
}
