package umu.tds.apps.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ImageIcon;

public class Grupo extends Contacto {

    // Atributos específicos de Grupo
    private String nombreGrupo;
    private List<ContactoIndividual> listaContactos; // Lista de contactos que pertenecen al grupo
    private Usuario creador;
    private ImageIcon imagenGrupo; // Imagen opcional del grupo

    // Constructor para inicializar un grupo con todos los atributos
    public Grupo(String nombreGrupo, List<ContactoIndividual> listaContactos, Usuario creador, ImageIcon imagenGrupo) {
        super(nombreGrupo); // Usa el constructor de la clase base Contacto
        this.nombreGrupo = nombreGrupo;
        this.listaContactos = new ArrayList<>(listaContactos); // Inicializa la lista con los contactos proporcionados
        this.creador = creador;
        this.imagenGrupo = imagenGrupo;
    }

    // Constructor básico (por compatibilidad con código existente)
    public Grupo(String nombreGrupo) {
        super(nombreGrupo);
        this.nombreGrupo = nombreGrupo;
        this.listaContactos = new ArrayList<>();
    }

    // Getters y Setters
    public List<ContactoIndividual> getListaContactos() {
        return Collections.unmodifiableList(listaContactos);
    }

    public void setListaContactos(List<ContactoIndividual> listaContactos) {
        this.listaContactos = new ArrayList<>(listaContactos);
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public Usuario getCreador() {
        return creador;
    }

    public void setCreador(Usuario creador) {
        this.creador = creador;
    }

    public ImageIcon getImagenGrupo() {
        return imagenGrupo;
    }

    public void setImagenGrupo(ImageIcon imagenGrupo) {
        this.imagenGrupo = imagenGrupo;
    }

    // Métodos
    public void agregarContacto(ContactoIndividual contacto) {
        if (!listaContactos.contains(contacto)) {
            listaContactos.add(contacto);
        }
    }

    public void eliminarContacto(ContactoIndividual contacto) {
        listaContactos.remove(contacto);
    }
}
