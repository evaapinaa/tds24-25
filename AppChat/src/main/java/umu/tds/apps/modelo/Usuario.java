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
	private Optional<String> saludo;  // Saludo es opcional
	private String imagenPerfil;
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
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	public Optional<String> getSaludo() {
		return saludo;
	}
	
	public void setSaludo(Optional<String> saludo) {
		this.saludo = saludo;
	}
	
	public String getImagenPerfil() {
		return imagenPerfil;
	}
	
	public void setImagenPerfil(String imagenPerfil) {
		this.imagenPerfil = imagenPerfil;
	}
	
	
	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}
	
	// Método para obtener el número de mensajes enviados en el último mes (DESCUENTO)
	public int getNumeroMensajesUltimoMes() {
	    LocalDate ahora = LocalDate.now();
	    LocalDate inicioDelMes = ahora.withDayOfMonth(1);

	    return (int) listaMensajes.stream()
	        .filter(mensaje -> {
	            LocalDate fechaEnvio = mensaje.getFecha();
	            return (fechaEnvio.isEqual(inicioDelMes) || (fechaEnvio.isAfter(inicioDelMes) && fechaEnvio.isBefore(ahora.plusDays(1))));
	        })
	        .count();
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
	
    // CONSTRUCTOR
	
	public Usuario(String usuario, String contraseña, String telefono, String email, Optional<String> saludo, String imagenPerfil, LocalDate fechaNacimiento) {
		this.usuario = usuario;
		this.contraseña = contraseña;
		this.telefono = telefono;
		this.email = email;
		this.saludo = saludo;
		this.imagenPerfil = imagenPerfil;
		this.fechaNacimiento = fechaNacimiento;
		this.listaContactos = new LinkedList<Contacto>();
		this.listaMensajes = new LinkedList<Mensaje>();
		
	}
    
    
}
