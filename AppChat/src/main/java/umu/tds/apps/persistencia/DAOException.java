package umu.tds.apps.persistencia;


/**
 * Clase que representa una excepción específica para las operaciones DAO.
 * Extiende la clase Exception estándar de Java.
 */
@SuppressWarnings("serial")
public class DAOException extends Exception {

	
    /**
     * Constructor que crea una nueva excepción DAO con el mensaje especificado.
     * 
     * @param mensaje El mensaje de la excepción que describe la causa del error.
     */
	public DAOException(String mensaje) {
		super(mensaje);
	}
}
