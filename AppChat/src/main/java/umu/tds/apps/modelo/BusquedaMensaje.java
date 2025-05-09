package umu.tds.apps.modelo;

import java.util.List;


/**
 * Interfaz que define el comportamiento de búsqueda de mensajes.
 * Implementa el patrón Strategy para filtrar mensajes.
*/
public interface BusquedaMensaje {
    /**
     * Filtra una lista de mensajes según un criterio específico.
     * 
     * @param mensajes Lista de mensajes a filtrar
     * @return Lista de mensajes filtrados
     */
    List<Mensaje> buscar(List<Mensaje> mensajes);
}