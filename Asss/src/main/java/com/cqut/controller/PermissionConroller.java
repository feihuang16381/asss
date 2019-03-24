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
import com.cqut.service.IPermissionService;

@Controller
public class PermissionConroller {
	@Resource
	private IPermissionService service;
	
    /**
     * 
     * 方法简述：获取特定的或所有的权限信息
     * @param row
     * @param page
     * @return 
     * @author wuzhenwei
     * @date 2017年7月1日 下午10:00:15
     */
	@RequestMapping(value ="/permlev/allLevel",method=RequestMethod.POST)
	@ResponseBody
	public String getPermLev(
			@RequestParam(value="row",required=false) int row,
			@RequestParam(value="page",required=false) int page,
			@RequestBody Map<String,String> name
			){
		
		String perm_name = name.get("permission_name");
		Map<String,Object> result = service.getPermLev(row,page,perm_name);
		if(result == null){
			return "-1"; 
		}
		else{
			return JSONObject.toJSONString(result);
		}
	
	}	

	
	/**
	 * 
	 * 方法简述：新增权限
	 * @param permlev
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月1日 下午10:00:57
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/permlev/newpermlev/{module_ids}",method=RequestMethod.POST)
	@ResponseBody
	public String addPermLev(
			@PathVariable String module_ids,
			@RequestBody Map<String,Object> permlev){
		Map<String, Object> result = service.addPermLev(module_ids,permlev);
		
		return JSONObject.toJSONString(result);
	}

	/**
	 * 
	 * 方法简述：删除某一个或多个权限
	 * @param permlevIds
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月1日 下午10:01:31
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/permlev/delpermlevs/{permlevIds}",method=RequestMethod.DELETE)
	@ResponseBody
	public Map<String, Object> delPermLevById(@PathVariable String permlevIds){
		Map<String, Object> result = service.delPermLevById(permlevIds);
		return result;
	}
	
	/**
	 * 
	 * 方法简述：修改权限
	 * @param id
	 * @param permlev
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月1日 下午10:01:52
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/permlev/uppermlev/{id}",method = RequestMethod.PUT)
	@ResponseBody
	public String upPermLev(
			@PathVariable String id,
			@RequestBody Map<String,Object> permlev){
		
		Map<String, Object> result = service.upPermLev(id,permlev);
		if(result == null){
			return "-1";
		}else{
			return JSONObject.toJSONString(result);
		}
	}
	
	
	/**
	 * 
	 * 方法简述：获取所有的模块并排序
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月3日 上午10:14:11
	 */
	@RequestMapping(value ="/permission/modules",method=RequestMethod.GET)
	@ResponseBody
	public String getMoudle(){
		List<Map<String,Object>> result = service.getMoudle();
		if(result == null){
			return "-1"; 
		}
		else{
			return JSONObject.toJSONString(result);
		}
	}	

	/**
	 * 
	 * 方法简述：获取所给的权限等级所对应的模块
	 * @param level_id
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月3日 上午10:17:26
	 */
	@RequestMapping(value ="/permission/oldperm/{level_id}",method=RequestMethod.GET)
	@ResponseBody
	public String getOldPerm(@PathVariable String level_id){
		List<Map<String,Object>> result = service.getOldPerm(level_id);
		if(result == null){
			return "-1"; 
		}
		else{
			return JSONObject.toJSONString(result);
		}
	}
	
	/**
	 * 
	 * 方法简述：删除所给的权限等级对应所属模块的中间表
	 * @param ids
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月3日 上午10:17:49
	 */
	@RequestMapping(value ="/permission/delPerm/{permission_id}/{ids}",method=RequestMethod.DELETE)
	@ResponseBody
	public String delPerm(
			@PathVariable String permission_id,
			@PathVariable String ids){
		Map<String,Object> result = service.delPerm(permission_id,ids);
		if(result == null){
			return "-1"; 
		}
		else{
			return JSONObject.toJSONString(result);
		}
	}
	
	/**
	 * 
	 * 方法简述：修改权限对应的模块
	 * @param id
	 * @param module_ids
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月5日 下午3:50:51
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/permission/{id}/{module_ids}",method = RequestMethod.PUT)
	@ResponseBody
	public String upPermission(
			@PathVariable String id,
			@PathVariable String module_ids){
		
		List<Map<String,Object>> result = service.upPermission(id,module_ids);
		if(result == null){
			return "-1";
		}else{
			return JSONObject.toJSONString(result);
		}
	}
}
