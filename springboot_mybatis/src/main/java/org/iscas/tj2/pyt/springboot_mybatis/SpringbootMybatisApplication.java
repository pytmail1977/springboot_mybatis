package org.iscas.tj2.pyt.springboot_mybatis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//因为添加了数据库组件，所以autoconfig会去读取数据源配置，新建的项目还没有配置数据源，会导致异常出现。需要在启动类的@EnableAutoConfiguration或@SpringBootApplication中添加exclude
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//@SpringBootApplication
public class SpringbootMybatisApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootMybatisApplication.class, args);
	}
}
