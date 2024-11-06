package umu.tds.apps.modelo;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Usuario {
	
	// ATRIBUTOS
	
	private String usuario;
	private String contaseña;
	private String telefono;
	private LocalDate fechaNacimiento;
	private List<Contacto> listaContactos;
	private List<Mensaje> listaMensajes;
	
	// GETTERS AND SETTERS
	
	public String getUsuario() {
		return usuario;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getContaseña() {
		return contaseña;
	}
	
	public void setContaseña(String contaseña) {
		this.contaseña = contaseña;
	}
	
	public String getTelefono() {
		return telefono;
	}
	
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}
	
	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	
	public List<Contacto> getListaContactos() {
		return new LinkedList<Contacto>(listaContactos); // Comprobar
	}
	
	public void setListaContactos(List<Contacto> listaContactos) {
		this.listaContactos = listaContactos;
	}
	
	public List<Mensaje> getListaMensajes() {
		return new LinkedList<Mensaje>(listaMensajes); // Comprobar
	}
	
	public void setListaMensajes(List<Mensaje> listaMensajes) {
		this.listaMensajes = listaMensajes;
	}
	
	public Usuario(String nombre) {
		this.usuario = nombre;
	}
	
	
}
