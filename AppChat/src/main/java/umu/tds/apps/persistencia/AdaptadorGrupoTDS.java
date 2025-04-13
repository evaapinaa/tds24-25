
package umu.tds.apps.persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;

import beans.Entidad;
import umu.tds.apps.modelo.Mensaje;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.apps.modelo.ContactoIndividual;
import umu.tds.apps.modelo.Grupo;
import umu.tds.apps.modelo.Usuario;

public class AdaptadorGrupoTDS implements IAdaptadorGrupoDAO {

    private static ServicioPersistencia servPersistencia;
    private static AdaptadorGrupoTDS unicaInstancia = null;

    private AdaptadorGrupoTDS() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }

    public static AdaptadorGrupoTDS getUnicaInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new AdaptadorGrupoTDS();
        }
        return unicaInstancia;
    }
    
 // Método auxiliar para obtener mensajes desde códigos
    private List<Mensaje> obtenerMensajesDesdeCodigos(String codigos) {
        List<Mensaje> mensajes = new LinkedList<>();
        if (codigos == null || codigos.trim().isEmpty()) return mensajes;

        StringTokenizer st = new StringTokenizer(codigos, " ");
        while (st.hasMoreTokens()) {
            try {
                int codMensaje = Integer.parseInt(st.nextToken());
                Mensaje m = AdaptadorMensajeTDS.getUnicaInstancia().recuperarMensaje(codMensaje);
                if (m != null) {
                    mensajes.add(m);
                }
            } catch (NumberFormatException e) {
                System.err.println("Error al convertir código de mensaje: " + e.getMessage());
            }
        }
        return mensajes;
    }
    
    private String obtenerCodigosMensajes(List<Mensaje> list) {
        StringBuilder sb = new StringBuilder();
        for (Mensaje m : list) {
            sb.append(m.getCodigo()).append(" ");
        }
        return sb.toString().trim();
    }


    @Override
    public void registrarGrupo(Grupo grupo) {
        if (grupo.getNombreGrupo() == null || grupo.getCreador() == null) {
            throw new IllegalArgumentException("El nombre del grupo y el creador no pueden ser nulos.");
        }

        Entidad eGrupo = new Entidad();
        eGrupo.setNombre("grupo");
        
        // Crear una lista vacía
        List<Propiedad> propiedades = new ArrayList<>();
        
        // Añadir las propiedades una por una
        propiedades.add(new Propiedad("nombreGrupo", grupo.getNombreGrupo()));
        propiedades.add(new Propiedad("contactos", obtenerCodigosContactos(grupo.getListaContactos())));
        propiedades.add(new Propiedad("creador", String.valueOf(grupo.getCreador().getCodigo())));
        propiedades.add(new Propiedad("mensajesEnviados", obtenerCodigosMensajes(grupo.getListaMensajesEnviados())));
        
        // Añadir la propiedad imagenGrupo
        String rutaImagen = (grupo.getImagenGrupo() != null) ? grupo.getImagenGrupo().getDescription() : "";
        propiedades.add(new Propiedad("imagenGrupo", rutaImagen));
        
        // Establecer las propiedades en la entidad
        eGrupo.setPropiedades(propiedades);

        eGrupo = servPersistencia.registrarEntidad(eGrupo);
        grupo.setCodigo(eGrupo.getId());
        System.out.println("Grupo registrado correctamente: " + grupo.getNombreGrupo());
    }







    @Override
    public Grupo recuperarGrupo(int codigo) {
        if (PoolDAO.getUnicaInstancia().contiene(codigo)) {
            return (Grupo) PoolDAO.getUnicaInstancia().getObjeto(codigo);
        }

        Entidad eGrupo = servPersistencia.recuperarEntidad(codigo);
        if (eGrupo == null) {
            System.err.println("No se encontró el grupo con código: " + codigo);
            return null;
        }

        String nombreGrupo = servPersistencia.recuperarPropiedadEntidad(eGrupo, "nombreGrupo");
        String contactosCodigos = servPersistencia.recuperarPropiedadEntidad(eGrupo, "contactos");
        int creadorCodigo = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eGrupo, "creador"));
        
        // Recuperar la ruta de la imagen del grupo
        String rutaImagen = servPersistencia.recuperarPropiedadEntidad(eGrupo, "imagenGrupo");
        
        // Recuperar los códigos de los mensajes enviados
        String mensajesEnviadosCodigos = servPersistencia.recuperarPropiedadEntidad(eGrupo, "mensajesEnviados");

        Usuario creador = AdaptadorUsuarioTDS.getUnicaInstancia().recuperarUsuario(creadorCodigo);
        if (creador == null) {
            throw new IllegalStateException("El creador del grupo no existe: " + creadorCodigo);
        }

        List<ContactoIndividual> contactos = obtenerContactosDesdeCodigos(contactosCodigos);

        // Crear el ImageIcon para la imagen del grupo
        ImageIcon imagenGrupo = null;
        if (rutaImagen != null && !rutaImagen.trim().isEmpty()) {
            try {
                imagenGrupo = new ImageIcon(rutaImagen);
                // Si la imagen no se carga correctamente (getIconWidth devuelve -1)
                if (imagenGrupo.getIconWidth() <= 0) {
                    System.err.println("No se pudo cargar la imagen del grupo desde: " + rutaImagen);
                    imagenGrupo = null;
                }
            } catch (Exception e) {
                System.err.println("Error al cargar la imagen del grupo: " + e.getMessage());
                imagenGrupo = null;
            }
        }

        Grupo grupo = new Grupo(nombreGrupo, contactos, creador, imagenGrupo);
        grupo.setCodigo(codigo);

        // Agregar el grupo al PoolDAO antes de recuperar los mensajes para evitar recursividad infinita
        PoolDAO.getUnicaInstancia().addObjeto(codigo, grupo);
        
        // Recuperar y establecer los mensajes enviados
        if (mensajesEnviadosCodigos != null && !mensajesEnviadosCodigos.isEmpty()) {
            List<Mensaje> mensajesEnviados = obtenerMensajesDesdeCodigos(mensajesEnviadosCodigos);
            for (Mensaje mensaje : mensajesEnviados) {
                grupo.addMensajeEnviado(mensaje);
            }
        }
        
        return grupo;
    }
    
    @Override
    public void modificarGrupo(Grupo grupo) {
        // 1) Recuperar la entidad del grupo
        Entidad eGrupo = servPersistencia.recuperarEntidad(grupo.getCodigo());
        if (eGrupo == null) {
            System.err.println("No se encontró el grupo con código: " + grupo.getCodigo());
            return;
        }

        // 2) Verificar si ya existe la propiedad mensajesEnviados
        boolean tienePropiedadMensajes = false;
        for (Propiedad prop : eGrupo.getPropiedades()) {
            if (prop.getNombre().equals("mensajesEnviados")) {
                tienePropiedadMensajes = true;
                break;
            }
        }

        // 3) Si no existe la propiedad, crear una lista nueva con todas las propiedades
        if (!tienePropiedadMensajes) {
            // Crear una nueva lista con todas las propiedades existentes
            List<Propiedad> propiedadesNuevas = new ArrayList<>(eGrupo.getPropiedades());
            
            // Añadir la nueva propiedad
            propiedadesNuevas.add(new Propiedad("mensajesEnviados", 
                                             obtenerCodigosMensajes(grupo.getListaMensajesEnviados())));
            
            // Establecer las propiedades actualizadas
            eGrupo.setPropiedades(propiedadesNuevas);
            
            // Actualizar la entidad en el servicio de persistencia
            servPersistencia.modificarEntidad(eGrupo);
        } else {
            // 4) Si ya existe, actualizar las propiedades individualmente
            for (Propiedad prop : eGrupo.getPropiedades()) {
                switch (prop.getNombre()) {
                    case "nombreGrupo":
                        prop.setValor(grupo.getNombreGrupo());
                        break;
                    case "contactos":
                        prop.setValor(obtenerCodigosContactos(grupo.getListaContactos()));
                        break;
                    case "creador":
                        prop.setValor(String.valueOf(grupo.getCreador().getCodigo()));
                        break;
                    case "mensajesEnviados":
                        prop.setValor(obtenerCodigosMensajes(grupo.getListaMensajesEnviados()));
                        break;
                    case "imagenGrupo":
                        String desc = (grupo.getImagenGrupo() != null)
                            ? grupo.getImagenGrupo().getDescription()
                            : "";
                        prop.setValor(desc);
                        break;
                    default:
                        // Si tienes más propiedades, manéjalas aquí.
                        break;
                }
                servPersistencia.modificarPropiedad(prop);
            }
        }
        
        System.out.println("Grupo modificado en BD: " + grupo.getNombreGrupo());
    }
    





    @Override
    public List<Grupo> recuperarTodosGrupos() {
        List<Entidad> eGrupos = servPersistencia.recuperarEntidades("grupo");
        List<Grupo> grupos = new LinkedList<>();
        for (Entidad eGrupo : eGrupos) {
            Grupo grupo = recuperarGrupo(eGrupo.getId());
            if (grupo != null) {
                grupos.add(grupo);
            }
        }
        return grupos;
    }
    
    // Métodos auxiliares

    private String obtenerCodigosContactos(List<ContactoIndividual> listaContactos) {
        StringBuilder sb = new StringBuilder();
        for (ContactoIndividual c : listaContactos) {
            sb.append(c.getCodigo()).append(" ");
        }
        return sb.toString().trim();
    }

    private List<ContactoIndividual> obtenerContactosDesdeCodigos(String codigos) {
        List<ContactoIndividual> contactos = new LinkedList<>();
        if (codigos == null || codigos.trim().isEmpty()) return contactos;

        StringTokenizer st = new StringTokenizer(codigos, " ");
        while (st.hasMoreTokens()) {
            int codContacto = Integer.parseInt(st.nextToken());
            ContactoIndividual ci = AdaptadorContactoIndividualTDS.getUnicaInstancia().recuperarContactoIndividual(codContacto);
            if (ci != null) {
                // Verifica que el usuario esté correctamente configurado
                if (ci.getUsuario() == null) {
                    System.err.println("El contacto con código " + codContacto + " no tiene un usuario asociado.");
                } else {
                    contactos.add(ci);
                }
            }
        }
        return contactos;
    }


}
