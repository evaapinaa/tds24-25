package umu.tds.apps.persistencia;

import java.util.List;

import umu.tds.apps.modelo.Chat;

/**
 * Interface que define los métodos para la persistencia de los objetos Chat.
 */
public interface IAdaptadorChatDAO {

	/**
     * Registra un chat en la base de datos
     * 
     * @param chat Chat a registrar
     */
    public void registrarChat(Chat chat);
    
    
	/**
	 * Modifica un chat en la base de datos
	 * 
	 * @param chat Chat a modificar
	 */
    public void modificarChat(Chat chat);
    /**
     * Recupera un chat de la base de datos
     * 
     * @param codigo Código del chat a recuperar
     * @return Chat recuperado
     */
    public Chat recuperarChat(int codigo);

    /**
     * Recupera todos los chats de la base de datos
     * 
     * @return Lista con todos los chats recuperados
     */
    public List<Chat> recuperarTodosChats();
    
    
    /**
     * Recupera todos los chats de un usuario de la base de datos
     * 
     * @param usuario Usuario del que se quieren recuperar los chats
     * @return Lista con todos los chats recuperados
     */
    //public List<Chat> recuperarChatsUsuario(Usuario usuario);

    /**
     * Recupera todos los chats de un usuario con otro usuario de la base de datos
     * 
     * @param usuario Usuario del que se quieren recuperar los chats
     * @param otroUsuarioChat Otro usuario con el que se quieren recuperar los chats
     * @return Lista con todos los chats recuperados
     */
    //public List<Chat> recuperarChatsUsuarioOtroUsuario(Usuario usuario, Usuario otroUsuarioChat);

    /**
     * Recupera todos los chats de un usuario con otro usuario de la base de datos
     * 
     * @param usuario Usuario del que se quieren recuperar los chats
     * @param otroUsuarioChat Otro usuario con el que se quieren recuperar los chats
     * @return Lista con todos los chats recuperados
     */
    //public List<Chat> recuperarChatsUsuarioOtroUsuario(Usuario usuario, Usuario otroUsuarioChat);

}
