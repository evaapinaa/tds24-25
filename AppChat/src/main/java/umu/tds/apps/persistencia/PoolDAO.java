package umu.tds.apps.persistencia;

/*Esta clase implementa un pool para los adaptadores que lo necesiten*/

import java.util.Hashtable;

public class PoolDAO {
	private static PoolDAO unicaInstancia;
	private Hashtable<Integer, Object> pool;

	
	/**
	 * Constructor privado para implementar el patrón Singleton.
	 * Inicializa el pool de objetos.
	 */
	private PoolDAO() {
		pool = new Hashtable<Integer, Object>();
	}

	
	/**
	 * Método para obtener la única instancia de la clase (patrón Singleton).
	 * @return La única instancia de PoolDAO.
	 */
	public static PoolDAO getUnicaInstancia() {
		if (unicaInstancia == null) unicaInstancia = new PoolDAO();
		return unicaInstancia;
		
	}
	
	
	/**
	 * Obtiene un objeto del pool por su identificador.
	 * @param id Identificador del objeto a recuperar.
	 * @return El objeto recuperado o null si no existe.
	 */
	public Object getObjeto(int id) {
		return pool.get(id);
	} // devuelve null si no encuentra el objeto

	
	/**
	 * Añade un objeto al pool con el identificador especificado.
	 * @param id Identificador con el que se almacenará el objeto.
	 * @param objeto Objeto a almacenar.
	 */
	public void addObjeto(int id, Object objeto) {
		pool.put(id, objeto);
	}

	
	/**
	 * Verifica si existe un objeto en el pool con el identificador especificado.
	 * @param id Identificador a verificar.
	 * @return true si existe un objeto con ese identificador, false en caso contrario.
	 */
	public boolean contiene(int id) {
		return pool.containsKey(id);
	}
}
