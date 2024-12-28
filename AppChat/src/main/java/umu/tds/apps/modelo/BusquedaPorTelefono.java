package umu.tds.apps.modelo;

import java.util.List;
import java.util.stream.Collectors;

public class BusquedaPorTelefono implements BusquedaMensaje {
	
	private String telefonoBuscado;
	
	public BusquedaPorTelefono(String telefonoBuscado) {
		this.telefonoBuscado = telefonoBuscado;
	}
	
    @Override
    public List<Mensaje> buscar(List<Mensaje> mensajes, String criterio) {
        return mensajes.stream()
                .filter(m -> m.getEmisor().getTelefono().contains(criterio) || 
                             m.getReceptor().getTelefono().contains(criterio))
                .collect(Collectors.toList());
    }
}

