package umu.tds.apps.persistencia;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.net.URL;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.apps.modelo.Contacto;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.Usuario;
import beans.Entidad;
import beans.Propiedad;


public class AdaptadorUsuarioTDS implements IAdaptadorUsuarioDAO {
    private static ServicioPersistencia servPersistencia;
    private static AdaptadorUsuarioTDS unicaInstancia = null;

    public static AdaptadorUsuarioTDS getUnicaInstancia() {
        if (unicaInstancia == null)
            return new AdaptadorUsuarioTDS();
        else
            return unicaInstancia;
    }

    private AdaptadorUsuarioTDS() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }

    @Override
    public void registrarUsuario(Usuario usuario) {
        Entidad eUsuario = null;

        try {
            eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
        } catch (NullPointerException e) {}
        if (eUsuario != null) return;

        eUsuario = new Entidad();
        eUsuario.setNombre("usuario");
        eUsuario.setPropiedades(new ArrayList<>(Arrays.asList(
            new Propiedad("nombre", usuario.getUsuario()),
            new Propiedad("telefono", usuario.getTelefono()),
            new Propiedad("email", usuario.getEmail()),
            new Propiedad("contraseña", usuario.getContraseña()),
            new Propiedad("saludo", usuario.getSaludo().orElse("")),
            new Propiedad("contactos", obtenerCodigosContactos(usuario.getListaContactos())),
            new Propiedad("imagenPerfil", usuario.getImagenPerfil().getDescription()),
            new Propiedad("mensajesEnviados", obtenerCodigosMensajes(usuario.getListaMensajesEnviados())),
            new Propiedad("mensajesRecibidos", obtenerCodigosMensajes(usuario.getListaMensajesRecibidos()))
        )));
        
        eUsuario = servPersistencia.registrarEntidad(eUsuario);
        usuario.setCodigo(eUsuario.getId());
    }


    @Override
    public void modificarUsuario(Usuario usuario) {
        Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());

        for (Propiedad prop : eUsuario.getPropiedades()) {
            switch (prop.getNombre()) {
                case "nombre":
                    prop.setValor(usuario.getUsuario());
                    break;
                case "telefono":
                    prop.setValor(usuario.getTelefono());
                    break;
                case "email":
                    prop.setValor(usuario.getEmail());
                    break;
                case "saludo":
                    prop.setValor(usuario.getSaludo().orElse(""));
                    break;
                case "imagenPerfil":
					prop.setValor(usuario.getImagenPerfil().getDescription());
					break;
				case "contraseña":
					prop.setValor(usuario.getContraseña());
					break;
                case "contactos":
                    prop.setValor(obtenerCodigosContactos(usuario.getListaContactos()));
                    break;
                case "mensajesEnviados":
                    prop.setValor(obtenerCodigosMensajes(usuario.getListaMensajesEnviados()));
                    break;
                case "mensajesRecibidos":
                    prop.setValor(obtenerCodigosMensajes(usuario.getListaMensajesRecibidos()));
                    break;
            }
            servPersistencia.modificarPropiedad(prop);
        }
    }

    @Override
    public Usuario recuperarUsuario(int codigo) {
        if (PoolDAO.getUnicaInstancia().contiene(codigo))
            return (Usuario) PoolDAO.getUnicaInstancia().getObjeto(codigo);

        Entidad eUsuario = servPersistencia.recuperarEntidad(codigo);

        String nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, "nombre");
        String telefono = servPersistencia.recuperarPropiedadEntidad(eUsuario, "telefono");
        String contraseña = servPersistencia.recuperarPropiedadEntidad(eUsuario, "contraseña");
        String email = servPersistencia.recuperarPropiedadEntidad(eUsuario, "email");
        String rutaImagenPerfil = servPersistencia.recuperarPropiedadEntidad(eUsuario, "imagenPerfil");
        ImageIcon imagenPerfil = cargarImagenDesdeRuta(rutaImagenPerfil);
        String saludo = servPersistencia.recuperarPropiedadEntidad(eUsuario, "saludo");
        
        Usuario usuario = new Usuario(nombre, contraseña, telefono, email, Optional.ofNullable(saludo), imagenPerfil, null);
        usuario.setCodigo(codigo);

        PoolDAO.getUnicaInstancia().addObjeto(codigo, usuario);

        usuario.setListaContactos(obtenerContactosDesdeCodigos(
            servPersistencia.recuperarPropiedadEntidad(eUsuario, "contactos")
        ));
        usuario.setListaMensajesEnviados(obtenerMensajesDesdeCodigos(
            servPersistencia.recuperarPropiedadEntidad(eUsuario, "mensajesEnviados")
        ));
        usuario.setListaMensajesRecibidos(obtenerMensajesDesdeCodigos(
            servPersistencia.recuperarPropiedadEntidad(eUsuario, "mensajesRecibidos")
        ));

        return usuario;
    }

    @Override
    public List<Usuario> recuperarTodosUsuarios() {
        List<Entidad> eUsuarios = servPersistencia.recuperarEntidades("usuario");
        List<Usuario> usuarios = new LinkedList<>();

        for (Entidad eUsuario : eUsuarios) {
            usuarios.add(recuperarUsuario(eUsuario.getId()));
        }
        return usuarios;
    }

    // Métodos auxiliares
    private String obtenerCodigosContactos(List<Contacto> listaContactos) {
        StringBuilder codigos = new StringBuilder();
        for (Contacto contacto : listaContactos) {
            codigos.append(contacto.getCodigo()).append(" ");
        }
        return codigos.toString().trim();
    }

    private List<Contacto> obtenerContactosDesdeCodigos(String codigos) {
        List<Contacto> listaContactos = new LinkedList<>();
        StringTokenizer tokenizer = new StringTokenizer(codigos, " ");
        AdaptadorContactoIndividualTDS adaptadorContacto = AdaptadorContactoIndividualTDS.getUnicaInstancia();

        while (tokenizer.hasMoreTokens()) {
            listaContactos.add(adaptadorContacto.recuperarContactoIndividual(Integer.valueOf((String) tokenizer.nextElement())));
        }
        return listaContactos;
    }

    private String obtenerCodigosMensajes(List<Mensaje> listaMensajes) {
        StringBuilder codigos = new StringBuilder();
        for (Mensaje mensaje : listaMensajes) {
            codigos.append(mensaje.getCodigo()).append(" ");
        }
        return codigos.toString().trim();
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
    
    @SuppressWarnings("deprecation")
    private ImageIcon cargarImagenDesdeRuta(String rutaImagen) {
        // Ya no hacemos la carga real; solo creamos un ImageIcon vacío con la description puesta.
        ImageIcon icono = new ImageIcon();
        icono.setDescription(rutaImagen);  // guardamos la ruta para luego usarla en la vista
        return icono;
    }


    
}