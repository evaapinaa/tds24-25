package umu.tds.apps.modelo;

/**
 * Interfaz que define el comportamiento de los descuentos.
 * Implementa el patrón Strategy para calcular diferentes tipos de descuentos.
*/
public interface Descuento {
	
	
    /**
     * Calcula el precio con descuento.
     * 
     * @param precio Precio original
     * @return Precio con descuento aplicado
     */
	public double calcularDescuento(double precio);
}
