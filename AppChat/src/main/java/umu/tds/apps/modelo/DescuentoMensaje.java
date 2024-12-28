package umu.tds.apps.modelo;

public class DescuentoMensaje implements Descuento {

	private int mensajesEnviados;

	public DescuentoMensaje(int mensajesEnviados) {
		this.mensajesEnviados = mensajesEnviados;
	}

	@Override
	public double calcularDescuento(double precio) {
		if (mensajesEnviados < 100) {
			return precio;
		} else if (mensajesEnviados < 500) {
			return precio * 0.9;
		} else {
			return precio * 0.8;
		}
	}
}
