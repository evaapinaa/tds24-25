package umu.tds.apps.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;


/**
 * Clase que representa un mensaje enviado entre usuarios.
*/
public class Mensaje {

    // ATRIBUTOS
    private String texto;
    private int emoticono;
    private LocalDate fecha;
    private LocalDateTime hora;
    private Usuario emisor;
    private Usuario receptor;
    private Chat chat;
    private int codigo;

    // GETTERS Y SETTERS
    /**
     * Obtiene el texto del mensaje.
     * 
     * @return Texto del mensaje
     */
    public String getTexto() {
        return texto;
    }
    
    /**
     * Establece el texto del mensaje.
     * 
     * @param texto Nuevo texto
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * Obtiene el código del emoticono.
     * 
     * @return Código del emoticono
     */
    public int getEmoticono() {
        return emoticono;
    }
    /**
     * Establece el código del emoticono.
     * 
     * @param emoticono Nuevo código
     */
    public void setEmoticono(int emoticono) {
        this.emoticono = emoticono;
    }

    
    /**
     * Obtiene la fecha de envío del mensaje.
     * 
     * @return Fecha del mensaje
     */
    public LocalDate getFecha() {
        return fecha;
    }
    /**
     * Establece la fecha de envío del mensaje.
     * 
     * @param fecha Nueva fecha
     */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene la hora de envío del mensaje.
     * 
     * @return Hora del mensaje
     */
    public LocalDateTime getHora() {
        return hora;
    }
    /**
     * Establece la hora de envío del mensaje.
     * 
     * @param hora Nueva hora
     */
    public void setHora(LocalDateTime hora) {
        this.hora = hora;
    }

    /**
     * Obtiene el usuario emisor del mensaje.
     * 
     * @return Usuario emisor
     */
    public Usuario getEmisor() {
        return emisor;
    }
    
    /**
     * Establece el usuario emisor del mensaje.
     * 
     * @param emisor Nuevo emisor
     */
    public void setEmisor(Usuario emisor) {
        this.emisor = emisor;
    }

    
    /**
     * Obtiene el usuario receptor del mensaje.
     * 
     * @return Usuario receptor
     */
    public Usuario getReceptor() {
        return receptor;
    }
    
    /**
     * Establece el usuario receptor del mensaje.
     * 
     * @param receptor Nuevo receptor
     */
    public void setReceptor(Usuario receptor) {
        this.receptor = receptor;
    }

    
    /**
     * Obtiene el chat al que pertenece el mensaje.
     * 
     * @return Chat del mensaje
     */
    public Chat getChat() {
        return chat;
    }
    
    /**
     * Establece el chat al que pertenece el mensaje.
     * 
     * @param chat Nuevo chat
     */
    public void setChat(Chat chat) {
        this.chat = chat;
    }

    
    /**
     * Obtiene el código identificador del mensaje.
     * 
     * @return Código del mensaje
     */
    public int getCodigo() {
        return codigo;
    }
    
    /**
     * Establece el código identificador del mensaje.
     * 
     * @param codigo Nuevo código
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    
    /**
     * Crea un nuevo mensaje de texto.
     * 
     * @param emisor Usuario que envía el mensaje
     * @param texto Contenido del mensaje
     * @param receptor Usuario que recibe el mensaje
     * @param chat Chat al que pertenece el mensaje
     * @throws IllegalArgumentException si el chat es null
     */
    public Mensaje(Usuario emisor, String texto, Usuario receptor, Chat chat) {
        if (chat == null) {
            throw new IllegalArgumentException("El chat no puede ser null (uso normal).");
        }
        this.emisor = emisor;
        this.texto = texto;
        this.receptor = receptor;
        this.chat = chat;
        this.fecha = LocalDate.now();
        this.hora = LocalDateTime.now();
    }

    /**
     * Crea un nuevo mensaje de tipo emoticono.
     * 
     * @param emisor Usuario que envía el mensaje
     * @param emoticono Código del emoticono
     * @param receptor Usuario que recibe el mensaje
     * @param chat Chat al que pertenece el mensaje
     * @throws IllegalArgumentException si el chat es null
     */
    public Mensaje(Usuario emisor, int emoticono, Usuario receptor, Chat chat) {
        if (chat == null) {
            throw new IllegalArgumentException("El chat no puede ser null (uso normal).");
        }
        this.emisor = emisor;
        this.emoticono = emoticono;
        this.receptor = receptor;
        this.chat = chat;
        this.fecha = LocalDate.now();
        this.hora = LocalDateTime.now();
    }

    /**
     * Constructor "sin chat" para uso normal (internamente exige chat).
     * 
     * @param emisor Usuario que envía el mensaje
     * @param texto Contenido del mensaje
     * @param receptor Usuario que recibe el mensaje
     */
    public Mensaje(Usuario emisor, String texto, Usuario receptor) {
        this(emisor, texto, receptor, null); 
    }

    
    /**
     * Constructor "sin chat" para emoticono (uso normal).
     * 
     * @param emisor Usuario que envía el mensaje
     * @param emoticono Código del emoticono
     * @param receptor Usuario que recibe el mensaje
     */
    public Mensaje(Usuario emisor, int emoticono, Usuario receptor) {
        this(emisor, emoticono, receptor, null);
    }

    /**
     * Constructor especial para TDS (no chequea chat != null).
     * Usado para la recuperación desde el almacenamiento.
     */
    public Mensaje() {
        // Constructor "vacío" para la recuperación TDS
        // No lanza excepción si chat == null
    }
    
    
    
    /**
     * Compara este mensaje con otro objeto para determinar igualdad.
     * Dos mensajes son iguales si tienen el mismo código.
     * 
     * @param obj Objeto a comparar
     * @return true si los mensajes son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Mensaje other = (Mensaje) obj;
        return codigo == other.codigo;
    }
    
    
    /**
     * Genera un código hash basado en el código del mensaje.
     * 
     * @return Código hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
    
    
}
