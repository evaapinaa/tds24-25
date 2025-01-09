package umu.tds.apps.modelo;

import java.util.ArrayList;
import java.util.List;


public class EstrategiaBusquedaMensaje {

    private List<BusquedaMensaje> estrategias = new ArrayList<>();

    // Añadir una estrategia
    public void addEstrategiaBusqueda(BusquedaMensaje estrategia) {
        estrategias.add(estrategia);
    }

    // Ejecutar todas las estrategias de búsqueda
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
