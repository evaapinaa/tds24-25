package umu.tds.apps.vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import umu.tds.apps.controlador.AppChat;
import umu.tds.apps.modelo.ContactoIndividual;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.Usuario;

/**
 * Componente personalizado que implementa un renderizador de celdas para la lista de resultados de búsqueda.
 * Esta clase se encarga de mostrar cada mensaje en un formato visual adecuado dentro de una lista,
 * incluyendo la información del emisor, receptor, texto del mensaje y fecha/hora.
 * 
 * <p>El renderizador aplica diferentes estilos visuales para los mensajes, incluyendo formatos
 * específicos cuando un elemento está seleccionado.</p>
 * 
 * @author TDS-2025
 * @version 1.0
 */
public class BusquedaCellRenderer extends JPanel implements ListCellRenderer<Mensaje> {

    private static final long serialVersionUID = 1L;
    private JLabel lblEmisor;
    private JLabel lblReceptor;
    private JLabel lblTexto;
    private JLabel lblFecha;

    
    /**
     * Constructor que inicializa y configura el renderizador de celdas para mensajes.
     * Establece el diseño del panel, crea las etiquetas para cada parte del mensaje
     * y configura los estilos visuales iniciales.
     */
    public BusquedaCellRenderer() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel superior para emisor y receptor
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        topPanel.setOpaque(false);
        lblEmisor = new JLabel();
        lblReceptor = new JLabel();
        lblEmisor.setFont(new Font("Sans Serif", Font.BOLD, 12));
        lblReceptor.setFont(new Font("Sans Serif", Font.BOLD, 12));
        topPanel.add(lblEmisor);
        topPanel.add(lblReceptor);
        add(topPanel, BorderLayout.NORTH);

        // Etiqueta para el texto del mensaje
        lblTexto = new JLabel();
        lblTexto.setFont(new Font("Sans Serif", Font.PLAIN, 12));
        add(lblTexto, BorderLayout.CENTER);

        // Etiqueta para la fecha
        lblFecha = new JLabel();
        lblFecha.setHorizontalAlignment(SwingConstants.RIGHT);
        lblFecha.setFont(new Font("Sans Serif", Font.ITALIC, 11));
        add(lblFecha, BorderLayout.SOUTH);

        // Configurar colores iniciales
        setBackground(new Color(250, 250, 250)); // Fondo suave
        setBorder(new LineBorder(new Color(200, 200, 200), 1, true)); // Bordes redondeados
    }

    
    /**
     * Implementación del método de la interfaz ListCellRenderer que configura la apariencia
     * de cada elemento de la lista de mensajes.
     * 
     * @param list La lista que contiene el elemento
     * @param mensaje El mensaje a renderizar
     * @param index El índice del elemento en la lista
     * @param isSelected Indica si el elemento está seleccionado
     * @param cellHasFocus Indica si el elemento tiene el foco
     * @return El componente configurado para mostrar el mensaje
     */
    @Override
    public Component getListCellRendererComponent(
            JList<? extends Mensaje> list,
            Mensaje mensaje,
            int index,
            boolean isSelected,
            boolean cellHasFocus) 
    {
        // Configurar emisor y receptor. Comprobar si agregado (contactoindividual) o no (teléfono)
        String emi = obtenerNombreOContacto(mensaje.getEmisor());
        String rec = obtenerNombreOContacto(mensaje.getReceptor());
		
        lblEmisor.setText("Emisor: " + emi);
        lblReceptor.setText("Receptor: " + rec);

        // Configurar texto del mensaje
        String texto = (mensaje.getTexto() != null) ? mensaje.getTexto() : "[Sin texto]";
        lblTexto.setText("Mensaje: " + texto);

        // Configurar fecha 
        if (mensaje.getFecha() != null) {
            lblFecha.setText(mensaje.getFecha().toString() + " - " + mensaje.getHora().getHour() + ":" + mensaje.getHora().getMinute());
        } else {
            lblFecha.setText("Sin fecha");
        }

        // Configurar colores de selección y hover
        if (isSelected) {
            setBackground(new Color(173, 216, 230)); // Azul claro para selección
            lblEmisor.setForeground(Color.BLACK);
            lblReceptor.setForeground(Color.BLACK);
            lblTexto.setForeground(Color.BLACK);
            lblFecha.setForeground(Color.BLACK);
        } else {
            setBackground(new Color(245, 245, 245)); // Fondo suave para celdas normales
            lblEmisor.setForeground(new Color(33, 33, 33)); // Texto oscuro
            lblReceptor.setForeground(new Color(33, 33, 33));
            lblTexto.setForeground(new Color(66, 66, 66));
            lblFecha.setForeground(new Color(100, 100, 100)); // Texto más tenue para la fecha
        }

        setOpaque(true);
        return this;
    }

    
    /**
     * Sobrescribe el método paintComponent para dibujar elementos gráficos personalizados,
     * como una línea separadora entre cada celda de la lista.
     * 
     * @param g El contexto gráfico utilizado para pintar
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Línea separadora al final de cada celda
        g.setColor(new Color(220, 220, 220)); // Línea gris clara
        g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
    }
    
    
    /**
     * Obtiene el nombre del usuario o contacto para mostrar en la interfaz.
     * Si el usuario es un contacto registrado, devuelve su nombre; si es el usuario actual,
     * devuelve "Tú"; si no es un contacto registrado, devuelve el número de teléfono.
     * 
     * @param usuario El usuario cuyo nombre o identificador se desea obtener
     * @return El nombre o identificador del usuario a mostrar
     */
    private String obtenerNombreOContacto(Usuario usuario) {
        if (usuario == null) {
            return "(Desconocido)";
        }

        // Si el usuario proporcionado es el usuario actual
        if (usuario.equals(AppChat.getUsuarioActual())) {
            return "Tú"; // Puedes cambiar esto por tu nombre si lo deseas
        }

        // Verificar si el usuario es un contacto
        ContactoIndividual contacto = AppChat.getUnicaInstancia().obtenerContactoPorTelefono(usuario.getTelefono());
        if (contacto != null) {
            return contacto.getNombre(); // Devuelve el nombre del contacto si está registrado
        } else {
            return usuario.getTelefono(); // Devuelve el número de teléfono si no es contacto
        }
    }

}
