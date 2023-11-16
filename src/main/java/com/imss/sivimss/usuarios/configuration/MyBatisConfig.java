package com.imss.sivimss.usuarios.configuration;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.imss.sivimss.usuarios.util.Database;

public class MyBatisConfig {

	@Autowired
	private static SqlSessionFactory sqlSessionFactory;
	
	public static  SqlSessionFactory buildqlSessionFactory() {	    
	    try {
	    	sqlSessionFactory =  Database.buildqlSessionFactory();
	    	
	    }catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return sqlSessionFactory;
	}
	private MyBatisConfig() {
	    throw new IllegalStateException("MyBatisConfig class");
	  }
}
