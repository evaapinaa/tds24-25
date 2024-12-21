package umu.tds.apps.persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.apps.modelo.ContactoIndividual;
import beans.Entidad;
import beans.Propiedad;

public class AdaptadorContactoIndividualTDS implements IAdaptadorContactoIndividualDAO {
    private static ServicioPersistencia servPersistencia;
    private static AdaptadorContactoIndividualTDS unicaInstancia = null;

    public static AdaptadorContactoIndividualTDS getUnicaInstancia() {
        if (unicaInstancia == null)
            return new AdaptadorContactoIndividualTDS();
        else
            return unicaInstancia;
    }

    private AdaptadorContactoIndividualTDS() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }

    @Override
    public void registrarContactoIndividual(ContactoIndividual contacto) {
        Entidad eContacto = null;

        try {
            eContacto = servPersistencia.recuperarEntidad(contacto.getCodigo());
        } catch (NullPointerException e) {}
        if (eContacto != null) return;

        eContacto = new Entidad();
        eContacto.setNombre("contactoIndividual");
        eContacto.setPropiedades(new ArrayList<>(Arrays.asList(
            new Propiedad("nombre", contacto.getNombre()),
            new Propiedad("telefono", contacto.getTelefono()),
            new Propiedad("usuario", String.valueOf(contacto.getUsuario().getCodigo()))
        )));

        eContacto = servPersistencia.registrarEntidad(eContacto);
        contacto.setCodigo(eContacto.getId());
    }

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

    @Override
    public void borrarContactoIndividual(ContactoIndividual contacto) {
        Entidad eContacto = servPersistencia.recuperarEntidad(contacto.getCodigo());
        servPersistencia.borrarEntidad(eContacto);
    }

    @Override
    public ContactoIndividual recuperarContactoIndividual(int codigo) {
        if (PoolDAO.getUnicaInstancia().contiene(codigo))
            return (ContactoIndividual) PoolDAO.getUnicaInstancia().getObjeto(codigo);

        Entidad eContacto = servPersistencia.recuperarEntidad(codigo);

        String nombre = servPersistencia.recuperarPropiedadEntidad(eContacto, "nombre");
        String telefono = servPersistencia.recuperarPropiedadEntidad(eContacto, "telefono");
        int usuarioId = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eContacto, "usuario"));

        ContactoIndividual contacto = new ContactoIndividual(nombre, telefono, AdaptadorUsuarioTDS.getUnicaInstancia().recuperarUsuario(usuarioId));
        contacto.setCodigo(codigo);

        PoolDAO.getUnicaInstancia().addObjeto(codigo, contacto);
        return contacto;
    }

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
