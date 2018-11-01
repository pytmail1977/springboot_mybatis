package org.iscas.tj2.pyt.springboot_mybatis.service;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.iscas.tj2.pyt.springboot_mybatis.dao.ProjectMapper;
import org.iscas.tj2.pyt.springboot_mybatis.dao.RoleMapper;
import org.iscas.tj2.pyt.springboot_mybatis.dao.UserMapper;
import org.iscas.tj2.pyt.springboot_mybatis.domain.Project;
import org.iscas.tj2.pyt.springboot_mybatis.domain.Role;
import org.iscas.tj2.pyt.springboot_mybatis.domain.User;
import org.springframework.stereotype.Service;

@Service("DbService")
public class DbService {
	private SqlSessionFactory sessionFactory;//
	
    public DbService() {
		super();
		//SqlSessionFactory sessionFactory;
		Reader reader;
		try {
			reader = Resources.getResourceAsReader("conf.xml");
			// 构建sqlSession的工厂
			this.sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

    public User getUserInfo(String strWeixinId) {
    	SqlSession session = sessionFactory.openSession();
    	User user = null;
    	UserMapper mapper = session.getMapper(UserMapper.class);
    	try {
    		user = mapper.selectUserByWeixinId(strWeixinId);
    		session.commit();
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
    	}                 
    	return user;
    }	
    
    public List<Project> getProjectsInfo(String strWeixinId) {
    //public Project getProjectsInfo(String strWeixinId) {
    	SqlSession session = sessionFactory.openSession();
    	List<Project> projects = null;
    	//Project projects = null;
    	ProjectMapper mapper = session.getMapper(ProjectMapper.class);
    	try {
    		projects = mapper.selectProjectsByWeixinId(strWeixinId);
    		session.commit();
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
    	}                 
    	return projects;
    }
    
    public Role getRoleInfo(Integer intRoleId) {
    	SqlSession session = sessionFactory.openSession();
    	Role role = null;
    	RoleMapper mapper = session.getMapper(RoleMapper.class);
    	try {
    		//role = mapper.selectByPrimaryKey(intRoleId);
    		role = mapper.selectByIdRole(intRoleId);
    		session.commit();
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
    	}                 
    	return role;
    }   
    
    

}
