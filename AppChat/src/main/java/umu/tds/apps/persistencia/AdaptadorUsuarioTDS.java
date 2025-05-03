
package umu.tds.apps.persistencia;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;

import beans.Entidad;
import beans.Propiedad;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

import umu.tds.apps.modelo.Chat;
import umu.tds.apps.modelo.Contacto;
import umu.tds.apps.modelo.ContactoIndividual;
import umu.tds.apps.modelo.Grupo;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.Usuario;

public class AdaptadorUsuarioTDS implements IAdaptadorUsuarioDAO {

	private static ServicioPersistencia servPersistencia;
	private static AdaptadorUsuarioTDS unicaInstancia = null;

	
	 /**
	 * Constructor privado para implementar el patrón Singleton.
	 * Inicializa el servicio de persistencia de la aplicación.
	 */
	private AdaptadorUsuarioTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	
	 /**
	 * Método para obtener la única instancia de la clase (patrón Singleton).
	 * @return La única instancia de AdaptadorUsuarioTDS.
	 */
	public static AdaptadorUsuarioTDS getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new AdaptadorUsuarioTDS();
		}
		return unicaInstancia;
	}

	
	 /**
	 * Registra un usuario en la base de datos.
	 * Si el usuario ya existe en la base de datos (tiene código asignado), no se realiza ninguna acción.
	 * @param usuario El objeto Usuario que se desea registrar en la base de datos.
	 */
	@Override
	public void registrarUsuario(Usuario usuario) {
		Entidad eUsuario = null;
		try {
			eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		} catch (NullPointerException e) {
		}
		if (eUsuario != null)
			return; // ya existe

		eUsuario = new Entidad();
		eUsuario.setNombre("usuario");
		eUsuario.setPropiedades(new ArrayList<>(Arrays.asList(new Propiedad("nombre", usuario.getUsuario()),
				new Propiedad("telefono", usuario.getTelefono()), new Propiedad("email", usuario.getEmail()),
				new Propiedad("contraseña", usuario.getContraseña()),
				new Propiedad("saludo", usuario.getSaludo().orElse("")),
				new Propiedad("fechaRegistro", usuario.getFechaRegistro().toString()),
				new Propiedad("contactos", obtenerCodigosContactos(usuario.getListaContactos())),
				new Propiedad("imagenPerfil",
						(usuario.getImagenPerfil() != null) ? usuario.getImagenPerfil().getDescription() : ""),
				new Propiedad("mensajesEnviados", obtenerCodigosMensajes(usuario.getListaMensajesEnviados())),
				new Propiedad("mensajesRecibidos", obtenerCodigosMensajes(usuario.getListaMensajesRecibidos())),
				new Propiedad("chats", obtenerCodigosChats(usuario.getListaChats())),
				new Propiedad("premium", String.valueOf(usuario.isPremium())),
				new Propiedad("fechaNacimiento", usuario.getFechaNacimiento().toString())

		)));

		eUsuario = servPersistencia.registrarEntidad(eUsuario);
		usuario.setCodigo(eUsuario.getId());
		System.out.println("Usuario registrado con ID: " + eUsuario.getId());
	}

	
	
	 /**
	 * Modifica un usuario existente en la base de datos.
	 * Actualiza todas las propiedades del usuario en la base de datos.
	 * @param usuario El objeto Usuario con los datos actualizados.
	 */
	@Override
	public void modificarUsuario(Usuario usuario) {
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		if (eUsuario == null) {
			System.err.println("No se encontró el usuario con ID: " + usuario.getCodigo());
			return;
		}

		for (Propiedad prop : eUsuario.getPropiedades()) {
			switch (prop.getNombre()) {
			case "nombre":
				prop.setValor(usuario.getUsuario());
				break;
			case "telefono":
				prop.setValor(usuario.getTelefono());
				break;
			case "email":
				prop.setValor(usuario.getEmail());
				break;
			case "contraseña":
				prop.setValor(usuario.getContraseña());
				break;
			case "saludo":
				prop.setValor(usuario.getSaludo().orElse(""));
				break;
			case "imagenPerfil":
				prop.setValor((usuario.getImagenPerfil() != null) ? usuario.getImagenPerfil().getDescription() : "");
				break;
			case "contactos":
				prop.setValor(obtenerCodigosContactos(usuario.getListaContactos()));
				break;
			case "mensajesEnviados":
				prop.setValor(obtenerCodigosMensajes(usuario.getListaMensajesEnviados()));
				break;
			case "mensajesRecibidos":
				prop.setValor(obtenerCodigosMensajes(usuario.getListaMensajesRecibidos()));
				break;
			case "chats":
				prop.setValor(obtenerCodigosChats(usuario.getListaChats()));
				break;
			case "premium":
				prop.setValor(String.valueOf(usuario.isPremium()));
				break;
			case "fechaNacimiento":
				prop.setValor(usuario.getFechaNacimiento().toString());
				break;
			}
			servPersistencia.modificarPropiedad(prop);
		}
		System.out.println("Usuario " + usuario.getTelefono() + " modificado en BD.");
	}

	
	/**
	 * Recupera un usuario de la base de datos por su código.
	 * Si el usuario ya está en el pool de objetos, lo devuelve directamente.
	 * @param codigo El código identificador del usuario a recuperar.
	 * @return El objeto Usuario recuperado, o null si no existe.
	 */
	@Override
	public Usuario recuperarUsuario(int codigo) {
		if (PoolDAO.getUnicaInstancia().contiene(codigo)) {
			return (Usuario) PoolDAO.getUnicaInstancia().getObjeto(codigo);
		}

		Entidad eUsuario = servPersistencia.recuperarEntidad(codigo);
		if (eUsuario == null) {
			System.err.println("No se encontró el usuario con ID: " + codigo);
			return null;
		}

		// Propiedades básicas
		String nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, "nombre");
		String telefono = servPersistencia.recuperarPropiedadEntidad(eUsuario, "telefono");
		String contraseña = servPersistencia.recuperarPropiedadEntidad(eUsuario, "contraseña");
		String email = servPersistencia.recuperarPropiedadEntidad(eUsuario, "email");
		String saludo = servPersistencia.recuperarPropiedadEntidad(eUsuario, "saludo");
		String rutaImagenPerfil = servPersistencia.recuperarPropiedadEntidad(eUsuario, "imagenPerfil");
		String premiumStr = servPersistencia.recuperarPropiedadEntidad(eUsuario, "premium");
		String fechaNacimientoStr = servPersistencia.recuperarPropiedadEntidad(eUsuario, "fechaNacimiento");

		// Crea el usuario base
		ImageIcon imagenPerfil = null;
		if (rutaImagenPerfil != null && !rutaImagenPerfil.isEmpty()) {
			imagenPerfil = new ImageIcon();
			imagenPerfil.setDescription(rutaImagenPerfil);
		}

		Usuario usuario = new Usuario(nombre, contraseña, telefono, email, Optional.ofNullable(saludo), imagenPerfil,
				null, null // Fecha de registro se asigna después
		);
		usuario.setCodigo(codigo);

		// Recuperar la fecha de registro
		String fechaRegistroStr = servPersistencia.recuperarPropiedadEntidad(eUsuario, "fechaRegistro");
		if (fechaRegistroStr != null) {
			usuario.setFechaRegistro(LocalDate.parse(fechaRegistroStr));
		}

		// Añadir a PoolDAO antes de cargar sus referencias
		PoolDAO.getUnicaInstancia().addObjeto(codigo, usuario);

		// Cargar listas
		String contactos = servPersistencia.recuperarPropiedadEntidad(eUsuario, "contactos");
		usuario.setListaContactos(obtenerContactosDesdeCodigos(contactos));

		String mensajesEnviados = servPersistencia.recuperarPropiedadEntidad(eUsuario, "mensajesEnviados");
		usuario.setListaMensajesEnviados(obtenerMensajesDesdeCodigos(mensajesEnviados));

		String mensajesRecibidos = servPersistencia.recuperarPropiedadEntidad(eUsuario, "mensajesRecibidos");
		usuario.setListaMensajesRecibidos(obtenerMensajesDesdeCodigos(mensajesRecibidos));

		String chats = servPersistencia.recuperarPropiedadEntidad(eUsuario, "chats");
		usuario.setListaChats(obtenerChatsDesdeCodigos(chats));

		System.out.println("Usuario recuperado: " + telefono + ", Chats: " + usuario.getListaChats().size());

		boolean premium = false;
		if (premiumStr != null) {
			try {
				premium = Boolean.parseBoolean(premiumStr);
			} catch (NumberFormatException e) {
				System.err.println("Error al convertir la propiedad 'premium' a booleano: " + premiumStr);
			}
		}
		usuario.setPremium(premium);
		
	    LocalDate fechaNacimiento = null;
	    if (fechaNacimientoStr != null && !fechaNacimientoStr.isEmpty()) {
	        try {
	            fechaNacimiento = LocalDate.parse(fechaNacimientoStr);
	        } catch (Exception e) {
	            System.err.println("Error al parsear fecha de nacimiento: " + fechaNacimientoStr);
	        }
	    }
	    usuario.setFechaNacimiento(fechaNacimiento);
	    
		return usuario;
	}

	
	 /**
	 * Recupera todos los usuarios almacenados en la base de datos.
	 * @return Una lista con todos los objetos Usuario recuperados.
	 */
	@Override
	public List<Usuario> recuperarTodosUsuarios() {
		List<Entidad> eUsuarios = servPersistencia.recuperarEntidades("usuario");
		List<Usuario> usuarios = new LinkedList<>();
		for (Entidad eUsuario : eUsuarios) {
			Usuario u = recuperarUsuario(eUsuario.getId());
			if (u != null) {
				usuarios.add(u);
			}
		}
		return usuarios;
	}

	// --------------------------------------------------------------------------------
	// Métodos auxiliares
	// --------------------------------------------------------------------------------

	
	 /**
	 * Convierte una lista de contactos en una cadena de texto con sus códigos separados por espacios.
	 * @param listaContactos Lista de contactos a convertir.
	 * @return Cadena de texto con los códigos de los contactos separados por espacios.
	 */
	private String obtenerCodigosContactos(List<Contacto> listaContactos) {
		StringBuilder sb = new StringBuilder();
		for (Contacto c : listaContactos) {
			sb.append(c.getCodigo()).append(" ");
		}
		return sb.toString().trim();
	}

	
	 /**
	 * Convierte una cadena de texto con códigos de contactos en una lista de objetos Contacto.
	 * Distingue entre contactos individuales y grupos.
	 * @param codigos Cadena de texto con los códigos separados por espacios.
	 * @return Lista de objetos Contacto correspondientes a los códigos.
	 */
	private List<Contacto> obtenerContactosDesdeCodigos(String codigos) {
		List<Contacto> contactos = new LinkedList<>();
		if (codigos == null || codigos.trim().isEmpty())
			return contactos;

		StringTokenizer st = new StringTokenizer(codigos, " ");
		while (st.hasMoreTokens()) {
			int codContacto = Integer.parseInt(st.nextToken());
			Entidad eContacto = servPersistencia.recuperarEntidad(codContacto);

			if (eContacto.getNombre().equals("contactoIndividual")) {
				ContactoIndividual ci = AdaptadorContactoIndividualTDS.getUnicaInstancia()
						.recuperarContactoIndividual(codContacto);
				if (ci != null) {
					contactos.add(ci);
				}
			} else if (eContacto.getNombre().equals("grupo")) {
				Grupo grupo = AdaptadorGrupoTDS.getUnicaInstancia().recuperarGrupo(codContacto);
				if (grupo != null) {
					contactos.add(grupo);
				}
			} else {
				System.err.println("La entidad con código " + codContacto + " no es un contacto válido.");
			}
		}
		return contactos;
	}

	
	 /**
	 * Convierte una lista de mensajes en una cadena de texto con sus códigos separados por espacios.
	 * @param mensajes Lista de mensajes a convertir.
	 * @return Cadena de texto con los códigos de los mensajes separados por espacios.
	 */
	private String obtenerCodigosMensajes(List<Mensaje> mensajes) {
		StringBuilder sb = new StringBuilder();
		for (Mensaje m : mensajes) {
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
		if (codigos == null || codigos.trim().isEmpty())
			return mensajes;

		StringTokenizer st = new StringTokenizer(codigos, " ");
		while (st.hasMoreTokens()) {
			int codMensaje = Integer.parseInt(st.nextToken());
			Mensaje m = AdaptadorMensajeTDS.getUnicaInstancia().recuperarMensaje(codMensaje);
			if (m != null) {
				mensajes.add(m);
			}
		}
		return mensajes;
	}

	
	 /**
	 * Convierte una lista de chats en una cadena de texto con sus códigos separados por espacios.
	 * @param chats Lista de chats a convertir.
	 * @return Cadena de texto con los códigos de los chats separados por espacios.
	 */
	private String obtenerCodigosChats(List<Chat> chats) {
		StringBuilder sb = new StringBuilder();
		for (Chat c : chats) {
			sb.append(c.getCodigo()).append(" ");
		}
		return sb.toString().trim();
	}

	
	 /**
	 * Convierte una cadena de texto con códigos de chats en una lista de objetos Chat.
	 * @param codigos Cadena de texto con los códigos separados por espacios.
	 * @return Lista de objetos Chat correspondientes a los códigos.
	 */
	private List<Chat> obtenerChatsDesdeCodigos(String codigos) {
		List<Chat> listaChats = new LinkedList<>();
		if (codigos == null || codigos.trim().isEmpty())
			return listaChats;

		StringTokenizer st = new StringTokenizer(codigos, " ");
		while (st.hasMoreTokens()) {
			int codChat = Integer.parseInt(st.nextToken());
			Chat c = AdaptadorChatTDS.getUnicaInstancia().recuperarChat(codChat);
			if (c != null) {
				listaChats.add(c);
			}
		}
		return listaChats;
	}
}
