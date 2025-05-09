package umu.tds.apps.persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import beans.Entidad;
import beans.Propiedad;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

import umu.tds.apps.modelo.Chat;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.Usuario;

public class AdaptadorMensajeTDS implements IAdaptadorMensajeDAO {

    private static ServicioPersistencia servPersistencia;
    private static AdaptadorMensajeTDS unicaInstancia = null;

    
     /**
     * Constructor privado para implementar el patrón Singleton.
     * Inicializa el servicio de persistencia de la aplicación.
     */
    private AdaptadorMensajeTDS() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }

    
     /**
     * Método para obtener la única instancia de la clase (patrón Singleton).
     * @return La única instancia de AdaptadorMensajeTDS.
     */
    public static AdaptadorMensajeTDS getUnicaInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new AdaptadorMensajeTDS();
        }
        return unicaInstancia;
    }

    
     /**
     * Registra un mensaje en la base de datos.
     * Si el mensaje ya existe en la base de datos (tiene código asignado), no se realiza ninguna acción.
     * @param mensaje El objeto Mensaje que se desea registrar en la base de datos.
     */
    @Override
    public void registrarMensaje(Mensaje mensaje) {
        // Ver si ya existe
        try {
            Entidad eMensaje = servPersistencia.recuperarEntidad(mensaje.getCodigo());
            if (eMensaje != null) return; // ya está
        } catch (NullPointerException e) {
            // no pasa nada, eMensaje sigue siendo null
        }

        // Crear entidad
        Entidad eMensaje = new Entidad();
        eMensaje.setNombre("mensaje");
        eMensaje.setPropiedades(new ArrayList<>(Arrays.asList(
            new Propiedad("texto", mensaje.getTexto()),
            new Propiedad("emisor", String.valueOf(mensaje.getEmisor().getCodigo())),
            new Propiedad("receptor", String.valueOf(mensaje.getReceptor().getCodigo())),
            new Propiedad("fecha", mensaje.getFecha().toString()),
            new Propiedad("hora", mensaje.getHora().toString()),
            new Propiedad("chat", String.valueOf(mensaje.getChat().getCodigo()))
        )));

        // Registrar
        eMensaje = servPersistencia.registrarEntidad(eMensaje);
        mensaje.setCodigo(eMensaje.getId());
        System.out.println("Mensaje registrado con ID: " + eMensaje.getId());
    }

    
     /**
     * Recupera un mensaje de la base de datos por su código.
     * Si el mensaje ya está en el pool de objetos, lo devuelve directamente.
     * @param codigo El código identificador del mensaje a recuperar.
     * @return El objeto Mensaje recuperado, o null si no existe.
     */
    @Override
    public Mensaje recuperarMensaje(int codigo) {
        // 1) PoolDAO check
        if (PoolDAO.getUnicaInstancia().contiene(codigo)) {
            return (Mensaje) PoolDAO.getUnicaInstancia().getObjeto(codigo);
        }

        // 2) Entidad
        Entidad eMensaje = servPersistencia.recuperarEntidad(codigo);
        if (eMensaje == null) {
            System.err.println("No se encontró el mensaje con ID: " + codigo);
            return null;
        }

        // 3) Tomar props
        String texto = servPersistencia.recuperarPropiedadEntidad(eMensaje, "texto");
        int emisorId = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, "emisor"));
        int receptorId = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, "receptor"));
        int chatId = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, "chat"));

        String fechaStr = servPersistencia.recuperarPropiedadEntidad(eMensaje, "fecha");
        String horaStr = servPersistencia.recuperarPropiedadEntidad(eMensaje, "hora");

        // 4) Crear Mensaje "parcial" usando el constructor "especial" sin check
        Mensaje mensaje = new Mensaje(); 
        mensaje.setCodigo(codigo);

        // 5) Añadir a PoolDAO
        PoolDAO.getUnicaInstancia().addObjeto(codigo, mensaje);

        // 6) Recuperar Emisor, Receptor y Chat
        Usuario emisor = AdaptadorUsuarioTDS.getUnicaInstancia().recuperarUsuario(emisorId);
        Usuario receptor = AdaptadorUsuarioTDS.getUnicaInstancia().recuperarUsuario(receptorId);
        Chat chat = AdaptadorChatTDS.getUnicaInstancia().recuperarChat(chatId);

        // 7) Asignar campos
        mensaje.setEmisor(emisor);
        mensaje.setReceptor(receptor);
        mensaje.setChat(chat);  // Ya no lanza IllegalArgumentException
        mensaje.setTexto(texto);

        // 8) Fecha/Hora
        java.time.LocalDate fecha = java.time.LocalDate.parse(fechaStr);
        java.time.LocalDateTime hora = java.time.LocalDateTime.parse(horaStr);
        mensaje.setFecha(fecha);
        mensaje.setHora(hora);

        System.out.println("Mensaje recuperado: ID " + codigo + " - " + texto);
        return mensaje;
    }

    
     /**
     * Recupera todos los mensajes almacenados en la base de datos.
     * @return Una lista con todos los objetos Mensaje recuperados.
     */
    @Override
    public List<Mensaje> recuperarTodosMensajes() {
        List<Entidad> eMensajes = servPersistencia.recuperarEntidades("mensaje");
        List<Mensaje> mensajes = new LinkedList<>();
        for (Entidad eMensaje : eMensajes) {
            Mensaje m = recuperarMensaje(eMensaje.getId());
            if (m != null) {
                mensajes.add(m);
            }
        }
        return mensajes;
    }
    

}
