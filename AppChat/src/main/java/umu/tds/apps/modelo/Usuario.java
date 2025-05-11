
package umu.tds.apps.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.ImageIcon;

import umu.tds.apps.persistencia.AdaptadorUsuarioTDS;



/**
 * Clase que representa un usuario en el sistema AppChat.
 * Contiene toda la información personal del usuario, sus contactos, chats y mensajes.
*/
public class Usuario {

	// ATRIBUTOS

	private String usuario;
	private String contraseña;
	private String telefono;
	private String email;
	private Optional<String> saludo; // Saludo es opcional
	private ImageIcon imagenPerfil;
	private LocalDate fechaNacimiento;
	private List<Contacto> listaContactos;
	private List<Mensaje> listaMensajesEnviados;
	private List<Mensaje> listaMensajesRecibidos;
	private List<Chat> listaChats;
	private boolean premium;
	private int codigo;
	private LocalDate fechaRegistro;

	// GETTERS AND SETTERS

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contaseña) {
		this.contraseña = contaseña;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Optional<String> getSaludo() {
		return saludo;
	}

	public void setSaludo(Optional<String> saludo) {
		this.saludo = saludo;
	}

	public ImageIcon getImagenPerfil() {
		return imagenPerfil;
	}

	public void setImagenPerfil(ImageIcon imagenPerfil) {
		this.imagenPerfil = imagenPerfil;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public LocalDate getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDate fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public boolean isPremium() {
		return premium;
	}

	public void setPremium(boolean premium) {
		this.premium = premium;
	}

	public List<Chat> getListaChats() {
		return listaChats;
	}

	public void setListaChats(List<Chat> listaChats) {
		this.listaChats = listaChats;
	}



	 /**
     * Verifica si la contraseña proporcionada es válida para este usuario.
     * 
     * @param contraseña Contraseña a verificar
     * @return true si la contraseña es correcta, false en caso contrario
     */
	public boolean isClaveValida(String contraseña) {
		return this.contraseña.equals(contraseña);
	}

	 /**
     * Obtiene el número de mensajes enviados por el usuario en el último mes.
     * Usado para calcular descuentos en la suscripción premium.
     * 
     * @return Cantidad de mensajes enviados en el último mes
     */
	public long getNumeroMensajesUltimoMes() {
		// Fecha actual
		LocalDate ahora = LocalDate.now();

		// Primer día y último día del mes actual
		LocalDate primerDiaDelMes = ahora.withDayOfMonth(1);
		LocalDate ultimoDiaDelMes = primerDiaDelMes.plusMonths(1).minusDays(1);

		// Contar los mensajes enviados por el usuario en el mes actual
		return listaMensajesEnviados.stream().filter(mensaje -> {
			// Verificar si la fecha está dentro del mes actual
			LocalDate fechaEnvio = mensaje.getFecha();
			return !fechaEnvio.isBefore(primerDiaDelMes) && !fechaEnvio.isAfter(ultimoDiaDelMes);
		}).count();
	}
	
	
	 /**
     * Obtiene la lista de grupos a los que pertenece el usuario.
     * 
     * @return Lista de grupos
     */
	public List<Grupo> getGrupos() {
		return listaContactos.stream().filter(c -> c instanceof Grupo).map(c -> (Grupo) c).toList(); 
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public List<Contacto> getListaContactos() {
		return new LinkedList<Contacto>(listaContactos); // comprobar
	}

	public void setListaContactos(List<Contacto> listaContactos) {
		this.listaContactos = listaContactos;
	}

	public List<Mensaje> getListaMensajesEnviados() {
		return Collections.unmodifiableList(listaMensajesEnviados);
	}

	public void setListaMensajesEnviados(List<Mensaje> listaMensajesEnviados) {
		this.listaMensajesEnviados = listaMensajesEnviados;
	}

	public List<Mensaje> getListaMensajesRecibidos() {
		return Collections.unmodifiableList(listaMensajesRecibidos);
	}

	public void setListaMensajesRecibidos(List<Mensaje> listaMensajesRecibidos) {
		this.listaMensajesRecibidos = listaMensajesRecibidos;
	}

	 /**
     * Añade un contacto a la lista de contactos del usuario.
     * 
     * @param contacto Contacto a añadir
     * @return true si el contacto se añadió correctamente, false si ya existía
     */
	public boolean añadirContacto(Contacto contacto) {
		if (!listaContactos.contains(contacto)) {
			listaContactos.add(contacto);

			// Guardar los cambios del usuario en persistencia
			AdaptadorUsuarioTDS.getUnicaInstancia().modificarUsuario(this);

			return true;
		}
		return false;
	}

	
	 /**
     * Añade un chat a la lista de chats del usuario.
     * 
     * @param chat Chat a añadir
     */
	public void añadirChat(Chat chat) {
		if (!listaChats.contains(chat)) {
			listaChats.add(chat);
		}
	}

	
    /**
     * Obtiene el chat existente con otro usuario, si existe.
     * 
     * @param otroUsuario Usuario con el que se busca el chat
     * @return Chat encontrado o null si no existe
     */
	public Chat obtenerChatCon(Usuario otroUsuario) {
		return listaChats.stream().filter(ch -> ch.involucraUsuario(otroUsuario)).findFirst().orElse(null);
	}

	
	 /**
     * Obtiene todos los mensajes intercambiados con otro usuario.
     * 
     * @param otroUsuario Usuario con el que se intercambiaron mensajes
     * @return Lista de mensajes ordenados cronológicamente
     */
	public List<Mensaje> obtenerMensajesCon(Usuario otroUsuario) {
		List<Mensaje> mensajes = new LinkedList<>();

		// Añadir mensajes enviados al otro usuario
		for (Mensaje mensaje : listaMensajesEnviados) {
			if (mensaje.getReceptor().equals(otroUsuario)) {
				mensajes.add(mensaje);
			}
		}

		// Añadir mensajes recibidos del otro usuario
		for (Mensaje mensaje : listaMensajesRecibidos) {
			if (mensaje.getEmisor().equals(otroUsuario)) {
				mensajes.add(mensaje);
			}
		}

		// Ordenar por fecha y hora
		mensajes.sort((m1, m2) -> {
			int cmp = m1.getFecha().compareTo(m2.getFecha());
			if (cmp == 0) {
				return m1.getHora().compareTo(m2.getHora());
			}
			return cmp;
		});

		return mensajes;
	}


	 
    /**
     * Activa la suscripción premium para el usuario.
     */
	public void activarPremium() {
		this.premium = true;
	}

	 /**
     * Desactiva la suscripción premium para el usuario.
     */
	public void desactivarPremium() {
		this.premium = false;
	}
	
	
    /**
     * Obtiene el chat que contiene mensajes con otro usuario.
     * 
     * @param otroUsuario Usuario con el que se buscan mensajes
     * @return Chat encontrado o null si no existe
     */
	public Chat getChatMensajes(Usuario otroUsuario) {
		return listaChats.stream().filter(chat -> chat.getOtroUsuarioChat().equals(otroUsuario)).findFirst()
				.orElse(null);
	}
	
	
	  /**
     * Añade un mensaje a la lista de mensajes enviados.
     * 
     * @param mensaje Mensaje enviado
     */
	public void añadirMensajeEnviado(Mensaje mensaje) {
		this.listaMensajesEnviados.add(mensaje);
	}

	
	   /**
     * Añade un mensaje a la lista de mensajes recibidos.
     * 
     * @param mensaje Mensaje recibido
     */
	public void añadirMensajeRecibido(Mensaje mensaje) {
		this.listaMensajesRecibidos.add(mensaje);
	}

	
    /**
     * Filtra los mensajes según varios criterios.
     * 
     * @param texto Texto contenido en el mensaje
     * @param telefono Teléfono del emisor o receptor
     * @param contacto Nombre del contacto
     * @return Lista de mensajes que cumplen los criterios
     */
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
			estrategiaBusqueda.addEstrategiaBusqueda(new BusquedaPorContacto(this, contacto));
		}

		// Obtener mensajes enviados y recibidos del usuario actual
		List<Mensaje> mensajes = new ArrayList<>();
		mensajes.addAll(this.getListaMensajesEnviados());
		mensajes.addAll(this.getListaMensajesRecibidos());

		// Ejecutar todas las estrategias y devolver los mensajes filtrados
		return estrategiaBusqueda.ejecutarBusqueda(mensajes);
	}
	
	
	 /**
     * Crea un nuevo chat con otro usuario o devuelve el existente.
     * 
     * @param otroUsuario Usuario con el que crear el chat
     * @return Chat nuevo o existente
     */
	public Chat crearChatCon(Usuario otroUsuario) {
	    // Verificar si ya existe un chat con este usuario
	    Chat chatExistente = obtenerChatCon(otroUsuario);
	    if (chatExistente != null) {
	        return chatExistente; // Devuelve el chat existente sin crear uno nuevo
	    }
	    
	    // Crear nuevo chat
	    Chat nuevoChat = new Chat(this, otroUsuario);
	    
	    // Añadir el chat a ambos usuarios
	    this.añadirChat(nuevoChat);
	    otroUsuario.añadirChat(nuevoChat);
	    
	    return nuevoChat;
	}
		
	
		// CONSTRUCTOR
	 /**
     * Crea un nuevo usuario con la información básica.
     * 
     * @param usuario Nombre de usuario
     * @param contraseña Contraseña del usuario
     * @param telefono Número de teléfono (identificador único)
     * @param email Correo electrónico
     * @param saludo Mensaje de saludo opcional
     * @param imagenPerfil Imagen de perfil del usuario
     * @param fechaNacimiento Fecha de nacimiento del usuario
     * @param fechaRegistro Fecha de registro en el sistema
     */

	public Usuario(String usuario, String contraseña, String telefono, String email, Optional<String> saludo,
			ImageIcon imagenPerfil, LocalDate fechaNacimiento, LocalDate fechaRegistro) {
		this.usuario = usuario;
		this.contraseña = contraseña;
		this.telefono = telefono;
		this.email = email;
		this.saludo = saludo;
		this.imagenPerfil = imagenPerfil;
		this.fechaNacimiento = fechaNacimiento;
		this.fechaRegistro = LocalDate.now();
		this.listaContactos = new LinkedList<>();
		this.listaMensajesEnviados = new LinkedList<>();
		this.listaMensajesRecibidos = new LinkedList<>();
		this.listaChats = new LinkedList<>();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (telefono == null) {
			if (other.telefono != null)
				return false;
		} else if (!telefono.equals(other.telefono))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		return telefono.hashCode();
	}

	
	 /**
     * Obtiene el contacto asociado a un usuario.
     * 
     * @param otroUsuario Usuario del que se busca el contacto
     * @return Contacto asociado o null si no existe
     */
	public Contacto obtenerContactoCon(Usuario otroUsuario) {
		return listaContactos.stream().filter(c -> {
			if (c instanceof ContactoIndividual) {
				return ((ContactoIndividual) c).getUsuario().equals(otroUsuario);
			}
			return false;
		}).findFirst().orElse(null);
	}

}
