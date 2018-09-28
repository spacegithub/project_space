package com.mr.data.api.product;


import com.mr.data.common.constant.BusinessConstant;
import com.mr.data.common.util.CompareUtil;
import com.mr.modules.api.mapper.BlackNameIMapper;
import com.mr.modules.api.model.BlackNameI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.*;

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

    public static P301Product  p301Product;

    //具有详情的原因set
    private static Set<String> detailResonSet = new HashSet<String>();
    static{
        detailResonSet.add(BusinessConstant.LFIC_CODE);//法院失信
        detailResonSet.add(BusinessConstant.ADMP_CODE);//行政处罚
    }

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
    //    List<BlackNameI> list = mapper.findAll(blackNameI);
        List<BlackNameI> list = mapper.findAllByName(blackNameI);
        list = getFinalList(list,personId);
        if(list.size()>0){
            Map retMap = new HashMap();
            retMap.put("blackMatch","1");
            List blackReasons = new ArrayList();
            BlackNameI blackNameITmp = null;
            Map<String,Object> blackDetails = new HashMap<String,Object>();
            for(int i=0;i<list.size();i++){
                blackNameITmp = list.get(i);
                String subject = blackNameITmp.getSubject();
                String blackReason = findReason(subject);
                blackReasons.add(blackReason);
                if(detailResonSet.contains(blackReason)){
                    List detailList = new ArrayList();
                    detailList.add(fillDetailMap(blackNameITmp,blackReason));
                    blackDetails.put(blackReason,detailList);
                }
            }
            retMap.put("blackDetails",blackDetails);
            retMap.put("blackReason",fillReason(blackReasons));
            retObj = retMap;
        }
        log.info("处理完毕...结果为 {}",retObj);
        return retObj;
    }



    public String findReason(String subject){
        String reason = "";
        if(BusinessConstant.FRUAD.contains(subject)){
            reason = BusinessConstant.FRUAD_CODE;
        }else if(BusinessConstant.LFIC.contains(subject)){
            reason = BusinessConstant.LFIC_CODE;
        }else if(BusinessConstant.ADMP.contains(subject)){
            reason = BusinessConstant.ADMP_CODE;
        }
        return reason;
    }

    public Map fillDetailMap(BlackNameI blackNameI,String resson){
        Map map = new HashMap();
        if(resson.equalsIgnoreCase(BusinessConstant.LFIC_CODE)){
            map = fillDetailMapByLFIC(blackNameI);
        }else if(resson.equalsIgnoreCase(BusinessConstant.ADMP_CODE)){
            map = fillDetailMapByADMP(blackNameI);
        }
        return map;
    }

    public String fillReason(List list){
        StringBuilder stringBuilder = new StringBuilder();
        for(int j=0;j<list.size();j++){
            if(stringBuilder.toString().contains((String)list.get(j))){
                continue;
            }
            stringBuilder.append(list.get(j)).append(":");
        }
        String reasons = stringBuilder.toString();
        reasons = reasons.substring(0,reasons.lastIndexOf(":"));
        return reasons;
    }

    /***
     * 法院失信
     * */
    public Map fillDetailMapByLFIC(BlackNameI blackNameI){
        Map map = new HashMap();
        map.put("performance",blackNameI.getPunishReason());
        map.put("publishDate",blackNameI.getPublishDate());
        map.put("duty",blackNameI.getPunishResult());
        map.put("case_no",blackNameI.getJudgeNo());
        map.put("disruptTypeName",BusinessConstant.disruptTypeName);
        return map;
    }
    /**
     * 行政处罚
     * */
    public Map fillDetailMapByADMP(BlackNameI blackNameI){
        Map map = new HashMap();
        map.put("cf_wsh",blackNameI.getJudgeNo());
        map.put("cf_cfmc",blackNameI.getSubject());
        map.put("cf_sy",blackNameI.getPunishReason());
        map.put("cf_jg",blackNameI.getPunishResult());
        map.put("cf_xzjg",blackNameI.getJudgeAuth());
        map.put("reg_date",blackNameI.getPublishDate());
        return map;
    }

    /**
     * 获取相似度,若达到0.75以上，视为同一个
     * */
    public boolean isSame(String idCard,String personId){
       boolean flag = true;
       if(CompareUtil.getSimilarityRatio(idCard,personId)<0.75){
           flag=false;
       }
       return flag;
    }


    /**
     * 获取满足要求的list
     * */
    public List<BlackNameI> getFinalList(List<BlackNameI> list,String idCard){
        List<BlackNameI> finalList = new ArrayList();
        for(int i=0;i<list.size();i++){
            if(isSame(idCard,list.get(i).getPersonId())){
                finalList.add(list.get(i));
            }
        }
        return finalList;
    }

}
