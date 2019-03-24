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
import com.cqut.service.IProjectService;

@Controller
public class ProjectController {
	@Resource 
	private IProjectService service;
	
	/**
	 * 
	 * 方法简述：获取项目信息
	 * @param row
	 * @param page
	 * @param name
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-1 下午9:07:34
	 */
	@RequestMapping(value="/project/getproject",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getProject(@RequestParam("row") int row,
			@RequestParam("page")int page,
			@RequestParam("name") String name){
		   Map<String, Object> result = service.getProject(row, page, name);
		    if(result == null){
				return "-1";
			}
		    return JSON.toJSON(result).toString();
	}
	
	/**
	 * 
	 * 方法简述：新增一个项目
	 * @param project
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-1 下午9:08:31
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/project/addproject",method=RequestMethod.POST,  produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String addProject(@RequestBody Map<String,Object> project){
		Map<String, Object> result = service.addProject(project);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 
	 * 方法简述：修改项目信息
	 * @param id
	 * @param project
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-1 下午9:10:14
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/project/updateproject/{id}",method=RequestMethod.PUT,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String updateProject(@PathVariable(value = "id") String id,
			@RequestBody Map<String, Object> project){
		Map<String, Object> result = service.updateProject(id, project);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 
	 * 方法简述：删除一个或者多个项目
	 * @param id
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-1 下午9:11:08
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/project/deleteproject/{id}",method=RequestMethod.DELETE,produces="application/json;charset=UTF-8")
	@ResponseBody
	public int deleteProject(@PathVariable(value = "id") String id){
		int result = service.deleteProject(id);
		return result;
	}
}
