package umu.tds.apps.modelo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase que implementa la búsqueda de mensajes por contacto.
 * Implementa la interfaz BusquedaMensaje.
*/
public class BusquedaPorContacto implements BusquedaMensaje {
    
    private String contactoBuscado;
    private Usuario usuarioActual;

    // Constructor actualizado para recibir el usuario actual
    /**
     * Crea una nueva instancia de búsqueda por contacto.
     * 
     * @param usuarioActual Usuario actual que realiza la búsqueda
     * @param contactoBuscado Nombre del contacto a buscar
     */
    public BusquedaPorContacto(Usuario usuarioActual, String contactoBuscado) {
        this.usuarioActual = usuarioActual;
        this.contactoBuscado = contactoBuscado;
    }
    
    
    /**
     * Filtra los mensajes para obtener solo aquellos relacionados con el contacto buscado.
     * 
     * @param mensajes Lista de mensajes a filtrar
     * @return Lista de mensajes filtrados
     */
    @Override
    public List<Mensaje> buscar(List<Mensaje> mensajes) {
        if (contactoBuscado == null || contactoBuscado.isEmpty()) return mensajes;
        
        return mensajes.stream()
                .filter(m -> {
                    Usuario otroUsuario = m.getEmisor().equals(usuarioActual) ? m.getReceptor() : m.getEmisor();
                    Contacto contacto = usuarioActual.obtenerContactoCon(otroUsuario);
                    if (contacto instanceof ContactoIndividual) {
                        String nombreContacto = ((ContactoIndividual) contacto).getNombre();
                        boolean matches = nombreContacto.toLowerCase().contains(contactoBuscado.toLowerCase());
                        System.out.println("Comparando contacto: " + nombreContacto + " con buscado: " + contactoBuscado + " -> " + matches);
                        return matches;
                    }
                    System.out.println("Mensaje con " + otroUsuario.getUsuario() + " no es ContactoIndividual.");
                    return false;
                })
                .collect(Collectors.toList());
    }

}