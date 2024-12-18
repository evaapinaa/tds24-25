package umu.tds.apps.modelo;


public class Contacto {
	
	// ATRIBUTOS
	private String nombre;
	private Usuario usuario;
	
	
	// GETTERS Y SETTERS
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Contacto(String nombre) {
		this.nombre = nombre;
	}

}
