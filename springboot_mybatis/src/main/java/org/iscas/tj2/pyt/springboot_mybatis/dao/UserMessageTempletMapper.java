package org.iscas.tj2.pyt.springboot_mybatis.dao;

import org.iscas.tj2.pyt.springboot_mybatis.domain.UserMessageTemplet;

public interface UserMessageTempletMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table UserMessageTemplet
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	int deleteByPrimaryKey(Integer idUsermessagetemplet);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table UserMessageTemplet
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	int insert(UserMessageTemplet record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table UserMessageTemplet
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	int insertSelective(UserMessageTemplet record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table UserMessageTemplet
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	UserMessageTemplet selectByPrimaryKey(Integer idUsermessagetemplet);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table UserMessageTemplet
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	int updateByPrimaryKeySelective(UserMessageTemplet record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table UserMessageTemplet
	 * @mbg.generated  Fri Oct 12 09:57:46 CST 2018
	 */
	int updateByPrimaryKey(UserMessageTemplet record);
}