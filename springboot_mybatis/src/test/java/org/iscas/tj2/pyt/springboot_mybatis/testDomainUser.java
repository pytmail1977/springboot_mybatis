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
import org.iscas.tj2.pyt.springboot_mybatis.dao.UserMapper;
import org.iscas.tj2.pyt.springboot_mybatis.domain.Project;
import org.iscas.tj2.pyt.springboot_mybatis.domain.User;
import org.iscas.tj2.pyt.springboot_mybatis.domain.UserKey;
import org.iscas.tj2.pyt.springboot_mybatis.service.DbService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class testDomainUser {

	private DbService db = new DbService();
	
	@Test
	public void testSelect() throws IOException {
		SqlSessionFactory sessionFactory;
		Reader reader = Resources.getResourceAsReader("conf.xml");
		// 构建sqlSession的工厂
		sessionFactory = new SqlSessionFactoryBuilder().build(reader);

		SqlSession session = sessionFactory.openSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			UserKey userkey = new UserKey();
			userkey.setId_User(1);
			userkey.setWeixinId_User("ocaDy1CWp497l5Kq21mkw3HflzaY");

			User user = mapper.selectByPrimaryKey(userkey);
			System.out.println("这里是数据库输出");
			System.out.println(user.getWeixinId_User());

			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
	}
	
    //@Autowired
    //private UserMapper userMapper;
    
    @Test
    public void testInsert() throws Exception {
        
        //Assert.assertEquals(3, UserMapper.getAll().size());
		SqlSessionFactory sessionFactory;
		Reader reader = Resources.getResourceAsReader("conf.xml");
		// 构建sqlSession的工厂
		sessionFactory = new SqlSessionFactoryBuilder().build(reader);

		SqlSession session = sessionFactory.openSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			//mybatis自动生成的insert函数
			//mapper.insert(new User(2,"weixinId","count","passwd","name","phone",2,"email","memo"));
			
			//自己加的Myinsert函数，依赖xml中自定义的sql
			//mapper.Myinsert(new User(3,"weixinId","count","passwd"));
			
			//自己加的MyinsertNoXml函数，不依赖于xml，采用@Insert注解
			mapper.MyinsertNoXml(new User(4,"weixinId","count","passwd"));
			
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
    }
    
	@Test
	/*
	 * 测试使用用户的微信id查询他的用户信息
	 */
	public void testSelectUserByUserWeixinId() throws Exception {
		
		// 反馈用户信息
		System.out.println("从数据库查询用户信息：---------\n");
		if (null == db) {
			System.out.println("db 是空指针");
		}
		User user = db.getUserInfo("ocaDy1CWp497l5Kq21mkw3HflzaY");
		if (null == user) {
			System.out.println("user is null");
			return;		
		}
		String strContent = "Project: " + user.getName_User();
		System.out.println(strContent);
		System.out.println("-----------------");
	}
}
