package com.mr.data.modules.api.site;

/**
 * Created by feng on 18-3-16
 */
public interface ResourceGroup{
	/**
	 * 返回调度结果
	 */
	 Integer start();

	/**
	 * 是否执行结束
	 */
	Boolean isFinish();

	/**
	 * 执行结果
	 */
	Integer getResultCode();

	/**
	 * 返回错误信息
	 */
	String getThrowableInfo();

	/**
	 * 持久化error log 关键字设置
	 */
	void setSaveErrKeyWords(String keyWords);
}
