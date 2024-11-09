package umu.tds.apps.modelo;

import java.util.ArrayList;
import java.util.List;

public class Chat {

	// ATRIBUTOS
	private Usuario usuario1;
	private Usuario usuario2;
	private List<Mensaje> mensajes;

	// GETTERS Y SETTERS

	public Usuario getUsuario1() {
		return usuario1;
	}
	
	public void setUsuario1(Usuario usuario1) {
		this.usuario1 = usuario1;
	}
	
	public Usuario getUsuario2() {
		return usuario2;
	}
	
	
	public void setUsuario2(Usuario usuario2) {
		this.usuario2 = usuario2;
	}
	
	public List<Mensaje> getMensajes() {
		return new ArrayList<>(mensajes);
	}
	
	public void setMensajes(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}
	

	public Chat(Usuario usuario1, Usuario usuario2) {
        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
        this.mensajes = new ArrayList<>();
    }
}
