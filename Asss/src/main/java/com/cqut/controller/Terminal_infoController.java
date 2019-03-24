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
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.cqut.clumn.AuthCheckAnnotation;
import com.cqut.service.ITerminal_infoService;

@Controller
public class Terminal_infoController {
	@Resource
	private ITerminal_infoService service;
	
    /**
     * 
     * 方法简述：获取所有站点信息 或者相应站点的信息
     * @param row
     * @param page
     * @param info
     * @return 
     * @author lilongshun
     * @date 2017年7月2日 上午10:04:16
     */
	@RequestMapping(value ="/terminal/allterminal",method=RequestMethod.POST)
	@ResponseBody
	public String getTerminal(
			@RequestParam(value="row",required=false) int row,
			@RequestParam(value="page",required=false) int page,
			@RequestBody Map<String,String> info){
		
		String code_name = info.get("code_name");
		String type_id = info.get("type_id");
		Map<String,Object> result = service.getTerminal(row,page,code_name,type_id);
		if(result == null){
			return "-1"; 
		}
		else{
			return JSONObject.toJSONString(result);
		}
	}	
	
	/**
	 * 
	 * 方法简述：获取所有区域名
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月2日 上午10:45:38
	 */
	@RequestMapping(value ="/Tarea/all",method=RequestMethod.GET)
	@ResponseBody
	public String getArea(){
		List<Map<String,Object>> result = service.getArea();
		if(result == null){
			return "-1"; 
		}
		else{
			return JSONObject.toJSONString(result);
		}
	}
	
	
	/**
	 * 
	 * 方法简述：获取所有站点类型名称
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月2日 上午10:46:40
	 */
	@RequestMapping(value ="/Ttype/all",method=RequestMethod.GET)
	@ResponseBody
	public String getType(){
		List<Map<String,Object>> result = service.getType();
		if(result == null){
			return "-1"; 
		}
		else{
			return JSONObject.toJSONString(result);
		}
	}

	/**
	 * 
	 * 方法简述：获取所有项目ID与name
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月2日 上午10:59:17
	 */
	@RequestMapping(value ="/Tproject/all",method=RequestMethod.GET)
	@ResponseBody
	public String getProject(){
		List<Map<String,Object>> result = service.getProject();
		if(result == null){
			return "-1"; 
		}
		else{
			return JSONObject.toJSONString(result);
		}
	}
	
	/**
	 * 
	 * 方法简述：新增一个站点
	 * @param terminal
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月2日 上午10:05:02
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/terminal/newterminal",method=RequestMethod.POST)
	@ResponseBody
	public String addTerminal(@RequestParam("terminal") String terminal,
			@RequestParam("files") MultipartFile[] files){
		JSONObject json = JSONObject.parseObject(terminal);
		Map<String, Object> result = service.addTerminal(json,files);
		return JSONObject.toJSONString(result);
	}

	/**
	 * 
	 * 方法简述：删除一个或多个站点
	 * @param terminalIds
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月2日 上午10:05:40
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/terminal/delterminals/{terminalIds}",method=RequestMethod.DELETE)
	@ResponseBody
	public Map<String, Object> delTerminalById(@PathVariable String terminalIds){
		Map<String, Object> result = service.delTerminalById(terminalIds);
		return result;
	}
	
	/**
	 * 
	 * 方法简述：修改某条站点信息
	 * @param id
	 * @param terminal
	 * @return 
	 * @author wuzhenwei
	 * @date 2017年7月2日 上午10:05:59
	 */
	@AuthCheckAnnotation(check = true)
	@RequestMapping(value="/terminal/upterminal/{id}",method = RequestMethod.PUT)
	@ResponseBody
	public String upTerminal(@PathVariable String id,@RequestBody Map<String,Object> terminal){
		
		Map<String, Object> result = service.upTerminal(id,terminal);
		if(result == null){
			return "-1";
		}else{
			return JSONObject.toJSONString(result);
		}
	}
	
	/**
	 * 根据条件查询站点信息
	 * @param terminal
	 * @return
	 * @author lilongshun
	 */
	@RequestMapping(value="/terminal/getTerminal",method = RequestMethod.PUT)
	@ResponseBody
	public String getTerminal(@RequestBody Map<String,Object> condition){
		
		Map<String, Object> result = service.getTerminal(condition);
		if(result == null){
			return "-1";
		}else{
			return JSONObject.toJSONString(result);
		}
	}
	
	/**
	 * 查询站点的详细信息
	 * @param terminal
	 * @return
	 * @author lilongshun
	 */
	@RequestMapping(value="/terminal/getTerminalMessage",method = RequestMethod.PUT)
	@ResponseBody
	public Map<String, Object> getTerminalMessage(String terminalId){
		if(terminalId == null)
			return null;
		Map<String, Object> result = service.getTerminalMessage(terminalId);
		return result;
	}
}
