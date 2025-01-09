package umu.tds.apps.modelo;

import java.util.List;
import java.util.stream.Collectors;

public class BusquedaPorTelefono implements BusquedaMensaje {
	
	private String telefonoBuscado;
	
	public BusquedaPorTelefono(String telefonoBuscado) {
		this.telefonoBuscado = telefonoBuscado;
	}
	
    @Override
    public List<Mensaje> buscar(List<Mensaje> mensajes) {
    	if (telefonoBuscado == null || telefonoBuscado.isEmpty()) return mensajes;
        return mensajes.stream()
                .filter(m -> m.getEmisor().getTelefono().contains(telefonoBuscado) || 
                             m.getReceptor().getTelefono().contains(telefonoBuscado))
                .collect(Collectors.toList());
    }
}

