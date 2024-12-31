package umu.tds.apps.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    public String getTexto() {
        return texto;
    }
    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getEmoticono() {
        return emoticono;
    }
    public void setEmoticono(int emoticono) {
        this.emoticono = emoticono;
    }

    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalDateTime getHora() {
        return hora;
    }
    public void setHora(LocalDateTime hora) {
        this.hora = hora;
    }

    public Usuario getEmisor() {
        return emisor;
    }
    public void setEmisor(Usuario emisor) {
        this.emisor = emisor;
    }

    public Usuario getReceptor() {
        return receptor;
    }
    public void setReceptor(Usuario receptor) {
        this.receptor = receptor;
    }

    public Chat getChat() {
        return chat;
    }
    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public int getCodigo() {
        return codigo;
    }
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    // ----------------------------------------------------------------
    // CONSTRUCTORES DE USO "NORMAL" (lanzan excepción si chat==null)
    // ----------------------------------------------------------------
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

    // ----------------------------------------------------------------
    // CONSTRUCTORES "SIN chat" (uso normal) => internamente exigen chat
    // ----------------------------------------------------------------
    public Mensaje(Usuario emisor, String texto, Usuario receptor) {
        this(emisor, texto, receptor, null); 
    }

    public Mensaje(Usuario emisor, int emoticono, Usuario receptor) {
        this(emisor, emoticono, receptor, null);
    }

    // ----------------------------------------------------------------
    // CONSTRUCTOR ESPECIAL PARA TDS (NO chequea chat != null)
    // ----------------------------------------------------------------
    public Mensaje() {
        // Constructor "vacío" para la recuperación TDS
        // No lanza excepción si chat == null
    }

}
