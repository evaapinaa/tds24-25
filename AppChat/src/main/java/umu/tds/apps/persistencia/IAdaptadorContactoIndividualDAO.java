
package umu.tds.apps.persistencia;

import java.util.List;

import umu.tds.apps.modelo.ContactoIndividual;

/**
 * Interface que define los métodos para la persistencia de los objetos ContactoIndividual.
 */
public interface IAdaptadorContactoIndividualDAO {
	
	/**
     * Registra un contacto individual en la base de datos.
     * 
     * @param contacto El objeto ContactoIndividual que se desea registrar.
     */
	public void registrarContactoIndividual(ContactoIndividual contacto);
	
    /**
     * Modifica un contacto individual existente en la base de datos.
     * 
     * @param contacto El objeto ContactoIndividual con los datos actualizados.
     */
	public void modificarContactoIndividual(ContactoIndividual contacto);
	
	/**
     * Elimina un contacto individual de la base de datos.
     * 
     * @param contacto El objeto ContactoIndividual que se desea eliminar.
     */
	public void borrarContactoIndividual(ContactoIndividual contacto);
	
    /**
     * Recupera un contacto individual de la base de datos por su código.
     * 
     * @param codigo El código identificador del contacto a recuperar.
     * @return El objeto ContactoIndividual recuperado, o null si no existe.
     */
	public ContactoIndividual recuperarContactoIndividual(int codigo);
	
    /**
     * Recupera todos los contactos individuales almacenados en la base de datos.
     * 
     * @return Una lista con todos los objetos ContactoIndividual recuperados.
     */
	public List<ContactoIndividual> recuperarTodosContactosIndividuales();

}
