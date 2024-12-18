package umu.tds.apps.persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.apps.modelo.Mensaje;
import beans.Entidad;
import beans.Propiedad;


//Usa un pool para evitar problemas doble referencia con mensajes
public class AdaptadorMensajeTDS implements IAdaptadorUsuarioDAO {
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorMensajeTDS unicaInstancia = null;

	public static AdaptadorMensajeTDS getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			return new AdaptadorMensajeTDS();
		else
			return unicaInstancia;
	}

	private AdaptadorMensajeTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	/* cuando se registra un mensaje se le asigna un identificador �nico */
	public void registrarMensaje(Mensaje mensaje) {
		Entidad eMensaje = null;

		// Si la entidad esta registrada no la registra de nuevo
		try {
			eMensaje = servPersistencia.recuperarEntidad(mensaje.getCodigo());
		} catch (NullPointerException e) {}
		if (eMensaje != null) return;

		// registrar primero los atributos que son objetos
		AdaptadorMensajeTDS adaptadorMensaje = AdaptadorMensajeTDS.getUnicaInstancia();
		for (Mensaje m : mensaje.getVentas())
			adaptadorMensaje.registrarMensaje(m);

		// crear entidad mensaje
		eMensaje = new Entidad();
		eMensaje.setNombre("cliente");
		eMensaje.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(new Propiedad("texto", mensaje.getTexto()),
				new Propiedad("hora", mensaje.getHora().toString()),
				new Propiedad("emoticono", String.valueOf(mensaje.getEmoticono())),
				new Propiedad("receptor", String.valueOf(mensaje.getReceptor().getCodigo())),
				new Propiedad("togroup", String.valueOf(grupo)),
				new Propiedad("emisor", String.valueOf(mensaje.getEmisor().getCodigo())))));

		// registrar entidad mensaje
		eMensaje = servPersistencia.registrarEntidad(eMensaje);
		// asignar identificador unico
		// Se aprovecha el que genera el servicio de persistencia
		mensaje.setCodigo(eMensaje.getId());
	}

	public Mensaje recuperarMensaje(int codigo) {

		// Si la entidad est� en el pool la devuelve directamente
		if (PoolDAO.getUnicaInstancia().contiene(codigo))
			return (Mensaje) PoolDAO.getUnicaInstancia().getObjeto(codigo);

		// si no, la recupera de la base de datos
		Entidad eMensaje;
		List<Mensaje> ventas = new LinkedList<Mensaje>();
		String dni;
		String nombre;

		// recuperar entidad
		eMensaje = servPersistencia.recuperarEntidad(codigo);

		// recuperar propiedades que no son objetos
		dni = servPersistencia.recuperarPropiedadEntidad(eMensaje, "dni");
		nombre = servPersistencia.recuperarPropiedadEntidad(eMensaje, "nombre");

		Mensaje mensaje = new Mensaje(dni, nombre);
		mensaje.setCodigo(codigo);

		// IMPORTANTE:a�adir el mensaje al pool antes de llamar a otros
		// adaptadores
		PoolDAO.getUnicaInstancia().addObjeto(codigo, mensaje);

		// recuperar propiedades que son objetos llamando a adaptadores
		mensajes = obtenerMensajesDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eMensaje, "mensajes"));

		for (Mensaje m : mensajes)
			mensaje.addMensaje(m);

		return mensaje;
	}

	public List<Mensaje> recuperarTodosMensajes() {

		List<Entidad> eMensajes = servPersistencia.recuperarEntidades("mensaje");
		List<Mensaje> clientes = new LinkedList<Mensaje>();

		for (Entidad eCliente : eMensajes) {
			clientes.add(recuperarMensaje(eCliente.getId()));
		}
		return clientes;
	}

	// -------------------Funciones auxiliares-----------------------------
	private String obtenerCodigosMensajes(List<Mensaje> listaMensajes) {
		String aux = "";
		for (Mensaje m : listaMensajes) {
			aux += m.getCodigo() + " ";
		}
		return aux.trim();
	}

	private List<Mensaje> obtenerMensajesDesdeCodigos(String mensajes) {

		List<Mensaje> listaMensajes = new LinkedList<Mensaje>();
		StringTokenizer strTok = new StringTokenizer(mensajes, " ");
		AdaptadorMensajeTDS adaptadorV = AdaptadorMensajeTDS.getUnicaInstancia();
		while (strTok.hasMoreTokens()) {
			listaMensajes.add(adaptadorV.recuperarMensaje(Integer.valueOf((String) strTok.nextElement())));
		}
		return listaMensajes;
	}
}
