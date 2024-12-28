package umu.tds.apps.modelo;

import java.util.List;
import java.util.stream.Collectors;

public class BusquedaPorTexto implements BusquedaMensaje {
	
	private String textoBuscado;
	
	public BusquedaPorTexto(String textoBuscado) {
		this.textoBuscado = textoBuscado;	
	}
	
    @Override
    public List<Mensaje> buscar(List<Mensaje> mensajes, String criterio) {
        return mensajes.stream()
                .filter(m -> m.getTexto().toLowerCase().contains(criterio.toLowerCase()))
                .collect(Collectors.toList());
    }
}
