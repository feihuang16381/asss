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
import com.cqut.service.IAccountService;

@Controller
public class AccountController {
	@Resource
	private IAccountService service;

	/**
	 * 
	 * 方法简述：获取登录用户的信息
	 * @param row
	 * @param page
	 * @param user_account
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-26 上午10:43:58
	 */
	@RequestMapping(value="/account/getaccount",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getAccount(@RequestParam("row") int row,
			@RequestParam("page")int page,
			@RequestParam("user_account") String user_account){
		    Map<String, Object> result = service.getAccount(row, page, user_account);
		    if(result == null){
				return "-1";
			}
		    return JSON.toJSON(result).toString();
	}
	
	/**
	 * 
	 * 方法简述：新增一个登录用户
	 * @param account
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-26 上午11:01:30
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/account/addaccount",method=RequestMethod.POST,  produces = "application/json;charset=UTF-8")
	@ResponseBody
	public int addAccount(@RequestBody Map<String, Object> account){
		return service.addAccount(account);
	}
	
	/**
	 * 
	 * 方法简述：修改登录用户信息
	 * @param id
	 * @param account
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-26 上午11:03:31
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/account/updateaccount/{id}",method=RequestMethod.PUT,produces="application/json;charset=UTF-8")
	@ResponseBody
	public int updateAccount(@PathVariable(value = "id") String id,
			@RequestBody Map<String, Object> account){
		return service.updateAccount(id, account);
	}
	
	/**
	 * 
	 * 方法简述：修改登录用户密码
	 * @param id
	 * @param password
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-26 上午11:09:47
	 */
	@RequestMapping(value="/account/updatepassword",method=RequestMethod.PUT,produces="application/json;charset=UTF-8")
	@ResponseBody
	public int updatePassword(@RequestParam("id") String id,
			@RequestParam("oldpassword")String oldpassword,
			@RequestParam("newpassword")String newpassword){
		int result = service.updatePassword(id, oldpassword, newpassword);
		return result;
	}
	
	/**
	 * 
	 * 方法简述：修改登录用户是否启用
	 * @param id
	 * @param state
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-26 上午11:10:34
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/account/updatestate",method=RequestMethod.PUT,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String updateState(@RequestParam("id") String id,
			@RequestParam("state")int state){
		Map<String, Object> result = service.updateState(id, state);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 
	 * 方法简述：删除一个或多个登录用户
	 * @param id
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-26 上午11:12:08
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/account/deleteaccount/{id}",method=RequestMethod.DELETE,produces="application/json;charset=UTF-8")
	@ResponseBody
	public int deleteAccount(@PathVariable(value = "id") String id){
		int result = service.deleteAccount(id);
		return result;
	}
	
	/**
	 * 
	 * 方法简述：下拉框获取人员名字
	 * @return 
	 * @author huhongjie
	 * @date 2017-7-26 上午11:14:35
	 */
	@RequestMapping(value="/account/getuser",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getUserName(){
		    List<Map<String, Object>> result = service.getUserName();
		    if(result == null){
				return "-1";
			}
		    return JSON.toJSON(result).toString();
	}
	
	
	
}
