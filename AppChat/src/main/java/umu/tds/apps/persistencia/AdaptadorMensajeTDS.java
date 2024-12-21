package umu.tds.apps.persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.Usuario;
import beans.Entidad;
import beans.Propiedad;



public class AdaptadorMensajeTDS implements IAdaptadorMensajeDAO {
    private static ServicioPersistencia servPersistencia;
    private static AdaptadorMensajeTDS unicaInstancia = null;

    public static AdaptadorMensajeTDS getUnicaInstancia() {
        if (unicaInstancia == null)
            return new AdaptadorMensajeTDS();
        else
            return unicaInstancia;
    }

    private AdaptadorMensajeTDS() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }

    @Override
    public void registrarMensaje(Mensaje mensaje) {
        Entidad eMensaje = null;

        // Si la entidad está registrada, no la registra de nuevo
        try {
            eMensaje = servPersistencia.recuperarEntidad(mensaje.getCodigo());
        } catch (NullPointerException e) {}
        if (eMensaje != null) return;

        // Crear entidad Mensaje
        eMensaje = new Entidad();
        eMensaje.setNombre("mensaje");
        eMensaje.setPropiedades(new ArrayList<>(Arrays.asList(
            new Propiedad("texto", mensaje.getTexto()),
            new Propiedad("emisor", String.valueOf(mensaje.getEmisor().getCodigo())),
            new Propiedad("receptor", String.valueOf(mensaje.getReceptor().getCodigo())),
            new Propiedad("fecha", mensaje.getFecha().toString()),
            new Propiedad("hora", mensaje.getHora().toString())
        )));

        // Registrar entidad mensaje
        eMensaje = servPersistencia.registrarEntidad(eMensaje);
        mensaje.setCodigo(eMensaje.getId());
    }

    @Override
    public Mensaje recuperarMensaje(int codigo) {
        if (PoolDAO.getUnicaInstancia().contiene(codigo))
            return (Mensaje) PoolDAO.getUnicaInstancia().getObjeto(codigo);

        Entidad eMensaje = servPersistencia.recuperarEntidad(codigo);

        String texto = servPersistencia.recuperarPropiedadEntidad(eMensaje, "texto");
        int emisorId = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, "emisor"));
        int receptorId = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, "receptor"));

        Usuario emisor = AdaptadorUsuarioTDS.getUnicaInstancia().recuperarUsuario(emisorId);
        Usuario receptor = AdaptadorUsuarioTDS.getUnicaInstancia().recuperarUsuario(receptorId);

        Mensaje mensaje = new Mensaje(emisor, texto, receptor);
        mensaje.setCodigo(codigo);

        PoolDAO.getUnicaInstancia().addObjeto(codigo, mensaje);
        return mensaje;
    }

    @Override
    public List<Mensaje> recuperarTodosMensajes() {
        List<Entidad> eMensajes = servPersistencia.recuperarEntidades("mensaje");
        List<Mensaje> mensajes = new LinkedList<>();

        for (Entidad eMensaje : eMensajes) {
            mensajes.add(recuperarMensaje(eMensaje.getId()));
        }
        return mensajes;
    }
    
    private String obtenerCodigosMensajes(List<Mensaje> listaMensajes) {
        StringBuilder codigos = new StringBuilder();
        for (Mensaje mensaje : listaMensajes) {
            codigos.append(mensaje.getCodigo()).append(" ");
        }
        return codigos.toString().trim(); // Devuelve la lista de códigos separados por espacio
    }
    
    private List<Mensaje> obtenerMensajesDesdeCodigos(String codigos) {
        List<Mensaje> listaMensajes = new LinkedList<>();
        StringTokenizer tokenizer = new StringTokenizer(codigos, " ");
        AdaptadorMensajeTDS adaptadorMensaje = AdaptadorMensajeTDS.getUnicaInstancia();
        
        while (tokenizer.hasMoreTokens()) {
            int codigo = Integer.parseInt(tokenizer.nextToken());
            listaMensajes.add(adaptadorMensaje.recuperarMensaje(codigo));
        }
        return listaMensajes;
    }


}
