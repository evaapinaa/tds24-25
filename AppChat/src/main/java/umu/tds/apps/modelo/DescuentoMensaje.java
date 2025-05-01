// Implementación específica para el descuento por mensajes
package umu.tds.apps.modelo;


/**
 * Implementación específica para el descuento por cantidad de mensajes enviados.
 * Implementa la interfaz Descuento.
 */
public class DescuentoMensaje implements Descuento {
    private int mensajesEnviados;

    /**
     * Crea una nueva instancia de descuento por mensajes.
     * 
     * @param mensajesEnviados Cantidad de mensajes enviados por el usuario
     */
    public DescuentoMensaje(int mensajesEnviados) {
        this.mensajesEnviados = mensajesEnviados;
    }

    
    /**
     * Calcula el descuento basado en la cantidad de mensajes enviados.
     * - 30% de descuento para más de 300 mensajes
     * - 20% de descuento para más de 200 mensajes
     * - 15% de descuento para más de 100 mensajes
     * 
     * @param precio Precio original
     * @return Precio con descuento aplicado
     */
    @Override
    public double calcularDescuento(double precio) {
        // Diferentes descuentos según el número de mensajes enviados
        if (mensajesEnviados > 300) {
            return precio * 0.7; // 30% de descuento
        } else if (mensajesEnviados > 200) {
            return precio * 0.8; // 20% de descuento
        } else if (mensajesEnviados > 100) {
            return precio * 0.85; // 15% de descuento
        }
        
        // Sin descuento
        return precio;
    }
}
