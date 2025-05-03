
package umu.tds.apps.persistencia;

import java.util.List;

import umu.tds.apps.modelo.Grupo;

/**
 * Interface que define los métodos para la persistencia de los objetos Grupo.
 */
public interface IAdaptadorGrupoDAO {

	
	/**
     * Registra un grupo en la base de datos.
     * 
     * @param grupo El objeto Grupo que se desea registrar.
     */
	public void registrarGrupo(Grupo grupo);
	
    /**
     * Recupera un grupo de la base de datos por su código.
     * 
     * @param codigo El código identificador del grupo a recuperar.
     * @return El objeto Grupo recuperado, o null si no existe.
     */
	public Grupo recuperarGrupo(int codigo);
	
    /**
     * Recupera todos los grupos almacenados en la base de datos.
     * 
     * @return Una lista con todos los objetos Grupo recuperados.
     */
	public List<Grupo> recuperarTodosGrupos();
	
    /**
     * Modifica un grupo existente en la base de datos.
     * 
     * @param grupo El objeto Grupo con los datos actualizados.
     */
	public void modificarGrupo(Grupo grupo);
}
