package umu.tds.apps.modelo;

import java.util.ArrayList;
import java.util.List;

public class EstrategiaBusquedaMensaje {
    
    // En lugar de un solo 'private BusquedaMensaje estrategiaBusqueda;'
    // almacenamos varias
    private List<BusquedaMensaje> estrategias = new ArrayList<>();

    // Añadimos una estrategia concreta (por texto, teléfono, contacto…)
    public void addEstrategiaBusqueda(BusquedaMensaje estrategia) {
        estrategias.add(estrategia);
    }

    // Aplica TODAS las estrategias en secuencia
    public List<Mensaje> ejecutarBusqueda(List<Mensaje> mensajes) {
        List<Mensaje> filtrados = new ArrayList<>(mensajes);
        
        for (BusquedaMensaje e : estrategias) {
            // Cada estrategia filtra la lista resultante anterior
            filtrados = e.buscar(filtrados, "ignorar o no, depende de tu diseño");
        }
        
        return filtrados;
    }
}