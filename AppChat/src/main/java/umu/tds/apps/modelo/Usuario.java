package umu.tds.apps.modelo;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.ImageIcon;

public class Usuario {
	
	// ATRIBUTOS
	
	private String usuario;
	private String contraseña;
	private String telefono;
	private String email;
	private Optional<String> saludo;  // Saludo es opcional
	private ImageIcon imagenPerfil;
	private LocalDate fechaNacimiento;
	private List<Contacto> listaContactos;
	private List<Mensaje> listaMensajesEnviados;
	private List<Mensaje> listaMensajesRecibidos;
	private List<Chat> listaChats;
	private boolean premium;
	private int codigo;
	
	// GETTERS AND SETTERS
	
	public String getUsuario() {
		return usuario;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getContraseña() {
		return contraseña;
	}
	
	public void setContraseña(String contaseña) {
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
	
	public ImageIcon getImagenPerfil() {
		return imagenPerfil;
	}
	
	public void setImagenPerfil(ImageIcon imagenPerfil) {
		this.imagenPerfil = imagenPerfil;
	}
	
	
	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}
	
	public int getCodigo() {
		return codigo;
	}
	
	public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
	
	public boolean isPremium() {
		return premium;
	}
	
	public void setPremium(boolean premium) {
		this.premium = premium;
	}
	
	public List<Chat> getListaChats() {
		return listaChats;
	}
	
	public void setListaChats(List<Chat> listaChats) {
		this.listaChats = listaChats;
	}

	// Método para comprobar si la clave es válida
	public boolean isClaveValida(String contraseña) {
		return this.contraseña.equals(contraseña);
	}
	
	
	// Método para obtener el número de mensajes enviados en el último mes (DESCUENTO)
	public long getNumeroMensajesUltimoMes() {
	    LocalDate ahora = LocalDate.now();
	    LocalDate inicioDelMes = ahora.withDayOfMonth(1);

	    return listaMensajesEnviados.stream()
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
	
	public List<Mensaje> getListaMensajesEnviados() {
		return new LinkedList<Mensaje>(listaMensajesEnviados); // comprobar
	}
	
	public void setListaMensajesEnviados(List<Mensaje> listaMensajesEnviados) {
		this.listaMensajesEnviados = listaMensajesEnviados;
	}
	
	public List<Mensaje> getListaMensajesRecibidos() {
		return new LinkedList<Mensaje>(listaMensajesRecibidos); // comprobar
	}
	
	public void setListaMensajesRecibidos(List<Mensaje> listaMensajesRecibidos) {
		this.listaMensajesRecibidos = listaMensajesRecibidos;
	}
	
    // CONSTRUCTOR
	
	public Usuario(String usuario, String contraseña, String telefono, String email, Optional<String> saludo, ImageIcon imagenPerfil, LocalDate fechaNacimiento) {
		this.usuario = usuario;
		this.contraseña = contraseña;
		this.telefono = telefono;
		this.email = email;
		this.saludo = saludo;
		this.imagenPerfil = imagenPerfil;
		this.fechaNacimiento = fechaNacimiento;
		this.listaContactos = new LinkedList<Contacto>();
		this.listaMensajesEnviados = new LinkedList<Mensaje>();
		this.listaMensajesRecibidos = new LinkedList<Mensaje>();
		
	}
	
	
    

}
