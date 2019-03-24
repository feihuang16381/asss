
package com.cqut.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cqut.dao.base.EntityDao;
import com.cqut.dao.base.SearchDao;
import com.cqut.entity.user.Area;
import com.cqut.entity.user.Login;
import com.cqut.util.EntityIDFactory;
import com.cqut.util.MD5;

@Service("accountService")
public class AccountService implements IAccountService{
	@Resource(name = "entityDao")
    private EntityDao entityDao;
	@Resource(name = "searchDao")
	private SearchDao searchDao;
	
	@Override
	public Map<String, Object> getAccount(int row, int page,
			String user_account) {
		String condition = " 1=1 ";
		if (user_account != null && !user_account.isEmpty())
			condition += " AND login.user_account like '%"
					+ user_account + "%'";
		String joinEntity = " LEFT JOIN user ON user.id = login.user_id ";
		List<Map<String, Object>> resultList = searchDao
				.searchWithpagingInMysql(
						new String[] { " login.id, "
								+ " login.user_id, "
								+ " user.user_name, "
								+ " login.user_account, "
								+ " user.telephone, "
								+ " login.state "},
						" login ", joinEntity, null, null, condition,
						null, "login.id", "ASC", row, page);
		int count = searchDao.getForeignCount(" login.id ", " login ", null, null, null, condition);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", resultList);
		map.put("total", count);
		return map;
	}
	
	@Override
	public int addAccount(Map<String, Object> account) {
		String user_id = account.get("user_id").toString();
		if(isExist(user_id)){
			return 0;
		}
		Login login = new Login();
		String login_id = EntityIDFactory.createId();
		String password = account.get("password").toString();
		account.remove("password");
		login.setId(login_id);
		login.setPassword(MD5.MD5(password));
		login.setProperties(account);
		
		Map<String, Object> result = new HashMap<String, Object>();;
		String resultString = entityDao.save(login) == 1 ? "true": "false";
		result.put("result", resultString);
		
		return 1;
	}
	
	@Override
	public int updateAccount(String id,
			Map<String, Object> account) {
		Login login = entityDao.getByID(id, Login.class);
		String USER_ID = login.getUser_id();
		String user_id = account.get("user_id").toString();
		if(isExist(user_id)){
			if(USER_ID.equals(user_id)){
				account.remove("user_id");
				login.setProperties(account);
				return entityDao.updatePropByID(login, id);
			}else{
				return 0;
			}
		}	
		login.setProperties(account);
		return entityDao.updatePropByID(login, id);
	}
	
	@Override
	public int updatePassword(String id, String oldpassword, String newpassword) {
		String condition = " login.id = " + id + " ";
		List<Map<String, Object>> resultList = searchDao.searchForeign(
				new String[] { " login.id, "
						+ " login.password "},
				" login ", null, null, null, condition);
		String OLDPassword = MD5.MD5(oldpassword);
		String NEWPassword = MD5.MD5(newpassword);
		int n = resultList.get(0).get("password").toString().equals(OLDPassword)?1:0;
		if(n == 1){
			Login login = entityDao.getByID(id, Login.class);
			login.setPassword(NEWPassword);
			return entityDao.updatePropByID(login,id);
		}	
		return 0;		
	}
	
	@Override
	public Map<String, Object> updateState(String id, int state) {
		Login login = entityDao.getByID(id, Login.class);
		login.setState(state);
		String resultString = entityDao.updatePropByID(login,
				id) == 1 ? "true" : "false";
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("result", resultString);
		return result;
	}
	
	@Override
	public int deleteAccount(String login_id) {
		String[] ids = login_id.split(",");
		int BackValue = 0;
		for(String id:ids){
			BackValue += entityDao.deleteByID(id, Login.class);
		}
		if(BackValue != ids.length){
			BackValue = BackValue - ids.length;
		}
		//返回删除成功的个数
	    return BackValue;
	}

	@Override
	public List<Map<String, Object>> getUserName() {
		List<Map<String, Object>> resultList = searchDao.searchForeign(
						new String[] { " user.id, "
								+ " user.user_name "},
						" user ", null, null, null, null);
		return resultList;
	}
	
	public boolean isExist(String user_id){
		List<Map<String, Object>> resultList = searchDao.searchForeign(
				new String[] { " login.id, "
						+ " login.user_id "},
				" login ", null, null, null, null);
		for(int i = 0; i < resultList.size(); i++){
			if(resultList.get(i).get("user_id").equals(user_id))
				return true;
		}
		return false;
	}
	
}
