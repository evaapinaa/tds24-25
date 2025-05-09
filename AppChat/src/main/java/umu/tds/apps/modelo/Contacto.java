
package umu.tds.apps.modelo;

/**
 * Clase abstracta que representa un contacto genérico en el sistema.
 * Puede ser un ContactoIndividual o un Grupo.
*/
public class Contacto {
	
	// ATRIBUTOS
	private int codigo;
	private String nombre;
	private Usuario usuario;
	
	
	// GETTERS Y SETTERS
	 /**
     * Obtiene el código identificador del contacto.
     * 
     * @return Código del contacto
     */
	public int getCodigo() {
		return codigo;
	}
	
	
	 /**
     * Establece el código identificador del contacto.
     * 
     * @param codigo Nuevo código
     */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	
	 /**
     * Obtiene el nombre del contacto.
     * 
     * @return Nombre del contacto
     */
	public String getNombre() {
		return nombre;
	}
	
	 /**
     * Establece el nombre del contacto.
     * 
     * @param nombre Nuevo nombre
     */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	 
     /**
     * Obtiene el usuario asociado al contacto.
     * 
     * @return Usuario asociado
     */
	public Usuario getUsuario() {
		return usuario;
	}
	
	 /**
     * Establece el usuario asociado al contacto.
     * 
     * @param usuario Nuevo usuario
     */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
    /**
     * Crea un nuevo contacto con el nombre especificado.
     * 
     * @param nombre Nombre del contacto
     */
	public Contacto(String nombre) {
		this.nombre = nombre;
	}

}
