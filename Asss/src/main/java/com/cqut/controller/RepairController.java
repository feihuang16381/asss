package com.cqut.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cqut.clumn.AuthCheckAnnotation;
import com.cqut.service.IRepairService;

@Controller
public class RepairController {
    @Resource
    private IRepairService service;

    /**
     * 
     * 方法简述：
     * 
     * @author tangcailin
     * @date 2017年10月24日下午4:41:46
     */
    @RequestMapping(value = "/repair/getmaterialreplaced", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<Map<String, Object>> getmaterialreplaced() {
        List<Map<String, Object>> list = service.getRepairStatis();
        return list;
    }

    /**
     * 
     * 方法简述：维修统计下的人员工作量，通过部门ID获取当前部门人员和对应的统计量
     * 
     * @author lilongshun
     * @date 2017年10月24日下午4:44:22
     */
    @RequestMapping(value = "/repair/getworkload", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<Map<String, Object>> getWorkload(
            @RequestParam("department_id") String department_id) {
        return service.getWorkload(department_id);
    }
    
	 /**
	  * 提交维修报告	
	  * @param addRepairReport
	  * @param files
	  * @return
	  * @author lilongshun
	  */
	 @RequestMapping(value = "/repairReport/addRepairReport", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	 @ResponseBody
	 public String addRepairReport(@RequestParam("file") MultipartFile[] file,@RequestParam("addRepairReport") String addRepairReport, HttpServletRequest request){
		 if(file == null || file.length < 3)
			 return "文件数量出错";
		 JSONObject json = JSONObject.parseObject(addRepairReport);
		 String result =  service.addRepairReport(json,file,request);
		 return result;
	 }
	 
	 /**
	  * 获取维修记录列表信息	
	  * @param addRepairReport
	  * @param files
	  * @return
	  * @author lilongshun
	  */
	 @RequestMapping(value = "/repairReport/getRepairReport", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	 @ResponseBody
	 public String getRepairReport(@RequestParam("row")  int row,
	            @RequestParam("page") int page,
	            @RequestParam("task_name") String task_name,
	            @RequestParam("terminal_name") String terminal_name,
	            @RequestParam("repair_type") String repair_type,HttpSession session){
		String user_id = (String) session.getAttribute("user_id");
		List<Map<String, Object>> result = service.getRepairReport(row,page,user_id,task_name,terminal_name,repair_type);
		if(result == null)
			return "-1";
		return JSON.toJSON(result).toString();
	 }
	 
	 
	 /**
	  * 获取自己的维修记录列表信息	
	  * @param addRepairReport
	  * @param files
	  * @return
	  * @author lilongshun
	  */
	 @RequestMapping(value = "/repairReport/getMyRepairReport", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	 @ResponseBody
	 public String getMyRepairReport(@RequestParam("row")  int row,
	            @RequestParam("page") int page,
	            @RequestParam("task_name") String task_name,
	            @RequestParam("terminal_name") String terminal_name,
	            @RequestParam("repair_type") String repair_type,HttpSession session){
		String user_id = (String) session.getAttribute("user_id");
		List<Map<String, Object>> result = service.getRepairReport(row,page,user_id,null,null,null);
		if(result == null)
			return "-1";
		return JSON.toJSON(result).toString();
	 }
	 
	 /**
	  * 获取维修记录的详细信息
	  * @param addRepairReport
	  * @param files
	  * @return
	  * @author lilongshun
	  */
	 @RequestMapping(value = "/repairReport/getRepairDetails", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	 @ResponseBody
	 public Map<String, Object> getRepairDetails(@RequestParam("repair_id") String repair_id,@RequestParam("repair_type") String repair_type){
		 return service.getRepairDetails(repair_id,Integer.parseInt(repair_type));
	 }
	 
	 /**
	  * 
	  * 方法简述：删除一条或者多条维护报告
	  * 
	  * @param id
	  * @return
	  * @author lilongshun
	  * @date 2017-7-3 上午9:04:06
	  */
	 @AuthCheckAnnotation(check = true)
	 @RequestMapping(value = "/repair/deleterecord", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	 @ResponseBody
	 public int deleteRecord(@RequestParam(value = "id") String id) {
		 int result = service.deleteRecord(id);
		 return result;
	 }
	 
	 
	 
	 /**
	  * 
	  * 方法简述：获取所有未完成的任务
	  * 
	  * @param id
	  * @return
	  * @author lilongshun
	  * @date 2017-7-3 上午9:04:06
	  */
	 @RequestMapping(value = "/repair/getAllTask", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	 @ResponseBody
	 public List<Map<String, Object>> getAllTask(){
		 List<Map<String, Object>> list = service.getAllTask();
		 return list;
	 }
}
