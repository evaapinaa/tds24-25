// VentanaPremium.java
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
import umu.tds.apps.modelo.EstrategiaDescuento;

import java.awt.Color;
import java.awt.Font;

public class VentanaPremium extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JLabel lblPrecio;
    private double precioActual;

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
        gbl_panelPremium.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_panelPremium.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gbl_panelPremium.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        panelPremium.setLayout(gbl_panelPremium);

        // Mostrar la fecha de registro
        JLabel lblFechaRegistro = new JLabel("Fecha de Registro: " + AppChat.getUsuarioActual().getFechaRegistro());
        lblFechaRegistro.setFont(new Font("Arial", Font.BOLD, 11));
        GridBagConstraints gbc_lblFechaRegistro = new GridBagConstraints();
        gbc_lblFechaRegistro.gridwidth = 2;
        gbc_lblFechaRegistro.insets = new Insets(0, 0, 5, 0);
        gbc_lblFechaRegistro.anchor = GridBagConstraints.WEST;
        gbc_lblFechaRegistro.gridx = 0;
        gbc_lblFechaRegistro.gridy = 0;
        panelPremium.add(lblFechaRegistro, gbc_lblFechaRegistro);
        
        // Mostrar el número de mensajes enviados
        JLabel lblMensajesEnviados = new JLabel("Mensajes enviados: " + AppChat.getUsuarioActual().getNumeroMensajesUltimoMes());
        lblMensajesEnviados.setFont(new Font("Arial", Font.BOLD, 11));
        GridBagConstraints gbc_lblMensajesEnviados = new GridBagConstraints();
        gbc_lblMensajesEnviados.gridwidth = 2;
        gbc_lblMensajesEnviados.insets = new Insets(0, 0, 5, 0);
        gbc_lblMensajesEnviados.anchor = GridBagConstraints.WEST;
        gbc_lblMensajesEnviados.gridx = 0;
        gbc_lblMensajesEnviados.gridy = 1; 
        panelPremium.add(lblMensajesEnviados, gbc_lblMensajesEnviados);

        // Calcular precio inicial
        precioActual = AppChat.getUnicaInstancia().calcularPrecioPremium();
        
        // Mostrar el precio final después de aplicar descuentos
        lblPrecio = new JLabel("Precio Final: " + String.format("%.2f", precioActual) + " €");
        lblPrecio.setFont(new Font("Arial", Font.BOLD, 14));
        GridBagConstraints gbc_lblPrecio = new GridBagConstraints();
        gbc_lblPrecio.gridwidth = 2;
        gbc_lblPrecio.insets = new Insets(15, 0, 15, 0);
        gbc_lblPrecio.gridx = 0;
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
        GridBagConstraints gbc_btnDescuentoFecha = new GridBagConstraints();
        gbc_btnDescuentoFecha.insets = new Insets(0, 0, 5, 0);
        gbc_btnDescuentoFecha.gridx = 0;
        gbc_btnDescuentoFecha.gridy = 3;
        panelPremium.add(btnCalcularDescuentoFecha, gbc_btnDescuentoFecha);

        // Botón para aplicar descuento por mensajes
        JButton btnCalcularDescuentoMensajes = new JButton("Aplicar Descuento por Mensajes");
        btnCalcularDescuentoMensajes.setForeground(new Color(255, 255, 255));
        btnCalcularDescuentoMensajes.setBackground(new Color(0, 128, 128));
        btnCalcularDescuentoMensajes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                aplicarDescuentoPorMensajes();
            }
        });
        GridBagConstraints gbc_btnDescuentoMensajes = new GridBagConstraints();
        gbc_btnDescuentoMensajes.gridx = 1;
        gbc_btnDescuentoMensajes.gridy = 3;
        panelPremium.add(btnCalcularDescuentoMensajes, gbc_btnDescuentoMensajes);

        // Botón para activar Premium
        JButton btnActivarPremium = new JButton("Activar Premium");
        btnActivarPremium.setFont(new Font("Arial", Font.BOLD, 12));
        btnActivarPremium.setForeground(new Color(255, 255, 255));
        btnActivarPremium.setBackground(new Color(0, 128, 128));
        btnActivarPremium.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activarPremium();
            }
        });
        
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(198, 249, 249));
        contentPanel.add(panelBotones, BorderLayout.SOUTH);
        panelBotones.add(btnActivarPremium);
    }
    
    private void aplicarDescuentoPorFecha() {
        try {
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
            double precioBase = AppChat.PRECIO_PREMIUM;
            EstrategiaDescuento estrategia = new EstrategiaDescuento();
            estrategia.setEstrategiaDescuento(new DescuentoFecha(fechaRegistro));
            
            precioActual = estrategia.calcularPrecioFinal(precioBase);
            
            // Actualizar la etiqueta de precio
            lblPrecio.setText("Precio Final: " + String.format("%.2f", precioActual) + " €");
            
            JOptionPane.showMessageDialog(this, "¡Descuento aplicado! Precio reducido un 20%.", "Descuento aplicado", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al aplicar el descuento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void aplicarDescuentoPorMensajes() {
        try {
            // Obtener el número de mensajes enviados
            long mensajesEnviados = AppChat.getUsuarioActual().getNumeroMensajesUltimoMes();
            
            // Verificar si el usuario cumple los requisitos mínimos
            if (mensajesEnviados <= 100) {
                JOptionPane.showMessageDialog(this, "Necesitas más de 100 mensajes enviados para obtener un descuento.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Aplicar el descuento
            double precioBase = AppChat.PRECIO_PREMIUM;
            EstrategiaDescuento estrategia = new EstrategiaDescuento();
            estrategia.setEstrategiaDescuento(new DescuentoMensaje((int)mensajesEnviados));
            
            precioActual = estrategia.calcularPrecioFinal(precioBase);
            
            // Actualizar la etiqueta de precio
            lblPrecio.setText("Precio Final: " + String.format("%.2f", precioActual) + " €");
            
            // Determinar el porcentaje de descuento
            int porcentajeDescuento = 0;
            if (mensajesEnviados > 300) porcentajeDescuento = 30;
            else if (mensajesEnviados > 200) porcentajeDescuento = 20;
            else if (mensajesEnviados > 100) porcentajeDescuento = 15;
            
            JOptionPane.showMessageDialog(this, "¡Descuento aplicado! Precio reducido un " + porcentajeDescuento + "%.", "Descuento aplicado", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al aplicar el descuento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void activarPremium() {
        // Aquí iría la lógica de pago real, ahora solo simulamos
        int opcion = JOptionPane.showConfirmDialog(
            this, 
            "¿Confirmar el pago de " + String.format("%.2f", precioActual) + " € para activar Premium?", 
            "Confirmar Pago", 
            JOptionPane.YES_NO_OPTION
        );
        
        if (opcion == JOptionPane.YES_OPTION) {
            // Llamar al controlador para activar Premium
            boolean exito = AppChat.getUnicaInstancia().activarPremium();
            
            if (exito) {
                JOptionPane.showMessageDialog(
                    this, 
                    "¡Felicidades! Ahora eres un usuario Premium.", 
                    "Premium Activado", 
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                // Cerrar la ventana
                dispose();
            } else {
                JOptionPane.showMessageDialog(
                    this, 
                    "Ha ocurrido un error al activar Premium. Inténtalo más tarde.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}