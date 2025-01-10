package umu.tds.apps.controlador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import umu.tds.apps.modelo.BusquedaPorContacto;
import umu.tds.apps.modelo.BusquedaPorTelefono;
import umu.tds.apps.modelo.BusquedaPorTexto;
import umu.tds.apps.modelo.BusquedaMensaje;
import umu.tds.apps.modelo.Chat;
import umu.tds.apps.modelo.Contacto;
import umu.tds.apps.modelo.ContactoIndividual;
import umu.tds.apps.modelo.EstrategiaBusquedaMensaje;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.RepositorioUsuarios;
import umu.tds.apps.modelo.Usuario;
import umu.tds.apps.persistencia.AdaptadorChatTDS;
import umu.tds.apps.persistencia.AdaptadorContactoIndividualTDS;
import umu.tds.apps.persistencia.FactoriaDAO;
import umu.tds.apps.persistencia.IAdaptadorChatDAO;
import umu.tds.apps.persistencia.IAdaptadorMensajeDAO;
import umu.tds.apps.persistencia.IAdaptadorUsuarioDAO;

public class AppChat {

    private Usuario usuarioActual;
    private RepositorioUsuarios repositorioUsuarios;
    private IAdaptadorUsuarioDAO adaptadorUsuario;
    private IAdaptadorMensajeDAO adaptadorMensaje;
    private IAdaptadorChatDAO adaptadorChat;

    private static AppChat unicaInstancia = null;

    // constructor privado (singleton)
    private AppChat() {
        inicializarAdaptador();
        inicializarRepositorio();
    }

    public void inicializarRepositorio() {
        repositorioUsuarios = RepositorioUsuarios.getUnicaInstancia();
    }

    public void inicializarAdaptador() {
        try {
            FactoriaDAO factoria = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
            adaptadorUsuario = factoria.getUsuarioDAO();
            adaptadorMensaje = factoria.getMensajeDAO();
            adaptadorChat = factoria.getChatDAO();
            System.out.println("Adaptadores inicializados correctamente.");
        } catch (Exception e) {
            System.err.println("Error al inicializar los adaptadores: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static AppChat getUnicaInstancia() {
        if (unicaInstancia == null) {
            // Creamos y guardamos en la variable estática
            unicaInstancia = new AppChat();
        }
        return unicaInstancia;
    }

    public static List<Mensaje> obtenerListaMensajesRecientesPorUsuario() {
        Usuario usuarioActual = AppChat.getUsuarioActual();
        if (usuarioActual == null) {
            throw new IllegalStateException("No hay un usuario autenticado.");
        }

        List<Chat> listaChats = usuarioActual.getListaChats();
        List<Mensaje> todosLosMensajes = new ArrayList<>();

        for (Chat chat : listaChats) {
            todosLosMensajes.addAll(chat.getMensajes());
        }
        

        // Ordenar por fecha/hora descendente
        todosLosMensajes.sort((m1, m2) -> {
            int cmpFecha = m2.getFecha().compareTo(m1.getFecha());
            if (cmpFecha == 0) {
                return m2.getHora().compareTo(m1.getHora());
            }
            return cmpFecha;
        });

        return todosLosMensajes;
    }

    // iniciar sesion
    public boolean iniciarSesion(String telefono, char[] contraseña) {
        usuarioActual = repositorioUsuarios.getUsuario(telefono);
        if (usuarioActual == null || !usuarioActual.isClaveValida(new String(contraseña))) {
            return false;
        }
        return true;
    }

    // registrar usuario
    public boolean registrarUsuario(String usuario, char[] contraseña, String telefono, String email,
                                    Optional<String> saludo, Icon imagenPerfil, LocalDate fechaNacimiento, LocalDate fechaRegistro) {
        String rutaImagen = null;
        if (imagenPerfil instanceof ImageIcon) {
            rutaImagen = ((ImageIcon) imagenPerfil).getDescription();
        }
        ImageIcon iconoPerfil = (rutaImagen != null) ? new ImageIcon(rutaImagen) : null;

        Usuario nuevoUsuario = new Usuario(usuario, new String(contraseña), telefono, 
                                           email, saludo, iconoPerfil, fechaNacimiento, fechaRegistro);

        // Verificar si el número de teléfono ya está registrado
        if (repositorioUsuarios.getUsuario(telefono) != null) {
            return false;
        }

        try {
            adaptadorUsuario.registrarUsuario(nuevoUsuario);
            repositorioUsuarios.addUsuario(nuevoUsuario);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void cambiarImagenPerfil(ImageIcon imagenPerfil) {
        if (usuarioActual != null) {
            usuarioActual.setImagenPerfil(imagenPerfil);
            adaptadorUsuario.modificarUsuario(usuarioActual);
            System.out.println("Imagen de perfil actualizada: " + imagenPerfil.getDescription());
        } else {
            System.err.println("No hay usuario autenticado.");
        }
    }

    public void cambiarSaludo(String saludo) {
		if (usuarioActual == null) {
			throw new IllegalStateException("No hay un usuario autenticado.");
		}
        usuarioActual.setSaludo(Optional.of(saludo));
        adaptadorUsuario.modificarUsuario(usuarioActual);
    }

    public static Usuario getUsuarioActual() {
        return (unicaInstancia != null) ? unicaInstancia.usuarioActual : null;
    }

    // Obtener mensajes
    public List<Mensaje> obtenerMensajesEnviados() {
        if (usuarioActual == null) {
            throw new IllegalStateException("No hay un usuario autenticado.");
        }
        return usuarioActual.getListaMensajesEnviados();
    }

    public List<Mensaje> obtenerMensajesRecibidos() {
        if (usuarioActual == null) {
            throw new IllegalStateException("No hay un usuario autenticado.");
        }
        return usuarioActual.getListaMensajesRecibidos();
    }

    public List<Mensaje> obtenerMensajesConContacto(Usuario contacto) {
        List<Mensaje> mensajesConContacto = new LinkedList<>();
        for (Mensaje mensaje : usuarioActual.getListaMensajesEnviados()) {
            if (mensaje.getReceptor().equals(contacto)) {
                mensajesConContacto.add(mensaje);
            }
        }
        for (Mensaje mensaje : usuarioActual.getListaMensajesRecibidos()) {
            if (mensaje.getEmisor().equals(contacto)) {
                mensajesConContacto.add(mensaje);
            }
        }
        mensajesConContacto.sort((m1, m2) -> {
            int cmp = m1.getFecha().compareTo(m2.getFecha());
            if (cmp == 0) {
                return m1.getHora().compareTo(m2.getHora());
            }
            return cmp;
        });
        return mensajesConContacto;
    }

    public boolean añadirContacto(String nombre, String telefono) {
        Usuario usuarioContacto = repositorioUsuarios.getUsuario(telefono);
        if (usuarioContacto == null) {
            return false; // El número no está en el sistema
        }
        ContactoIndividual nuevoContacto = new ContactoIndividual(nombre, telefono, usuarioContacto);
        boolean yaExiste = usuarioActual.getListaContactos().stream()
            .anyMatch(c -> (c instanceof ContactoIndividual)
                           && ((ContactoIndividual)c).getTelefono().equals(telefono));
        if (yaExiste) {
            return false;
        }

        try {
            usuarioActual.añadirContacto(nuevoContacto);
            AdaptadorContactoIndividualTDS.getUnicaInstancia().registrarContactoIndividual(nuevoContacto);
            adaptadorUsuario.modificarUsuario(usuarioActual);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Filtrar Mensajes y poder combinar varios filtros
    public List<Mensaje> filtrarMensajes(String texto, String telefono, String contacto) {
        EstrategiaBusquedaMensaje estrategiaBusqueda = new EstrategiaBusquedaMensaje();

        // Añadir estrategias sólo si los valores no están vacíos o nulos
        if (texto != null && !texto.isEmpty()) {
            estrategiaBusqueda.addEstrategiaBusqueda(new BusquedaPorTexto(texto));
        }
        if (telefono != null && !telefono.isEmpty()) {
            estrategiaBusqueda.addEstrategiaBusqueda(new BusquedaPorTelefono(telefono));
        }
        if (contacto != null && !contacto.isEmpty()) {
            estrategiaBusqueda.addEstrategiaBusqueda(new BusquedaPorContacto(usuarioActual, contacto));
        }

        // Obtener mensajes enviados y recibidos del usuario actual
        List<Mensaje> mensajes = new ArrayList<>();
        mensajes.addAll(usuarioActual.getListaMensajesEnviados());
        mensajes.addAll(usuarioActual.getListaMensajesRecibidos());

        // Ejecutar todas las estrategias y devolver los mensajes filtrados
        return estrategiaBusqueda.ejecutarBusqueda(mensajes);
    }




    // Crear nuevo chat
    public void crearNuevoChat(Usuario uActual, Usuario otroUsuario) {
        // Buscamos si ya existe
        Chat posibleChat = uActual.obtenerChatCon(otroUsuario);
        if (posibleChat != null) {
            System.out.println("Ya existe un chat entre " + uActual.getTelefono()
                + " y " + otroUsuario.getTelefono() + ", ID: " + posibleChat.getCodigo());
            return;
        }
        Chat nuevoChat = new Chat(uActual, otroUsuario);
        adaptadorChat.registrarChat(nuevoChat);

        uActual.añadirChat(nuevoChat);
        otroUsuario.añadirChat(nuevoChat);

        adaptadorUsuario.modificarUsuario(uActual);
        adaptadorUsuario.modificarUsuario(otroUsuario);
    }

    // Método para enviar mensaje
    public boolean enviarMensaje(Usuario uActual, Usuario uDestino, String texto) {
        if (texto == null) {
            System.err.println("Error: texto null");
            return false;
        }

        Chat chat = uActual.obtenerChatCon(uDestino);
        if (chat == null) {
            chat = new Chat(uActual, uDestino);
            adaptadorChat.registrarChat(chat);
            uActual.añadirChat(chat);
            uDestino.añadirChat(chat);
            adaptadorUsuario.modificarUsuario(uActual);
            adaptadorUsuario.modificarUsuario(uDestino);
        }

        Mensaje mensaje = new Mensaje(uActual, texto, uDestino, chat);
        adaptadorMensaje.registrarMensaje(mensaje);

        // Actualizar listas de mensajes utilizando los métodos de Usuario
        uActual.añadirMensajeEnviado(mensaje);
        uDestino.añadirMensajeRecibido(mensaje);

        // Persistir cambios en la base de datos
        adaptadorUsuario.modificarUsuario(uActual);
        adaptadorUsuario.modificarUsuario(uDestino);

        // Actualizar el chat y persistirlo
        chat.addMensaje(mensaje);
        adaptadorChat.modificarChat(chat);

        System.out.println("Mensaje enviado: " + texto);
        return true;
    }

    
    public boolean enviarEmoji(Usuario uActual, Usuario uDestino, int emoji) {
    	String texto = "EMOJI:" + emoji;
    	System.out.println("Emoji enviado: " + emoji);
    	return enviarMensaje(uActual, uDestino, texto);

    }

    public Usuario obtenerUsuarioPorTelefono(String telefono) {
        if (telefono == null) return null;
        return repositorioUsuarios.getUsuario(telefono.trim());
    }
    
    public Usuario obtenerUsuarioPorNombre(String nombre) {
		return usuarioActual.getListaContactos().stream()
				.filter(c -> c instanceof ContactoIndividual && ((ContactoIndividual) c).getNombre().equals(nombre))
				.map(c -> ((ContactoIndividual) c).getUsuario()).findFirst().orElse(null);
    }

	public ContactoIndividual obtenerContactoPorTelefono(String telefono) {
	    return usuarioActual.getListaContactos()
	            .stream()
	            .filter(c -> c instanceof ContactoIndividual && 
	                         ((ContactoIndividual) c).getTelefono().equals(telefono))
	            .map(c -> (ContactoIndividual) c)
	            .findFirst()
	            .orElse(null);
	}

}
