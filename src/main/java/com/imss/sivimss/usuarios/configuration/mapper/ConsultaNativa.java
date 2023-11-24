package com.imss.sivimss.usuarios.configuration.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;


@Repository
public interface ConsultaNativa {
	static class PureSqlProvider{
        public String sql(String sql) {
            return sql;
        }
 
        public String count(String from) {
            return "SELECT count(*) FROM " + from;
        }
    }
	@SelectProvider(type = PureSqlProvider.class, method = "sql")
	public List<Map<String, Object>> selectPaginado(String sql );
}