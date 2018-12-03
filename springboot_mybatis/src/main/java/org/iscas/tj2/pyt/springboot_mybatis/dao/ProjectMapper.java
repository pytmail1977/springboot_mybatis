package org.iscas.tj2.pyt.springboot_mybatis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.iscas.tj2.pyt.springboot_mybatis.domain.Project;
import org.iscas.tj2.pyt.springboot_mybatis.domain.User;

public interface ProjectMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table Project
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	int deleteByPrimaryKey(Integer idProject);


	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table Project
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	int insert(Project record);


	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table Project
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	int insertSelective(Project record);


	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table Project
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	Project selectByPrimaryKey(Integer idProject);


	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table Project
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	int updateByPrimaryKeySelective(Project record);


	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table Project
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	int updateByPrimaryKey(Project record);


	/**
	 * 自己加的，按照weixinId从数据库中取出工程
	 * SELECT Project.* FROM User,Role,UserRoleRelation,Permission,RolePRMSRelation,Project
		WHERE User.WeixinId_User = "ocaDy1CWp497l5Kq21mkw3HflzaY"
		AND User.Id_User = UserRoleRelation.Id_User 
		AND UserRoleRelation.Id_Role = Role.Id_Role 
		AND Role.Id_Role = RolePRMSRelation.Id_Role 
		AND RolePRMSRelation.Id_Permission = Permission.Id_Permission 
		AND Permission.Id_Project = Project.Id_Project ;

	 */
	/*
    @Select("SELECT Project.* FROM User,Role,UserRoleRelation,Permission,RolePRMSRelation,Project\n" + 
    		"WHERE User.WeixinId_User = #{weixinId}\n" + 
    		"AND User.Id_User = UserRoleRelation.Id_User \n" + 
    		"AND UserRoleRelation.Id_Role = Role.Id_Role \n" + 
    		"AND Role.Id_Role = RolePRMSRelation.Id_Role \n" + 
    		"AND RolePRMSRelation.Id_Permission = Permission.Id_Permission \n" + 
    		"AND Permission.Id_Project = Project.Id_Project ;\n"
    		)
    		*/
	 List<Project> selectProjectsByWeixinId(@Param("weixinId") String strWeixinId);
	 //@Select("SELECT * FROM Project WHERE Id_Project = 1;")
	 //Project selectProjectsByWeixinId(@Param("weixinId") String strWeixinId);
	 
	 
	 List<Project> selectProjectsByUserId(@Param("IdUser") int intUserId);
	 

}