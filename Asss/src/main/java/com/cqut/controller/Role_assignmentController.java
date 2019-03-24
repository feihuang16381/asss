package com.cqut.controller;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.cqut.service.IRole_assignmentService;

@Controller
public class Role_assignmentController {
	@Resource
	private IRole_assignmentService service;
	
    /**
     * 
     * 方法简述：获取所有的角色
     * @return 
     * @author wuzhenwei
     * @date 2017年7月3日 上午9:10:30
     */
	@RequestMapping(value ="/role_ass/roles",method=RequestMethod.GET)
	@ResponseBody
	public String getRole(){
		List<Map<String,Object>> result = service.getRole();
		if(result == null){
			return "-1"; 
		}
		else{
			return JSONObject.toJSONString(result);
		}
	}	

	
//	/**
//	 * 
//	 * 方法简述：获取所有的用户
//	 * @return 
//	 * @author wuzhenwei
//	 * @date 2017年7月3日 上午9:11:53
//	 */
//	@RequestMapping(value ="/role_ass/users",method=RequestMethod.GET)
//	@ResponseBody
//	public String getOperator(){
//		List<Map<String,Object>> result = service.getOperator();
//		if(result == null){
//			return "-1"; 
//		}
//		else{
//			return JSONObject.toJSONString(result);
//		}
//	}	

	/**
	 * 
	 * 方法简述：获取选中用户对应的角色
	 * @param user_id
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月3日 上午9:48:41
	 */
	@RequestMapping(value ="/role_ass/oldperm/{user_id}",method=RequestMethod.GET)
	@ResponseBody
	public String getOldPerm(@PathVariable String user_id){
		List<Map<String,Object>> result = service.getOldPerm(user_id);
		if(result == null){
			return "-1"; 
		}
		else{
			return JSONObject.toJSONString(result);
		}
	}
	
	/**
	 * 
	 * 方法简述：删除选中用户对应的角色
	 * @param ids
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月3日 上午9:56:33
	 */
	@RequestMapping(value ="/role_ass/delPerm/{user_id}/{ids}",method=RequestMethod.DELETE)
	@ResponseBody
	public String delPerm(
			@PathVariable String user_id,
			@PathVariable String ids){
		Map<String,Object> result = service.delPerm(user_id,ids);
		if(result == null){
			return "-1"; 
		}
		else{
			return JSONObject.toJSONString(result);
		}
	}
	
	/**
	 * 
	 * 方法简述：添加选中用户对应的角色
	 * @param id
	 * @param role_ids
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月3日 上午9:25:01
	 */
	@RequestMapping(value="/role_ass/{user_id}/{role_ids}",method = RequestMethod.PUT)
	@ResponseBody
	public String upPermission(@PathVariable String user_id,
			@PathVariable String role_ids){
		
		List<Map<String,Object>> result = service.upPermission(user_id,role_ids);
		if(result == null){
			return "-1";
		}else{
			return JSONObject.toJSONString(result);
		}
	}
}
