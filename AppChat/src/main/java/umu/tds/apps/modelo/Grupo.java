
package umu.tds.apps.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ImageIcon;

/**
 * Clase que representa un grupo de contactos.
 * Extiende la clase Contacto.
*/
public class Grupo extends Contacto {

    // Atributos específicos de Grupo
    private String nombreGrupo;
    private List<ContactoIndividual> listaContactos; // Lista de contactos que pertenecen al grupo
    private Usuario creador;
    private ImageIcon imagenGrupo; // Imagen opcional del grupo
    private List<Mensaje> listaMensajesEnviados;

    // Constructor para inicializar un grupo con todos los atributos
    /**
     * Crea un nuevo grupo con todos los atributos.
     * 
     * @param nombreGrupo Nombre del grupo
     * @param listaContactos Lista de contactos que pertenecen al grupo
     * @param creador Usuario que creó el grupo
     * @param imagenGrupo Imagen opcional del grupo
     */
    public Grupo(String nombreGrupo, List<ContactoIndividual> listaContactos, Usuario creador, ImageIcon imagenGrupo) {
        super(nombreGrupo); // Usa el constructor de la clase base Contacto
        this.nombreGrupo = nombreGrupo;
        this.listaContactos = new ArrayList<>(listaContactos); // Inicializa la lista con los contactos proporcionados
        this.creador = creador;
        this.imagenGrupo = imagenGrupo;
        listaMensajesEnviados = new ArrayList<>();
    }

    // Constructor básico (por compatibilidad con código existente)
    /**
     * Constructor básico que crea un grupo solo con su nombre.
     * 
     * @param nombreGrupo Nombre del grupo
     */
    public Grupo(String nombreGrupo) {
        super(nombreGrupo);
        this.nombreGrupo = nombreGrupo;
        this.listaContactos = new ArrayList<>();
    }

    // Getters y Setters
    /**
     * Obtiene la lista de contactos individuales que pertenecen al grupo.
     * 
     * @return Lista inmutable de contactos
     */
    public List<ContactoIndividual> getListaContactos() {
        return Collections.unmodifiableList(listaContactos);
    }

    
    /**
     * Establece la lista de contactos del grupo.
     * 
     * @param listaContactos Nueva lista de contactos
     */
    public void setListaContactos(List<ContactoIndividual> listaContactos) {
        this.listaContactos = new ArrayList<>(listaContactos);
    }

    
    /**
     * Obtiene el nombre del grupo.
     * 
     * @return Nombre del grupo
     */
    public String getNombreGrupo() {
        return nombreGrupo;
    }

     /**
     * Establece el nombre del grupo.
     * 
     * @param nombreGrupo Nuevo nombre
     */
    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    /**
     * Obtiene el usuario creador del grupo.
     * 
     * @return Usuario creador
     */
    public Usuario getCreador() {
        return creador;
    }

    /**
     * Establece el usuario creador del grupo.
     * 
     * @param creador Nuevo creador
     */
    public void setCreador(Usuario creador) {
        this.creador = creador;
    }

    
    /**
     * Obtiene la imagen asociada al grupo.
     * 
     * @return Imagen del grupo
     */
    public ImageIcon getImagenGrupo() {
        return imagenGrupo;
    }
    
    
    /**
     * Obtiene la lista de mensajes enviados en el grupo.
     * 
     * @return Lista inmutable de mensajes
     */
	public List<Mensaje> getListaMensajesEnviados() {
		return Collections.unmodifiableList(listaMensajesEnviados);
	}
	
	
	/**
     * Añade un mensaje a la lista de mensajes enviados en el grupo.
     * 
     * @param mensaje Mensaje a añadir
     */
	public void addMensajeEnviado(Mensaje mensaje) {
		listaMensajesEnviados.add(mensaje);
	}

	
	  /**
     * Establece la imagen del grupo.
     * 
     * @param imagenGrupo Nueva imagen
     */
    public void setImagenGrupo(ImageIcon imagenGrupo) {
        this.imagenGrupo = imagenGrupo;
    }

    // Métodos
    /**
     * Agrega un nuevo contacto al grupo.
     * 
     * @param contacto Contacto a agregar
     */
    public void agregarContacto(ContactoIndividual contacto) {
        if (!listaContactos.contains(contacto)) {
            listaContactos.add(contacto);
        }
    }

    /**
     * Elimina un contacto del grupo.
     * 
     * @param contacto Contacto a eliminar
     */
    public void eliminarContacto(ContactoIndividual contacto) {
        listaContactos.remove(contacto);
    }
}
