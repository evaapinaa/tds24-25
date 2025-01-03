package umu.tds.apps.vista;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import umu.tds.apps.controlador.AppChat;
import umu.tds.apps.modelo.DescuentoFecha;

import java.awt.Color;
import java.awt.Font;

public class VentanaPremium extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTextField textFieldFechaRegistro;
    private JTextField textFieldMensajesMes;
    private JLabel lblPrecio;
    private double precioBase = 50.0; // Precio base de la suscripción premium

    public static void main(String[] args) {
        try {
            VentanaPremium dialog = new VentanaPremium();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public VentanaPremium() {
        setTitle("Activar Premium");
        setBounds(100, 100, 400, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));

        // Panel de activación premium
        JPanel panelPremium = new JPanel();
        panelPremium.setBackground(new Color(198, 249, 249));
        panelPremium.setBorder(new TitledBorder("Activación Premium"));
        contentPanel.add(panelPremium, BorderLayout.CENTER);
        GridBagLayout gbl_panelPremium = new GridBagLayout();
        gbl_panelPremium.columnWidths = new int[]{0, 0, 0};
        gbl_panelPremium.rowHeights = new int[]{0, 0, 0, 0};
        gbl_panelPremium.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gbl_panelPremium.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        panelPremium.setLayout(gbl_panelPremium);

        // Campo para fech de registro
        /*JLabel lblFechaRegistro = new JLabel("Fecha de Registro (yyyy-mm-dd):");
        lblFechaRegistro.setFont(new Font("Arial", Font.BOLD, 11));
        GridBagConstraints gbc_lblFechaRegistro = new GridBagConstraints();
        gbc_lblFechaRegistro.insets = new Insets(0, 0, 5, 5);
        gbc_lblFechaRegistro.anchor = GridBagConstraints.EAST;
        gbc_lblFechaRegistro.gridx = 0;
        gbc_lblFechaRegistro.gridy = 0;
        panelPremium.add(lblFechaRegistro, gbc_lblFechaRegistro);

        textFieldFechaRegistro = new JTextField();
        GridBagConstraints gbc_textFieldFechaRegistro = new GridBagConstraints();
        gbc_textFieldFechaRegistro.insets = new Insets(0, 0, 5, 0);
        gbc_textFieldFechaRegistro.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldFechaRegistro.gridx = 1;
        gbc_textFieldFechaRegistro.gridy = 0;
        panelPremium.add(textFieldFechaRegistro, gbc_textFieldFechaRegistro);
        textFieldFechaRegistro.setColumns(10);*/
        
     // Mostrar la fecha de registro
        JLabel lblFechaRegistro = new JLabel("Fecha de Registro: " + AppChat.getUsuarioActual().getFechaRegistro());
        lblFechaRegistro.setFont(new Font("Arial", Font.BOLD, 11));
        GridBagConstraints gbc_lblFechaRegistro = new GridBagConstraints();
        gbc_lblFechaRegistro.insets = new Insets(0, 0, 5, 5);
        gbc_lblFechaRegistro.anchor = GridBagConstraints.WEST;
        gbc_lblFechaRegistro.gridx = 0;
        gbc_lblFechaRegistro.gridy = 0;
        panelPremium.add(lblFechaRegistro, gbc_lblFechaRegistro);



        // Mostrar el precio final después de aplicar descuentos
        lblPrecio = new JLabel("Precio Final: " + precioBase + " €");
        lblPrecio.setFont(new Font("Arial", Font.BOLD, 11));
        GridBagConstraints gbc_lblPrecio = new GridBagConstraints();
        gbc_lblPrecio.insets = new Insets(0, 0, 5, 0);
        gbc_lblPrecio.gridx = 1;
        gbc_lblPrecio.gridy = 2;
        panelPremium.add(lblPrecio, gbc_lblPrecio);

        // Botón para calcular descuento
        JButton btnCalcularDescuento = new JButton("Calcular Descuento por Fecha");
        btnCalcularDescuento.setForeground(new Color(255, 255, 255));
        btnCalcularDescuento.setBackground(new Color(0, 128, 128));
        btnCalcularDescuento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calcularPrecioConDescuento();
            }
        });
        GridBagConstraints gbc_btnCalcularDescuento = new GridBagConstraints();
        gbc_btnCalcularDescuento.gridx = 1;
        gbc_btnCalcularDescuento.gridy = 1;
        panelPremium.add(btnCalcularDescuento, gbc_btnCalcularDescuento);
    }
    
    

    private void calcularPrecioConDescuento() {
        try {
            // obtenemos la fecha de registro del usuario actual
            LocalDate fechaRegistro = AppChat.getUsuarioActual().getFechaRegistro();

            if (fechaRegistro == null) {
                JOptionPane.showMessageDialog(this, "La fecha de registro no está disponible.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // crreamos una instancia de descuentoFecha
            DescuentoFecha descuentoFecha = new DescuentoFecha(fechaRegistro);

            // calculamos el precio tras el descuento
            double precioFinal = descuentoFecha.calcularDescuento(precioBase);

            // mostramos el precio
            lblPrecio.setText("Precio Final: " + String.format("%.2f", precioFinal) + " €");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al calcular el descuento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
