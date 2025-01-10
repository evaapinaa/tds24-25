package umu.tds.apps.vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import umu.tds.apps.controlador.AppChat;
import umu.tds.apps.modelo.ContactoIndividual;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.Usuario;

public class BusquedaCellRenderer extends JPanel implements ListCellRenderer<Mensaje> {

    private static final long serialVersionUID = 1L;
    private JLabel lblEmisor;
    private JLabel lblReceptor;
    private JLabel lblTexto;
    private JLabel lblFecha;

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Línea separadora al final de cada celda
        g.setColor(new Color(220, 220, 220)); // Línea gris clara
        g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
    }
    
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
