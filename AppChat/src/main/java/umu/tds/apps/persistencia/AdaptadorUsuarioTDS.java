package umu.tds.apps.persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;

import beans.Entidad;
import beans.Propiedad;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

import umu.tds.apps.modelo.Chat;
import umu.tds.apps.modelo.Contacto;
import umu.tds.apps.modelo.ContactoIndividual;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.Usuario;

public class AdaptadorUsuarioTDS implements IAdaptadorUsuarioDAO {

    private static ServicioPersistencia servPersistencia;
    private static AdaptadorUsuarioTDS unicaInstancia = null;

    private AdaptadorUsuarioTDS() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }

    public static AdaptadorUsuarioTDS getUnicaInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new AdaptadorUsuarioTDS();
        }
        return unicaInstancia;
    }

    @Override
    public void registrarUsuario(Usuario usuario) {
        Entidad eUsuario = null;
        try {
            eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
        } catch (NullPointerException e) {}
        if (eUsuario != null) return; // ya existe

        eUsuario = new Entidad();
        eUsuario.setNombre("usuario");
        eUsuario.setPropiedades(new ArrayList<>(Arrays.asList(
            new Propiedad("nombre", usuario.getUsuario()),
            new Propiedad("telefono", usuario.getTelefono()),
            new Propiedad("email", usuario.getEmail()),
            new Propiedad("contraseña", usuario.getContraseña()),
            new Propiedad("saludo", usuario.getSaludo().orElse("")),

            new Propiedad("contactos", obtenerCodigosContactos(usuario.getListaContactos())),
            new Propiedad("imagenPerfil", (usuario.getImagenPerfil() != null)
                                            ? usuario.getImagenPerfil().getDescription()
                                            : ""),
            new Propiedad("mensajesEnviados", obtenerCodigosMensajes(usuario.getListaMensajesEnviados())),
            new Propiedad("mensajesRecibidos", obtenerCodigosMensajes(usuario.getListaMensajesRecibidos())),
            new Propiedad("chats", obtenerCodigosChats(usuario.getListaChats()))
        )));

        eUsuario = servPersistencia.registrarEntidad(eUsuario);
        usuario.setCodigo(eUsuario.getId());
        System.out.println("Usuario registrado con ID: " + eUsuario.getId());
    }

    @Override
    public void modificarUsuario(Usuario usuario) {
        Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
        if (eUsuario == null) {
            System.err.println("No se encontró el usuario con ID: " + usuario.getCodigo());
            return;
        }
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
                case "contraseña":
                    prop.setValor(usuario.getContraseña());
                    break;
                case "saludo":
                    prop.setValor(usuario.getSaludo().orElse(""));
                    break;
                case "imagenPerfil":
                    prop.setValor((usuario.getImagenPerfil() != null)
                                  ? usuario.getImagenPerfil().getDescription()
                                  : "");
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
                case "chats":
                    prop.setValor(obtenerCodigosChats(usuario.getListaChats()));
                    break;
            }
            servPersistencia.modificarPropiedad(prop);
        }
        System.out.println("Usuario " + usuario.getTelefono() + " modificado en BD.");
    }

    @Override
    public Usuario recuperarUsuario(int codigo) {
        if (PoolDAO.getUnicaInstancia().contiene(codigo)) {
            return (Usuario) PoolDAO.getUnicaInstancia().getObjeto(codigo);
        }

        Entidad eUsuario = servPersistencia.recuperarEntidad(codigo);
        if (eUsuario == null) {
            System.err.println("No se encontró el usuario con ID: " + codigo);
            return null;
        }

        // Propiedades
        String nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, "nombre");
        String telefono = servPersistencia.recuperarPropiedadEntidad(eUsuario, "telefono");
        String contraseña = servPersistencia.recuperarPropiedadEntidad(eUsuario, "contraseña");
        String email = servPersistencia.recuperarPropiedadEntidad(eUsuario, "email");
        String saludo = servPersistencia.recuperarPropiedadEntidad(eUsuario, "saludo");
        String rutaImagenPerfil = servPersistencia.recuperarPropiedadEntidad(eUsuario, "imagenPerfil");

        // Crea el usuario base
        ImageIcon imagenPerfil = null;
        if (rutaImagenPerfil != null && !rutaImagenPerfil.isEmpty()) {
            imagenPerfil = new ImageIcon();
            imagenPerfil.setDescription(rutaImagenPerfil);
        }

        Usuario usuario = new Usuario(
            nombre,
            contraseña,
            telefono,
            email,
            Optional.ofNullable(saludo),
            imagenPerfil,
            null
        );
        usuario.setCodigo(codigo);

        // Añadir a PoolDAO antes de cargar sus referencias
        PoolDAO.getUnicaInstancia().addObjeto(codigo, usuario);

        // Cargar listas
        String contactos = servPersistencia.recuperarPropiedadEntidad(eUsuario, "contactos");
        usuario.setListaContactos(obtenerContactosDesdeCodigos(contactos));

        String mensajesEnviados = servPersistencia.recuperarPropiedadEntidad(eUsuario, "mensajesEnviados");
        usuario.setListaMensajesEnviados(obtenerMensajesDesdeCodigos(mensajesEnviados));

        String mensajesRecibidos = servPersistencia.recuperarPropiedadEntidad(eUsuario, "mensajesRecibidos");
        usuario.setListaMensajesRecibidos(obtenerMensajesDesdeCodigos(mensajesRecibidos));

        String chats = servPersistencia.recuperarPropiedadEntidad(eUsuario, "chats");
        usuario.setListaChats(obtenerChatsDesdeCodigos(chats));

        System.out.println("Usuario recuperado: " + telefono
                           + ", Chats: " + usuario.getListaChats().size());
        return usuario;
    }

    @Override
    public List<Usuario> recuperarTodosUsuarios() {
        List<Entidad> eUsuarios = servPersistencia.recuperarEntidades("usuario");
        List<Usuario> usuarios = new LinkedList<>();
        for (Entidad eUsuario : eUsuarios) {
            Usuario u = recuperarUsuario(eUsuario.getId());
            if (u != null) {
                usuarios.add(u);
            }
        }
        return usuarios;
    }

    // --------------------------------------------------------------------------------
    // Métodos auxiliares
    // --------------------------------------------------------------------------------

    private String obtenerCodigosContactos(List<Contacto> listaContactos) {
        StringBuilder sb = new StringBuilder();
        for (Contacto c : listaContactos) {
            sb.append(c.getCodigo()).append(" ");
        }
        return sb.toString().trim();
    }

    private List<Contacto> obtenerContactosDesdeCodigos(String codigos) {
        List<Contacto> contactos = new LinkedList<>();
        if (codigos == null || codigos.trim().isEmpty()) return contactos;

        StringTokenizer st = new StringTokenizer(codigos, " ");
        while (st.hasMoreTokens()) {
            int codContacto = Integer.parseInt(st.nextToken());
            ContactoIndividual ci = AdaptadorContactoIndividualTDS.getUnicaInstancia().recuperarContactoIndividual(codContacto);
            if (ci != null) {
                contactos.add(ci);
            }
        }
        return contactos;
    }

    private String obtenerCodigosMensajes(List<Mensaje> mensajes) {
        StringBuilder sb = new StringBuilder();
        for (Mensaje m : mensajes) {
            sb.append(m.getCodigo()).append(" ");
        }
        return sb.toString().trim();
    }

    private List<Mensaje> obtenerMensajesDesdeCodigos(String codigos) {
        List<Mensaje> mensajes = new LinkedList<>();
        if (codigos == null || codigos.trim().isEmpty()) return mensajes;

        StringTokenizer st = new StringTokenizer(codigos, " ");
        while (st.hasMoreTokens()) {
            int codMensaje = Integer.parseInt(st.nextToken());
            Mensaje m = AdaptadorMensajeTDS.getUnicaInstancia().recuperarMensaje(codMensaje);
            if (m != null) {
                mensajes.add(m);
            }
        }
        return mensajes;
    }

    private String obtenerCodigosChats(List<Chat> chats) {
        StringBuilder sb = new StringBuilder();
        for (Chat c : chats) {
            sb.append(c.getCodigo()).append(" ");
        }
        return sb.toString().trim();
    }

    private List<Chat> obtenerChatsDesdeCodigos(String codigos) {
        List<Chat> listaChats = new LinkedList<>();
        if (codigos == null || codigos.trim().isEmpty()) return listaChats;

        StringTokenizer st = new StringTokenizer(codigos, " ");
        while (st.hasMoreTokens()) {
            int codChat = Integer.parseInt(st.nextToken());
            Chat c = AdaptadorChatTDS.getUnicaInstancia().recuperarChat(codChat);
            if (c != null) {
                listaChats.add(c);
            }
        }
        return listaChats;
    }
}
