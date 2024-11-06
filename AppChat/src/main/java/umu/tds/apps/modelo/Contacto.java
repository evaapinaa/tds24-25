package umu.tds.apps.modelo;

public class Contacto {
	
	// ATRIBUTOS
	private String usuario;
	
	
	// GETTERS Y SETTERS
	
	public String getUsuario() {
		return usuario;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public Contacto(String nombre) {
		this.usuario = nombre;
	}

}
