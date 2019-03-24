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
import com.cqut.service.IAreaService;


@Controller
public class AreaController {
	@Resource
	private IAreaService service;
	
	/**
	 * 
	 * 方法简述：获取区域信息
	 * @param row
	 * @param page
	 * @param area_name
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-1 下午8:52:23
	 */
	@RequestMapping(value="/area/getarea",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getArea(@RequestParam("row") int row,
			@RequestParam("page")int page,
			@RequestParam("area_name") String area_name){
		    Map<String, Object> result = service.getArea(row, page, area_name);
		    if(result == null){
				return "-1";
			}
		    return JSON.toJSON(result).toString();
	}
	
	/**
	 * 
	 * 方法简述：新增一个区域
	 * @param area
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-1 下午8:47:40
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/area/addarea",method=RequestMethod.POST,  produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String addArea(@RequestBody Map<String,Object> area){
		Map<String, Object> result = service.addArea(area);
		System.out.println(JSONObject.toJSONString(result));
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 
	 * 方法简述：修改区域信息
	 * @param id
	 * @param area
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-1 下午8:53:43
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/area/updatearea/{id}",method=RequestMethod.PUT,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String updateArea(@PathVariable(value = "id") String id,
			@RequestBody Map<String, Object> area){
		Map<String, Object> result = service.updateArea(id, area);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 
	 * 方法简述：删除一个或者多个区域
	 * @param id
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-1 下午8:57:02
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/area/deletearea/{id}",method=RequestMethod.DELETE,produces="application/json;charset=UTF-8")
	@ResponseBody
	public int deleteArea(@PathVariable(value = "id") String id){
		int result = service.deleteArea(id);
		return result;
	}
}
