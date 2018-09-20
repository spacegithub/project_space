package com.mr.data.api.product;


import com.mr.data.common.constant.BusinessConstant;
import com.mr.modules.api.mapper.BlackNameIMapper;
import com.mr.modules.api.model.BlackNameI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zqzhou
 * @Description: 风险名单
 * @Date: Created in 2018/9/19 17:27
 */
@Component
@Slf4j
public class P301Product extends BasicApiQuery{

    @Autowired
    public BlackNameIMapper blackNameIMapper;

    public static P301Product  p301Product ;

    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        p301Product = this;
        p301Product.blackNameIMapper = this.blackNameIMapper;
    }

    @Override
    public Object query(Map map) {
        log.info("开始查询风险名单,参数为： {}",map.toString());
        Object retObj = null;
        //通过反射得到的对象，blackNameIMapper为null,所以通过这种方式取得blackNameIMapper
        BlackNameIMapper mapper = this.p301Product.blackNameIMapper;

        String personId = (String)map.get("idCard");
        String personName = (String)map.get("realName");
        BlackNameI blackNameI = new BlackNameI();
        blackNameI.setPersonName(personName);
        blackNameI.setPersonId(personId);
        List<BlackNameI> list = mapper.findAll(blackNameI);
        if(list.size()>0){
            Map retMap = new HashMap();
            retMap.put("blackMatch","1");
            List blackReasons = new ArrayList();
            BlackNameI blackNameITmp = null;
            for(int i=0;i<list.size();i++){
                blackNameITmp = list.get(i);
                String subject = blackNameITmp.getSubject();
                String blackReason = findReason(subject);
                blackReasons.add(blackReason);
                if(blackReason.equalsIgnoreCase(BusinessConstant.LFIC_CODE)){
                    List detailList = new ArrayList();
                    detailList.add(fillDetailMap(blackNameITmp));
                    Map<String,Object> blackDetails = new HashMap<String,Object>();
                    blackDetails.put(blackReason,detailList);
                    retMap.put("blackDetails",blackDetails);
                }
            }
            retMap.put("blackReason",fillReason(blackReasons));
            retObj = retMap;
        }
        log.info("处理完毕...");
        return retObj;
    }



    public String findReason(String subject){
        String reason = "";
        if(BusinessConstant.FRUAD.contains(subject)){
            reason = BusinessConstant.FRUAD_CODE;
        }else if(BusinessConstant.LFIC.contains(subject)){
            reason = BusinessConstant.LFIC_CODE;
        }
        return reason;
    }

    public Map fillDetailMap(BlackNameI blackNameI){
        Map map = new HashMap();
        map.put("performance",blackNameI.getPunishReason());
        map.put("publishDate",blackNameI.getPublishDate());
        map.put("duty",blackNameI.getPunishResult());
        map.put("disruptTypeName",BusinessConstant.disruptTypeName);
        return map;
    }

    public String fillReason(List list){
        StringBuilder stringBuilder = new StringBuilder();
        for(int j=0;j<list.size();j++){
            stringBuilder.append(list.get(j)).append(":");
        }
        String reasons = stringBuilder.toString();
        reasons = reasons.substring(0,reasons.lastIndexOf(":"));
        return reasons;
    }
}
