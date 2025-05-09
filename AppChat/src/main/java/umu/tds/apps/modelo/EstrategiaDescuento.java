// Clase para manejar la estrategia de descuento
package umu.tds.apps.modelo;


/**
 * Clase que gestiona la estrategia de descuento a aplicar.
 * Implementa el patrón Strategy.
*/
public class EstrategiaDescuento {
    private Descuento estrategiaDescuento;

    
    /**
     * Establece la estrategia de descuento a utilizar.
     * 
     * @param estrategiaDescuento Estrategia de descuento
     */
    public void setEstrategiaDescuento(Descuento estrategiaDescuento) {
        this.estrategiaDescuento = estrategiaDescuento;
    }

    /**
     * Calcula el precio final aplicando la estrategia de descuento actual.
     * Si no hay estrategia definida, devuelve el precio original.
     * 
     * @param precioOriginal Precio original
     * @return Precio con descuento aplicado
     */
    public double calcularPrecioFinal(double precioOriginal) {
        if (estrategiaDescuento == null) {
            return precioOriginal; // Sin descuento si no hay estrategia
        }
        return estrategiaDescuento.calcularDescuento(precioOriginal);
    }
}
