package umu.tds.apps.persistencia;

import java.util.List;

import umu.tds.apps.modelo.Usuario;

public interface IAdaptadorUsuarioDAO {
	
	public void registrarUsuario(Usuario usuario);
	public void modificarUsuario(Usuario usuario);
	public Usuario recuperarUsuario(int id);
	public List<Usuario> recuperarTodosUsuarios();
}
