package umu.tds.apps.controlador;

import java.util.Arrays;
import java.util.List;

import umu.tds.apps.modelo.Contacto;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.Usuario;



public class AppChat {
	
	//private Usuario usuario;
	
	public static List<Mensaje> obtenerListaMensajesRecientesPorUsuario() {
		Mensaje[] values = new Mensaje[] {
				new Mensaje(new Usuario("Ana"), "mensaje1", new Contacto("Ana")), 
				new Mensaje(new Usuario("Rosa"),"mensaje2", new Contacto("Rosa")), 
				new Mensaje(new Usuario("Jose"),"mensaje3", new Contacto("Jose"))};
		
		return Arrays.asList(values);
	}
}
