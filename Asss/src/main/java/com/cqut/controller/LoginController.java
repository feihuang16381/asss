package com.cqut.controller;

import com.cqut.dao.base.EntityDao;
import com.cqut.entity.user.Login;
import com.cqut.entity.user.Module;
import com.cqut.entity.user.User;
import com.cqut.util.EntityIDFactory;
import com.cqut.util.MD5;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {

    @Resource
    EntityDao entityDao;	
    
    /**
     * 登录成功过后返回对对应的模块
     * @param login
     * @return
     * @author lilongshun
     */
    @RequestMapping(value = "/access/token", method = RequestMethod.POST)
    @ResponseBody
    public Object getLogin(@RequestBody Map<String, String> login,HttpServletRequest request){

        String loginName =  login.get("userName");
        String passWord = login.get("password");

        String condition = "Login.user_account = '"+loginName+"'";

        List<Login> list = entityDao.getByCondition(condition,Login.class);

        if( list == null || list.size()==0){
            System.out.println("账户不存在");
            return 0;
        }
        else{
            Login DBlogin = list.get(0);

            if (DBlogin.getPassword().equals(MD5.MD5(passWord))) {
                    if(DBlogin.getState() == 0) {
                        System.out.println("账户被禁用");
                        return  -2;
                    }
                    else {
                        System.out.println("登陆成功");
                        request.getSession().setAttribute("user_id", list.get(0).getUser_id());
                        User user = entityDao.getByID(list.get(0).getUser_id(), User.class);
                        if(user.getUser_type() != null && user.getUser_type().equals("1")) //管理员登录
                        	return getPermission("all");
                        else
                        	return getPermission(list.get(0).getUser_id());
                    }
               }


           else  {
                System.out.println(""
                		+ "密码错误");
                return -1;
                }

        }
    }
    
    /**
     * 根据权限返回对应的模块
     * @param department_id
     * @author lilongshun
     */
    private List<Map<String, Object>> getPermission(String userID){
    	String condition = "";
    	if(userID.equals("all")){
    		condition = " 1=1 ";
    	}
    	else
    	{
    		condition = " module.id IN ("
    					 + " SELECT DISTINCT module_id FROM module_assignment"
    					 + " WHERE module_assignment.permission_id in ("
    					 + " SELECT DISTINCT permission_id FROM permission_assignment"
    					 + " WHERE department_id = (SELECT user.department_id FROM user WHERE user.id = '" + userID + "')))";
    	}
    	List<Map<String, Object>> list= entityDao.searchForeign(new String[]{"*"}, "module", null, null, condition);
    	if(userID.equals("all") && list == null)
    		initMoudle();
    	List<Map<String, Object>> meunList = new ArrayList<Map<String,Object>>();
    	for (Map<String, Object> map : list) {
			if(map.get("haschild") != null && (int)map.get("haschild") == 0){
				meunList.add(map);
			}
		}
    	list = removeList(list, meunList);
    	
    	for (Map<String, Object> map : meunList) {
    		if(map.get("id") != null){
	    		List<Map<String, Object>>  childList = getChild(map.get("id").toString(), list);
	    		if(childList != null && childList.size() > 0){
	    			map.put("childList", childList);
	    		}
	    		else
	    			map.put("childList", new ArrayList<Map<String, Object>>());
    		}
		}
    	return meunList;
    }
    
    private List<Map<String, Object>> removeList(List<Map<String, Object>> list, List<Map<String, Object>> removeList){
    	for (Map<String, Object> map : removeList) {
    		list.remove(map);
		}
    	return list;
    }
    /**
     * 通过递归获取子模块，应该用不到递归
     * @param parent
     * @param rootMenu
     * @return
     */
    private List<Map<String, Object>> getChild(String parent, List<Map<String, Object>> rootMenu) {
        // 子菜单
        List<Map<String, Object>> childList = new ArrayList<>();
        for (Map<String, Object> map : rootMenu) {
            // 遍历所有节点，将父菜单parent与传过来的parent比较         
        	if(map.get("parent") != null && map.get("parent").toString().equals(parent)){
        		childList.add(map);
        	}
        }
        rootMenu = removeList(rootMenu, childList);
        // 把子菜单的子菜单再循环一遍
        for (Map<String, Object> map : childList) {// 没有url子菜单还有子菜单
            if (map.get("is_endofmoduleLevel") != null && (int)map.get("is_endofmoduleLevel") != 0) {
                // 递归
            	map.put("childList", getChild((map.get("id") == null ? map.get("id").toString() :""), rootMenu));
            }
        } 
        // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }
    
    //所有moudle为空，初始化
    private void initMoudle(){
    	Module pmodule = new Module();
    	String parentId = EntityIDFactory.createId();
    	pmodule.setId(parentId);
    	pmodule.setName("分配管理");
    	pmodule.setIs_endofmoduleLevel(0);
    	pmodule.setUrl("");
    	pmodule.setHaschild(0);
    	entityDao.save(pmodule);
    	
    	Module cModule = new Module();
    	cModule.setId(EntityIDFactory.createId());
    	cModule.setName("模块管理");
    	cModule.setIs_endofmoduleLevel(1);
    	cModule.setParent(parentId);
    	cModule.setUrl("");
    	cModule.setHaschild(1);
    	entityDao.save(cModule);
    }
}