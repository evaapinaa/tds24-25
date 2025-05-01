
package umu.tds.apps.modelo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


/**
 * Clase que representa un chat entre dos usuarios y contiene sus mensajes.
*/
public class Chat {

	// ATRIBUTOS
	private int codigo;
	private Usuario usuario;
	private Usuario otroUsuarioChat;
	private List<Mensaje> mensajes;

	// GETTERS Y SETTERS
	 /**
     * Obtiene el código identificador del chat.
     * 
     * @return Código del chat
     */
	public int getCodigo() {
		return codigo;
	}
	
	
	 /**
     * Establece el código identificador del chat.
     * 
     * @param codigo Nuevo código
     */
	public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
	
	
	 /**
     * Obtiene el primer usuario del chat.
     * 
     * @return Primer usuario
     */
	public Usuario getUsuario() {
		return usuario;
	}
	
	
    /**
     * Establece el primer usuario del chat.
     * 
     * @param usuario Nuevo usuario
     */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
    /**
     * Obtiene el segundo usuario del chat.
     * 
     * @return Segundo usuario
     */
	public Usuario getOtroUsuarioChat() {
		return otroUsuarioChat;
	}
	
	
	 /**
     * Establece el segundo usuario del chat.
     * 
     * @param otroUsuarioChat Nuevo usuario
     */
	public void setUsuario2(Usuario otroUsuarioChat) {
		this.otroUsuarioChat = otroUsuarioChat;
	}
	
    /**
     * Obtiene la lista de mensajes del chat.
     * 
     * @return Lista de mensajes
     */
	public List<Mensaje> getMensajes() {
		return new ArrayList<>(mensajes);
	}
	
	
	 /**
     * Establece la lista de mensajes del chat.
     * 
     * @param mensajes Nueva lista de mensajes
     */
	public void setMensajes(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}
	

    /**
     * Crea un nuevo chat entre dos usuarios.
     * 
     * @param usuario Primer usuario
     * @param otroUsuarioChat Segundo usuario
     */
	public Chat(Usuario usuario, Usuario otroUsuarioChat) {
        this.usuario = usuario;
        this.otroUsuarioChat = otroUsuarioChat;
        this.mensajes = new ArrayList<>();
    }
	
	
	 /**
     * Añade un mensaje al chat y ordena los mensajes cronológicamente.
     * 
     * @param mensaje Mensaje a añadir
     */
	public void addMensaje(Mensaje mensaje) {
	    mensajes.add(mensaje);
	    mensajes.sort(Comparator.comparing(Mensaje::getFecha).thenComparing(Mensaje::getHora));
	}

	
	
	 /**
     * Verifica si un usuario es parte de este chat.
     * 
     * @param otroUsuario Usuario a verificar
     * @return true si el usuario es parte del chat, false en caso contrario
     */
	public boolean involucraUsuario(Usuario otroUsuario) {
	    return usuario.equals(otroUsuario) || otroUsuarioChat.equals(otroUsuario);
	}

    /**
     * Compara este chat con otro objeto para determinar igualdad.
     * Dos chats son iguales si tienen los mismos usuarios (independientemente del orden).
     * 
     * @param obj Objeto a comparar
     * @return true si los chats son iguales, false en caso contrario
     */
	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
	        return true;
	    if (obj == null || getClass() != obj.getClass())
	        return false;
	    Chat chat = (Chat) obj;
	    return (usuario.equals(chat.usuario) && otroUsuarioChat.equals(chat.otroUsuarioChat)) ||
	           (usuario.equals(chat.otroUsuarioChat) && otroUsuarioChat.equals(chat.usuario));
	}

	
    /**
     * Genera un código hash basado en los usuarios del chat.
     * 
     * @return Código hash
     */
	@Override
	public int hashCode() {
	    return Objects.hash(usuario, otroUsuarioChat);
	}

}
