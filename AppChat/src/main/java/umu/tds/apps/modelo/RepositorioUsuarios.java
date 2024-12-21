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
	
	public static RepositorioUsuarios getUnicaInstancia(){
		return unicaInstancia;
	}
	
	/*devuelve todos los usuarios*/
	public List<Usuario> getUsuarios(){
		ArrayList<Usuario> lista = new ArrayList<Usuario>();
		for (Usuario u:usuarios.values()) 
			lista.add(u);
		return lista;
	}
	
	public Usuario getUsuario(int codigo) {
		for (Usuario c:usuarios.values()) {
			if (c.getCodigo()==codigo) return c;
		}
		return null;
	}
	public Usuario getUsuario(String telefono) {
		return usuarios.get(telefono); 
	}
	
	public void addUsuario(Usuario usu) {
		usuarios.put(usu.getTelefono(),usu);
	}
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
