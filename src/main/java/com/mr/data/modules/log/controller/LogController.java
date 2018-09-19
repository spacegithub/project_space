package com.mr.data.modules.log.controller;

import com.mr.data.common.base.controller.BaseController;
import com.mr.data.modules.log.model.Log;
import com.mr.data.modules.log.service.LogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author cuiP
 * Created by JK on 2017/5/5.
 */
//@Controller
//@RequestMapping("/admin/log")
public class LogController extends BaseController {

    private static final String BASE_PATH = "admin/log/";

    @Resource
    private LogService logService;


    /**
     * 分页查询日志列表
     *
     * @param pageNum   当前页码
     * @param username  用户名
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param modelMap
     * @return
     */
    @GetMapping
    public String list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            String username, String startTime, String endTime, ModelMap modelMap) {
        try {
//            log.debug("分页查询日志列表参数! pageNum = {}, username = {}, username = {}, startTime = {}, endTime = {}", pageNum, username, startTime, endTime);
//            PageInfo<Log> pageInfo = logService.findPage(pageNum, PAGESIZE, username, startTime, endTime);
//            log.info("分页查询日志列表结果！ pageInfo = {}", pageInfo);
//            modelMap.put("pageInfo", pageInfo);
//            modelMap.put("username", username);
//            modelMap.put("startTime", startTime);
//            modelMap.put("endTime", endTime);
        } catch (Exception e) {
            log.error("分页查询日志列表失败! e = {}", e);
        }
        return BASE_PATH + "log-list";
    }


    /**
     * 根据主键ID删除管理员
     *
     * @param id
     * @return
     */
//    @OperationLog(value = "删除日志")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        try {
            log.debug("删除日志! id = {}", id);

            logService.deleteById(id);
            log.info("删除日志成功! id = {}", id);

            return ResponseEntity.ok("已删除!");
        } catch (Exception e) {
            log.error("删除日志失败! id = {}, e = {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 批量删除日志
     * @param ids
     * @return
     */
//    @OperationLog(value = "批量删除日志")
    @DeleteMapping(value = "/batch/{ids}")
    public ResponseEntity<String> deleteBatch(@PathVariable("ids") List<Object> ids) {
        try {
            log.debug("批量删除日志! ids = {}", ids);

            if (null == ids) {
                log.info("批量删除日志不存在! ids = {}", ids);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            logService.deleteByCondition(Log.class, "id", ids);
            log.info("批量删除日志成功! ids = {}", ids);

            return ResponseEntity.ok("已删除!");
        } catch (Exception e) {
            log.error("批量删除日志失败! ids = {}, e = {}", ids, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 查询日志详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public String view(@PathVariable("id")Long id, ModelMap modelMap){
        Log log = logService.findById(id);

        modelMap.put("model", log);
        return BASE_PATH + "log-view";
    }

}
