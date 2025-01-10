package umu.tds.apps.modelo;

import java.util.List;
import java.util.stream.Collectors;

public class BusquedaPorContacto implements BusquedaMensaje {
    
    private String contactoBuscado;
    private Usuario usuarioActual;

    // Constructor actualizado para recibir el usuario actual
    public BusquedaPorContacto(Usuario usuarioActual, String contactoBuscado) {
        this.usuarioActual = usuarioActual;
        this.contactoBuscado = contactoBuscado;
    }
    
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