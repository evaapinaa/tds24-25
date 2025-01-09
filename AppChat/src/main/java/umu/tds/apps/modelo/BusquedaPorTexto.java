package umu.tds.apps.modelo;

import java.util.List;
import java.util.stream.Collectors;

public class BusquedaPorTexto implements BusquedaMensaje {
	
	private String textoBuscado;
	
	public BusquedaPorTexto(String textoBuscado) {
		this.textoBuscado = textoBuscado;	
	}
	
	public List<Mensaje> buscar(List<Mensaje> mensajes) {
	    if (textoBuscado == null || textoBuscado.isEmpty()) return mensajes;
	    return mensajes.stream()
	                   .filter(m -> m.getTexto().toLowerCase().contains(textoBuscado.toLowerCase()))
	                   .collect(Collectors.toList());
	}

}
