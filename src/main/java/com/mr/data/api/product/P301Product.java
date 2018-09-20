package com.mr.data.api.product;


import com.mr.modules.api.mapper.BlackNameIMapper;
import com.mr.modules.api.model.BlackNameI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
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
        // 增加处理逻辑
        String personId = (String)map.get("idCard");
        String personName = (String)map.get("realName");
        BlackNameI blackNameI = new BlackNameI();
        blackNameI.setPersonName(personName);
        blackNameI.setPersonId(personId);
        BlackNameI selBlackName =  this.p301Product.blackNameIMapper.selectBlackName(blackNameI); //通过反射得到的对象，blackNameIMapper为null,所以通过这种方式取得blackNameIMapper
        log.info("处理完毕...");
        return selBlackName;
    }
}
