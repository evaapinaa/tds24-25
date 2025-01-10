package umu.tds.apps.modelo;

import java.util.ArrayList;
import java.util.List;

public class Grupo extends Contacto {

    // Atributos específicos de Grupo
	private String nombreGrupo;
    private List<ContactoIndividual> listaContactos; // Lista de contactos que pertenecen al grupo
    
    // getters y setters
    public List<ContactoIndividual> getListaContactos() {
        return new ArrayList<>(listaContactos);
    }

    public void setListaContactos(List<ContactoIndividual> listaContactos) {
         this.listaContactos = listaContactos;
    }
    
    public String getNombreGrupo() {
    	return nombreGrupo;      
    }
     
	public void setNombreGrupo(String nombreGrupo) {
    	this.nombreGrupo = nombreGrupo;
    }
	
    // Constructor
    public Grupo(String nombre) {
        super(nombre); // Usa el constructor de la clase base Contacto
        this.listaContactos = new ArrayList<>(); // Inicializa la lista de contactos
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