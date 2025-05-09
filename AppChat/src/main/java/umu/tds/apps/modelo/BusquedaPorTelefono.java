package umu.tds.apps.modelo;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Clase que implementa la búsqueda de mensajes por número de teléfono.
 * Implementa la interfaz BusquedaMensaje.
*/
public class BusquedaPorTelefono implements BusquedaMensaje {
	
	private String telefonoBuscado;
	
	
    /**
     * Crea una nueva instancia de búsqueda por teléfono.
     * 
     * @param telefonoBuscado Número de teléfono a buscar
     */
	public BusquedaPorTelefono(String telefonoBuscado) {
		this.telefonoBuscado = telefonoBuscado;
	}
	
	
	
    /**
     * Filtra los mensajes para obtener solo aquellos relacionados con el teléfono buscado.
     * 
     * @param mensajes Lista de mensajes a filtrar
     * @return Lista de mensajes filtrados
     */
    @Override
    public List<Mensaje> buscar(List<Mensaje> mensajes) {
    	if (telefonoBuscado == null || telefonoBuscado.isEmpty()) return mensajes;
        return mensajes.stream()
                .filter(m -> m.getEmisor().getTelefono().contains(telefonoBuscado) || 
                             m.getReceptor().getTelefono().contains(telefonoBuscado))
                .collect(Collectors.toList());
    }
}

