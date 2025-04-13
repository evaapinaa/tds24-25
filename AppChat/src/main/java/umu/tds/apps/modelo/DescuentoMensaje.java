// Implementación específica para el descuento por mensajes
package umu.tds.apps.modelo;

public class DescuentoMensaje implements Descuento {
    private int mensajesEnviados;

    public DescuentoMensaje(int mensajesEnviados) {
        this.mensajesEnviados = mensajesEnviados;
    }

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
