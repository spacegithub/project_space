package com.mr.data;

import com.mr.data.common.base.mapper.BaseMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement // 开启注解事务管理，等同于xml配置文件中的 <tx:annotation-driven />
@MapperScan(basePackages = "com.mr.*", markerInterface = BaseMapper.class)
public class MrDataFactoryHmdApplication {

	public static void main(String[] args) {
		SpringApplication.run(MrDataFactoryHmdApplication.class, args);
	}
}
