package com.cqut.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.enterprise.inject.Any;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cqut.clumn.AuthCheckAnnotation;
import com.cqut.service.IMessageService;

@Controller
public class MessageController {
	@Resource
	private IMessageService service;
	
	
	/**
	 * 
	 * 方法简述：获取公司公告列表
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-1 下午9:52:49
	 */
	@RequestMapping(value="/message/getnoticelist",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getNoticeList(@RequestParam("row") int row,
			@RequestParam("page")int page,
			@RequestParam("name") String name){
		    Map<String, Object> result = service.getNoticeList(row, page, name);
		    if(result == null){
				return "-1";
			}
		    return JSON.toJSON(result).toString();
	}
	
	
	/**
	 * 
	 * 方法简述：获取公司公告列表
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-1 下午9:52:49
	 */
	@RequestMapping(value="/message/DealData",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String DealData(){
		    List<Map<String, Object>> result = service.DealData();
		    if(result == null){
				return "-1";
			}
		    return JSON.toJSON(result).toString();
	}
	
	/**
	 * 
	 * 方法简述：获取公司公告列表
	 * @return 
	 * @author huangfei
	 * @date  2018-7-16 下午5:00
	 */
	@RequestMapping(value="/message/getnoticelist2",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	@ResponseBody
	public  List<Map<String, Object>> getNoticeList2(){
		   List<Map<String, Object>>  result = service.getNoticeList2();
		   
		    return result;
	}
	
	/**
	 * 
	 * 方法简述：领取任务
	 * @return 
	 * @author huangfei
	 * @date  2018-7-19 下午7:00
	 */
	@RequestMapping(value="/message/ReceiveTask",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
	@ResponseBody
	public  int ReceiveTask(String taskID){
		   int  result = service.ReceiveTask(taskID);
		   
		    return result;
	}
	/**
	 * 方法简述
	 * @author huangfei
	 * @date 2018-8-15
	 * 
	 */
	@RequestMapping(value="/message/ReadNotice",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	@ResponseBody
	public int ReadNotice(String noticeID){
		return service.ReadNotice(noticeID);
		
	}
	
	
	/**
	 * 
	 * 方法简述：创建通知
	 * @param notice
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-3 下午7:31:23
	 */
	@RequestMapping(value="/message/addnotice",method=RequestMethod.POST,  produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String addNotice(@RequestBody Map<String,Object> notice){
		Map<String, Object> result = service.addNotice(notice);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 
	 * 方法简述：修改通知
	 * @param id
	 * @param notice
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-3 下午7:52:08
	 */
	@RequestMapping(value="/message/updatenotice/{id}",method=RequestMethod.PUT,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String updateNotice(@PathVariable(value = "id") String id,
			@RequestBody Map<String, Object> notice){
		Map<String, Object> result = service.updateNotice(id, notice);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 
	 * 方法简述：删除通知信息
	 * @param id
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-31 上午10:17:30
	 */
	@RequestMapping(value="/message/deletenotice/{id}",method=RequestMethod.DELETE,produces="application/json;charset=UTF-8")
	@ResponseBody
	public int deleteNotice(@PathVariable(value = "id") String id){
		int result = service.deleteNotice(id);
		return result;
	}
	
	/**
	 * 
	 * 方法简述：web端：获取任务列表
	 * @param row
	 * @param page
	 * @param name
	 * @return 
	 * @author huhongjie
	 * @date 2017-8-2 下午8:26:20
	 */
	@RequestMapping(value="/message/gettasks",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getTasks(@RequestParam("row") int row,
			@RequestParam("page")int page,
			@RequestParam("name") String name){
		    Map<String, Object> result = service.getTasks(row, page, name);
		    if(result == null){
				return "-1";
			}
		    return JSON.toJSON(result).toString();
	}
	
	/**
	 * 
	 * 方法简述：获取任务分配表中参与人员的id
	 * @param task_id
	 * @return 
	 * @author huhongjie
	 * @date 2017-8-2 下午8:56:36
	 */
	@RequestMapping(value="/message/getuser_ids/{task_id}",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getUser_ids(@PathVariable("task_id") String task_id){
		    List<Map<String, Object>> result = service.getUser_ids(task_id);
		    if(result == null){
				return "-1";
			}
		    return JSON.toJSON(result).toString();
	}
	
	/**
	 * 
	 * 方法简述：获取站点信息表的id和名字
	 * @return 
	 * @author huhongjie
	 * @date 2017-8-2 下午9:38:14
	 */
	@RequestMapping(value="/message/getterminalname",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getTerminalName(){
		    List<Map<String, Object>> result = service.getTerminalName();
		    if(result == null){
				return "-1";
			}
		    return JSON.toJSON(result).toString();
	}
	
	/**
	 * 
	 * 方法简述：获取任务列表
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-3 下午9:26:34
	 */
	@RequestMapping(value="/message/gettasklist",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getTaskList(@RequestParam("row") int row,
			@RequestParam("page")int page,
			@RequestParam("user_id") String user_id,
			@RequestParam("name") String name,
			@RequestParam("type") String type,
			@RequestParam("typeNum") int typeNum){
		    Map<String, Object> result = service.getTaskList(row, page, user_id, name, type, typeNum);
		    if(result == null){
				return "-1";
			}
		    return JSON.toJSON(result).toString();
	}
	

	/**
	 * 
	 * 方法简述：获取任务列表
	 * @return 
	 * @author huangfei
	 * @date 2018-7-16 下午5:00
	 */
	@RequestMapping(value="/message/gettasklist2",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getTaskList2(){
		    List<Map<String, Object>> result = service.getTaskList2();
		    if(result == null){
				return "-1";
			}
		    return JSON.toJSON(result).toString();
	}
	
	/**
	 * 
	 * 方法简述：首页自己对应的获取任务列表
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-3 下午9:26:34
	 */
	@RequestMapping(value="/message/getMytasklist",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getMytasklist(@RequestParam("row") int row,
			@RequestParam("page")int page, HttpServletRequest request){
			String user_id = (String) request.getSession().getAttribute("user_id");
		    Map<String, Object> result = service.getTaskList(row, page, user_id, null, null, 0);
		    if(result == null){
				return "-1";
			}
		    return JSON.toJSON(result).toString();
	}
	
	/**
	 * 
	 * 方法简述：新增一条任务
	 * @param user_id
	 * @param task
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-3 下午8:57:51
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/message/addtask",method=RequestMethod.POST,  produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String addTask(@RequestBody Map<String,Object> task, HttpServletRequest request){
		System.out.println(task.toString());
		Map<String, Object> result = service.addTask(task,request);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 
	 * 方法简述：修改任务信息,审核任务,任务审核后不可修改
	 * @param id
	 * @param task
	 * @return 
	 * @author huhongjie
	 * @date 2017-8-3 下午7:41:04
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/message/updatetask/{id}",method=RequestMethod.PUT,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String updateTask(@PathVariable(value = "id") String id,
			@RequestBody Map<String, Object> task,HttpServletRequest request){
		Map<String, Object> result = service.updateTask(id, task,request.getSession());
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 
	 * 方法简述：删除一个或者多个任务
	 * @param id
	 * @return 
	 * @author huhongjie
	 * @date 2017-8-3 下午6:52:31
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/message/deletetask/{id}",method=RequestMethod.DELETE,produces="application/json;charset=UTF-8")
	@ResponseBody
	public int deleteTask(@PathVariable(value = "id") String id, HttpServletRequest request){
		int result = service.deleteTask(id,request.getSession());
		return result;
	}
	
	/**
	 * 
	 * 方法简述：审核任务
	 * @param id
	 * @return 
	 * @author huhongjie
	 * @date 2017-8-3 下午6:52:31
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/message/auditTask/{id}",method=RequestMethod.PUT,produces="application/json;charset=UTF-8")
	@ResponseBody
	public int auditTask(@PathVariable(value = "id") String id){
		int result = service.auditTask(id);
		return result;
	}
}
