package org.iscas.tj2.pyt.springboot_mybatis.dao;

import org.iscas.tj2.pyt.springboot_mybatis.domain.VarItem;

public interface VarItemMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table VarItem
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	int deleteByPrimaryKey(Integer idVaritem);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table VarItem
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	int insert(VarItem record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table VarItem
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	int insertSelective(VarItem record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table VarItem
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	VarItem selectByPrimaryKey(Integer idVaritem);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table VarItem
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	int updateByPrimaryKeySelective(VarItem record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table VarItem
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	int updateByPrimaryKey(VarItem record);
}