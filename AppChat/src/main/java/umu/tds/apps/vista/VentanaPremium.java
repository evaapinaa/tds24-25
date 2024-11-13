package umu.tds.apps.vista;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
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
        JLabel lblFechaRegistro = new JLabel("Fecha de Registro (yyyy-mm-dd):");
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
        textFieldFechaRegistro.setColumns(10);

        // Campo para mensajes enviados en el último mes
        JLabel lblMensajesMes = new JLabel("Mensajes enviados en el último mes:");
        lblMensajesMes.setFont(new Font("Arial", Font.BOLD, 11));
        GridBagConstraints gbc_lblMensajesMes = new GridBagConstraints();
        gbc_lblMensajesMes.anchor = GridBagConstraints.EAST;
        gbc_lblMensajesMes.insets = new Insets(0, 0, 5, 5);
        gbc_lblMensajesMes.gridx = 0;
        gbc_lblMensajesMes.gridy = 1;
        panelPremium.add(lblMensajesMes, gbc_lblMensajesMes);

        textFieldMensajesMes = new JTextField();
        GridBagConstraints gbc_textFieldMensajesMes = new GridBagConstraints();
        gbc_textFieldMensajesMes.insets = new Insets(0, 0, 5, 0);
        gbc_textFieldMensajesMes.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldMensajesMes.gridx = 1;
        gbc_textFieldMensajesMes.gridy = 1;
        panelPremium.add(textFieldMensajesMes, gbc_textFieldMensajesMes);
        textFieldMensajesMes.setColumns(10);

        // Mostrar el precio final después de aplicar descuentos
        lblPrecio = new JLabel("Precio Final: " + precioBase + " €");
        lblPrecio.setFont(new Font("Arial", Font.BOLD, 11));
        GridBagConstraints gbc_lblPrecio = new GridBagConstraints();
        gbc_lblPrecio.insets = new Insets(0, 0, 5, 0);
        gbc_lblPrecio.gridx = 1;
        gbc_lblPrecio.gridy = 2;
        panelPremium.add(lblPrecio, gbc_lblPrecio);

        // Botón para calcular descuento
        JButton btnCalcularDescuento = new JButton("Calcular Descuento");
        btnCalcularDescuento.setForeground(new Color(255, 255, 255));
        btnCalcularDescuento.setBackground(new Color(0, 128, 128));
        btnCalcularDescuento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calcularPrecioConDescuento();
            }
        });
        GridBagConstraints gbc_btnCalcularDescuento = new GridBagConstraints();
        gbc_btnCalcularDescuento.gridx = 1;
        gbc_btnCalcularDescuento.gridy = 3;
        panelPremium.add(btnCalcularDescuento, gbc_btnCalcularDescuento);
    }

    private void calcularPrecioConDescuento() {
        try {
            double descuento = 0.0;

            // Obtener fecha de registro (suponiendo un formato de "yyyy-mm-dd")
            String fechaRegistro = textFieldFechaRegistro.getText();
            if (!fechaRegistro.isEmpty()) {
                descuento += 0.1; // Descuento del 10% por fecha de registro
            }

            // Obtener cantidad de mensajes enviados
            int mensajesMes = Integer.parseInt(textFieldMensajesMes.getText());
            if (mensajesMes > 100) {
                descuento += 0.15; // Descuento adicional del 15% si envió más de 100 mensajes
            }

            // Calcular precio final
            double precioFinal = precioBase * (1 - descuento);
            lblPrecio.setText("Precio Final: " + String.format("%.2f", precioFinal) + " €");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, introduce un número válido en el campo de mensajes enviados.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
