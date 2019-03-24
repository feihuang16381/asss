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
import com.cqut.service.IModuleService;

@Controller
public class ModuleController {
	@Resource
	private IModuleService service;
	
    /**
     * 
     * 方法简述：获取所有模块信息 或者相应模块名的信息
     * @param row
     * @param page
     * @param name
     * @return 
     * @author wuzhenwei
     * @date 2017年7月1日 下午9:28:50
     */
	@RequestMapping(value ="/module/allmodule",method=RequestMethod.POST)
	@ResponseBody
	public String getModule(
			@RequestParam(value="row",required=false) int row,
			@RequestParam(value="page",required=false) int page,
			@RequestBody Map<String,String> name){
		
		String name1 = name.get("name");
		Map<String,Object> result = service.getModule(row,page,name1);
		if(result == null){
			return "-1"; 
		}
		else{
			return JSONObject.toJSONString(result);
		}
	
	}	

	
	/**
	 * 
	 * 方法简述：新增模块
	 * @param module
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月1日 下午9:29:35
	 */
	@RequestMapping(value="/module/newmodule",method=RequestMethod.POST)
	@ResponseBody
	public String addModule(@RequestBody Map<String,Object> module){
		Map<String, Object> result = service.addModule(module);
		
		return JSONObject.toJSONString(result);
	}

	/**
	 * 
	 * 方法简述：删除一条或多条数据
	 * @param moduleIds
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月1日 下午9:30:04
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/module/delmodules/{moduleIds}",method=RequestMethod.DELETE)
	@ResponseBody
	public Map<String, Object> delModuleById(@PathVariable String moduleIds){
		Map<String, Object> result = service.delModuleById(moduleIds);
		return result;
	}
	
	/**
	 * 
	 * 方法简述：修改模块信息
	 * @param id
	 * @param module
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月1日 下午9:52:37
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/module/upmodule/{id}",method = RequestMethod.PUT)
	@ResponseBody
	public String upModule(@PathVariable String id,@RequestBody Map<String,Object> module){
		
		Map<String, Object> result = service.upModule(id,module);
		if(result == null){
			return "-1";
		}else{
			return JSONObject.toJSONString(result);
		}
	}
	
	/**
	 * 
	 * 方法简述：修改模块信息
	 * @param id
	 * @param module
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月1日 下午9:52:37
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/module/getFMoudle",method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getFMoudle(){
		List<Map<String, Object>> result = service.getFMoudle();
		return result;
	}
}
