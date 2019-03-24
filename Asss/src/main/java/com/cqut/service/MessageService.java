package com.cqut.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.cqut.dao.base.EntityDao;
import com.cqut.dao.base.SearchDao;
import com.cqut.entity.user.Area;
import com.cqut.entity.user.Notice;
import com.cqut.entity.user.Task;
import com.cqut.entity.user.Task_Distribute;
import com.cqut.entity.user.User;
import com.cqut.util.DateFactory;
import com.cqut.util.EntityIDFactory;

@Service("messageService")
public class MessageService implements IMessageService{
	@Resource(name = "entityDao")
    private EntityDao entityDao;
	@Resource(name = "searchDao")
	private SearchDao searchDao;
	
	@Override
	public Map<String, Object> getNoticeList(int row, int page, String name) {
		String condition = " 1=1 ";
		if (name != null && !name.isEmpty())
			condition += " AND notice.title like '%"
					+ name + "%' or user.user_name like '%" + name + "%'";
		String joinEntity = " LEFT JOIN user ON user.id = notice.founder ";
		List<Map<String, Object>> resultList = searchDao.searchWithpagingInMysql(
				new String[] { " notice.id, "
						+ " notice.founder, "
						+ " notice.title, "	
						+ " user.user_name, "
						+ " notice.content, "
						+ " DATE_FORMAT(notice.create_time, '%Y-%c-%d %H:%i:%s') as create_time"}, 
						" notice ", joinEntity, null, null, condition,
						null, " create_time ", " DESC ", row, page);
		int count = searchDao.getForeignCount(" notice.id ", " notice ", joinEntity, null, null, condition);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", resultList);
		map.put("total", count);
		return map;
	}
	
	
	

	@Override
	public List<Map<String, Object>> getNoticeList2(){
		String joinEntity = " LEFT JOIN user ON user.id = notice.founder ";
		String[] properties = new String[] { " notice.id, "
				+ " notice.founder, "
				+ " notice.title, "	
				+ " user.user_name, "
				+ " notice.content, "
				+ " DATE_FORMAT(notice.create_time, '%Y-%c-%d %H:%i:%s') as create_time,"
				+"notice.state"};
	 String baseEntity = "notice";
		List<Map<String, Object>> resultList =searchDao.searchForeign(properties, baseEntity, joinEntity, null, null, null);
		return resultList;
		
	}
	
	@Override
	public Map<String, Object> addNotice(Map<String, Object> notice) {
		Map<String, Object> result = new HashMap<String, Object>();
		Date date = DateFactory.convertToDateTime(new Date());			
		Notice notices = new Notice();
		notices.setId(EntityIDFactory.createId());
		notices.setCreate_time(date);
		notices.setProperties(notice);
		String resultString = entityDao.save(notices) == 1 ? "true": "false";
		result.put("result", resultString);
		return result;
	}
	
	@Override
	public Map<String, Object> updateNotice(String id,
			Map<String, Object> notice) {
		Map<String, Object> result = new HashMap<String, Object>();
		Date date = DateFactory.convertToDateTime(new Date());
		Notice notices = entityDao.getByID(id, Notice.class);
		notices.setCreate_time(date);
		notices.setProperties(notice);
		String resultString = entityDao.updatePropByID(notices, id) == 1 ? "true": "false";
		result.put("result", resultString);
		return result;
	}
	
	@Override
	public int deleteNotice(String notice_id) {
		String[] ids = notice_id.split(",");
		int BackValue = 0;
		for(String id:ids){
			BackValue += entityDao.deleteByID(id, Notice.class);
		}
		if(BackValue != ids.length){
			BackValue = BackValue - ids.length;
		}
		//返回删除成功的个数
	    return BackValue;
	}
	
	@Override
	public Map<String, Object> getTasks(int row, int page, String name) {
		String condition = " 1=1 ";
		if (name != null && !name.isEmpty())
			condition += " AND task.task_name like '%" + name + "%'";
		String joinEntity =" LEFT JOIN terminal_info ON terminal_info.id = task.terminal_id "
				+ " LEFT JOIN user ON user.id = task.founder ";
		List<Map<String, Object>> resultList = searchDao.searchWithpagingInMysql(
				new String[] { " task.id, "
						+ " task.founder as user_id, "
						+ " user.user_name, "
						+ " task.terminal_id, "
						+ " task.task_name, "
						+ " task.isAudit, "
						+ " task.terminal_id, "
						+ " case when task.isComplete = 0 then '未完成' else '完成' end isComplete , "
						+ " task.content, "
						+ " terminal_info.terminal_name, "	
						+ " case when task.type = 1 then '危险源巡检' "
						+ " when task.type = 2 then '危险源维护' " 
						+ " when task.type = 3 then '危险源标定' "
						+ " else '通用维护' end type, "
						+ " DATE_FORMAT(task.time, '%Y-%c-%d %H:%i:%s') as time "}, 
						" task ", joinEntity, null, null, condition,
						null, " time ", "DESC ", row, page);
		System.out.println(resultList.toString());
		int count = searchDao.getForeignCount(" task.id ", " task ", joinEntity, null, null, condition);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", resultList);
		map.put("total", count);
		return map;
	}
	
	@Override
	public List<Map<String, Object>> getUser_ids(String task_id) {
		String condition = " task_distribute.task_id = "+ task_id +"";
		List<Map<String, Object>> resultList = searchDao.searchForeign(
				new String[]{" task_distribute.user_id "}, " task_distribute ", null, null, null, condition);
		return resultList;
	}
	
	@Override
	public List<Map<String, Object>> getTerminalName() {
		List<Map<String, Object>> resultList = searchDao.searchForeign(
				new String[]{" terminal_info.id, terminal_info.terminal_name "}, " terminal_info ", null, null, null, null);
		return resultList;
	}
	
	@Override
	public int ReceiveTask(String taskID){
	String condition = "task_distribute.task_id = '"+taskID+"'";
	Task_Distribute task_Distribute = new Task_Distribute();
	int Is_Receive = 1;
	task_Distribute.setIs_receive(Is_Receive);
	int num = entityDao.updatePropByCondition(task_Distribute, condition);
		
		
		return num;
		
	}
	
	@Override
	public Map<String, Object> getTaskList(int row, int page, String user_id, String name, String type, int typeNum) {
		String condition = " 1=1 ";
		if(typeNum != 1)
			condition += " AND task_id in (select task_id from task_distribute where user_id = '"+ user_id +"')";
		if(typeNum == 1)
			condition += " AND task_id in (select id from task where founder = '"+ user_id +"')";
		if(typeNum == 2)
			condition += " AND task_distribute.is_receive = 0 ";
		if(typeNum == 3)
			condition += " AND task_distribute.is_receive = 1 ";
		if (name != null && !name.isEmpty())
			condition += " AND task.task_name like '%"
					+ name + "%' or terminal_info.terminal_name like '%" + name + "%'"
					+ " or terminal_info.station_code like '%" + name + "%'";
		if(type !=null && !type.isEmpty())
			condition += " AND task.type like '%"+ type + "%'";
		String joinEntity = " LEFT JOIN task ON task.id = task_distribute.task_id "
				+ " LEFT JOIN terminal_info ON terminal_info.id = task.terminal_id "
				+ " LEFT JOIN user ON user.id = task_distribute.user_id ";
		List<Map<String, Object>> resultList = searchDao.searchWithpagingInMysql(
				new String[] { " task_distribute.id, "
						+ " task.id as task_id, "
						+ " user.id as user_id, "
						+ " user.user_name, "
						+ " task.terminal_id, "
						+ " task.task_name, "
						+ " terminal_info.terminal_name, "	
						+ " terminal_info.station_code, "
						+ " task.founder, "
						+ " task.type, "
						+ " DATE_FORMAT(task.time, '%Y-%c-%d %H:%i:%s') as time, "
						+ " task_distribute.is_receive "}, 
						" task_distribute ", joinEntity, null, null, condition,
						null, " time ", " DESC ", row, page);
		int count = searchDao.getForeignCount(" task_distribute.id ", " task_distribute ", joinEntity, null, null, condition);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", resultList);
		map.put("total", count);
		return map;
	}
	
	@Override
	public List<Map<String, Object>> getTaskList2(){
		String baseEntity = " task_distribute ";
		String[] properties = new String[] { " task_distribute.id, "
				+ " task.id as task_id, "
				+ " user.id as user_id, "
				+ " user.user_name, "
				+ " task.terminal_id, "
				+ " task.task_name, "
				+ " terminal_info.terminal_name, "	
				+ " terminal_info.station_code, "
				+ " task.founder, "
				+ " task.type, "
				+"task.remark,"
				+ " DATE_FORMAT(task.time, '%Y-%c-%d %H:%i:%s') as time, "
				+ " task_distribute.is_receive "};
		String joinEntity = " LEFT JOIN task ON task.id = task_distribute.task_id "
				+ " LEFT JOIN terminal_info ON terminal_info.id = task.terminal_id "
				+ " LEFT JOIN user ON user.id = task_distribute.user_id ";
		
		
		
		List<Map<String, Object>> list =	searchDao.searchForeign(properties, baseEntity, joinEntity, null, null, null);
		return list;
		
	}
	public List<Map<String, Object>> DealData(){
		List<Map<String, Object>> list = getTaskList2();
		List<Map<String, Object>> resultList = new ArrayList();
		List<String> list2 = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			if(!list2.contains(list.get(i).get("task_id"))){
				String user_id=list.get(i).get("user_name").toString();
				StringBuffer stringBuffer = new StringBuffer(user_id);
			for (int j =0; j < list.size(); j++) {
				if(list.get(i).get("task_id").equals(list.get(j).get("task_id"))&&!list.get(i).get("user_id").equals(list.get(j).get("user_id"))){
					stringBuffer.append(",");
					stringBuffer.append(list.get(j).get("user_name"));
					
				}
											
			}
			Map<String, Object> map = new HashMap<>();
			map.put("id", list.get(i).get("id"));
			map.put("task_id", list.get(i).get("task_id"));
			map.put("user_name", stringBuffer.toString());
			map.put("terminal_id", list.get(i).get("terminal_id"));
			map.put("task_name", list.get(i).get("task_name"));
			map.put("terminal_name", list.get(i).get("terminal_name"));
			map.put("station_code", list.get(i).get("station_code"));
			map.put("founder", list.get(i).get("founder"));
			map.put("type", list.get(i).get("type"));
			map.put("time", list.get(i).get("time"));
			map.put("is_receive", list.get(i).get("is_receive"));
			map.put("remark", list.get(i).get("remark"));
			
			
			resultList.add(map);
			list2.add(list.get(i).get("task_id").toString());
			}
			
		}
		return resultList;
		
	}
	
	@Override
	public Map<String, Object> addTask(Map<String, Object> task, HttpServletRequest request) {	
		Map<String, Object> result = new HashMap<String, Object>();

		String user_ids = task.get("user_id").toString();
		task.remove("user_id");
		String time = task.get("time").toString();
		task.remove("time");	
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date;
		try {
			date = (Date) df.parseObject(time);
			Task tasks = new Task();
			String task_id = EntityIDFactory.createId();
			tasks.setId(task_id);
			tasks.setTime(date);
			tasks.setProperties(task);
			tasks.setIsComplete(0);
			tasks.setIsAudit(0);
			tasks.setFounder(request.getSession().getAttribute("user_id").toString());
			int num = entityDao.save(tasks);
			
			Task_Distribute task_distribute = new Task_Distribute();
			user_ids = user_ids.substring(1, user_ids.length()-1);
			user_ids = user_ids.replace(" ", "");
			String[] ids = user_ids.split(",");
			int BackValue = 0;
			for(String id:ids){
					task_distribute.setId(EntityIDFactory.createId());
					task_distribute.setTask_id(task_id);
					task_distribute.setUser_id(id);
					task_distribute.setIs_receive(0);
					BackValue += entityDao.save(task_distribute);
			}
			String resultString = num + BackValue == 1 + ids.length ? "true": "false";
		    result.put("result", resultString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public Map<String, Object> updateTask(String id, Map<String, Object> task, HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		String user_ids = task.get("user_id").toString();
		task.remove("user_id");
		String time = task.get("time").toString();
		task.remove("time");	
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date;
		try {
			date = (Date) df.parseObject(time);
			Task tasks = entityDao.getByID(id, Task.class);
			if(tasks.getIsAudit() == 1){
				result.put("result", "任务已审核，不可修改");
				 return result;
			}
			tasks.setTime(date);
			tasks.setProperties(task);
			int num = entityDao.updatePropByID(tasks, id);//修改任务表
			
			//删除原有的数据，添加新的数据
			String condition = " task_distribute.task_id = '"+ id +"'";
			entityDao.deleteByCondition(condition, Task_Distribute.class);
						
			Task_Distribute task_distribute = new Task_Distribute();
			user_ids = user_ids.substring(1, user_ids.length()-1);
			user_ids = user_ids.replace(" ", "");
			String[] ids = user_ids.split(",");
			int BackValue = 0;
			for(String ID:ids){
					task_distribute.setId(EntityIDFactory.createId());
					task_distribute.setTask_id(id);
					task_distribute.setUser_id(ID);
					task_distribute.setIs_receive(0);
					BackValue += entityDao.save(task_distribute);
			}
			String resultString = num + BackValue == 1 + ids.length ? "true": "false";
			result.put("result", resultString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public int deleteTask(String task_id, HttpSession session) {		
		String[] ids = task_id.split(",");
		int BackValue = 0;
		for(String id:ids){
			BackValue += entityDao.deleteByID(id, Task.class);
		}
		if(BackValue != ids.length){
			BackValue = BackValue - ids.length;
		}
		//返回删除成功的个数
	    return BackValue;
	}

	@Override
	public int isReceive(String task_id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int auditTask(String id) {
		Task task = new Task();
		task.setId(id);
		task.setIsAudit(1);
		int result = entityDao.updatePropByID(task, id);
		return result;
	}




	@Override
	public int ReadNotice(String noticeID) {
		// TODO Auto-generated method stub
		int state = 1;
		Notice notice = new Notice();
		notice.setState(state);
		String condition = "notice.id = '" +noticeID+"'" ;
		int result = entityDao.updatePropByCondition(notice, condition);
		
		return result;
	}
	
}
