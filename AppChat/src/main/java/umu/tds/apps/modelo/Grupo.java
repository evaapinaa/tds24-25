package umu.tds.apps.modelo;

import java.util.List;

public class Grupo extends Contacto {

	public String nombre;
	private List<ContactoIndividual> listaContactos;
	
	public Grupo(String nombre) {
		super(nombre);
		this.nombre = nombre;
	}
}
