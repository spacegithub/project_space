package com.mr.modules.api.mapper;

import com.mr.data.common.base.mapper.BaseMapper;
import com.mr.modules.api.model.EntInfoTmp;
import org.apache.ibatis.annotations.Param;

public interface EntInfoTmpMapper extends BaseMapper<EntInfoTmp> {

    int selectCountByKey(@Param("enterpriseName") String enterpriseName, @Param("enterpriseCode") String enterpriseCode);

}