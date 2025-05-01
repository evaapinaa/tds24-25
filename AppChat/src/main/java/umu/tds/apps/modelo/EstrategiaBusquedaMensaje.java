package umu.tds.apps.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que coordina múltiples estrategias de búsqueda de mensajes.
 * Implementa el patrón Strategy combinado con el patrón Composite.
*/
public class EstrategiaBusquedaMensaje {

    private List<BusquedaMensaje> estrategias = new ArrayList<>();

    /**
     * Añade una estrategia de búsqueda a la lista de estrategias.
     * 
     * @param estrategia Estrategia de búsqueda a añadir
     */
    public void addEstrategiaBusqueda(BusquedaMensaje estrategia) {
        estrategias.add(estrategia);
    }

    /**
     * Ejecuta todas las estrategias de búsqueda en secuencia, filtrando progresivamente la lista.
     * 
     * @param mensajes Lista de mensajes a filtrar
     * @return Lista de mensajes después de aplicar todos los filtros
     * @throws IllegalArgumentException si la lista de mensajes es null
     */
    public List<Mensaje> ejecutarBusqueda(List<Mensaje> mensajes) {
        if (mensajes == null) {
            throw new IllegalArgumentException("La lista de mensajes no puede ser nula");
        }
        List<Mensaje> filtrados = new ArrayList<>(mensajes);
        for (BusquedaMensaje estrategia : estrategias) {
            filtrados = estrategia.buscar(filtrados);
        }
        return filtrados;
    }

}
