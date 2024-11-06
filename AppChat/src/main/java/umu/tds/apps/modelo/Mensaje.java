package umu.tds.apps.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Mensaje {
	
	// ATRIBUTOS
	private String imagen;
	private String texto;
	private int emoticono;
	private LocalDate fecha;
	private LocalDateTime hora;
	private Usuario emisor;
	private Contacto receptor;
	

	// GETTERS Y SETTES
	public String getImagen() {
		return imagen;
	}
	
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	public String getTexto() {
		return texto;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public int getEmoticono() {
		return emoticono;
	}
	
	public void setEmoticono(int emoticono) {
		this.emoticono = emoticono;
	}
	
	public LocalDate getFecha() {
		return fecha;
	}
	
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	
	public LocalDateTime getHora() {
		return hora;
	}
	
	public void setHora(LocalDateTime hora) {
		this.hora = hora;
	}
	
	public Usuario getEmisor() {
		return emisor;
	}
	
	public void setEmisor(Usuario emisor) {
		this.emisor = emisor;
	}
	
	public Contacto getReceptor() {
		return receptor;
	}
	
	public void setReceptor(Contacto receptor) {
		this.receptor = receptor;
	}
	
	public Mensaje(Usuario emisor, String texto, Contacto receptor) {
		this.emisor = emisor;
		this.texto = texto;
		this.receptor = receptor; // Controlador método -> devolverListaMensajesOrdenadaRecientesPorUsuario(usuarioActual)
								// devuelve lista de mensajes que necesita el renderizador
								// paquete modelo, no appchat
	}
		
}
