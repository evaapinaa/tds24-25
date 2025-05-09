package umu.tds.apps.persistencia;

import java.util.List;

import umu.tds.apps.modelo.Usuario;

/**
 * Interface que define los métodos para la persistencia de los objetos Usuario.
 */
public interface IAdaptadorUsuarioDAO {
	
    /**
     * Registra un usuario en la base de datos.
     * 
     * @param usuario El objeto Usuario que se desea registrar.
     */
	public void registrarUsuario(Usuario usuario);
	
    /**
     * Modifica un usuario existente en la base de datos.
     * 
     * @param usuario El objeto Usuario con los datos actualizados.
     */
	public void modificarUsuario(Usuario usuario);
	
    /**
     * Recupera un usuario de la base de datos por su identificador.
     * 
     * @param id El identificador del usuario a recuperar.
     * @return El objeto Usuario recuperado, o null si no existe.
     */
	public Usuario recuperarUsuario(int id);
	
    /**
     * Recupera todos los usuarios almacenados en la base de datos.
     * 
     * @return Una lista con todos los objetos Usuario recuperados.
     */
	public List<Usuario> recuperarTodosUsuarios();
}
