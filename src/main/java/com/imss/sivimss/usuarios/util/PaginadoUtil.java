package com.imss.sivimss.usuarios.util;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.imss.sivimss.usuarios.configuration.MyBatisConfig;
import com.imss.sivimss.usuarios.configuration.mapper.ConsultaNativa;


@Service
public class PaginadoUtil {

	@Autowired
	private static MyBatisConfig myBatisConfig;
	
	public static Page<Map<String, Object>> paginado(Integer pagina, Integer tamanio, String query){
		
		Page<Map<String, Object>> objetoMapeado = null;
		SqlSessionFactory sqlSessionFactory = myBatisConfig.buildqlSessionFactory();
		String queryPage = query + " LIMIT " + (pagina*tamanio) + ", " + tamanio;
		String queryConteo = "SELECT COUNT(*) AS conteo FROM (" + query + ") tem";
		List<Map<String, Object>> resp;
		List<Map<String, Object>> respTotal;
		Pageable pageable = PageRequest.of(pagina, tamanio);
		
		
		try(SqlSession session = sqlSessionFactory.openSession()) {
			
			ConsultaNativa consultas = session.getMapper(ConsultaNativa.class);
			resp = consultas.selectPaginado(queryPage);
			respTotal = consultas.selectPaginado(queryConteo);
			
			Integer conteo =  Integer.parseInt( respTotal.get(0).get("conteo").toString() );
			objetoMapeado = new PageImpl<>(resp, pageable, conteo);
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return objetoMapeado;
		
	}
	
}
