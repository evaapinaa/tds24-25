package umu.tds.apps.modelo;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Usuario {
	
	// ATRIBUTOS
	
	private String usuario;
	private String contraseña;
	private String telefono;
	private String email;
	private Optional<String> imagenPerfil;  // Foto es opcional
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
		return contraseña;
	}
	
	public void setContaseña(String contaseña) {
		this.contraseña = contaseña;
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
		return new LinkedList<Contacto>(listaContactos); // comprobar
	}
	
	public void setListaContactos(List<Contacto> listaContactos) {
		this.listaContactos = listaContactos;
	}
	
	public List<Mensaje> getListaMensajes() {
		return new LinkedList<Mensaje>(listaMensajes); // comprobar
	}
	
	public void setListaMensajes(List<Mensaje> listaMensajes) {
		this.listaMensajes = listaMensajes;
	}
	
    public Usuario(String nombre, LocalDate fechaNacimiento, String email, String telefono, String contraseña, Optional<String> imagenPerfil) {
        this.usuario = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.email = email;
        this.telefono = telefono;
        this.contraseña = contraseña;
        this.imagenPerfil = imagenPerfil;
        this.listaContactos = new LinkedList<Contacto>();
        this.listaMensajes = new LinkedList<Mensaje>();
    }
}
