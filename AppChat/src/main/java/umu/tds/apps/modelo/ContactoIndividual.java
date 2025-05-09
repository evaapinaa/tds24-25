
package umu.tds.apps.modelo;

/**
 * Clase que representa un contacto individual en la lista de contactos de un usuario.
 * Extiende la clase Contacto.
*/

public class ContactoIndividual extends Contacto {
	
	// ATRIBUTOS
	private String telefono;
	private Usuario usuario;

	// GETTERS Y SETTERS

	   
    /**
     * Obtiene el número de teléfono del contacto.
     * 
     * @return Número de teléfono
     */
	public String getTelefono() {
		return telefono;
	}

	 
    /**
     * Establece el número de teléfono del contacto.
     * 
     * @param telefono Nuevo número de teléfono
     */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
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

	// CONSTRUCTOR

    /**
     * Crea un nuevo contacto individual.
     * 
     * @param nombre Nombre del contacto
     * @param telefono Número de teléfono del contacto
     * @param usuario Usuario asociado al contacto
     */
	public ContactoIndividual(String nombre, String telefono, Usuario usuario) {
		super(nombre);
		this.telefono = telefono;
		this.usuario = usuario;
	}

}
