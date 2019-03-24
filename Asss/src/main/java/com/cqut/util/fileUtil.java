package com.cqut.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import eu.bitwalker.useragentutils.UserAgent;

public class fileUtil {

   public static String saveFile(MultipartFile file,String path) { 
       // 判断文件是否为空  
       if (!file.isEmpty()) {  
           try {  
               File filepath = new File(path);
               if (!filepath.exists()) 
                   filepath.mkdirs();
               String name =  System.currentTimeMillis()+ "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
               // 文件保存路径  
               String savePath = path + "/" + name;  
               // 转存文件  
               file.transferTo(new File(savePath));  
               return name;  
           } catch (Exception e) {  
               e.printStackTrace();  
           }  
       }  
       return "false";  
   }
   
   public static boolean deleteFile(String path){
	   File file = new File("path");
	   return file.delete();
   }
   
   /**
    * 文件下载
    *
    * @param path 文件的路径
    * @return 上传结果
    */
   public static void downloadFile(HttpServletResponse response, HttpServletRequest request, String path) throws IOException {
       File file = new File(path);

       //判断文件是否存在
       if(!file.exists()) {
           return;
       }

       //判断文件类型
       String mimeType = URLConnection.guessContentTypeFromName(file.getName());
       if(mimeType == null) {
           mimeType = "application/octet-stream";
       }
       response.setContentType(mimeType);

       //设置文件响应大小
       response.setContentLengthLong(file.length());

       //文件名编码，解决乱码问题
       String fileName = file.getName();
       String encodedFileName = null;
       String userAgentString = request.getHeader("User-Agent");
       String browser = UserAgent.parseUserAgentString(userAgentString).getBrowser().getGroup().getName();
       if(browser.equals("Chrome") || browser.equals("Internet Exploer") || browser.equals("Safari")) {
           encodedFileName = URLEncoder.encode(fileName,"utf-8").replaceAll("\\+", "%20");
       } else {
           encodedFileName = MimeUtility.encodeWord(fileName);
       }

       //设置Content-Disposition响应头，一方面可以指定下载的文件名，另一方面可以引导浏览器弹出文件下载窗口
       response.setHeader("Content-Disposition", "attachment;fileName=\"" + encodedFileName + "\"");

       //文件下载
       InputStream in = new BufferedInputStream(new FileInputStream(file));
       FileCopyUtils.copy(in, response.getOutputStream());
   }

}
