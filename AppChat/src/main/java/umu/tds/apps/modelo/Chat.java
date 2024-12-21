package umu.tds.apps.modelo;

import java.util.ArrayList;
import java.util.List;

public class Chat {

	// ATRIBUTOS
	private int codigo;
	private Usuario usuario;
	private Usuario otroUsuarioChat;
	private List<Mensaje> mensajes;

	// GETTERS Y SETTERS
	public int getCodigo() {
		return codigo;
	}
	
	public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Usuario getOtroUsuarioChat() {
		return otroUsuarioChat;
	}
	
	
	public void setUsuario2(Usuario otroUsuarioChat) {
		this.otroUsuarioChat = otroUsuarioChat;
	}
	
	public List<Mensaje> getMensajes() {
		return new ArrayList<>(mensajes);
	}
	
	public void setMensajes(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}
	

	public Chat(Usuario usuario, Usuario otroUsuarioChat) {
        this.usuario = usuario;
        this.otroUsuarioChat = otroUsuarioChat;
        this.mensajes = new ArrayList<>();
    }
}
