package org.iscas.tj2.pyt.springboot_mybatis.dao;

import org.iscas.tj2.pyt.springboot_mybatis.domain.FuncProjectRelation;

public interface FuncProjectRelationMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table FuncProjectRelation
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	int deleteByPrimaryKey(Integer idFuncprojectrelation);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table FuncProjectRelation
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	int insert(FuncProjectRelation record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table FuncProjectRelation
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	int insertSelective(FuncProjectRelation record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table FuncProjectRelation
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	FuncProjectRelation selectByPrimaryKey(Integer idFuncprojectrelation);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table FuncProjectRelation
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	int updateByPrimaryKeySelective(FuncProjectRelation record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table FuncProjectRelation
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	int updateByPrimaryKey(FuncProjectRelation record);
}