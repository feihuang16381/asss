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
import com.cqut.service.IRole_typeService;

@Controller
public class Role_typeCtoller {
	@Resource
	private IRole_typeService service;
	
    /**
     * 
     * 方法简述：获取特定的或所有的角色
     * @param row
     * @param page
     * @param name
     * @return 
     * @author wuzhenwei
     * @date 2017年7月1日 下午8:52:40
     */
	@RequestMapping(value ="/role/allrole",method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getRole(
			@RequestParam(value="row",required=false) int row,
			@RequestParam(value="page",required=false) int page,
			@RequestBody Map<String,String> name){
		
		String type = name.get("role_type");
		Map<String,Object> result = service.getRole(row,page,type);
		if(result == null){
			return "-1"; 
		}
		else{
			return JSONObject.toJSONString(result);
		}
	
	}	

	
	/**
	 * 
	 * 方法简述：新增角色
	 * @param role
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月1日 下午9:04:20
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/role/newrole/{ids}",method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String addRole(
			@PathVariable(value = "ids") String ids,
			@RequestBody Map<String,Object> role){
        System.out.println(role.toString());
		Map<String, Object> result = service.addRole(ids,role);
		
		return JSONObject.toJSONString(result);
	}

	/**
	 * 
	 * 方法简述：删除一个或多个角色
	 * @param roleIds
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月1日 下午9:02:13
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/role/delroles/{roleIds}",method=RequestMethod.DELETE)
	@ResponseBody
	public Map<String, Object> delRoleById(@PathVariable String roleIds){
		Map<String, Object> result = service.delRoleById(roleIds);
		return result;
	}
	
	/**
	 * 
	 * 方法简述：修改角色
	 * @param area
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年6月3日 下午4:29:23
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/role/uprole/{id}",method = RequestMethod.PUT)
	@ResponseBody
	public String upRole(@PathVariable String id,@RequestBody Map<String,Object> role){
		
		Map<String, Object> result = service.upRole(id,role);
		if(result == null){
			return "-1";
		}else{
			return JSONObject.toJSONString(result);
		}
	}
	
	
	/**
	 * 
	 * 方法简述：获取所有的权限
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月3日 上午10:41:35
	 */
	@RequestMapping(value ="/level_ass/levels",method=RequestMethod.GET)
	@ResponseBody
	public String getLevel(){
		List<Map<String,Object>> result = service.getLevel();
		if(result == null){
			return "-1"; 
		}
		else{
			return JSONObject.toJSONString(result);
		}
	}	

	/**
	 * 
	 * 方法简述：获取角色对应的权限
	 * @param role_id
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月3日 上午10:42:06
	 */
	@RequestMapping(value ="/level_ass/oldperm/{role_id}",method=RequestMethod.GET)
	@ResponseBody
	public String getOldPerm(@PathVariable String role_id){
		List<Map<String,Object>> result = service.getOldPerm(role_id);
		if(result == null){
			return "-1"; 
		}
		else{
			return JSONObject.toJSONString(result);
		}
	}
	
	/**
	 * 
	 * 方法简述：删除角色对应的权限
	 * @param ids
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月3日 上午10:42:19
	 */
	@RequestMapping(value ="/level_ass/delPerm/{role_id}/{ids}",method=RequestMethod.DELETE)
	@ResponseBody
	public String delPerm(@PathVariable(value = "role_id") String role_id,
			@PathVariable(value = "ids") String ids){
		Map<String,Object> result = service.delPerm(role_id,ids);
		if(result == null){
			return "-1"; 
		}
		else{
			return JSONObject.toJSONString(result);
		}
	}
	
	/**
	 * 
	 * 方法简述：添加或修改角色对应的权限
	 * @param id
	 * @param level_ids
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月3日 上午10:42:56
	 */
	@RequestMapping(value="/level_ass/{id}/{level_ids}",method = RequestMethod.PUT)
	@ResponseBody
	public String upPermission(@PathVariable(value = "id") String id,
			@PathVariable(value = "level_ids") String level_ids){
		
		List<Map<String,Object>> result = service.upPermission(id,level_ids);
		if(result == null){
			return "-1";
		}else{
			return JSONObject.toJSONString(result);
		}
	}
}
