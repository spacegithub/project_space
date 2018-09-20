package com.mr.data.api.ctrl;

import com.mr.data.api.product.BasicApiQuery;
import com.mr.data.api.service.AntiFraudApiService;
import com.mr.data.common.constant.SystemConstant;
import com.mr.data.common.exception.BusiException;
import com.mr.data.common.util.ParamUtil;
import com.mr.data.common.util.ProdCheckUtil;
import com.mr.data.common.util.UUIDUtil;
import com.mr.data.modules.api.xls.util.DateUtil;
import com.mr.framework.json.JSONUtil;
import com.mr.modules.api.mapper.PersonInfoTmpMapper;
import com.mr.modules.api.mapper.QryInfoMapper;
import com.mr.modules.api.model.PersonInfoTmp;
import com.mr.modules.api.model.QryInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
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

    @Autowired
    public QryInfoMapper qryInfoMapper;

    @Autowired
    public PersonInfoTmpMapper personInfoTmpMapper;


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
            BasicApiQuery apiProduct = antiFraudApiService.getApiProduct(prodCode);
            apiProduct.checkArgs(paraMap);

            String tranId = UUIDUtil.getUUID();
            //数据入库
            prepareData(tranId,paraMap);
            //查询
            Object result = apiProduct.query(paraMap);

            if(result!=null){
                rtnObj = apiProduct.createSuccessResultObject(tranId,result);
                updateQryInfo(tranId,rtnObj,SystemConstant.QRY_STATUS_S);
            }else{
                rtnObj = apiProduct.createFailResultObject(tranId);
                updateQryInfo(tranId,rtnObj,SystemConstant.QRY_STATUS_F);
            }

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

    /**
     * 数据入库
     * */
    public void prepareData(String tranId,Map paraMap){
        String qryParam = JSONUtil.toJsonStr(paraMap);
        prepareQryInfo(tranId,qryParam);
        preparePersonInfoTmp(paraMap);
    }

    public void prepareQryInfo(String tranId,String qryParam){
        QryInfo qryInfo = new QryInfo();
        qryInfo.setQryId(tranId);
        qryInfo.setQryParam(qryParam);
        qryInfo.setStatus(SystemConstant.QRY_STATUS_Q);
        qryInfoMapper.insert(qryInfo);
    }

    public void preparePersonInfoTmp(Map paraMap){
        String personName = (String)paraMap.get("realName");
        String personId = (String)paraMap.get("idCard");
        if(personInfoTmpMapper.selectCountByKey(personName,personId)==0){
            PersonInfoTmp personInfoTmp = new PersonInfoTmp();
            personInfoTmp.setPersonName(personName);
            personInfoTmp.setPersonId(personId);
            personInfoTmp.setPersonPhonenum((String)paraMap.get("mobile"));
            personInfoTmp.setPersonType("1"); //身份证
            personInfoTmpMapper.insert(personInfoTmp);
        }
    }


    public void updateQryInfo(String tranId,Object obj,String status){
        QryInfo qryInfo = new QryInfo();
        qryInfo.setQryId(tranId);
        qryInfo.setStatus(status);
        qryInfo.setQryRst(JSONUtil.toJsonStr(obj));
        qryInfo.setUpdatedAt(DateUtil.dateToString(new Date(),"yyyy-MM-dd HH:mm:ss"));
        qryInfoMapper.updateByPrimaryKeySelective(qryInfo);
    }
}
