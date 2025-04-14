package umu.tds.apps.controlador;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import umu.tds.apps.modelo.Chat;
import umu.tds.apps.modelo.Contacto;
import umu.tds.apps.modelo.ContactoIndividual;
import umu.tds.apps.modelo.DescuentoFecha;
import umu.tds.apps.modelo.DescuentoMensaje;
import umu.tds.apps.modelo.EstrategiaDescuento;
import umu.tds.apps.modelo.Grupo;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.RepositorioUsuarios;
import umu.tds.apps.modelo.Usuario;
import umu.tds.apps.persistencia.AdaptadorContactoIndividualTDS;
import umu.tds.apps.persistencia.AdaptadorGrupoTDS;
import umu.tds.apps.persistencia.AdaptadorUsuarioTDS;
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

	public static final double PRECIO_PREMIUM = 50.0; // Precio base de la suscripción premium

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

		Usuario nuevoUsuario = new Usuario(usuario, new String(contraseña), telefono, email, saludo, iconoPerfil,
				fechaNacimiento, fechaRegistro);

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

	public double calcularPrecioPremium() {
		if (usuarioActual == null) {
			throw new IllegalStateException("No hay un usuario autenticado.");
		}

		// Precio base
		double precio = PRECIO_PREMIUM;

		// Aplicar estrategia de descuento según la fecha de registro
		EstrategiaDescuento estrategia = new EstrategiaDescuento();
		estrategia.setEstrategiaDescuento(new DescuentoFecha(usuarioActual.getFechaRegistro()));
		precio = estrategia.calcularPrecioFinal(precio);

		// Obtener el número de mensajes enviados en el último mes
		long mensajesEnviados = usuarioActual.getNumeroMensajesUltimoMes();

		// Aplicar estrategia de descuento según el número de mensajes
		estrategia.setEstrategiaDescuento(new DescuentoMensaje((int) mensajesEnviados));
		precio = estrategia.calcularPrecioFinal(precio);

		return precio;
	}

	public boolean activarPremium() {
	    if (usuarioActual == null) {
	        throw new IllegalStateException("No hay un usuario autenticado.");
	    }
	    
	    // Verificar si ya es Premium
	    if (usuarioActual.isPremium()) {
	        return true; // Ya es Premium, no es necesario hacer nada
	    }

	    // Activar Premium
	    usuarioActual.activarPremium();
	    
	    // Persistir los cambios
	    adaptadorUsuario.modificarUsuario(usuarioActual);
	    
	    return true;
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
		if (usuarioActual == null) {
			throw new IllegalStateException("No hay un usuario autenticado.");
		}

		return usuarioActual.obtenerMensajesCon(contacto);
	}

	public boolean añadirContacto(String nombre, String telefono) {
		Usuario usuarioContacto = repositorioUsuarios.getUsuario(telefono);
		if (usuarioContacto == null) {
			return false; // El número no está en el sistema
		}
		ContactoIndividual nuevoContacto = new ContactoIndividual(nombre, telefono, usuarioContacto);
		boolean yaExiste = usuarioActual.getListaContactos().stream().anyMatch(
				c -> (c instanceof ContactoIndividual) && ((ContactoIndividual) c).getTelefono().equals(telefono));
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

	// FUNCION NUEVA
	public boolean crearGrupo(String nombreGrupo, List<ContactoIndividual> contactos, Optional<ImageIcon> imagenGrupo) {
		if (nombreGrupo == null || nombreGrupo.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre del grupo no puede estar vacío.");
		}

		boolean nombreDuplicado = getUsuarioActual().getGrupos().stream()
				.anyMatch(grupo -> grupo.getNombre().equalsIgnoreCase(nombreGrupo));
		if (nombreDuplicado) {
			throw new IllegalArgumentException("Ya existe un grupo con este nombre.");
		}

		Grupo nuevoGrupo = new Grupo(nombreGrupo, contactos, getUsuarioActual(), imagenGrupo.orElse(null));
		getUsuarioActual().añadirContacto(nuevoGrupo);

		// Persistir el grupo y el usuario
		AdaptadorGrupoTDS.getUnicaInstancia().registrarGrupo(nuevoGrupo);
		AdaptadorUsuarioTDS.getUnicaInstancia().modificarUsuario(getUsuarioActual());

		return true;
	}

	// Filtrar Mensajes y poder combinar varios filtros
	public List<Mensaje> filtrarMensajes(String texto, String telefono, String contacto) {
		return usuarioActual.filtrarMensajes(texto, telefono, contacto);
	}

	// Crear nuevo chat
	public void crearNuevoChat(Usuario uActual, Usuario otroUsuario) {
		// Llamar al método de Usuario y persistir el resultado
		Chat nuevoChat = uActual.crearChatCon(otroUsuario);

		// Solo persistimos si es un chat recién creado
		if (nuevoChat.getCodigo() == 0) { // Asumiendo que los chats nuevos tienen código 0
			adaptadorChat.registrarChat(nuevoChat);
			adaptadorUsuario.modificarUsuario(uActual);
			adaptadorUsuario.modificarUsuario(otroUsuario);
		}

	}

	// Método para enviar mensaje
	public boolean enviarMensaje(Usuario uActual, Usuario receptor, String texto) {
		if (texto == null) {
			System.err.println("Error: texto null");
			return false;
		}

		Chat chat = uActual.obtenerChatCon(receptor);
		if (chat == null) {
			chat = new Chat(uActual, receptor);
			adaptadorChat.registrarChat(chat);
			uActual.añadirChat(chat);
			receptor.añadirChat(chat);
			adaptadorUsuario.modificarUsuario(uActual);
			adaptadorUsuario.modificarUsuario(receptor);
		}

		Mensaje mensaje = new Mensaje(uActual, texto, receptor, chat);
		adaptadorMensaje.registrarMensaje(mensaje);

		// Actualizar listas de mensajes utilizando los métodos de Usuario
		uActual.añadirMensajeEnviado(mensaje);
		receptor.añadirMensajeRecibido(mensaje);

		// Persistir cambios en la base de datos
		adaptadorUsuario.modificarUsuario(uActual);
		adaptadorUsuario.modificarUsuario(receptor);

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
		if (telefono == null)
			return null;
		return repositorioUsuarios.getUsuario(telefono.trim());
	}

	public Usuario obtenerUsuarioPorNombre(String nombre) {
		return usuarioActual.getListaContactos().stream()
				.filter(c -> c instanceof ContactoIndividual && ((ContactoIndividual) c).getNombre().equals(nombre))
				.map(c -> ((ContactoIndividual) c).getUsuario()).findFirst().orElse(null);
	}

	public ContactoIndividual obtenerContactoPorTelefono(String telefono) {
		return usuarioActual.getListaContactos().stream()
				.filter(c -> c instanceof ContactoIndividual && ((ContactoIndividual) c).getTelefono().equals(telefono))
				.map(c -> (ContactoIndividual) c).findFirst().orElse(null);
	}

	public boolean enviarMensajeAGrupo(Usuario uActual, Grupo grupo, String texto) {
		if (texto == null || grupo == null) {
			System.err.println("Error: texto o grupo es null");
			return false;
		}

		List<ContactoIndividual> miembros = grupo.getListaContactos();
		if (miembros.isEmpty()) {
			System.err.println("El grupo no tiene miembros.");
			return false;
		}

		boolean enviado = true;
		for (ContactoIndividual miembro : miembros) {
			Usuario receptor = miembro.getUsuario();
			if (receptor != null) {
				enviado &= enviarMensaje(uActual, receptor, texto);
			} else {
				System.err.println("No se pudo enviar mensaje a: " + miembro.getNombre());
				enviado = false;
			}
		}
		return enviado;
	}

	public boolean enviarEmojiAGrupo(Usuario emisor, Grupo grupo, int emoji) {
		String texto = "EMOJI:" + emoji;
		System.out.println("Emoji enviado a grupo: " + emoji);
		return enviarMensajeAGrupo(emisor, grupo, texto);
	}

	/**
	 * Genera un PDF con la información de los contactos y grupos del usuario
	 * 
	 * @param rutaArchivo La ruta donde se guardará el archivo PDF
	 * @return true si el PDF se generó correctamente, false en caso contrario
	 */
	/**
	 * Genera un PDF con la información de los contactos y grupos del usuario,
	 * incluyendo mensajes enviados
	 * 
	 * @param rutaArchivo La ruta donde se guardará el archivo PDF
	 * @return true si el PDF se generó correctamente, false en caso contrario
	 */
	public boolean generarPDFContactos(String rutaArchivo) {
		// Verificar que el usuario sea premium
		if (!usuarioActual.isPremium()) {
			System.err.println("Esta función solo está disponible para usuarios Premium");
			return false;
		}

		Document documento = new Document();

		try {
			PdfWriter.getInstance(documento, new FileOutputStream(rutaArchivo));
			documento.open();

			// Fuentes para el documento
			Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
			Font fontSubtitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
			Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 10);

			// Título principal
			documento.add(new Paragraph("Contactos y Grupos de " + usuarioActual.getUsuario(), fontTitle));
			documento.add(new Paragraph(" ")); // Espacio

			// Información del usuario
			documento.add(new Paragraph("Información del Usuario:", fontSubtitle));
			documento.add(new Paragraph("Nombre: " + usuarioActual.getUsuario(), fontNormal));
			documento.add(new Paragraph("Teléfono: " + usuarioActual.getTelefono(), fontNormal));
			documento.add(new Paragraph("Email: " + usuarioActual.getEmail(), fontNormal));
			documento.add(new Paragraph(" ")); // Espacio

			// Tabla de contactos individuales
			documento.add(new Paragraph("Contactos Individuales:", fontSubtitle));
			documento.add(new Paragraph(" ")); // Espacio

			// Crear tabla para contactos individuales
			PdfPTable tablaContactos = new PdfPTable(2); // 2 columnas
			tablaContactos.setWidthPercentage(100);
			tablaContactos.setWidths(new float[] { 2, 1 }); // Proporción de ancho de columnas

			// Encabezados de la tabla
			PdfPCell celdaNombre = new PdfPCell(new Phrase("Nombre", fontSubtitle));
			PdfPCell celdaTelefono = new PdfPCell(new Phrase("Teléfono", fontSubtitle));
			tablaContactos.addCell(celdaNombre);
			tablaContactos.addCell(celdaTelefono);

			// Añadir contactos individuales a la tabla
			for (Contacto contacto : usuarioActual.getListaContactos()) {
				if (contacto instanceof ContactoIndividual) {
					ContactoIndividual ci = (ContactoIndividual) contacto;
					tablaContactos.addCell(new Phrase(ci.getNombre(), fontNormal));
					tablaContactos.addCell(new Phrase(ci.getTelefono(), fontNormal));
				}
			}

			documento.add(tablaContactos);
			documento.add(new Paragraph(" ")); // Espacio

			// Sección de grupos
			documento.add(new Paragraph("Grupos:", fontSubtitle));
			documento.add(new Paragraph(" ")); // Espacio

			// Procesar cada grupo
			for (Contacto contacto : usuarioActual.getListaContactos()) {
				if (contacto instanceof Grupo) {
					Grupo grupo = (Grupo) contacto;
					documento.add(new Paragraph("Grupo: " + grupo.getNombre(), fontSubtitle));

					// Tabla de miembros del grupo
					PdfPTable tablaMiembros = new PdfPTable(2); // 2 columnas
					tablaMiembros.setWidthPercentage(100);
					tablaMiembros.setWidths(new float[] { 2, 1 }); // Proporción de ancho de columnas

					// Encabezados de la tabla
					PdfPCell celdaMiembro = new PdfPCell(new Phrase("Miembro", fontSubtitle));
					PdfPCell celdaTelefonoMiembro = new PdfPCell(new Phrase("Teléfono", fontSubtitle));
					tablaMiembros.addCell(celdaMiembro);
					tablaMiembros.addCell(celdaTelefonoMiembro);

					// Añadir miembros del grupo a la tabla
					for (ContactoIndividual miembro : grupo.getListaContactos()) {
						tablaMiembros.addCell(new Phrase(miembro.getNombre(), fontNormal));
						tablaMiembros.addCell(new Phrase(miembro.getTelefono(), fontNormal));
					}

					documento.add(tablaMiembros);
					documento.add(new Paragraph(" ")); // Espacio

					List<Mensaje> mensajesGrupo = grupo.getListaMensajesEnviados();
					if (mensajesGrupo != null && !mensajesGrupo.isEmpty()) {
						documento.add(new Paragraph("Mensajes enviados al grupo:", fontSubtitle));
						documento.add(new Paragraph(" ")); // Espacio

						// Tabla de mensajes enviados al grupo
						PdfPTable tablaMensajes = new PdfPTable(3); // 3 columnas: Emisor, Mensaje, Fecha/Hora
						tablaMensajes.setWidthPercentage(100);
						tablaMensajes.setWidths(new float[] { 1.5f, 3, 1.5f }); // Proporción de ancho de columnas

						// Encabezados de la tabla
						tablaMensajes.addCell(new Phrase("Emisor", fontSubtitle));
						tablaMensajes.addCell(new Phrase("Mensaje", fontSubtitle));
						tablaMensajes.addCell(new Phrase("Fecha y Hora", fontSubtitle));

						// Añadir mensajes a la tabla
						for (Mensaje mensaje : mensajesGrupo) {
							String nombreEmisor = mensaje.getEmisor().equals(usuarioActual) ? "Yo"
									: mensaje.getEmisor().getUsuario();

							String textoMensaje = mensaje.getTexto();
							if (textoMensaje.startsWith("EMOJI:")) {
								textoMensaje = "[Emoji]"; // Representación simple para emojis
							}

							String fechaHora = mensaje.getFecha().toString() + " " + String.format("%02d:%02d",
									mensaje.getHora().getHour(), mensaje.getHora().getMinute());

							tablaMensajes.addCell(new Phrase(nombreEmisor, fontNormal));
							tablaMensajes.addCell(new Phrase(textoMensaje, fontNormal));
							tablaMensajes.addCell(new Phrase(fechaHora, fontNormal));
						}

						documento.add(tablaMensajes);
					} else {
						documento.add(new Paragraph("No hay mensajes enviados a este grupo.", fontNormal));
					}

					documento.add(new Paragraph(" ")); // Espacio
				}
			}

			documento.close();
			return true;

		} catch (DocumentException | IOException e) {
			e.printStackTrace();
			if (documento.isOpen()) {
				documento.close();
			}
			return false;
		}
	}

	/**
	 * Genera un PDF con los mensajes intercambiados entre el usuario actual y otro
	 * usuario
	 * 
	 * @param otroUsuario El otro usuario con el que se han intercambiado mensajes
	 * @param rutaArchivo La ruta donde se guardará el archivo PDF
	 * @return true si el PDF se generó correctamente, false en caso contrario
	 */
	public boolean generarPDFMensajesUsuario(Usuario otroUsuario, String rutaArchivo) {
		// Verificar que el usuario sea premium
		if (!usuarioActual.isPremium()) {
			System.err.println("Esta función solo está disponible para usuarios Premium");
			return false;
		}

		if (otroUsuario == null) {
			System.err.println("El usuario de destino no puede ser nulo");
			return false;
		}

		Document documento = new Document();

		try {
			PdfWriter.getInstance(documento, new FileOutputStream(rutaArchivo));
			documento.open();

			// Fuentes para el documento
			Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
			Font fontSubtitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
			Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 10);

			// Título principal
			documento.add(new Paragraph("Mensajes con " + otroUsuario.getUsuario(), fontTitle));
			documento.add(new Paragraph(" ")); // Espacio

			// Información de los usuarios
			documento.add(new Paragraph(
					"Usuario actual: " + usuarioActual.getUsuario() + " (" + usuarioActual.getTelefono() + ")",
					fontNormal));
			documento.add(new Paragraph(
					"Usuario conversación: " + otroUsuario.getUsuario() + " (" + otroUsuario.getTelefono() + ")",
					fontNormal));
			documento.add(new Paragraph(" ")); // Espacio

			// Obtener los mensajes intercambiados
			List<Mensaje> mensajes = usuarioActual.obtenerMensajesCon(otroUsuario);

			// Tabla de mensajes
			documento.add(new Paragraph("Mensajes intercambiados:", fontSubtitle));
			documento.add(new Paragraph(" ")); // Espacio

			if (mensajes.isEmpty()) {
				documento.add(new Paragraph("No hay mensajes intercambiados con este usuario.", fontNormal));
			} else {
				PdfPTable tablaMensajes = new PdfPTable(4); // 4 columnas: Emisor, Receptor, Mensaje, Fecha/Hora
				tablaMensajes.setWidthPercentage(100);
				tablaMensajes.setWidths(new float[] { 1.5f, 1.5f, 4, 2 }); // Proporción de ancho de columnas

				// Encabezados de la tabla
				tablaMensajes.addCell(new Phrase("Emisor", fontSubtitle));
				tablaMensajes.addCell(new Phrase("Receptor", fontSubtitle));
				tablaMensajes.addCell(new Phrase("Mensaje", fontSubtitle));
				tablaMensajes.addCell(new Phrase("Fecha y Hora", fontSubtitle));

				// Añadir mensajes a la tabla
				for (Mensaje mensaje : mensajes) {
					String nombreEmisor = mensaje.getEmisor().equals(usuarioActual) ? "Yo" : otroUsuario.getUsuario();
					String nombreReceptor = mensaje.getReceptor().equals(usuarioActual) ? "Yo"
							: otroUsuario.getUsuario();
					String textoMensaje = mensaje.getTexto();
					if (textoMensaje.startsWith("EMOJI:")) {
						textoMensaje = "[Emoji]"; // Representación simple para emojis
					}
					String fechaHora = mensaje.getFecha().toString() + " "
							+ String.format("%02d:%02d", mensaje.getHora().getHour(), mensaje.getHora().getMinute());

					tablaMensajes.addCell(new Phrase(nombreEmisor, fontNormal));
					tablaMensajes.addCell(new Phrase(nombreReceptor, fontNormal));
					tablaMensajes.addCell(new Phrase(textoMensaje, fontNormal));
					tablaMensajes.addCell(new Phrase(fechaHora, fontNormal));
				}

				documento.add(tablaMensajes);
			}

			documento.close();
			return true;

		} catch (DocumentException | IOException e) {
			e.printStackTrace();
			if (documento.isOpen()) {
				documento.close();
			}
			return false;
		}
	}

}