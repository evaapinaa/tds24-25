package umu.tds.apps.modelo;

import java.util.List;
import java.util.stream.Collectors;

public class BusquedaPorContacto implements BusquedaMensaje {
	
	private String contactoBuscado;

	public BusquedaPorContacto(String contactoBuscado) {
		this.contactoBuscado = contactoBuscado;
	}
	
    @Override
    public List<Mensaje> buscar(List<Mensaje> mensajes, String criterio) {
        return mensajes.stream()
                .filter(m -> m.getEmisor().getUsuario().toLowerCase().contains(criterio.toLowerCase()) || 
                             m.getReceptor().getUsuario().toLowerCase().contains(criterio.toLowerCase()))
                .collect(Collectors.toList());
    }
}