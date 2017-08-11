package com.bw30.open.wftAdm.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.bw30.open.common.model.WftConnSession;
import com.bw30.open.wft.mongo.MongoDBService;

@Controller
public class TestController {
	@Resource
	private MongoDBService mongoDBService;
	
	@RequestMapping("insert.do")
	public void insertMongodb(HttpServletRequest request,HttpServletResponse response){
		File file = new File("/opt/tomcat/10006.log");
		BufferedReader reader = null;
		try {
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			int lint =2;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				System.out.println("line " + line + ": " + tempString);
				int index = tempString.indexOf(";");
				String str = tempString.substring(index + 1);
				System.out.println("line " + line + ": " + str);
				WftConnSession wftConnSession = JSON.parseObject(str,
						WftConnSession.class);
				Query query = new Query();
				query.addCriteria(Criteria.where("csid").is(wftConnSession.getCsid()));
				List<WftConnSession> list = mongoDBService.find(query, WftConnSession.class, MongoDBService.CONNDATAKEY+"10006");
				if(null !=list && list.size()>0){
					continue;
				}else{
					mongoDBService.save(wftConnSession, MongoDBService.CONNDATAKEY+"10006");
					lint++;
				}
				line++;
			}
			reader.close();
			System.out.println("line="+line +"========lint="+lint);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	public void setMongoDBService(MongoDBService mongoDBService) {
		this.mongoDBService = mongoDBService;
	}
	
}
