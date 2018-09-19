package com.mr.data.modules.api.service;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 18-3-17
 */
public interface SiteService {
	String start(String groupIndex, String callId) throws Exception;

	String startByParams(String groupIndex, String callId, Map mapParams) throws Exception;

	Boolean isFinish(String callId) throws Exception;

	String getResultCode(String callId) throws Exception;

	String getThrowableInfo(String callId) throws Exception;

	Boolean delSiteTaskInstance(String callId) throws Exception;

	public int deleteBySource(String source);

	public int deleteByBizKey(String primaryKey);

	public int deleteByUrl(String url);

	String importData(FileInputStream fis, String uploadFilePath) throws Exception;

	String importICName(FileInputStream fis, String uploadFilePath) throws Exception;
}
