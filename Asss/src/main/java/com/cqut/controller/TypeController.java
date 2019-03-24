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

import com.alibaba.fastjson.JSONObject;
import com.cqut.clumn.AuthCheckAnnotation;
import com.cqut.service.ITypeService;

@Controller
public class TypeController {
	@Resource
	private ITypeService service;
	
    /**
     * 
     * 方法简述：获取一条相应的或所有类型信息
     * @param row
     * @param page
     * @param name
     * @return 
     * @author wuzhenwei
     * @date 2017年7月1日 下午10:18:46
     */
	@RequestMapping(value ="/type/alltype",method=RequestMethod.POST)
	@ResponseBody
	public String getType(
			@RequestParam(value="row",required=false) int row,
			@RequestParam(value="page",required=false) int page,
			@RequestBody Map<String,String> name){
		
		String type = name.get("name");
		Map<String,Object> result = service.getType(row,page,type);
		if(result == null){
			return "-1"; 
		}
		else{
			return JSONObject.toJSONString(result);
		}
	
	}	

	
	/**
	 * 
	 * 方法简述：新增管理站点类型
	 * @param type
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月1日 下午10:19:18
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/type/newtype",method=RequestMethod.POST)
	@ResponseBody
	public String addType(@RequestBody Map<String,Object> type){
		Map<String, Object> result = service.addType(type);
		
		return JSONObject.toJSONString(result);
	}

	/**
	 * 
	 * 方法简述：删除一条或多条站点类型信息
	 * @param typeIds
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月1日 下午10:20:09
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/type/deltypes/{typeIds}",method=RequestMethod.DELETE)
	@ResponseBody
	public Map<String, Object> delTypeById(@PathVariable String typeIds){
		Map<String, Object> result = service.delTypeById(typeIds);
		return result;
	}
	
	/**
	 * 
	 * 方法简述：修改某一条站点类型信息
	 * @param id
	 * @param type
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月1日 下午10:20:47
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/type/uptype/{id}",method = RequestMethod.PUT)
	@ResponseBody
	public String upType(@PathVariable String id,@RequestBody Map<String,Object> type){
		
		Map<String, Object> result = service.upType(id,type);
		if(result == null){
			return "-1";
		}else{
			return JSONObject.toJSONString(result);
		}
	}
}
