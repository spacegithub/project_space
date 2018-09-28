package com.mr.modules.api.mapper;

import com.mr.data.common.base.mapper.BaseMapper;
import com.mr.modules.api.model.BlackNameI;

import java.util.List;

public interface BlackNameIMapper extends BaseMapper<BlackNameI> {

    BlackNameI selectBlackName(BlackNameI blackNameI);

    List<BlackNameI> findAll(BlackNameI t);

    List<BlackNameI> findAllByName(BlackNameI t);

}