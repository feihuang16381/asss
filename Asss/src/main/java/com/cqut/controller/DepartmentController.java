package com.cqut.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import com.cqut.service.IDepartmentService;

@Controller
public class DepartmentController {
	@Resource
	private IDepartmentService service;
	
	/**
	 * 
	 * 方法简述：获取部门信息,并把对应权限的id通过“111，222，333”的形式返回
	 * @param row
	 * @param page
	 * @param name
	 * @return 
	 * @author lilongshun
	 * @date 2017-7-1 下午8:32:25
	 */
	@RequestMapping(value="/department/getdepart",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getDepart(@RequestParam("row") int row,
			@RequestParam("page")int page,
			@RequestParam("name") String name){
		    Map<String, Object> result = service.getDepart(page, row, name);
		    if(result == null){
				return "-1";
			}
		    return JSON.toJSON(result).toString();
	}
	
	/**
	 * 
	 * 方法简述：新增一个部门。并给该部门新增权限
	 * @param department
	 * @return 
	 * @author lilongshun
	 * @date 2017-7-1 下午8:33:43
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/department/adddepart",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String addDepart(@RequestBody Map<String, Object> department){
		Map<String, Object> result = service.addDepart(department);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 
	 * 方法简述：修改一个部门的信息
	 * @param id
	 * @param department
	 * @return 
	 * @author lilongshun
	 * @date 2017-7-1 下午8:35:14
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/department/updatedepart/{id}",method=RequestMethod.PUT,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String updateDepart(@PathVariable(value = "id") String id,
			@RequestBody Map<String, Object> department){
		Map<String, Object> result = service.updateDepart(id, department);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 
	 * 方法简述：删除一个或者多个部门
	 * @param id
	 * @return 
	 * @author lilongshun
	 * @date 2017-7-1 下午8:36:37
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/department/deletedepart/{id}",method=RequestMethod.DELETE,produces="application/json;charset=UTF-8")
	@ResponseBody
	public int deleteDepart(@PathVariable(value = "id") String id){
		int result = service.deleteDepart(id);
		return result;
	}
	
	/**
	 * 获取所有权限列表
	 * @return
	 * @author 李龙顺
	 */
	@RequestMapping(value="/department/getAllPermission",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getAllPermission(){
		List<Map<String, Object>> result = service.getAllPermission();
		    if(result == null){
				return "-1";
			}
		    return JSON.toJSON(result).toString();
	}
	
}
