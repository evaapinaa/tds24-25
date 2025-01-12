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

    private AdaptadorChatTDS() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }

    public static AdaptadorChatTDS getUnicaInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new AdaptadorChatTDS();
        }
        return unicaInstancia;
    }

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

    private String obtenerCodigosMensajes(List<Mensaje> listaMensajes) {
        StringBuilder sb = new StringBuilder();
        for (Mensaje m : listaMensajes) {
            sb.append(m.getCodigo()).append(" ");
        }
        return sb.toString().trim();
    }

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