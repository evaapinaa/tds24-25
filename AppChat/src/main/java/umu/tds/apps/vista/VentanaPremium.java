package umu.tds.apps.vista;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
import umu.tds.apps.modelo.DescuentoMensaje;

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
        
     // Mostrar el número de mensajes enviados
        JLabel lblMensajesEnviados = new JLabel("Mensajes enviados: " + AppChat.getUsuarioActual().getNumeroMensajesUltimoMes());
        lblMensajesEnviados.setFont(new Font("Arial", Font.BOLD, 11));
        GridBagConstraints gbc_lblMensajesEnviados = new GridBagConstraints();
        gbc_lblMensajesEnviados.insets = new Insets(0, 0, 5, 5);
        gbc_lblMensajesEnviados.anchor = GridBagConstraints.WEST;
        gbc_lblMensajesEnviados.gridx = 0;
        gbc_lblMensajesEnviados.gridy = 1; 
        panelPremium.add(lblMensajesEnviados, gbc_lblMensajesEnviados);




        // Mostrar el precio final después de aplicar descuentos
        lblPrecio = new JLabel("Precio Final: " + precioBase + " €");
        lblPrecio.setFont(new Font("Arial", Font.BOLD, 11));
        GridBagConstraints gbc_lblPrecio = new GridBagConstraints();
        gbc_lblPrecio.insets = new Insets(0, 0, 5, 0);
        gbc_lblPrecio.gridx = 1;
        gbc_lblPrecio.gridy = 2;
        panelPremium.add(lblPrecio, gbc_lblPrecio);

     // Botón para aplicar descuento por fecha
        JButton btnCalcularDescuentoFecha = new JButton("Aplicar Descuento por Fecha");
        btnCalcularDescuentoFecha.setForeground(new Color(255, 255, 255));
        btnCalcularDescuentoFecha.setBackground(new Color(0, 128, 128));
        btnCalcularDescuentoFecha.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                aplicarDescuentoPorFecha();
            }
        });
        panelPremium.add(btnCalcularDescuentoFecha);

        // Botón para aplicar descuento por mensajes
        JButton btnCalcularDescuentoMensajes = new JButton("Aplicar Descuento por Mensajes");
        btnCalcularDescuentoMensajes.setForeground(new Color(255, 255, 255));
        btnCalcularDescuentoMensajes.setBackground(new Color(0, 128, 128));
        btnCalcularDescuentoMensajes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                aplicarDescuentoPorMensajes();
            }
        });
        panelPremium.add(btnCalcularDescuentoMensajes);


    }
    
    private void aplicarDescuentoPorFecha() {
        try {
            // Obtener el precio actual de la etiqueta
            double precioActual = Double.parseDouble(lblPrecio.getText().split(":")[1].trim().replace("€", ""));

            // Obtener la fecha de registro
            LocalDate fechaRegistro = AppChat.getUsuarioActual().getFechaRegistro();

            if (fechaRegistro == null) {
                JOptionPane.showMessageDialog(this, "La fecha de registro no está disponible.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar si el usuario cumple los requisitos para el descuento
            if (ChronoUnit.DAYS.between(fechaRegistro, LocalDate.now()) >= 7) {
                JOptionPane.showMessageDialog(this, "Tu fecha de registro no cumple los requisitos para el descuento.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Aplicar el descuento
            DescuentoFecha descuentoFecha = new DescuentoFecha(fechaRegistro);
            double precioConDescuento = descuentoFecha.calcularDescuento(precioActual);

            // Verificar si ya se aplicó el descuento (precio no cambia)
            if (precioConDescuento == precioActual) {
                JOptionPane.showMessageDialog(this, "Ya has aplicado este descuento.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Actualizar el precio en la etiqueta
            lblPrecio.setText("Precio Final: " + String.format("%.2f", precioConDescuento) + " €");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al aplicar el descuento por fecha: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void aplicarDescuentoPorMensajes() {
        try {
            // Obtener el precio actual de la etiqueta
            double precioActual = Double.parseDouble(lblPrecio.getText().split(":")[1].trim().replace("€", "").replace(",", "."));

            // Obtener el número de mensajes enviados
            int mensajesEnviados = (int) AppChat.getUsuarioActual().getNumeroMensajesUltimoMes();

            // Verificar si el usuario cumple los requisitos para el descuento
            if (mensajesEnviados <= 0) {
                JOptionPane.showMessageDialog(this, "No has enviado suficientes mensajes este mes para obtener el descuento.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Aplicar el descuento
            DescuentoMensaje descuentoMensajes = new DescuentoMensaje(mensajesEnviados);
            double precioConDescuento = descuentoMensajes.calcularDescuento(precioActual);

            // Verificar si ya se aplicó el descuento (precio no cambia)
            if (precioConDescuento == precioActual) {
                JOptionPane.showMessageDialog(this, "Ya has aplicado este descuento.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Actualizar el precio en la etiqueta
            lblPrecio.setText("Precio Final: " + String.format("%.2f", precioConDescuento) + " €");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al aplicar el descuento por mensajes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
