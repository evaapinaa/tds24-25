
package umu.tds.apps.modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import umu.tds.apps.persistencia.DAOException;
import umu.tds.apps.persistencia.FactoriaDAO;
import umu.tds.apps.persistencia.IAdaptadorUsuarioDAO;

// Patrón Singleton. No permitir crear más de una instancia de la clase. Impedir que se pueda hacer new RepositorioUsuarios()
// Constructor privado

/**
 * Repositorio que gestiona todos los usuarios del sistema.
 * Implementa el patrón Singleton.
 */

public class RepositorioUsuarios {
	private Map<String,Usuario> usuarios; 
	private static RepositorioUsuarios unicaInstancia = new RepositorioUsuarios();
	
	private FactoriaDAO dao;
	private IAdaptadorUsuarioDAO adaptadorUsuario;
	
	private RepositorioUsuarios() {
		usuarios = new HashMap<String,Usuario>();
		try {
  			dao = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
  			adaptadorUsuario = dao.getUsuarioDAO();
  			
  			this.cargarCatalogo();
  		} catch (DAOException eDAO) {
  			eDAO.printStackTrace();
  		}
	}
	
    /**
     * Obtiene la única instancia del repositorio (patrón Singleton).
     * 
     * @return Instancia única del repositorio
     */
	public static RepositorioUsuarios getUnicaInstancia(){
		return unicaInstancia;
	}
	
	 /**
     * Devuelve todos los usuarios registrados en el sistema.
     * 
     * @return Lista de todos los usuarios
     */
	public List<Usuario> getUsuarios(){
		ArrayList<Usuario> lista = new ArrayList<Usuario>();
		for (Usuario u:usuarios.values()) 
			lista.add(u);
		return lista;
	}
	
	
    /**
     * Obtiene un usuario por su código identificador.
     * 
     * @param codigo Código del usuario
     * @return Usuario encontrado o null si no existe
     */
	public Usuario getUsuario(int codigo) {
		for (Usuario c:usuarios.values()) {
			if (c.getCodigo()==codigo) return c;
		}
		return null;
	}
	
    /**
     * Obtiene un usuario por su número de teléfono.
     * 
     * @param telefono Número de teléfono
     * @return Usuario encontrado o null si no existe
     */
	public Usuario getUsuario(String telefono) {
		return usuarios.get(telefono); 
	}
	
	
	  /**
     * Añade un nuevo usuario al repositorio.
     * 
     * @param usu Usuario a añadir
     */
	public void addUsuario(Usuario usu) {
		usuarios.put(usu.getTelefono(),usu);
	}
	
    /**
     * Elimina un usuario del repositorio.
     * 
     * @param usu Usuario a eliminar
     */
	public void removeCliente (Usuario usu) {
		usuarios.remove(usu.getTelefono());
	}
	
	/*Recupera todos los clientes para trabajar con ellos en memoria*/
	private void cargarCatalogo() throws DAOException {
		 List<Usuario> usuariosBD = adaptadorUsuario.recuperarTodosUsuarios();
		 for (Usuario usu: usuariosBD) 
			     usuarios.put(usu.getTelefono(),usu);
	}
	
}
