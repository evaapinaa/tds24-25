
package umu.tds.apps.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.swing.ImageIcon;

import umu.tds.apps.persistencia.AdaptadorContactoIndividualTDS;
import umu.tds.apps.persistencia.AdaptadorUsuarioTDS;

public class Usuario {
	
	// ATRIBUTOS
	
	private String usuario;
	private String contraseña;
	private String telefono;
	private String email;
	private Optional<String> saludo;  // Saludo es opcional
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

	// Método para comprobar si la clave es válida
	public boolean isClaveValida(String contraseña) {
		return this.contraseña.equals(contraseña);
	}
	
	
	// Método para obtener el número de mensajes enviados en el último mes (DESCUENTO)
	public long getNumeroMensajesUltimoMes() {
	    // Fecha actual
	    LocalDate ahora = LocalDate.now();

	    // Primer día y último día del mes actual
	    LocalDate primerDiaDelMes = ahora.withDayOfMonth(1);
	    LocalDate ultimoDiaDelMes = primerDiaDelMes.plusMonths(1).minusDays(1);

	    // Contar los mensajes enviados por el usuario en el mes actual
	    return listaMensajesEnviados.stream()
	        .filter(mensaje -> {
	            // Convertir LocalDateTime a LocalDate
	            LocalDate fechaEnvio = mensaje.getFecha();

	            // Verificar si la fecha está dentro del mes actual
	            return !fechaEnvio.isBefore(primerDiaDelMes) && !fechaEnvio.isAfter(ultimoDiaDelMes);
	        })
	        .count();
	}
	
	public List<Grupo> getGrupos() {
	    return listaContactos.stream()
	            .filter(c -> c instanceof Grupo)
	            .map(c -> (Grupo) c)
	            .toList(); // Cambiar a `collect(Collectors.toList())` si usas una versión de Java anterior a 16.
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
	
	// Cambio
    public boolean añadirContacto(Contacto contacto) {
        if (!listaContactos.contains(contacto)) {
            listaContactos.add(contacto);
            
            // Guardar los cambios del usuario en persistencia
            AdaptadorUsuarioTDS.getUnicaInstancia().modificarUsuario(this);
            
            return true;
        }
        return false;
    }
	
	public void añadirChat(Chat chat) {
	    if (!listaChats.contains(chat)) {
	        listaChats.add(chat);
	    }
	}
	
	public Chat obtenerChatCon(Usuario otroUsuario) {
	    return listaChats.stream()
	        .filter(ch -> ch.involucraUsuario(otroUsuario))
	        .findFirst()
	        .orElse(null);
	}
	

	
	public List<Mensaje> obtenerMensajesCon(Usuario otroUsuario) {
	    List<Mensaje> mensajes = new LinkedList<>();
	    for (Mensaje mensaje : listaMensajesEnviados) {
	        if (mensaje.getReceptor().equals(otroUsuario)) {
	            mensajes.add(mensaje);
	        }
	    }
	    for (Mensaje mensaje : listaMensajesRecibidos) {
	        if (mensaje.getEmisor().equals(otroUsuario)) {
	            mensajes.add(mensaje);
	        }
	    }
	    mensajes.sort((m1, m2) -> {
	        if (m1.getFecha().equals(m2.getFecha())) {
	            return m1.getHora().compareTo(m2.getHora());
	        }
	        return m1.getFecha().compareTo(m2.getFecha());
	    });
	    return mensajes;
	}
	
	public void activarPremium() {
	    this.premium = true;
	}

	public void desactivarPremium() {
	    this.premium = false;
	}

	public Chat getChatMensajes(Usuario otroUsuario) {
		return listaChats.stream()
				.filter(chat -> chat.getOtroUsuarioChat().equals(otroUsuario))
				.findFirst()
				.orElse(null);
	}
	
    // Método para añadir un mensaje enviado
    public void añadirMensajeEnviado(Mensaje mensaje) {
        this.listaMensajesEnviados.add(mensaje);
    }

    // Método para añadir un mensaje recibido
    public void añadirMensajeRecibido(Mensaje mensaje) {
        this.listaMensajesRecibidos.add(mensaje);
    }

    // CONSTRUCTOR
	
	public Usuario(String usuario, String contraseña, String telefono, String email, Optional<String> saludo, ImageIcon imagenPerfil, LocalDate fechaNacimiento,
			LocalDate fechaRegistro) {
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


	public Contacto obtenerContactoCon(Usuario otroUsuario) {
	    return listaContactos.stream()
	            .filter(c -> {
	                if (c instanceof ContactoIndividual) {
	                    return ((ContactoIndividual) c).getUsuario().equals(otroUsuario);
	                }
	                return false;
	            })
	            .findFirst()
	            .orElse(null);
	}


}
