package com.mr.data.modules.api.service.impl;

import com.google.common.collect.Lists;
import com.mr.data.common.util.EhCacheUtils;
import com.mr.data.common.util.SpringUtils;
import com.mr.framework.core.date.DateUtil;
import com.mr.framework.core.io.FileUtil;
import com.mr.framework.core.util.StrUtil;
import com.mr.data.modules.api.TaskStatus;
import com.mr.data.modules.api.service.SiteService;
import com.mr.data.modules.api.site.ResourceGroup;
import com.mr.data.modules.api.site.SiteTask;
import com.mr.data.modules.api.site.SiteTaskExtend;
import com.mr.data.modules.api.xls.importfile.FileImportExecutor;
import com.mr.data.modules.api.xls.importfile.domain.MapResult;
import com.mr.data.modules.api.xls.importfile.domain.common.Configuration;
import com.mr.data.modules.api.xls.importfile.domain.common.ImportCell;
import com.mr.data.modules.api.xls.importfile.exception.FileImportException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by feng on 18-3-16
 */

@Service
@Slf4j
public class SiteServiceImpl implements SiteService {

	@Value("${download-dir}")
	private String downloadDir;

	/**
	 * @param groupIndex SiteTask enum index 信息
	 * @param callId     调用ID,系统唯一
	 * @return
	 */
	@Override
	public String startByParams(String groupIndex, String callId, Map mapParams) throws Exception {
		ResourceGroup task = null;

		log.info(String.valueOf(task));
		if (!Objects.isNull(getTask(callId))) {
			log.warn("task exists...");
			return "task exists...";
		}
		String region = (String) mapParams.get("region");
		String date = (String) mapParams.get("publishDate");
		String url = (String) mapParams.get("url");



		try {
			task = (ResourceGroup) SpringUtils.getBean(groupIndex);
		} catch (Exception e) {
			log.warn(e.getMessage());
			return "SiteTask object instance not found";
		}

		task.setSaveErrKeyWords(String.format("%s/%s occur Data_Saving_Error", groupIndex, callId));
		EhCacheUtils.put(callId, task);
		return TaskStatus.getName(task.start());
	}

	@Override
	public String start(String groupIndex, String callId) throws Exception {
		ResourceGroup task = null;

		log.info(String.valueOf(task));
		if (!Objects.isNull(getTask(callId))) {
			log.warn("task exists...");
			return "task exists...";
		}

		try {
			task = (ResourceGroup) SpringUtils.getBean(groupIndex);
		} catch (Exception e) {
			log.warn(e.getMessage());
			return "SiteTask object instance not found";
		}

		task.setSaveErrKeyWords(String.format("%s/%s occur Data_Saving_Error", groupIndex, callId));
		EhCacheUtils.put(callId, task);
		return TaskStatus.getName(task.start());
	}

	public Boolean isFinish(String callId) throws Exception {
		ResourceGroup task = getTask(callId);
		if (Objects.isNull(getTask(callId))) {
			log.warn("task not exists...");
			return false;
		}

		if (task.isFinish()) {
			SiteTask.putFinishQueue(callId);
			return true;
		}

		return false;
	}

	@Override
	public String getResultCode(String callId) throws Exception {
		if (Objects.isNull(getTask(callId))) {
			log.warn("task not exists...");
			return "task not exists...";
		}

		if (!isFinish(callId)) {
			return TaskStatus.CALL_ING.name;
		}
		return TaskStatus.getName(getTask(callId).getResultCode());
	}

	@Override
	public String getThrowableInfo(String callId) throws Exception {
		if (Objects.isNull(getTask(callId))) {
			log.warn("task not exists...");
			return "task not exists...";
		}

		if (!isFinish(callId)) {
			return "executing...";
		}
		return getTask(callId).getThrowableInfo();
	}

	@Override
	public Boolean delSiteTaskInstance(String callId) throws Exception {
		try {
			if (Objects.isNull(getTask(callId))) {
				log.warn("task not exists...");
				return false;
			}
			SiteTask.delSiteTaskInstance(callId);
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private ResourceGroup getTask(String callId) throws Exception {
		return ((ResourceGroup) EhCacheUtils.get(callId));
	}

	@Override
	public String importData(FileInputStream fis, String uploadFilePath) throws Exception {

		/******接受上传的文件并临时保存*****/

		log.info("uploadFlePath:" + uploadFilePath);
		// 截取上传文件的文件名
		String uploadFileName = uploadFilePath.substring(
				uploadFilePath.lastIndexOf('\\') + 1, uploadFilePath.indexOf('.'));
		log.info("multiReq.getFile():" + uploadFileName);
		// 截取上传文件的后缀
		String uploadFileSuffix = uploadFilePath.substring(
				uploadFilePath.indexOf('.') + 1, uploadFilePath.length());
		log.info("uploadFileSuffix:" + uploadFileSuffix);
		FileOutputStream fos = null;
		//文件全路径名
		String fullPath = downloadDir + File.separator + uploadFilePath;
		try {

			fos = new FileOutputStream(new File(downloadDir + File.separator + uploadFilePath));
			byte[] temp = new byte[1024];
			int i = fis.read(temp);
			while (i != -1) {
				fos.write(temp, 0, temp.length);
				fos.flush();
				i = fis.read(temp);
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.warn(uploadFilePath + "upload fail.");
			return "fail";
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		log.info(uploadFilePath + "upload success.");
		/******解析uploadFilePath并导入*****/
		int count = importXlsByNoConfig(fullPath);
		log.info("导入成功条数:{}", count);
		//删除导入的文件
		FileUtil.del(fullPath);
		if (count <= 0) return "fail";
		return "success " + count;
	}

	/**
	 * 把excel导入，变成map
	 *
	 * @throws FileImportException
	 * @throws FileNotFoundException
	 * @throws URISyntaxException
	 */
	private int importXlsByNoConfig(String filePath) throws FileImportException,
			FileNotFoundException,
			URISyntaxException {
		int count = 0;
		File importFile = new File(filePath);
		Configuration configuration = new Configuration();
		try {
			configuration.setStartRowNo(1);
			int i = 1;
			List<ImportCell> importCells = Lists.newArrayList(
//					new ImportCell(i++ ,"PRIMARY_KEY"),

			);
			configuration.setImportCells(importCells);
			configuration.setImportFileType(Configuration.ImportFileType.EXCEL);

			MapResult mapResult = (MapResult) FileImportExecutor.importFile(configuration, importFile, importFile.getName());
			List<Map> maps = mapResult.getResult();
			for (Map<String, Object> map : maps) {


				count++;
			}
		} catch (FileImportException e) {
			log.warn(e.getMessage());
		}
		return count;
	}

	@Override
	public String importICName(FileInputStream fis, String uploadFilePath) throws Exception {

		/******接受上传的文件并临时保存*****/

		log.info("uploadFlePath:" + uploadFilePath);
		// 截取上传文件的文件名
		String uploadFileName = uploadFilePath.substring(
				uploadFilePath.lastIndexOf('\\') + 1, uploadFilePath.indexOf('.'));
		log.info("multiReq.getFile():" + uploadFileName);
		// 截取上传文件的后缀
		String uploadFileSuffix = uploadFilePath.substring(
				uploadFilePath.indexOf('.') + 1, uploadFilePath.length());
		log.info("uploadFileSuffix:" + uploadFileSuffix);
		FileOutputStream fos = null;
		//文件全路径名
		String fullPath = downloadDir + File.separator + uploadFilePath;
		try {

			fos = new FileOutputStream(new File(downloadDir + File.separator + uploadFilePath));
			byte[] temp = new byte[1024];
			int i = fis.read(temp);
			while (i != -1) {
				fos.write(temp, 0, temp.length);
				fos.flush();
				i = fis.read(temp);
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.warn(uploadFilePath + "upload fail.");
			return "fail";
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		log.info(uploadFilePath + "upload success.");
		/******解析uploadFilePath并导入*****/
		int count = updateIcNameByXls(fullPath);
		log.info("导入成功条数:{}", count);
		//删除导入的文件
		FileUtil.del(fullPath);
		if (count <= 0) return "fail";
		return "success " + count;
	}

	/**
	 * 把excel导入，变成map, 更新icName
	 *
	 * @throws FileImportException
	 * @throws FileNotFoundException
	 * @throws URISyntaxException
	 */
	private int updateIcNameByXls(String filePath) throws FileImportException,
			FileNotFoundException,
			URISyntaxException {
		int count = 0;
		File importFile = new File(filePath);
		Configuration configuration = new Configuration();
		try {
			configuration.setStartRowNo(1);
			int i = 0;
			List<ImportCell> importCells = Lists.newArrayList(
					new ImportCell(i++, "url"),
					new ImportCell(i++, "icName")
			);
			configuration.setImportCells(importCells);
			configuration.setImportFileType(Configuration.ImportFileType.EXCEL);
			MapResult mapResult = (MapResult) FileImportExecutor.importFile(configuration, importFile, importFile.getName());
			List<Map> maps = mapResult.getResult();
			for (Map<String, Object> map : maps) {

				count++;
			}
		} catch (FileImportException e) {
			log.warn(e.getMessage());
		}
		return count;
	}
	public int deleteByBizKey(String key){
		return 0;
	}

	public int deleteBySource(String key){
		return 0;

	}
	public int deleteByUrl(String url){
		return 0;
	}

}
