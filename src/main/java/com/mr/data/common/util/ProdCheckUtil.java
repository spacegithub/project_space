package com.mr.data.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Author: zqzhou
 * @Description: 产品校验类
 * @Date: Created in 2018/9/19 18:01
 */
@Component
public class ProdCheckUtil {

    public static String prodCode;

    public static boolean checkIsExist(String prod){
        boolean flag = false;
        String[] prods = prodCode.split(",");
        for(int i=0;i<prods.length;i++){
            if(prods[i].equalsIgnoreCase(prod)){
                flag = true;
                break;
            }
        }
        return flag;
    }

    public String getProdCode() {
        return prodCode;
    }

    @Value("${prodCode}")
    public void setProdCode(String prodCode) {
        ProdCheckUtil.prodCode = prodCode;
    }
}
