package org.iscas.tj2.pyt.springboot_mybatis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.iscas.tj2.pyt.springboot_mybatis.domain.StructItem;

public interface StructItemMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table StructItem
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	int deleteByPrimaryKey(Integer idStructitem);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table StructItem
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	int insert(StructItem record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table StructItem
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	int insertSelective(StructItem record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table StructItem
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	StructItem selectByPrimaryKey(Integer idStructitem);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table StructItem
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	int updateByPrimaryKeySelective(StructItem record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table StructItem
	 * @mbg.generated  Sun Dec 02 10:35:33 CST 2018
	 */
	int updateByPrimaryKey(StructItem record);
	
	//2018-12-03 增加
	List<StructItem> selectStructItemsByStructId(@Param("IdStruct") int intStructId);
}