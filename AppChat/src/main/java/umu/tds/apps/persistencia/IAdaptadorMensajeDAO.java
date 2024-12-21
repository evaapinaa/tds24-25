package umu.tds.apps.persistencia;

import java.util.List;

import umu.tds.apps.modelo.Mensaje;

public interface IAdaptadorMensajeDAO {
	
	public void registrarMensaje(Mensaje mensaje);
	public Mensaje recuperarMensaje(int id);
	public List<Mensaje> recuperarTodosMensajes();
}
