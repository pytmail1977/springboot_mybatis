package org.iscas.tj2.pyt.springboot_mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

//因为添加了数据库组件，所以autoconfig会去读取数据源配置，新建的项目还没有配置数据源，会导致异常出现。
//需要在启动类的@EnableAutoConfiguration或@SpringBootApplication中添加exclude
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//@SpringBootApplication
@MapperScan("org.iscas.tj2.pyt.springboot_mybatis.dao.FuncProjectRelationMapper")
//public class SpringbootMybatisApplication {
public class SpringbootMybatisApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootMybatisApplication.class, args);
	}

	@Override//为了打包springboot项目
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// TODO Auto-generated method stub
		//return super.configure(builder);
		return builder.sources(this.getClass());
	}
	
}
