package umu.tds.apps.modelo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase que implementa la búsqueda de mensajes por texto contenido.
 * Implementa la interfaz BusquedaMensaje.
 */
public class BusquedaPorTexto implements BusquedaMensaje {
	
	private String textoBuscado;
	
    /**
     * Crea una nueva instancia de búsqueda por texto.
     * 
     * @param textoBuscado Texto a buscar en los mensajes
     */
	public BusquedaPorTexto(String textoBuscado) {
		this.textoBuscado = textoBuscado;	
	}
	
	
    /**
     * Filtra los mensajes para obtener solo aquellos que contienen el texto buscado.
     * 
     * @param mensajes Lista de mensajes a filtrar
     * @return Lista de mensajes filtrados
     */
	public List<Mensaje> buscar(List<Mensaje> mensajes) {
	    if (textoBuscado == null || textoBuscado.isEmpty()) return mensajes;
	    return mensajes.stream()
	                   .filter(m -> m.getTexto().toLowerCase().contains(textoBuscado.toLowerCase()))
	                   .collect(Collectors.toList());
	}

}
