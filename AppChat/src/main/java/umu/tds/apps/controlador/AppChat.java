package umu.tds.apps.controlador;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import umu.tds.apps.modelo.Contacto;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.RepositorioUsuarios;
import umu.tds.apps.modelo.Usuario;


// convertir controlador en singleton
public class AppChat {
	
	private Usuario usuarioActual;
	private RepositorioUsuarios repositorioUsuarios;
	
	public static List<Mensaje> obtenerListaMensajesRecientesPorUsuario() {
		Mensaje[] values = new Mensaje[] {
				new Mensaje(new Usuario("Ana", "1234", "555-1234", "ana@example.com", Optional.of("¡Hola!"), "imagen1.png", LocalDate.of(1990, 1, 1)), "mensaje1", new Contacto("Ana")),
				new Mensaje(new Usuario("Rosa", "5678", "555-5678", "rosa@example.com", Optional.of("¡Buenas!"), "imagen2.png", LocalDate.of(1991, 2, 2)), "mensaje2", new Contacto("Rosa")),
				new Mensaje(new Usuario("Jose", "91011", "555-9101", "jose@example.com", Optional.empty(), "imagen3.png", LocalDate.of(1992, 3, 3)), "mensaje3", new Contacto("Jose"))
		};
		return Arrays.asList(values);
	}
}
