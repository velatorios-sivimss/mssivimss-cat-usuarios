package  com.imss.usuarios.service;

import com.imss.usuarios.util.DatosUsuarioDTO;

/**
 * CapturaConuee Service, define el caso de uso del API
 *
 * @author    
 * @puesto dev
 * @date 24 nov. 2022
 */
public interface OoadService 
{	
	
	public Object obtenerOoad(DatosUsuarioDTO datosUsuarios);
	public Object paginado(DatosUsuarioDTO datosUsuarios, Integer pagina, Integer tamanio, String sort, String columna);
	
}
