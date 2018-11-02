package org.iscas.tj2.pyt.springboot_mybatis;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.iscas.tj2.pyt.springboot_mybatis.dao.ProjectMapper;
import org.iscas.tj2.pyt.springboot_mybatis.domain.Project;
import org.iscas.tj2.pyt.springboot_mybatis.domain.User;
import org.iscas.tj2.pyt.springboot_mybatis.domain.UserKey;
import org.iscas.tj2.pyt.springboot_mybatis.service.DbService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class testDomainProject {
	
	//@Autowired
	private DbService db = new DbService();

	/*
	 * 测试使用用户的微信id查询他有权限处理的工程
	 */
	@Test
	public void testSelectProjectByUserWeixinId() throws Exception {
		
		// 反馈用户的工程信息
		System.out.println("从数据库查询用户的工程信息：---------\n");
		if (null == db) {
			System.out.println("db 是空指针");
		}
		/*
		Project  projects = db.getProjectsInfo("ocaDy1CWp497l5Kq21mkw3HflzaY");
		if(null == projects) {
			System.out.println("project is null");
			return;
		}
		String strContent = "Project: ";
		strContent += projects.getNameProject();	
		*/
		
		List<Project> projects = db.getProjectsInfo("ocaDy1CWp497l5Kq21mkw3HflzaY");
		String strContent = "Project: ";
		
		if(null == projects) {
			System.out.println("projects is null");
			return;
		}else {
			int size = projects.size();
			System.out.println("number of projects:"+size+"\n");
		}
		
		Iterator <Project> iter= projects.iterator();
		
		while(iter.hasNext()) {
			Project project = iter.next();
			if (null != project) {
				String strTmp = project.getNameProject();
				System.out.println("project id:"+project.getIdProject()+"\n");
				strContent += iter.next().getNameProject();
			}else {
				System.out.println("project is null!\n");
			}
		}
		

		System.out.println(strContent);
		System.out.println("-----------------");
	}
	
	@Test
	public void testInsertProject() {
       	Project proj = new Project();
    	proj.setNameProject("NameProject");
    	proj.setMemoProject("MemoProject");
    	proj.setVersionProject("VerstionProject");
    	db.createProject(1,proj);
	}
	
}
