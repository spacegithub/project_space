package com.mr.data.modules.log.service.impl;
import com.mr.data.common.base.service.impl.BaseServiceImpl;
import com.mr.data.modules.log.model.Log;
import com.mr.data.modules.log.service.LogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author cuiP
 * Created by JK on 2017/4/27.
 */
@Transactional
@Service
public class LogServiceImpl extends BaseServiceImpl<Log> implements LogService{

}
