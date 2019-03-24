package com.cqut.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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
import com.cqut.service.ISparepartService;

@Controller
public class SparepartController {
	@Resource
	private ISparepartService service;
	
	/**
	 * 
	 * 方法简述：获取备件的信息
	 * @param row
	 * @param page
	 * @param name
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-1 下午9:24:35
	 */
	@RequestMapping(value="/sparepart/getsparepart",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getSparepart(@RequestParam("row") int row,
			@RequestParam("page")int page,
			@RequestParam("name") String name){
		    Map<String, Object> result = service.getSparepart(row, page, name);
		    if(result == null){
				return "-1";
			}
		    return JSON.toJSON(result).toString();
	}

	
	/**
	 * 
	 * 方法简述：获取自己的备件的信息
	 * @param row
	 * @param page
	 * @param name
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-1 下午9:24:35
	 */
	@RequestMapping(value="/sparepart/getMySparepart",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getMySparepart(@RequestParam("row") int row,
			@RequestParam("page")int page,
			@RequestParam("name") String name){
		    Map<String, Object> result = service.getSparepart(row, page, name);
		    if(result == null){
				return "-1";
			}
		    return JSON.toJSON(result).toString();
	}
	/**
	 * 
	 * 方法简述：获取备件id和名称
	 * @return 
	 * @author huhongjie
	 * @date 2017-10-21 下午2:54:41
	 */
	@RequestMapping(value="/sparepart/getsparts",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	@ResponseBody
	public  String getSparts(){
		 List<Map<String, Object>> result = service.getSparts();
		    if(result == null){
				return "-1";
			}
		    return JSON.toJSON(result).toString();
	}
	
	/**
	 * 
	 * 方法简述：新增一个备件
	 * @param sparepart
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-1 下午9:25:37
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/sparepart/addsparepart",method=RequestMethod.POST,  produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String addSparepart(@RequestBody Map<String,Object> sparepart, HttpServletRequest request){
		Map<String, Object> result = service.addSparepart(sparepart,request.getSession());
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 
	 * 方法简述：修改备件信息
	 * @param id
	 * @param sparepart
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-1 下午9:26:20
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/sparepart/updatesparepart/{id}",method=RequestMethod.PUT,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String updateSparepart(@PathVariable(value = "id") String id,
			@RequestBody Map<String, Object> sparepart){
		Map<String, Object> result = service.updateSparepart(id, sparepart);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 
	 * 方法简述：删除一个或者多个备件
	 * @param id
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-1 下午9:27:42
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/sparepart/deletesparepart/{id}",method=RequestMethod.DELETE,produces="application/json;charset=UTF-8")
	@ResponseBody
	public int deleteSparepart(@PathVariable(value = "id") String id){
		int result = service.deleteSparepart(id);
		return result;
	}
	
	/**
	 * 
	 * 方法简述：获取备件申请记录
	 * @param row
	 * @param page
	 * @param name
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-1 下午10:20:50
	 */
	@RequestMapping(value="/sparepart/getspare_record",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getSpare_Record(@RequestParam("row") int row,
			@RequestParam("page")int page,
			@RequestParam("name") String name){
		    Map<String, Object> result = service.getSpare_Record(row, page, name);
		    if(result == null){
				return "-1";
			}
		    return JSON.toJSON(result).toString();
	}
	
	/**
	 * 
	 * 方法简述：获取任务名及其id
	 * @return 
	 * @author huhongjie
	 * @date 2017-8-4 上午10:55:02
	 */
	@RequestMapping(value="/sparepart/gettaskname",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getTaskName(){
		    List<Map<String, Object>> result = service.getTaskName();
		    if(result == null){
				return "-1";
			}
		    return JSON.toJSON(result).toString();
	}
	
	/**
	 * 
	 * 方法简述：新增一条备件申请记录
	 * @param sparepart_application
	 * @return 
	 * @author lilongshun
	 * @date 2017-7-2 上午10:07:09
	 *{"user_id":"20170701164154959","number":"10","apply_time":"2017-07-02 09:29:51","remark":"备注","sparepartIdList":"","numberList":""}
	 **/
	@RequestMapping(value="/sparepart/addrecord",method=RequestMethod.POST,  produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String addRecord(@RequestBody Map<String,Object> sparepart_application, HttpServletRequest request){
		Map<String, Object> result = service.addRecord(sparepart_application, request.getSession());
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 
	 * 方法简述：修改备件申请记录表
	 * @param id
	 * @param sparepart_id
	 * @param sparepart_application_id
	 * @param sparepart_application
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-2 上午11:11:32
	 */
	@RequestMapping(value="/sparepart/updaterecord",method=RequestMethod.PUT,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String updateRecord(@RequestParam("id") String id,
			@RequestParam("sparepart_id") String sparepart_id,
			@RequestParam("sparepart_application_id") String sparepart_application_id,
			@RequestBody Map<String, Object> sparepart_application){
		Map<String, Object> result = service.updateRecord(id, sparepart_id, sparepart_application_id, sparepart_application);
		return JSONObject.toJSONString(result);
	}
	
	
	/**
	 * 
	 * 方法简述：修改备件申请记录表
	 * @param id
	 * @param sparepart_id
	 * @param sparepart_application_id
	 * @param sparepart_application
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-2 上午11:11:32
	 */
	@RequestMapping(value="/sparepart/auditSparepart/{id}",method=RequestMethod.PUT,produces="application/json;charset=UTF-8")
	@ResponseBody
	public int auditSparepart(@PathVariable("id") String id){
		int result = service.auditSparepart(id);
		return result;
	}
	
	/**
	 * 
	 * 方法简述：删除一条或者多条申请记录表
	 * @param id
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-2 上午11:14:41
	 */
	@RequestMapping(value="/sparepart/deleterecord/{id}",method=RequestMethod.DELETE,produces="application/json;charset=UTF-8")
	@ResponseBody
	public int deleteRecord(@PathVariable(value = "id") String id){
		int result = service.deleteRecord(id);
		return result;
	}
}
