package umu.tds.apps.persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import beans.Entidad;
import beans.Propiedad;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

import umu.tds.apps.modelo.Chat;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.Usuario;

public class AdaptadorChatTDS implements IAdaptadorChatDAO {

    private static ServicioPersistencia servPersistencia;
    private static AdaptadorChatTDS unicaInstancia = null;
    
     /**
     * Constructor privado para implementar el patrón Singleton.
     * Inicializa el servicio de persistencia de la aplicación.
     */

    private AdaptadorChatTDS() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }

    
     /**
     * Método para obtener la única instancia de la clase (patrón Singleton).
     * @return La única instancia de AdaptadorChatTDS.
     */
    public static AdaptadorChatTDS getUnicaInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new AdaptadorChatTDS();
        }
        return unicaInstancia;
    }

    
     /**
     * Registra un chat en la base de datos.
     * Si el chat ya existe en la base de datos (tiene código asignado), no se realiza ninguna acción.
     * @param chat El objeto Chat que se desea registrar en la base de datos.
     */
    @Override
    public void registrarChat(Chat chat) {
        // Ver si ya existe
        try {
            Entidad eChat = servPersistencia.recuperarEntidad(chat.getCodigo());
            if (eChat != null) return; // ya existe
        } catch (NullPointerException e) {}

        // Crear la entidad
        Entidad eChat = new Entidad();
        eChat.setNombre("chat");
        eChat.setPropiedades(new ArrayList<>(Arrays.asList(
            new Propiedad("usuario", String.valueOf(chat.getUsuario().getCodigo())),
            new Propiedad("otroUsuarioChat", String.valueOf(chat.getOtroUsuarioChat().getCodigo())),
            new Propiedad("mensajes", obtenerCodigosMensajes(chat.getMensajes()))
        )));

        eChat = servPersistencia.registrarEntidad(eChat);
        chat.setCodigo(eChat.getId());
        System.out.println("Chat registrado con ID: " + eChat.getId());
    }

    
     /**
     * Modifica un chat existente en la base de datos.
     * Actualiza todas las propiedades del chat en la base de datos.
     * @param chat El objeto Chat con los datos actualizados.
     */
    @Override
    public void modificarChat(Chat chat) {
        Entidad eChat = servPersistencia.recuperarEntidad(chat.getCodigo());
        if (eChat == null) {
            System.err.println("No se encontró el chat con ID: " + chat.getCodigo());
            return;
        }
        for (Propiedad prop : eChat.getPropiedades()) {
            switch (prop.getNombre()) {
                case "usuario":
                    prop.setValor(String.valueOf(chat.getUsuario().getCodigo()));
                    break;
                case "otroUsuarioChat":
                    prop.setValor(String.valueOf(chat.getOtroUsuarioChat().getCodigo()));
                    break;
                case "mensajes":
                    prop.setValor(obtenerCodigosMensajes(chat.getMensajes()));
                    break;
            }
            servPersistencia.modificarPropiedad(prop);
        }
        System.out.println("Chat " + chat.getCodigo() + " modificado en BD.");
    }

    
     /**
     * Recupera un chat de la base de datos por su código.
     * Si el chat ya está en el pool de objetos, lo devuelve directamente.
     * @param codigo El código identificador del chat a recuperar.
     * @return El objeto Chat recuperado, o null si no existe.
     */
    @Override
    public Chat recuperarChat(int codigo) {
        // 1) Consultar la caché
        if (PoolDAO.getUnicaInstancia().contiene(codigo)) {
            return (Chat) PoolDAO.getUnicaInstancia().getObjeto(codigo);
        }

        // 2) Recuperar la entidad
        Entidad eChat = servPersistencia.recuperarEntidad(codigo);
        if (eChat == null) {
            System.err.println("No se encontró el chat con ID: " + codigo);
            return null;
        }

        // 3) Crear el objeto Chat con valores "provisionales"
        Chat chat = new Chat(null, null); 
        chat.setCodigo(codigo);

        // 4) Añadirlo ya al PoolDAO para evitar recursividad
        PoolDAO.getUnicaInstancia().addObjeto(codigo, chat);

        // 5) Recuperar las propiedades del chat
        int usuarioId = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eChat, "usuario"));
        int otroUsuarioId = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eChat, "otroUsuarioChat"));
        String codigosMensajes = servPersistencia.recuperarPropiedadEntidad(eChat, "mensajes");

        // 6) Recuperar los usuarios
        Usuario u = AdaptadorUsuarioTDS.getUnicaInstancia().recuperarUsuario(usuarioId);
        Usuario u2 = AdaptadorUsuarioTDS.getUnicaInstancia().recuperarUsuario(otroUsuarioId);

        chat.setUsuario(u);
        chat.setUsuario2(u2);

        // 7) Recuperar los mensajes
        List<Mensaje> mensajes = obtenerMensajesDesdeCodigos(codigosMensajes);
        chat.setMensajes(mensajes);

        System.out.println("Chat recuperado: " + chat.getCodigo()
                           + ", Usuario: " + (u != null ? u.getTelefono() : "null")
                           + ", OtroUsuario: " + (u2 != null ? u2.getTelefono() : "null"));
        return chat;
    }

    
     /**
     * Recupera todos los chats almacenados en la base de datos.
     * @return Una lista con todos los objetos Chat recuperados.
     */
    @Override
    public List<Chat> recuperarTodosChats() {
        List<Entidad> eChats = servPersistencia.recuperarEntidades("chat");
        List<Chat> chats = new LinkedList<>();
        for (Entidad eChat : eChats) {
            Chat c = recuperarChat(eChat.getId());
            if (c != null) {
                chats.add(c);
            }
        }
        return chats;
    }

    // --------------------------------------------------------------------------------
    // Métodos auxiliares
    // --------------------------------------------------------------------------------

    
     /**
     * Convierte una lista de mensajes en una cadena de texto con sus códigos separados por espacios.
     * @param listaMensajes Lista de mensajes a convertir.
     * @return Cadena de texto con los códigos de los mensajes separados por espacios.
     */
    private String obtenerCodigosMensajes(List<Mensaje> listaMensajes) {
        StringBuilder sb = new StringBuilder();
        for (Mensaje m : listaMensajes) {
            sb.append(m.getCodigo()).append(" ");
        }
        return sb.toString().trim();
    }

    
     /**
     * Convierte una cadena de texto con códigos de mensajes en una lista de objetos Mensaje.
     * @param codigos Cadena de texto con los códigos separados por espacios.
     * @return Lista de objetos Mensaje correspondientes a los códigos.
     */
    private List<Mensaje> obtenerMensajesDesdeCodigos(String codigos) {
        List<Mensaje> mensajes = new LinkedList<>();
        if (codigos == null || codigos.trim().isEmpty()) return mensajes;

        StringTokenizer st = new StringTokenizer(codigos, " ");
        AdaptadorMensajeTDS adaptadorMensaje = AdaptadorMensajeTDS.getUnicaInstancia();

        while (st.hasMoreTokens()) {
            int codMensaje = Integer.parseInt(st.nextToken());
            Mensaje m = adaptadorMensaje.recuperarMensaje(codMensaje);
            if (m != null) {
                mensajes.add(m);
            }
        }
        return mensajes;
    }
}