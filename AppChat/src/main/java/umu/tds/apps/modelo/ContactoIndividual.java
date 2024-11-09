package umu.tds.apps.modelo;

public class ContactoIndividual extends Contacto {
	
	// ATRIBUTOS
	private String telefono;
	private Usuario usuario;

	// GETTERS Y SETTERS

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

	// CONSTRUCTOR

	public ContactoIndividual(String nombre, String telefono, Usuario usuario) {
		super(nombre);
		this.telefono = telefono;
		this.usuario = usuario;
	}

}
