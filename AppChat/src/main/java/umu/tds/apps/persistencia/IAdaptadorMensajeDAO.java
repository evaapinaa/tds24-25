package umu.tds.apps.persistencia;

import java.util.List;

import umu.tds.apps.modelo.Mensaje;

/**
 * Interface que define los métodos para la persistencia de los objetos Mensaje.
 */
public interface IAdaptadorMensajeDAO {
	
    /**
     * Registra un mensaje en la base de datos.
     * 
     * @param mensaje El objeto Mensaje que se desea registrar.
     */
	public void registrarMensaje(Mensaje mensaje);
	
    /**
     * Recupera un mensaje de la base de datos por su identificador.
     * 
     * @param id El identificador del mensaje a recuperar.
     * @return El objeto Mensaje recuperado, o null si no existe.
     */
	public Mensaje recuperarMensaje(int id);
	
    /**
     * Recupera todos los mensajes almacenados en la base de datos.
     * 
     * @return Una lista con todos los objetos Mensaje recuperados.
     */
	public List<Mensaje> recuperarTodosMensajes();
}
