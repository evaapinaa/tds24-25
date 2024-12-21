package umu.tds.apps.modelo;


public class Contacto {
	
	// ATRIBUTOS
	private int codigo;
	private String nombre;
	private Usuario usuario;
	
	
	// GETTERS Y SETTERS
	public int getCodigo() {
		return codigo;
	}
	
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
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
