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
import javax.swing.border.TitledBorder;

import umu.tds.apps.controlador.AppChat;
import umu.tds.apps.modelo.DescuentoFecha;
import umu.tds.apps.modelo.DescuentoMensaje;
import umu.tds.apps.modelo.EstrategiaDescuento;

import java.awt.Color;
import java.awt.Font;


/**
 * Ventana de activación de cuenta Premium para la aplicación AppChat.
 * Permite a los usuarios aplicar diferentes tipos de descuentos según
 * sus características (fecha de registro, cantidad de mensajes enviados)
 * y realizar el pago para activar la funcionalidad Premium.
 * 
 * @author TDS-2025
 * @version 1.0
 */
public class VentanaPremium extends JDialog {

	/** Número de versión para serialización */
	private static final long serialVersionUID = 1L;
	
	/** Panel principal de contenido */
	private final JPanel contentPanel = new JPanel();
	
	/** Etiqueta para mostrar el precio actual */
	private JLabel lblPrecio;
	
	/** Almacena el precio actual después de aplicar descuentos */
	private double precioActual;
	
	/** Indica si ya se ha aplicado el descuento por fecha de registro */
	private boolean descuentoFechaAplicado = false;
	
	/** Indica si ya se ha aplicado el descuento por número de mensajes */
	private boolean descuentoMensajeAplicado = false;

	
	/**
	 * Método principal para pruebas de la ventana.
	 * Crea y muestra una instancia de la ventana premium.
	 * 
	 * @param args Argumentos de línea de comandos (no utilizados)
	 */
	public static void main(String[] args) {
		try {
			VentanaPremium dialog = new VentanaPremium();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Constructor de la ventana Premium.
	 * Inicializa la interfaz gráfica, configura los componentes y
	 * prepara los listeners para gestionar los eventos de usuario.
	 */
	public VentanaPremium() {
		setTitle("Activar Premium");
		setBounds(100, 100, 565, 376);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(null);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		// indicar precio base
		precioActual = AppChat.PRECIO_PREMIUM;

		// Panel de activación premium
		JPanel panelPremium = new JPanel();
		panelPremium.setBackground(new Color(198, 249, 249));
		panelPremium.setBorder(new TitledBorder("Activación Premium"));
		contentPanel.add(panelPremium, BorderLayout.CENTER);
		GridBagLayout gbl_panelPremium = new GridBagLayout();
		gbl_panelPremium.columnWidths = new int[] { 25, 111, 0, 191, 25, 0 };
		gbl_panelPremium.rowHeights = new int[] { 10, 20, 20, 17, 25, 0, 0 };
		gbl_panelPremium.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_panelPremium.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panelPremium.setLayout(gbl_panelPremium);

		// Mostrar la fecha de registro
		JLabel lblFechaRegistro = new JLabel("Fecha de Registro: " + AppChat.getUsuarioActual().getFechaRegistro());
		lblFechaRegistro.setFont(new Font("Arial", Font.BOLD, 11));
		GridBagConstraints gbc_lblFechaRegistro = new GridBagConstraints();
		gbc_lblFechaRegistro.gridwidth = 3;
		gbc_lblFechaRegistro.fill = GridBagConstraints.BOTH;
		gbc_lblFechaRegistro.insets = new Insets(0, 0, 5, 5);
		gbc_lblFechaRegistro.gridx = 1;
		gbc_lblFechaRegistro.gridy = 1;
		panelPremium.add(lblFechaRegistro, gbc_lblFechaRegistro);

		// Botón para aplicar descuento por fecha
		JButton btnCalcularDescuentoFecha = new JButton("Aplicar Descuento por Fecha");
		btnCalcularDescuentoFecha.setForeground(new Color(255, 255, 255));
		btnCalcularDescuentoFecha.setBackground(new Color(0, 128, 128));
		btnCalcularDescuentoFecha.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aplicarDescuentoPorFecha();
			}
		});

		// Mostrar el número de mensajes enviados
		JLabel lblMensajesEnviados = new JLabel(
				"Mensajes enviados: " + AppChat.getUsuarioActual().getNumeroMensajesUltimoMes());
		lblMensajesEnviados.setFont(new Font("Arial", Font.BOLD, 11));
		GridBagConstraints gbc_lblMensajesEnviados = new GridBagConstraints();
		gbc_lblMensajesEnviados.gridwidth = 3;
		gbc_lblMensajesEnviados.fill = GridBagConstraints.BOTH;
		gbc_lblMensajesEnviados.insets = new Insets(0, 0, 5, 5);
		gbc_lblMensajesEnviados.gridx = 1;
		gbc_lblMensajesEnviados.gridy = 2;
		panelPremium.add(lblMensajesEnviados, gbc_lblMensajesEnviados);

		// Mostrar el precio final después de aplicar descuentos
		lblPrecio = new JLabel("Precio Final: " + String.format("%.2f", precioActual) + " €");
		lblPrecio.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblPrecio = new GridBagConstraints();
		gbc_lblPrecio.insets = new Insets(0, 0, 5, 0);
		gbc_lblPrecio.gridwidth = 3;
		gbc_lblPrecio.gridx = 1;
		gbc_lblPrecio.gridy = 3;
		panelPremium.add(lblPrecio, gbc_lblPrecio);
		GridBagConstraints gbc_btnCalcularDescuentoFecha = new GridBagConstraints();
		gbc_btnCalcularDescuentoFecha.insets = new Insets(0, 0, 5, 5);
		gbc_btnCalcularDescuentoFecha.gridx = 1;
		gbc_btnCalcularDescuentoFecha.gridy = 4;
		panelPremium.add(btnCalcularDescuentoFecha, gbc_btnCalcularDescuentoFecha);

		// Botón para aplicar descuento por mensajes
		JButton btnCalcularDescuentoMensajes = new JButton("Aplicar Descuento por Mensajes");
		btnCalcularDescuentoMensajes.setForeground(new Color(255, 255, 255));
		btnCalcularDescuentoMensajes.setBackground(new Color(0, 128, 128));
		btnCalcularDescuentoMensajes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aplicarDescuentoPorMensajes();
			}
		});
		GridBagConstraints gbc_btnCalcularDescuentoMensajes = new GridBagConstraints();
		gbc_btnCalcularDescuentoMensajes.insets = new Insets(0, 0, 5, 5);
		gbc_btnCalcularDescuentoMensajes.gridx = 3;
		gbc_btnCalcularDescuentoMensajes.gridy = 4;
		panelPremium.add(btnCalcularDescuentoMensajes, gbc_btnCalcularDescuentoMensajes);

		// Calcular precio inicial
		precioActual = AppChat.getUnicaInstancia().calcularPrecioPremium();

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

		// Verificar si el usuario ya es Premium
		if (AppChat.getUsuarioActual().isPremium()) {
			// Mostrar información en lugar de opciones de pago
			JOptionPane.showMessageDialog(this, "¡Ya eres un usuario Premium! Disfruta de todos los beneficios.",
					"Usuario Premium", JOptionPane.INFORMATION_MESSAGE);

			// Actualizar la interfaz para usuarios Premium
			btnActivarPremium.setText("Ya eres Premium");
			btnActivarPremium.setEnabled(false);
			btnCalcularDescuentoFecha.setEnabled(false);
			btnCalcularDescuentoMensajes.setEnabled(false);

			// Mostrar información sobre beneficios en lugar de precio
			lblPrecio.setText("Usuario Premium Activo");
		}
	}

	
	/**
	 * Aplica un descuento basado en la fecha de registro del usuario.
	 * Si el usuario se registró hace menos de 7 días, se le aplica
	 * un descuento del 20% sobre el precio base.
	 */
	private void aplicarDescuentoPorFecha() {
		// Verificar si el descuento ya se aplicó
		if (descuentoFechaAplicado) {
			JOptionPane.showMessageDialog(this, "El descuento por fecha ya ha sido aplicado.", "Información",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		try {
			// Obtener la fecha de registro
			LocalDate fechaRegistro = AppChat.getUsuarioActual().getFechaRegistro();

			if (fechaRegistro == null) {
				JOptionPane.showMessageDialog(this, "La fecha de registro no está disponible.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Verificar si el usuario cumple los requisitos para el descuento
			if (ChronoUnit.DAYS.between(fechaRegistro, LocalDate.now()) >= 7) {
				JOptionPane.showMessageDialog(this, "Tu fecha de registro no cumple los requisitos para el descuento.",
						"Información", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			// Aplicar el descuento
			EstrategiaDescuento estrategia = new EstrategiaDescuento();
			estrategia.setEstrategiaDescuento(new DescuentoFecha(fechaRegistro));
			precioActual = estrategia.calcularPrecioFinal(precioActual);

			// Actualizar la etiqueta de precio
			lblPrecio.setText("Precio Final: " + String.format("%.2f", precioActual) + " €");

			JOptionPane.showMessageDialog(this, "¡Descuento aplicado! Precio reducido un 20%.", "Descuento aplicado",
					JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error al aplicar el descuento: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		descuentoFechaAplicado = true;
	}

	
	/**
	 * Aplica un descuento basado en el número de mensajes enviados por el usuario.
	 * Se aplican diferentes porcentajes de descuento según la cantidad de mensajes:
	 * - Más de 100 mensajes: 15% de descuento
	 * - Más de 200 mensajes: 20% de descuento
	 * - Más de 300 mensajes: 30% de descuento
	 */
	private void aplicarDescuentoPorMensajes() {

		if (descuentoMensajeAplicado) {
			JOptionPane.showMessageDialog(this, "El descuento por mensajes ya ha sido aplicado.", "Información",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		try {
			// Obtener el número de mensajes enviados
			long mensajesEnviados = AppChat.getUsuarioActual().getNumeroMensajesUltimoMes();

			// Verificar si el usuario cumple los requisitos mínimos
			if (mensajesEnviados <= 100) {
				JOptionPane.showMessageDialog(this, "Necesitas más de 100 mensajes enviados para obtener un descuento.",
						"Información", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			// Aplicar el descuento
			double precioBase = AppChat.PRECIO_PREMIUM;
			EstrategiaDescuento estrategia = new EstrategiaDescuento();
			estrategia.setEstrategiaDescuento(new DescuentoMensaje((int) mensajesEnviados));

			precioActual = estrategia.calcularPrecioFinal(precioBase);

			// Actualizar la etiqueta de precio
			lblPrecio.setText("Precio Final: " + String.format("%.2f", precioActual) + " €");

			// Determinar el porcentaje de descuento
			int porcentajeDescuento = 0;
			if (mensajesEnviados > 300)
				porcentajeDescuento = 30;
			else if (mensajesEnviados > 200)
				porcentajeDescuento = 20;
			else if (mensajesEnviados > 100)
				porcentajeDescuento = 15;

			JOptionPane.showMessageDialog(this, "¡Descuento aplicado! Precio reducido un " + porcentajeDescuento + "%.",
					"Descuento aplicado", JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error al aplicar el descuento: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		descuentoMensajeAplicado = true;
	}

	
	/**
	 * Activa la cuenta Premium para el usuario actual.
	 * Muestra un diálogo de confirmación con el precio final y,
	 * si el usuario confirma, procesa la activación.
	 */
	private void activarPremium() {

		// Verificar si ya es premium (por si acaso xd
		if (AppChat.getUsuarioActual().isPremium()) {
			JOptionPane.showMessageDialog(this, "Ya eres un usuario Premium.", "Información",
					JOptionPane.INFORMATION_MESSAGE);
			dispose();
			return;
		}

		// Aquí iría la lógica de pago real, ahora solo simulamos
		int opcion = JOptionPane.showConfirmDialog(this,
				"¿Confirmar el pago de " + String.format("%.2f", precioActual) + " € para activar Premium?",
				"Confirmar Pago", JOptionPane.YES_NO_OPTION);

		if (opcion == JOptionPane.YES_OPTION) {
			// Llamar al controlador para activar Premium
			boolean exito = AppChat.getUnicaInstancia().activarPremium();

			if (exito) {
				JOptionPane.showMessageDialog(this, "¡Felicidades! Ahora eres un usuario Premium.", "Premium Activado",
						JOptionPane.INFORMATION_MESSAGE);

				// Cerrar la ventana
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Ha ocurrido un error al activar Premium. Inténtalo más tarde.",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}