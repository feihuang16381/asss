package com.cqut.interceptor;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cqut.clumn.AuthCheckAnnotation;
import com.cqut.dao.base.EntityDao;
import com.cqut.entity.user.User;

public class permissionInterceptor implements HandlerInterceptor{
	@Resource(name = "entityDao")
    private EntityDao entityDao;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		 HandlerMethod methodHandler=(HandlerMethod) handler;
	     AuthCheckAnnotation auth=methodHandler.getMethodAnnotation(AuthCheckAnnotation.class);
	     if(auth!=null && auth.check()){ //需要管理员权限
	    	 if(!isAdministrator(request)) //判断是否是管理员
	            return false;  
		 }
	     return true;
	}
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	private boolean isAdministrator(HttpServletRequest request){
		String user_id = (String) request.getSession().getAttribute("user_id");
		if(user_id != null && !user_id.isEmpty()){
			Map<String, Object> userMap = entityDao.findByID(new String[]{"*"}, user_id, User.class);
			if(userMap.get("user_type") != null && (int)userMap.get("user_type") != 0)
				return true;
		}
		return false;
	}
}
