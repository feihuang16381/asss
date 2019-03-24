package com.cqut.service;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.io.File;
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

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.cqut.dao.base.EntityDao;
import com.cqut.dao.base.SearchDao;
import com.cqut.entity.user.Area;
import com.cqut.entity.user.Common_Maintain;
import com.cqut.entity.user.Danger_Demarcate;
import com.cqut.entity.user.Danger_Inspection;
import com.cqut.entity.user.Danger_Maintain;
import com.cqut.entity.user.Participate_Relation;
import com.cqut.entity.user.Repair_Report;
import com.cqut.entity.user.Task;
import com.cqut.entity.user.User;
import com.cqut.entity.user.file;
import com.cqut.util.EntityIDFactory;
import com.cqut.util.fileUtil;

@Service("repairService")
public class RepairService implements IRepairService{
	@Resource(name = "entityDao")
    private EntityDao entityDao;
	@Resource(name = "searchDao")
	private SearchDao searchDao;
	
	@Override
	public List<Map<String, Object>> getRepairStatis() {
		String joinEntity = "LEFT JOIN(SELECT sparepart_id ,sum(number) AS sum FROM"
							+ " spare_part_Record GROUP BY sparepart_id) as spr on spare_part.id = spr.sparepart_id";
		List<Map<String, Object>> list = entityDao.searchForeign(new String[]{"*"}, "spare_part", joinEntity, null, " 1 = 1");
		return list;
	}

	@Override
	public List<Map<String, Object>> getWorkload(String department_id) {

		String condition = "1=1 ";
		if(department_id != null && !department_id.isEmpty()){
			condition += " and user.department_id = '" + department_id + "'";
		}
		List<User> users = entityDao.getByCondition(condition, User.class);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (User user : users) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", user.getUser_name());
			map.put("number", entityDao.getCountByCondition("Participate_Relation.user_id = " + user.getId(), Participate_Relation.class));
			list.add(map);
		}
		return list;
	}
	
	public String addRepairReport(JSONObject addRepairReport, MultipartFile[] files, HttpServletRequest request) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = addRepairReport.getString("repair_time");
		Date data = null;
		try {
			data = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Repair_Report re = new Repair_Report();
		String id = EntityIDFactory.createId();
		re.setId(id);
		re.setRepair_time(data);
		if(addRepairReport.getString("repair_type") != null)
			re.setRepair_type(addRepairReport.getString("repair_type"));
		if(addRepairReport.getString("task_id") != null)
			re.setTask_id(addRepairReport.getString("task_id"));
		String file_path = "D:/upload";

		String file_before = saveFileEntity(files[0],file_path);
		if(!file_before.equals("false"))
			re.setPicture_before(file_before);
		else
			return "文件存储失败！";
		
		String file_now = saveFileEntity(files[1],file_path);
		if(!file_now.equals("false"))
			re.setPicture_now(file_now);
		else
			return "文件存储失败！";
		
		String file_after = saveFileEntity(files[2],file_path);
		if(!file_after.equals("false"))
			re.setPicture_after(file_after);
		else
			return "文件存储失败！";
		
		String repair_id = EntityIDFactory.createId();
		if(addRepairReport.get("repair_type") != null)
			switch (addRepairReport.get("repair_type").toString()) {
			case "0"://通用维护
				Common_Maintain cm = new Common_Maintain();
				cm.setId(repair_id);
				cm.setRepair_report_id(id);
				cm.setProperties(addRepairReport);
				if(!(entityDao.save(cm) == 1))
					return "通用维护表存储失败";
				break;
				
			case "1"://危险源巡检表
				Danger_Inspection di = new Danger_Inspection();
				di.setId(repair_id);
				di.setRepair_report_id(id);
				di.setProperties(addRepairReport);
				if(!(entityDao.save(di) == 1))
					return "危险源巡检表存储失败";				
				break;
				
			case "2"://.危险源维护表
				Danger_Maintain dm = new Danger_Maintain();
				dm.setId(repair_id);
				dm.setRepair_report_id(id);
				dm.setProperties(addRepairReport);
				if(!(entityDao.save(dm) == 1))
					return "危险源维护表存储失败";				
				break;
			case "3"://危险源标定表
				Danger_Demarcate dd = new Danger_Demarcate();
				dd.setId(repair_id);
				dd.setRepair_report_id(id);
				dd.setProperties(addRepairReport);
				if(!(entityDao.save(dd) == 1))
					return "危险源标定表存储失败";				
				break;
			default:
				break;
			}
		else
			return "未选择维护类型";
		Participate_Relation pr_write = new Participate_Relation();
		pr_write.setId(EntityIDFactory.createId());
		pr_write.setRepair_report_id(id);
		if((String)request.getSession().getAttribute("user_id") != null){
			pr_write.setUser_id((String)request.getSession().getAttribute("user_id"));
			pr_write.setIs_writer(1);
		}
		else
			return "参与关系参数出错";
		Participate_Relation[] pr_user = null;
		if(addRepairReport.get("user_id") != null){
			String[] userId = addRepairReport.get("user_id").toString().split(",");
			pr_user = new Participate_Relation[userId.length];
			for (int i = 0; i < userId.length; i++) {
				pr_user[i] = new Participate_Relation();
				pr_user[i].setId(EntityIDFactory.createId());
				pr_user[i].setRepair_report_id(id);
				pr_user[i].setUser_id(userId[i]);
				pr_user[i].setIs_writer(0);
			}
		}
		Task task = new Task();
		task.setId(addRepairReport.getString("task_id"));
		task.setIsComplete(1);
		task.setRemark("");
		entityDao.updatePropByID(task, addRepairReport.getString("task_id"));
		for (int i = 0; i < pr_user.length; i++) {
			entityDao.save(pr_user[i]);
		}
		return entityDao.save(re) + entityDao.save(pr_write) == 2 ?"true":"false";
	}

	private String saveFileEntity(MultipartFile file, String path){
		String filePath = fileUtil.saveFile(file, path);
		if(!filePath.equals("false")){
			com.cqut.entity.user.file picture = new com.cqut.entity.user.file(); 
			String id = EntityIDFactory.createId();
			picture.setId(id);
			picture.setUrl(filePath);
			picture.setFile_name(file.getName());
			picture.setFile_path(path + "/" + filePath);
			if(entityDao.save(picture) == 1)
				return id;
		}
		return "false";
	}
 
	@Override
	public List<Map<String, Object>> getRepairReport(int row, int page,String user_id, String task_name ,String terminal_name, String repair_type) {
		String condition = " 1=1 ";
		if(user_id != null && !user_id.isEmpty()){
			Map<String, Object> userMap = entityDao.findByID(new String[]{"*"}, user_id, User.class);
			if(userMap.get("user_type") != null && (int)userMap.get("user_type") == 0)
				condition += "and repair_report.id in (SELECT participate_relation.repair_report_id FROM participate_relation WHERE participate_relation.user_id = '" + user_id + "')";
		}
		
		if (task_name != null && !task_name.isEmpty() && !task_name.endsWith("undefined"))
			condition += " AND TNA.task_name like '%"
					+ task_name + "%'";
		if (terminal_name != null && !terminal_name.isEmpty() && !terminal_name.endsWith("undefined"))
			condition += " AND TNA.terminal_name like '%"
					+ terminal_name + "%'";
		if(repair_type != null && !repair_type.isEmpty() && !terminal_name.endsWith("undefined"))
			condition += " AND repair_report.repair_type like '%"
					+ repair_type + "%'";
		
		String joinEntity = "LEFT JOIN (SELECT task.id as task_id,task.terminal_id,task.task_name,task.type as task_type,terminal_info.terminal_name FROM task LEFT JOIN terminal_info ON terminal_info.id = task.terminal_id)AS TNA ON TNA.task_id = repair_report.task_id";

		List<Map<String, Object>> resultList = searchDao
				.searchWithpagingInMysql(
						new String[] {	 " repair_report.id, "
										+" (case repair_report.repair_type when '1' then '危险源巡检' when '2' then '危险源维护' when '3' then '危险源标定' else '通用维护' end) as repair_type, "
										+" repair_report.repair_time, "
										+" repair_report.remark, "
										+" TNA.task_id, "
										+" TNA.terminal_id, "
										+" TNA.task_name, "
										+" (case TNA.task_type when '1' then '危险源巡检' when '2' then '危险源维护' when '3' then '危险源标定' else '通用维护' end) as task_type, "
										+" TNA.terminal_name "
								},
						" repair_report ", joinEntity, null, null, condition,
						null, "repair_report.id", "ASC", row, page);
		return resultList;
	}

	@Override
	public Map<String, Object> getRepairDetails(String repair_id,int repair_type){
		String joinEntity = " LEFT JOIN (SELECT task.id as task_id,task.terminal_id,task.task_name,task.type as task_type,terminal_info.terminal_name FROM task LEFT JOIN terminal_info ON terminal_info.id = task.terminal_id)AS TNA ON TNA.task_id = repair_report.task_id";		
		if(repair_type == 0){
			joinEntity += " LEFT JOIN common_maintain on common_maintain.repair_report_id = repair_report.id ";
		}
		if(repair_type == 1){
			joinEntity += " LEFT JOIN danger_inspection on danger_inspection.repair_report_id = repair_report.id ";
		}
		if(repair_type == 2){
			joinEntity += " LEFT JOIN danger_maintain on danger_maintain.repair_report_id = repair_report.id ";
		}
		if(repair_type == 3){
			joinEntity += " LEFT JOIN danger_demarcate on danger_demarcate.repair_report_id = repair_report.id ";
		}
		List<Map<String, Object>> list = entityDao.searchForeign(new String[]{"*,(case TNA.task_type when '1' then '危险源巡检' when '2' then '危险源维护' when '3' then '危险源标定' else '通用维护' end) as task_typeName","  DATE_FORMAT(repair_report.repair_time,'%y-%m-%d %H:%i:%s') as repair_data"}, "repair_report", joinEntity, null, "repair_report.id = '" + repair_id + "'");
		if(list == null || list.size() == 0)
			return null;
		Map<String, Object> map = list.get(0);
		if(map.get("picture_before") != null){
			Map<String, Object> picmap = entityDao.findByID(new String[]{"*"}, map.get("picture_before").toString(), file.class);
			if(picmap != null && picmap.get("url") != null)
				map.put("picture_before_url", picmap.get("url").toString());
		}
		if(map.get("picture_now") != null){
			Map<String, Object> picmap = entityDao.findByID(new String[]{"*"}, map.get("picture_now").toString(), file.class);
			if(picmap != null && picmap.get("url") != null)
				map.put("picture_now_url", picmap.get("url").toString());
		}
		if(map.get("picture_after") != null){
			Map<String, Object> picmap = entityDao.findByID(new String[]{"*"}, map.get("picture_after").toString(), file.class);
			if(picmap != null && picmap.get("url") != null)
				map.put("picture_after_url", picmap.get("url").toString());
		}
		
		if(map.get("id") != null){
			List<Map<String, Object>> userList = entityDao.searchForeign(new String[]{"*"," user.id as user_id"}, 
					"participate_relation",
					"LEFT JOIN user on user.id = participate_relation.user_id", null, 
					"participate_relation.repair_report_id = '" + map.get("id").toString() + "'");
			if(userList != null){
				List<String> user = new ArrayList<String>();
				for (Map<String, Object> userMap : userList) {
					if(userMap.get("user_id") != null && userMap.get("is_writer") != null && (int)userMap.get("is_writer") == 1)
						map.put("writer", userMap.get("user_id").toString());
					else if(userMap.get("user_id") != null && userMap.get("is_writer") != null && (int)userMap.get("is_writer") == 0)
						user.add(userMap.get("user_id").toString());
				}
				if(user.size() > 0)
					map.put("user", user);
			}
		}
		return map;
	}
	
	@Override
	public int deleteRecord(String Record_id) {
		String[] ids = Record_id.split(",");
		int BackValue = 0;
		for(String id:ids){
			Map<String, Object> re = entityDao.findByID(new String[]{"*"}, id, Repair_Report.class);
			if(re != null && re.get("task_id") != null){
				entityDao.deleteByID(re.get("task_id").toString(), Task.class);
			}
			if(re != null && re.get("id") != null){
				entityDao.deleteByID(id, Repair_Report.class);
			}
		}
		if(BackValue != ids.length){
			BackValue = BackValue - ids.length;
		}
		//返回删除成功的个数
	    return BackValue;
	}

	@Override
	public List<Map<String, Object>> getAllTask() {
		List<Map<String, Object>> list = entityDao.findByCondition(new String[]{"*"}, " task.isComplete = 0", Task.class);
		return list;
	}


}
