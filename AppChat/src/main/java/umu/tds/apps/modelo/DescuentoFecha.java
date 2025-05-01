// Implementación específica para el descuento por fecha
package umu.tds.apps.modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Implementación específica para el descuento por fecha de registro.
 * Implementa la interfaz Descuento.
*/
public class DescuentoFecha implements Descuento {
    private LocalDate fechaRegistro;

    
    /**
     * Crea una nueva instancia de descuento por fecha.
     * 
     * @param fechaRegistro Fecha de registro del usuario
     */
    public DescuentoFecha(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    
    
    /**
     * Calcula el descuento basado en la fecha de registro.
     * Aplica un 20% de descuento si el usuario se registró hace menos de 7 días.
     * 
     * @param precio Precio original
     * @return Precio con descuento aplicado
     */
    @Override
    public double calcularDescuento(double precio) {
        // Aplicamos 20% de descuento si el usuario se registró hace menos de 7 días
        long diasDesdeRegistro = ChronoUnit.DAYS.between(fechaRegistro, LocalDate.now());
        
        if (diasDesdeRegistro < 7) {
            return precio * 0.8; // 20% de descuento
        }
        
        // Sin descuento
        return precio;
    }
}