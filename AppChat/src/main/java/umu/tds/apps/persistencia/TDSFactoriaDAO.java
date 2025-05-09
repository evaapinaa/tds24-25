package umu.tds.apps.persistencia;

public class TDSFactoriaDAO extends FactoriaDAO {
	
	/**
	 * Constructor para la factoria TDS.
	 */
	public TDSFactoriaDAO () {
	}
	
	
	/**
	 * Obtiene el adaptador para la persistencia de usuarios.
	 * @return La única instancia de AdaptadorUsuarioTDS.
	 */
	@Override
	public IAdaptadorUsuarioDAO getUsuarioDAO() {
		return AdaptadorUsuarioTDS.getUnicaInstancia();
	}

	
	/**
	 * Obtiene el adaptador para la persistencia de mensajes.
	 * @return La única instancia de AdaptadorMensajeTDS.
	 */
	@Override
	public IAdaptadorMensajeDAO getMensajeDAO() {
		return AdaptadorMensajeTDS.getUnicaInstancia();
	}
	
	
	/**
	 * Obtiene el adaptador para la persistencia de contactos individuales.
	 * @return La única instancia de AdaptadorContactoIndividualTDS.
	 */
	@Override
	public IAdaptadorContactoIndividualDAO getContactoIndividualDAO() {
		return AdaptadorContactoIndividualTDS.getUnicaInstancia();
	}
	
	
	/**
	 * Obtiene el adaptador para la persistencia de chats.
	 * @return La única instancia de AdaptadorChatTDS.
	 */
	@Override
	public IAdaptadorChatDAO getChatDAO() {
		return AdaptadorChatTDS.getUnicaInstancia();
	}
	
	
	/**
	 * Obtiene el adaptador para la persistencia de grupos.
	 * @return La única instancia de AdaptadorGrupoTDS.
	 */
	@Override
	public IAdaptadorGrupoDAO getGrupoDAO() {
		return AdaptadorGrupoTDS.getUnicaInstancia();
	}
	

}
