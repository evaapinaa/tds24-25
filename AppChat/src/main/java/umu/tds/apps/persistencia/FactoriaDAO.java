package umu.tds.apps.persistencia;


/**
 * Clase abstracta que define una factoría para obtener las implementaciones
 * de los diferentes adaptadores DAO de la aplicación.
 * Implementa el patrón Factoría Abstracta.
 */

public abstract class FactoriaDAO {
	private static FactoriaDAO unicaInstancia;
	
	public static final String DAO_TDS = "umu.tds.apps.persistencia.TDSFactoriaDAO";
		
	/**
	 * Crea un tipo de factoria DAO.
	 * Solo existe el tipo TDSFactoriaDAO.
	 * @param tipo Tipo de factoria a crear.
	 * @return La única instancia de FactoriaDAO.
	 * @throws DAOException Si ocurre un error al crear la factoria.
	 */
	@SuppressWarnings("deprecation")
	public static FactoriaDAO getInstancia(String tipo) throws DAOException{
		if (unicaInstancia == null)
			try { unicaInstancia=(FactoriaDAO) Class.forName(tipo).newInstance();
			} catch (Exception e) {	
				throw new DAOException(e.getMessage());
			} 
		return unicaInstancia;
	}


	
	/**
	 * Obtiene la única instancia de la factoria DAO.
	 * Si no existe, la crea usando el tipo por defecto (DAO_TDS).
	 * @return La única instancia de FactoriaDAO.
	 * @throws DAOException Si ocurre un error al crear la factoria.
	 */
	public static FactoriaDAO getInstancia() throws DAOException{
			if (unicaInstancia == null) return getInstancia (FactoriaDAO.DAO_TDS);
					else return unicaInstancia;
		}

	/**
	 * Constructor protegido para ser extendido por subclases.
	 */
	protected FactoriaDAO (){}
		
		
	// Metodos factoria que devuelven adaptadores que implementen estos interfaces
	
	/**
	 * Obtiene un adaptador para la persistencia de usuarios.
	 * @return Adaptador para usuarios.
	 */
	public abstract IAdaptadorUsuarioDAO getUsuarioDAO();
	
	/**
	 * Obtiene un adaptador para la persistencia de mensajes.
	 * @return Adaptador para mensajes.
	 */
	public abstract IAdaptadorMensajeDAO getMensajeDAO();
	
	/**
	 * Obtiene un adaptador para la persistencia de contactos individuales.
	 * @return Adaptador para contactos individuales.
	 */
	public abstract IAdaptadorContactoIndividualDAO getContactoIndividualDAO();
	
	/**
	 * Obtiene un adaptador para la persistencia de chats.
	 * @return Adaptador para chats.
	 */
	public abstract IAdaptadorChatDAO getChatDAO();
	
	/**
	 * Obtiene un adaptador para la persistencia de grupos.
	 * @return Adaptador para grupos.
	 */
	public abstract IAdaptadorGrupoDAO getGrupoDAO();

}
