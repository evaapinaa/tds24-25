
package umu.tds.apps.persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.apps.modelo.ContactoIndividual;
import umu.tds.apps.modelo.Usuario;
import beans.Entidad;
import beans.Propiedad;

public class AdaptadorContactoIndividualTDS implements IAdaptadorContactoIndividualDAO {
    private static ServicioPersistencia servPersistencia;
    private static AdaptadorContactoIndividualTDS unicaInstancia = null;
    
    
    /**
     * Método para obtener la única instancia de la clase (patrón Singleton).
     * @return La única instancia de AdaptadorContactoIndividualTDS.
     */
    public static AdaptadorContactoIndividualTDS getUnicaInstancia() {
        if (unicaInstancia == null)
            return new AdaptadorContactoIndividualTDS();
        else
            return unicaInstancia;
    }

     /**
     * Constructor privado para implementar el patrón Singleton.
     * Inicializa el servicio de persistencia de la aplicación.
     */
    private AdaptadorContactoIndividualTDS() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }

    
     /**
     * Registra un contacto individual en la base de datos.
     * Valida que el usuario asociado al contacto sea válido antes de realizar el registro.
     * @param contacto El objeto ContactoIndividual que se desea registrar.
     * @throws IllegalArgumentException Si el usuario asociado al contacto no es válido.
     */
    @Override
    public void registrarContactoIndividual(ContactoIndividual contacto) {
        if (contacto.getUsuario() == null || contacto.getUsuario().getCodigo() <= 0) {
            throw new IllegalArgumentException("El usuario asociado al contacto no es válido.");
        }

        Entidad eContacto = new Entidad();
        eContacto.setNombre("contactoIndividual");
        eContacto.setPropiedades(new ArrayList<>(Arrays.asList(
            new Propiedad("nombre", contacto.getNombre()),
            new Propiedad("telefono", contacto.getTelefono()),
            new Propiedad("usuario", String.valueOf(contacto.getUsuario().getCodigo()))
        )));

        eContacto = servPersistencia.registrarEntidad(eContacto);
        contacto.setCodigo(eContacto.getId());
    }


    
    /**
     * Modifica un contacto individual existente en la base de datos.
     * Actualiza todas las propiedades del contacto en la base de datos.
     * @param contacto El objeto ContactoIndividual con los datos actualizados.
     */
    @Override
    public void modificarContactoIndividual(ContactoIndividual contacto) {
        Entidad eContacto = servPersistencia.recuperarEntidad(contacto.getCodigo());

        for (Propiedad prop : eContacto.getPropiedades()) {
            switch (prop.getNombre()) {
                case "nombre":
                    prop.setValor(contacto.getNombre());
                    break;
                case "telefono":
                    prop.setValor(contacto.getTelefono());
                    break;
                case "usuario":
                    prop.setValor(String.valueOf(contacto.getUsuario().getCodigo()));
                    break;
            }
            servPersistencia.modificarPropiedad(prop);
        }
    }

    
     /**
     * Elimina un contacto individual de la base de datos.
     * @param contacto El objeto ContactoIndividual que se desea eliminar.
     */
    @Override
    public void borrarContactoIndividual(ContactoIndividual contacto) {
        Entidad eContacto = servPersistencia.recuperarEntidad(contacto.getCodigo());
        servPersistencia.borrarEntidad(eContacto);
    }

    
     /**
     * Recupera un contacto individual de la base de datos por su código.
     * Si el contacto ya está en el pool de objetos, lo devuelve directamente.
     * @param codigo El código identificador del contacto a recuperar.
     * @return El objeto ContactoIndividual recuperado, o null si no existe o es un grupo.
     */
    @Override
    public ContactoIndividual recuperarContactoIndividual(int codigo) {
        if (PoolDAO.getUnicaInstancia().contiene(codigo))
            return (ContactoIndividual) PoolDAO.getUnicaInstancia().getObjeto(codigo);

        Entidad eContacto = servPersistencia.recuperarEntidad(codigo);

        // Verificar si la entidad es de tipo "grupo"
        if ("grupo".equals(eContacto.getNombre())) {
            System.err.println("La entidad con código " + codigo + " es un grupo, no un contacto individual.");
            return null; 
        }

        String nombre = servPersistencia.recuperarPropiedadEntidad(eContacto, "nombre");
        String telefono = servPersistencia.recuperarPropiedadEntidad(eContacto, "telefono");
        String usuarioIdStr = servPersistencia.recuperarPropiedadEntidad(eContacto, "usuario");

        if (usuarioIdStr == null || usuarioIdStr.trim().isEmpty()) {
            System.err.println("La propiedad 'usuario' es nula o inválida para el contacto con código: " + codigo);
            return null;
        }

        int usuarioId;
        try {
            usuarioId = Integer.parseInt(usuarioIdStr);
        } catch (NumberFormatException e) {
            System.err.println("Error al convertir la propiedad 'usuario' a entero: " + usuarioIdStr);
            return null;
        }

        Usuario usuario = AdaptadorUsuarioTDS.getUnicaInstancia().recuperarUsuario(usuarioId);
        if (usuario == null) {
            System.err.println("El usuario asociado con el ID " + usuarioId + " no existe.");
            return null;
        }

        ContactoIndividual contacto = new ContactoIndividual(nombre, telefono, usuario);
        contacto.setCodigo(codigo);

        PoolDAO.getUnicaInstancia().addObjeto(codigo, contacto);
        return contacto;
    }



    
     /**
     * Recupera todos los contactos individuales almacenados en la base de datos.
     * @return Una lista con todos los objetos ContactoIndividual recuperados.
     */
    @Override
    public List<ContactoIndividual> recuperarTodosContactosIndividuales() {
        List<Entidad> eContactos = servPersistencia.recuperarEntidades("contactoIndividual");
        List<ContactoIndividual> contactos = new LinkedList<>();

        for (Entidad eContacto : eContactos) {
            contactos.add(recuperarContactoIndividual(eContacto.getId()));
        }
        return contactos;
    }
}
