package umu.tds.apps.persistencia;

import java.util.List;

import umu.tds.apps.modelo.ContactoIndividual;

public interface IAdaptadorContactoIndividualDAO {
	
	public void registrarContactoIndividual(ContactoIndividual contacto);
	public void modificarContactoIndividual(ContactoIndividual contacto);
	public void borrarContactoIndividual(ContactoIndividual contacto);
	public ContactoIndividual recuperarContactoIndividual(int codigo);
	public List<ContactoIndividual> recuperarTodosContactosIndividuales();

}
