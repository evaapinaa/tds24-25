package umu.tds.apps.modelo;

public class FactoriaBusqueda {

    public static BusquedaMensaje crearEstrategia(String tipoFiltro) {
        try {
            // Busca directamente la clase basada en el nombre del filtro
            Class<?> clase = Class.forName("umu.tds.apps.estrategias.BusquedaPor" + tipoFiltro);

            // Verifica que la clase implementa la interfaz BusquedaMensaje
            if (!BusquedaMensaje.class.isAssignableFrom(clase)) {
                throw new IllegalArgumentException("La clase no implementa la interfaz BusquedaMensaje.");
            }

            // Crea una instancia de la clase
            return (BusquedaMensaje) clase.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Filtro de búsqueda desconocido: " + tipoFiltro, e);
        } catch (Exception e) {
            throw new RuntimeException("Error al instanciar la estrategia de búsqueda: " + tipoFiltro, e);
        }
    }
}