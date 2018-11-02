package org.iscas.tj2.pyt.springboot_mybatis.service;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.iscas.tj2.pyt.springboot_mybatis.dao.PermissionMapper;
import org.iscas.tj2.pyt.springboot_mybatis.dao.ProjectMapper;
import org.iscas.tj2.pyt.springboot_mybatis.dao.RoleMapper;
import org.iscas.tj2.pyt.springboot_mybatis.dao.RolePRMSRelationMapper;
import org.iscas.tj2.pyt.springboot_mybatis.dao.UserMapper;
import org.iscas.tj2.pyt.springboot_mybatis.dao.UserRoleRelationMapper;
import org.iscas.tj2.pyt.springboot_mybatis.domain.Permission;
import org.iscas.tj2.pyt.springboot_mybatis.domain.Project;
import org.iscas.tj2.pyt.springboot_mybatis.domain.Role;
import org.iscas.tj2.pyt.springboot_mybatis.domain.RolePRMSRelation;
import org.iscas.tj2.pyt.springboot_mybatis.domain.User;
import org.iscas.tj2.pyt.springboot_mybatis.domain.UserRoleRelation;
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
    
    public int createProject(int idUser,Project proj) {
    	SqlSession session = sessionFactory.openSession();
    	int idProject = 0;
    	int idPermission = 0;
    	int idRole = 0;
    	int idRolePRMSRelation = 0;
    	int idUserRoleRelation = 0;
    	int rightPermission = 1;
    	ProjectMapper projectMapper = session.getMapper(ProjectMapper.class);
    	PermissionMapper permissionMapper = session.getMapper(PermissionMapper.class);
    	RoleMapper roleMapper = session.getMapper(RoleMapper.class);
    	RolePRMSRelationMapper rolePRMSRelationMapper = session.getMapper(RolePRMSRelationMapper.class);
    	UserRoleRelationMapper userRoleRelationMapper = session.getMapper(UserRoleRelationMapper.class);
    	try {
    		//插入Project表
    		projectMapper.insertSelective(proj);
    		idProject = proj.getIdProject();
    		if (0 == idProject) {
    			return -1;
    		}   		    		
    		//插入 Permission表
    		Permission permission = new Permission();
    		permission.setIdProject(idProject);
    		permission.setRightPermission(rightPermission);
    		permissionMapper.insertSelective(permission);
    		idPermission = permission.getIdPermission();
    		if (0 == idPermission) {
    			session.rollback();
    			return -2;
    		} 
    		//插入 Role表
       		Role role = new Role();
    		role.setNameRole("creatorOfProject_"+idProject);
    		roleMapper.insertSelective(role);
    		idRole = role.getIdRole();
    		if (0 == idRole) {
    			session.rollback();
    			return -3;
    		} 	
    		//插入 RolePRMSRelation表
      		RolePRMSRelation rolePRMSRelation = new RolePRMSRelation();
    		rolePRMSRelation.setIdPermission(idPermission);
    		rolePRMSRelation.setIdRole(idRole);
    		rolePRMSRelationMapper.insertSelective(rolePRMSRelation);
    		idRolePRMSRelation = rolePRMSRelation.getIdRoleprmsrelation();
    		if (0 == idRolePRMSRelation) {
    			session.rollback();
    			return -4;
    		} 
    		//插入UserRoleRelation
      		UserRoleRelation userRoleRelation = new UserRoleRelation();
    		userRoleRelation.setIdRole(idRole);
    		userRoleRelation.setIdUser(idUser);
    		userRoleRelationMapper.insertSelective(userRoleRelation);
    		idUserRoleRelation = userRoleRelation.getIdUserrolerelation();
    		if (0 == idUserRoleRelation) {
    			session.rollback();
    			return -5;
    		} 
    		session.commit();
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
    	}                 
    	return 0;
    }   

    
    

}
