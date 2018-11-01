package org.iscas.tj2.pyt.springboot_mybatis;

import org.iscas.tj2.pyt.springboot_mybatis.domain.Role;
import org.iscas.tj2.pyt.springboot_mybatis.domain.User;
import org.iscas.tj2.pyt.springboot_mybatis.service.DbService;
import org.junit.Test;

public class testDomainRole {
	private DbService db = new DbService();
	@Test
	/*
	 * 测试使用用户的微信id查询他的用户信息
	 */
	public void testSelectRoleByRoleId() throws Exception {
		
		// 反馈用户信息
		System.out.println("从数据库查询角色信息：---------\n");
		if (null == db) {
			System.out.println("db 是空指针");
		}
		Role role = db.getRoleInfo(1);
		if (null == role) {
			System.out.println("role is null");
			return;		
		}
		String strContent = "Role: " + role.getNameRole();
		System.out.println(strContent);
		System.out.println("-----------------");
	}
}
