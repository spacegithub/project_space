package com.mr.modules.api.mapper;

import com.mr.data.common.base.mapper.BaseMapper;
import com.mr.modules.api.model.PersonInfoTmp;
import org.apache.ibatis.annotations.Param;

public interface PersonInfoTmpMapper extends BaseMapper<PersonInfoTmp> {

    int selectCountByKey(@Param("personName") String personName, @Param("personId") String personId);

}