package umu.tds.apps.persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;

import beans.Entidad;
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


    @Override
    public void registrarGrupo(Grupo grupo) {
        if (grupo.getNombreGrupo() == null || grupo.getCreador() == null) {
            throw new IllegalArgumentException("El nombre del grupo y el creador no pueden ser nulos.");
        }

        Entidad eGrupo = new Entidad();
        eGrupo.setNombre("grupo");
        eGrupo.setPropiedades(new ArrayList<>(Arrays.asList(
            new Propiedad("nombreGrupo", grupo.getNombreGrupo()),
            new Propiedad("contactos", obtenerCodigosContactos(grupo.getListaContactos())),
            new Propiedad("creador", String.valueOf(grupo.getCreador().getCodigo()))
        )));

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

        Usuario creador = AdaptadorUsuarioTDS.getUnicaInstancia().recuperarUsuario(creadorCodigo);
        if (creador == null) {
            throw new IllegalStateException("El creador del grupo no existe: " + creadorCodigo);
        }

        List<ContactoIndividual> contactos = obtenerContactosDesdeCodigos(contactosCodigos);

        Grupo grupo = new Grupo(nombreGrupo, contactos, creador, null);
        grupo.setCodigo(codigo);

        PoolDAO.getUnicaInstancia().addObjeto(codigo, grupo);
        return grupo;
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
