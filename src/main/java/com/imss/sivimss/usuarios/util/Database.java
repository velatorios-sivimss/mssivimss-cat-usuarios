package com.imss.sivimss.usuarios.util;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.imss.sivimss.usuarios.configuration.mapper.PersonaMapper;
import com.imss.sivimss.usuarios.configuration.mapper.UsuarioMapper;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

@Service
public class Database {
	private static final Logger log = LoggerFactory.getLogger(Database.class);


    public static String urlStatic;
    private static String envUserStatic;
    private static String envPassStatic;
    private static String driverStatic;
    private static String environmentStatic;

	@Value("${spring.datasource.url}") 
	private void setUrl (String url){
    	urlStatic = url;
    }
	@Value("${spring.datasource.username}")
	private void setEnvUSer(String envUser){
		envUserStatic = envUser;
	}
	@Value("${spring.datasource.password}")
	private void setEnvPass(String envPass){
		envPassStatic = envPass;
	}
	@Value("${spring.datasource.driverClassName}") 
	private void setDriver(String driver){ 
		driverStatic = driver; 
	}	 
	@Value("${spring.environment}")
	private void setEnv(String envUser){
		environmentStatic = envUser;
	}
	
	
	public static SqlSessionFactory buildqlSessionFactory() {
        try{
	    DataSource dataSource = new PooledDataSource(driverStatic, urlStatic, envUserStatic, envPassStatic);
	    Environment environment = new Environment(environmentStatic, new JdbcTransactionFactory(), dataSource);	        
	    Configuration configuration = new Configuration(environment);
	    configuration.addMapper(PersonaMapper.class);    
	    configuration.addMapper(UsuarioMapper.class);    
	    SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();	    
	    return builder.build(configuration);
        }catch(Exception e){
            log.info(e.getMessage());
         }
         return null;
	}
}
