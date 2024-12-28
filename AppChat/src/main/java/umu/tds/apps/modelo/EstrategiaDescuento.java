package umu.tds.apps.modelo;

public class EstrategiaDescuento {
	
    private Descuento estrategiaDescuento;

    public void setEstrategiaDescuento(Descuento estrategiaDescuento) {
        this.estrategiaDescuento = estrategiaDescuento;
    }

    public double calcularPrecioFinal(double precioOriginal) {
        if (estrategiaDescuento == null) {
            return precioOriginal; // Sin descuento si no hay estrategia
        }
        return estrategiaDescuento.calcularDescuento(precioOriginal);
    }

}
