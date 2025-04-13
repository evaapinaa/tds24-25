// Implementación específica para el descuento por fecha
package umu.tds.apps.modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DescuentoFecha implements Descuento {
    private LocalDate fechaRegistro;

    public DescuentoFecha(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

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