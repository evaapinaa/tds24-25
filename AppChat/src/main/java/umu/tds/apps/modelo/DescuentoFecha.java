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
        // sacamos cuantos dias lleva registrado
        long diasDesdeRegistro = ChronoUnit.DAYS.between(fechaRegistro, LocalDate.now());

        // si se ha registrado hace menos de 7 dias
        if (diasDesdeRegistro < 7) {
            return precio * 0.8; // 20% de descuento
        }

        // si no, no se aplica descuentoFecha
        return precio;
    }
}
