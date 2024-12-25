package umu.tds.apps.controlador;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import umu.tds.apps.modelo.Contacto;
import umu.tds.apps.modelo.ContactoIndividual;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.RepositorioUsuarios;
import umu.tds.apps.modelo.Usuario;
import umu.tds.apps.persistencia.DAOException;
import umu.tds.apps.persistencia.FactoriaDAO;
import umu.tds.apps.persistencia.IAdaptadorMensajeDAO;
import umu.tds.apps.persistencia.IAdaptadorUsuarioDAO;

// convertir controlador en singleton
public class AppChat {

	private Usuario usuarioActual;
	private RepositorioUsuarios repositorioUsuarios;
	private IAdaptadorUsuarioDAO adaptadorUsuario;
	private IAdaptadorMensajeDAO adaptadorMensaje;

	private static AppChat unicaInstancia = null;

	// singleton
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
			System.out.println("Adaptadores inicializados correctamente.");
		} catch (Exception e) {
			System.err.println("Error al inicializar los adaptadores: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static AppChat getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new AppChat();
		}
		return unicaInstancia;
	}

	public static List<Mensaje> obtenerListaMensajesRecientesPorUsuario() {
		Mensaje[] values = new Mensaje[] {
				new Mensaje(
						new Usuario("Ana", "1234", "555-1234", "ana@example.com", Optional.ofNullable("Hola"),
								new ImageIcon("hola"), LocalDate.of(1990, 1, 1)),
						"Holaa",
						new Usuario("Ana", "1234", "555-1234", "ana@example.com", Optional.ofNullable("Hola"),
								new ImageIcon("hola"), LocalDate.of(1990, 1, 1))),
				new Mensaje(
						new Usuario("Rosa", "5678", "555-5678", "rosa@example.com", Optional.ofNullable("Hola"),
								new ImageIcon("hola"), LocalDate.of(1991, 2, 2)),
						"Ok",
						new Usuario("Rosa", "1234", "555-1234", "ana@example.com", Optional.ofNullable("Hola"),
								new ImageIcon("hola"), LocalDate.of(1990, 1, 1))),
				new Mensaje(
						new Usuario("Jose", "91011", "555-9101", "jose@example.com", Optional.ofNullable("Hola"),
								new ImageIcon("hola"), LocalDate.of(1992, 3, 3)),
						"Nos vemos", new Usuario("Jose", "1234", "555-1234", "ana@example.com",
								Optional.ofNullable("Hola"), new ImageIcon("hola"), LocalDate.of(1990, 1, 1))) };
		return Arrays.asList(values);
	}

	// iniciar sesion login
	public boolean iniciarSesion(String telefono, char[] contraseña) {
	    usuarioActual = repositorioUsuarios.getUsuario(telefono);
	    if (usuarioActual == null || !usuarioActual.isClaveValida(new String(contraseña))) {
	        return false;
	    }
	    return true;
	}
	
	// registrar usuario

	public boolean registrarUsuario(String usuario, char[] contraseña, String telefono, String email,
	        Optional<String> saludo, Icon imagenPerfil, LocalDate fechaNacimiento) {
	    String rutaImagen = null;

	    if (imagenPerfil instanceof ImageIcon) {
	        rutaImagen = ((ImageIcon) imagenPerfil).getDescription();
	    }

	    ImageIcon iconoPerfil = rutaImagen != null ? new ImageIcon(rutaImagen) : null;

	    Usuario nuevoUsuario = new Usuario(usuario, new String(contraseña), telefono, email, saludo, iconoPerfil, fechaNacimiento);

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


	public void cambiarSaludo(String saludo) {
		usuarioActual.setSaludo(Optional.of(saludo));
		adaptadorUsuario.modificarUsuario(usuarioActual);
	}

	public static Usuario getUsuarioActual() {
		return unicaInstancia.usuarioActual;
	}

	
	// Obtener todos los mensajes enviados por el usuario actual
	public List<Mensaje> obtenerMensajesEnviados() {
	    if (usuarioActual == null) {
	        throw new IllegalStateException("No hay un usuario autenticado.");
	    }
	    return usuarioActual.getListaMensajesEnviados();
	}

	
	
	// Obtener todos los mensajes recibidos por el usuario actual
	public List<Mensaje> obtenerMensajesRecibidos() {
	    if (usuarioActual == null) {
	        throw new IllegalStateException("No hay un usuario autenticado.");
	    }
	    return usuarioActual.getListaMensajesRecibidos();
	}

	
	public List<Mensaje> obtenerMensajesConContacto(Usuario contacto) {
	    List<Mensaje> mensajesConContacto = new LinkedList<>();

	    // Filtrar mensajes enviados al contacto
	    for (Mensaje mensaje : usuarioActual.getListaMensajesEnviados()) {
	        if (mensaje.getReceptor().equals(contacto)) {
	            mensajesConContacto.add(mensaje);
	        }
	    }

	    // Filtrar mensajes recibidos del contacto
	    for (Mensaje mensaje : usuarioActual.getListaMensajesRecibidos()) {
	        if (mensaje.getEmisor().equals(contacto)) {
	            mensajesConContacto.add(mensaje);
	        }
	    }

	    // Ordenar por fecha y hora
	    mensajesConContacto.sort((m1, m2) -> {
	        if (m1.getFecha().equals(m2.getFecha())) {
	            return m1.getHora().compareTo(m2.getHora());
	        }
	        return m1.getFecha().compareTo(m2.getFecha());
	    });

	    return mensajesConContacto;
	}

}
