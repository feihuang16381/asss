package com.cqut.service;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cqut.dao.base.EntityDao;
import com.cqut.dao.base.SearchDao;
import com.cqut.entity.user.Terminal_info;
import com.cqut.entity.user.file;
import com.cqut.util.EntityIDFactory;
import com.cqut.util.fileUtil;

@Service("terminal_infoService")
public class Terminal_infoService implements ITerminal_infoService{
	@Resource(name = "entityDao")
    private EntityDao entityDao;
	@Resource(name = "searchDao")
	private SearchDao searchDao;
	
	@Override
	public Map<String, Object> getTerminal(int row, int page, String code_name,String type_id) {
		String[] properties = {
				"terminal_info.id",
				"terminal_info.station_code",
				"terminal_info.data_card",
				"terminal_info.terminal_name",
				"terminal_info.area_id",
				"area.area_name",
				"terminal_info.type_id",
				"type.type_name",
				"terminal_info.project_id",
				"project.name",
				"terminal_info.personliable",
				"terminal_info.personliable_phone",
				"DATE_FORMAT(terminal_info.install_time, '%Y-%c-%d %H:%i:%s') as install_time",
				"terminal_info.is_guarantee",
				"terminal_info.longitude",
				"terminal_info.latitude",
				"terminal_info.picture1",
				"terminal_info.picture2",
				"terminal_info.picture3",
				"terminal_info.remark"
		};
		String tableName = "terminal_info";
		String condition = "1 = 1";
		
		if (code_name != null && code_name.compareTo("null") != 0){
			condition = "1 = 1 and (terminal_info.terminal_name like '%"+code_name+"%' or "
					+ "terminal_info.station_code like '%"+code_name+"%')";
		}
		if (type_id != null && type_id.compareTo("null") != 0){
			condition += " and terminal_info.type_id like '%"+type_id+"%'";
		}
		String joinEntity = " LEFT JOIN area on terminal_info.area_id = area.id "
				+ " LEFT JOIN type on terminal_info.type_id = type.id "
				+ " LEFT JOIN project on terminal_info.project_id = project.id ";
		
		List<Map<String,Object>> resultList = searchDao.searchWithpagingInMysql
				(properties,tableName,joinEntity,null,null,condition,null,"terminal_info.id", " DESC ", row
						, page);
		
		int count = searchDao.getForeignCount(Terminal_info.getPrimaryKey(Terminal_info.class), tableName, null, null, null, condition);		
		
		Map<String,Object> result = new HashMap<String,Object>();
	    result.put("data", resultList);
	    result.put("total", count);
		return result;
    }

	@Override
	public Map<String, Object> addTerminal(Map<String, Object> Terminal,MultipartFile[] files) {
		Terminal_info terminal = new Terminal_info();
		
		terminal.setStation_code(Terminal.get("station_code").toString());
		terminal.setData_card(Terminal.get("data_card").toString());
		terminal.setTerminal_name(Terminal.get("terminal_name").toString());
		terminal.setArea_id(Terminal.get("area_id").toString());
		
		
		terminal.setType_id(Terminal.get("type_id").toString());
		terminal.setProject_id(Terminal.get("project_id").toString());
		terminal.setPersonliable(Terminal.get("personliable").toString());
		terminal.setPersonliable_phone(Terminal.get("personliable_phone").toString());
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try{
			date = df.parse(Terminal.get("install_time").toString());
		}
		catch(ParseException e){
			e.printStackTrace();
		}
		
		terminal.setInstall_time(date);
		terminal.setLongitude(Float.parseFloat(Terminal.get("longitude").toString()));
		terminal.setLatitude(Float.parseFloat(Terminal.get("latitude").toString()));
		terminal.setIs_guarantee(Integer.parseInt(Terminal.get("is_guarantee").toString()));
		
		if(files.length > 0){
			String fileID = saveFileEntity(files[0]);
			if(!fileID.equals("false"))
				terminal.setPicture1(fileID);
		}
		
		if(files.length > 1){
			String fileID = saveFileEntity(files[1]);
			if(!fileID.equals("false"))
				terminal.setPicture2(fileID);
		}
		
		if(files.length > 2){
			String fileID = saveFileEntity(files[2]);
			if(!fileID.equals("false"))
				terminal.setPicture3(fileID);
		}
		
		if(Terminal.get("remark")!=null){
			terminal.setRemark(Terminal.get("remark").toString());
		}
		
		String id = EntityIDFactory.createId();
		terminal.setId(id);
		
		Map<String, Object> result = new HashMap<String, Object>();;
		String resultString = entityDao.save(terminal) == 1 ? "true": "false";
		result.put("result", resultString);
		
		return result;
	}
	
	private String saveFileEntity(MultipartFile file){
		String path = "D:/upload";
		String filePath = fileUtil.saveFile(file, path);
		if(!filePath.equals("false")){
			com.cqut.entity.user.file picture = new com.cqut.entity.user.file(); 
			String id = EntityIDFactory.createId();
			picture.setId(id);
			picture.setFile_name(file.getName());
			picture.setFile_path(path + "/" + filePath);
			picture.setUrl(filePath);
			if(entityDao.save(picture) == 1)
				return id;
		}
		return "false";
	}
    /*** 
     * 保存文件 
     * @param file 
     * @return 
     */  
    private String saveFile(MultipartFile file, String path) { 
        // 判断文件是否为空  
        if (!file.isEmpty()) {  
            try {  
                File filepath = new File(path);
                if (!filepath.exists()) 
                    filepath.mkdirs();
                // 文件保存路径  
                String name = System.currentTimeMillis() + file.getOriginalFilename();
                String savePath = path + name;  
                // 转存文件  
                file.transferTo(new File(savePath));  
                return name;  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return "false";  
    }  
	
	@Override
	public Map<String, Object> delTerminalById(String terminalIds) {
		String[] TerminalIds = terminalIds.split(",");
		for (int i = 0; i < TerminalIds.length; i++) {
			deleteFileEntity(TerminalIds[i]);
		}
		String resultString = entityDao.deleteEntities(TerminalIds,
				Terminal_info.class) >= 1 ? "true" : "false";
				
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", resultString);
		return result;
	}
	
	private boolean deleteFileEntity(String terminalId){
		Map<String, Object> map = entityDao.findByID(new String[]{"*"}, terminalId, Terminal_info.class);
		if(map.get("picture1") != null)
			if(!deEntity(map.get("picture1").toString()))
					return false;
		if(map.get("picture2") != null)
			if(!deEntity(map.get("picture2").toString()))
					return false;
		if(map.get("picture3") != null)
			if(!deEntity(map.get("picture3").toString()))
					return false;	
		return true;
	}
	
	private boolean deEntity(String fileID){
		Map<String, Object> map = entityDao.findByID(new String[]{"*"}, fileID, file.class);
		if(map != null && map.get("file_path") != null){
			if(entityDao.deleteByID(fileID, file.class) == 1)
				return deleteFile(map.get("file_path").toString());
		}
		return false;
	}
	
	private boolean deleteFile(String path){
		File file = new File(path);
		return file.delete();
	}
	@Override
	public Map<String, Object> upTerminal(String id, Map<String, Object> Terminal) {
		Terminal_info terminal = entityDao.getByID(id, Terminal_info.class);
		if(terminal == null){
			// 数据库没有该id 对应的数据
		    return null;
		}
		terminal.setStation_code(Terminal.get("station_code").toString());
		terminal.setData_card(Terminal.get("data_card").toString());
		terminal.setTerminal_name(Terminal.get("terminal_name").toString());
		terminal.setArea_id(Terminal.get("area_id").toString());
		terminal.setType_id(Terminal.get("type_id").toString());
		terminal.setProject_id(Terminal.get("project_id").toString());
		terminal.setPersonliable(Terminal.get("personliable").toString());
		terminal.setPersonliable_phone(Terminal.get("personliable_phone").toString());
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try{
			date = df.parse(Terminal.get("install_time").toString());
		}
		catch(ParseException e){
			e.printStackTrace();
		}
		
		terminal.setInstall_time(date);
		terminal.setLongitude(Float.parseFloat(Terminal.get("longitude").toString()));
		terminal.setLatitude(Float.parseFloat(Terminal.get("latitude").toString()));
		terminal.setIs_guarantee(Integer.parseInt(Terminal.get("is_guarantee").toString()));
		if(Terminal.get("picture1")!=null){
			terminal.setPicture1(Terminal.get("picture1").toString());
		}
		
		if(Terminal.get("picture2")!=null){
			terminal.setPicture1(Terminal.get("picture2").toString());
		}
		
		if(Terminal.get("picture3")!=null){
			terminal.setPicture1(Terminal.get("picture3").toString());
		}
		
		if(Terminal.get("remark").toString() !=null){
			terminal.setRemark(Terminal.get("remark").toString());
		}
		String resultString = entityDao.updatePropByID(terminal,
				id) == 1 ? "true" : "false";
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("result", resultString);
		return result;
	}

	@Override
	public List<Map<String, Object>> getType() {
		String[] properties = {
				"type.id",
				"type.type_name"
		};
		String tableName = "type";
		String condition = "1 = 1";
		
		List<Map<String,Object>> resultList = searchDao.searchWithpagingInMysql
				(properties,tableName,null,null,null,condition,null,"type.id", " DESC ", 10
						, 1);
		return resultList;
	}

	@Override
	public List<Map<String, Object>> getArea() {
		String[] properties = {
				"area.id",
				"area.area_name"
		};
		String tableName = "area";
		String condition = "1 = 1";
		
		List<Map<String,Object>> resultList = searchDao.searchWithpagingInMysql
				(properties,tableName,null,null,null,condition,null,"area.id", " DESC ", 10
						, 1);
		return resultList;
	}

	@Override
	public List<Map<String, Object>> getProject() {
		String[] properties = {
				"project.id",
				"project.name"
		};
		String tableName = "project";
		String condition = "1 = 1";
		
		List<Map<String,Object>> resultList = searchDao.searchWithpagingInMysql
				(properties,tableName,null,null,null,condition,null,"project.id", " DESC ", 10
						, 1);
		return resultList;
	}

	@Override
	public Map<String, Object> getTerminal(Map<String, Object> terminalCondition) {
		String[] properties = {
				"*"
		};
		int row = (int) terminalCondition.get("row");
		int page = (int) terminalCondition.get("page");
		String tableName = "terminal_info";
		String condition = "1 = 1";
		if(terminalCondition.get("area_id") != null){
			condition += " and terminal_info.area_id = " + terminalCondition.get("area_id");
		}
		if(terminalCondition.get("station_code") != null){
			condition += " and terminal_info.station_code = " + terminalCondition.get("station_code");
		}
		if(terminalCondition.get("terminal_name") != null){
			condition += " and terminal_info.terminal_name = " + terminalCondition.get("terminal_name");
		}
		List<Map<String,Object>> resultList = searchDao.searchWithpagingInMysql
				(properties,tableName,null,null,null,condition,null,"terminal_info.install_time", " DESC ", row, page);
		
		int count = searchDao.getForeignCount(" terminal_info.id ", " terminal_info ", null, null, null, condition);
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("data", resultList);
		map.put("total", count);
		return map;
	}

	@Override
	public Map<String, Object> getTerminalMessage(String terminalId) {
		String condition = "terminal_info.id ='" + terminalId + "'";
		String baseEntity = "";
		String joinEntity = "";
		entityDao.searchForeign(new String[]{"*"}, baseEntity, joinEntity, null, condition);
		return null;
	}

}
