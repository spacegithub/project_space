package com.mr.modules.api.mapper;

import com.mr.data.common.base.mapper.BaseMapper;
import com.mr.modules.api.model.BlackNameI;

public interface BlackNameIMapper extends BaseMapper<BlackNameI> {

    public BlackNameI selectBlackName(BlackNameI blackNameI);
}