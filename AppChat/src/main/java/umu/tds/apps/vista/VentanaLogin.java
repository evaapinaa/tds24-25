package umu.tds.apps.vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import java.awt.Font;


import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import java.awt.Color;
import javax.swing.JTextField;

import javax.swing.border.TitledBorder;

import umu.tds.apps.controlador.AppChat;
import umu.tds.apps.vista.customcomponents.VisualUtils;


import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.border.LineBorder;

import javax.swing.border.CompoundBorder;

import java.awt.Toolkit;
import java.awt.SystemColor;

import java.awt.Cursor;

/**
 * La clase VentanaLogin proporciona la interfaz gráfica para la autenticación de usuarios
 * en la aplicación AppChat. Permite a los usuarios iniciar sesión con su número de teléfono
 * y contraseña, y ofrece la opción de registrarse si aún no tienen una cuenta.
 * <p>
 * Esta ventana es el punto de entrada principal a la aplicación.
 * </p>
 *
 * @author TDS-2025
 * @version 1.0
 */
public class VentanaLogin {

    /**
     * Marco principal de la ventana de login.
     */
	private JFrame frmAppchat;
	
	 /**
     * Campo de texto para ingresar el número de teléfono del usuario.
     */
	private JTextField textField;
	
    /**
     * Campo para ingresar la contraseña del usuario.
     */
	private JPasswordField passwordField;
	
    /**
     * Instancia del controlador de la aplicación.
     */
	private static AppChat controlador;


	  /**
     * Punto de entrada principal para la aplicación.
     * Crea e inicializa la ventana de login.
     *
     * @param args Argumentos de la línea de comando (no utilizados)
     */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaLogin window = new VentanaLogin();
					window.frmAppchat.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	/**
     * Constructor que inicializa el controlador de la aplicación y
     * configura la interfaz gráfica de login.
     */
	public VentanaLogin() {
		controlador = AppChat.getUnicaInstancia();
	    if (controlador == null) {
	        System.err.println("Error: No se pudo inicializar el controlador AppChat.");
	        JOptionPane.showMessageDialog(null, "Error crítico al iniciar la aplicación. Por favor, contacte al soporte.");
	        System.exit(1); // Finaliza la aplicación si no se puede inicializar
	    }
		initialize();
	}
	
	
    /**
     * Hace visible u oculta la ventana de login.
     *
     * @param visible true para hacer visible la ventana, false para ocultarla
     */
	public void setVisible(boolean visible) {
		frmAppchat.setVisible(visible);
	}

    /**
     * Inicializa y configura todos los componentes gráficos de la ventana de login.
     * Establece los listeners para manejar eventos de inicio de sesión y registro.
     */
	private void initialize() {
		frmAppchat = new JFrame();
		frmAppchat.setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaLogin.class.getResource("/umu/tds/apps/resources/icono.png")));
		frmAppchat.setTitle("AppChat");
		frmAppchat.setBounds(100, 100, 565, 376);
		// en medio de la pantalla
		frmAppchat.setLocationRelativeTo(null);
		frmAppchat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel gradientPanel = new VisualUtils.JPanelGradient(new Color(60, 179, 113), new Color(135, 206, 235));
	    gradientPanel.setLayout(new BorderLayout());
	    frmAppchat.setContentPane(gradientPanel);
	    
		JPanel panelSur = new JPanel();
		panelSur.setOpaque(false);

		frmAppchat.getContentPane().add(panelSur, BorderLayout.SOUTH);
		
		JButton btnRegistrar = new JButton("Registrar");
		btnRegistrar.setFocusPainted(false);
		btnRegistrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnRegistrar.setBackground(new Color(0, 128, 128));
		btnRegistrar.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		btnRegistrar.setFont(new Font("Arial", Font.BOLD, 11));
		btnRegistrar.setForeground(new Color(240, 255, 255));
		btnRegistrar.setIcon(null);
		btnRegistrar.setPreferredSize(new Dimension(115, 23));
		panelSur.add(btnRegistrar);
		btnRegistrar.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        VentanaRegistro ventanaRegistro = new VentanaRegistro();
		        
		        ventanaRegistro.setVisible(true);
		        frmAppchat.setVisible(false);
		    }
		});
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLogin.setFocusPainted(false);
		btnLogin.setBackground(new Color(240, 255, 255));
		btnLogin.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		btnLogin.setFont(new Font("Arial", Font.BOLD, 11));
		btnLogin.setIcon(null);
		btnLogin.setPreferredSize(new Dimension(115, 23));
		panelSur.add(btnLogin);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Login
				// Comprobar que el usuario y la contraseña son correctos
				// Si son correctos, mostrar la ventana principal
				
				String usuario = textField.getText();
				char[] contraseña = passwordField.getPassword();
				
				if(controlador.iniciarSesion(usuario, contraseña)) {
					
					// mostrar ventana principal
					VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
					ventanaPrincipal.setVisible(true);
					frmAppchat.setVisible(false);
				} else {
					textField.setBackground(new Color(255, 200, 200));
					passwordField.setBackground(new Color(255, 200, 200));
					Toolkit.getDefaultToolkit().beep(); // Sonido de error
				}
				
				
				
			}
		});
		
		// Enter para pulsar el botón de login
		frmAppchat.getRootPane().setDefaultButton(btnLogin);
		
		JPanel panelCentro = new JPanel();
		panelCentro.setFont(new Font("Arial", Font.BOLD, 11));
		panelCentro.setOpaque(false);
		panelCentro.setBorder(new CompoundBorder(new LineBorder(new Color(245, 245, 245), 3, true), new LineBorder(new Color(0, 128, 128, 150), 2, true)));
		GridBagLayout gbl_panelCentro = new GridBagLayout();
		gbl_panelCentro.columnWidths = new int[]{20, 0, 0, 20, 0};
		gbl_panelCentro.rowHeights = new int[]{20, 0, 0, 20, 0};
		gbl_panelCentro.columnWeights = new double[]{0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panelCentro.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelCentro.setLayout(gbl_panelCentro);
		
		JLabel lblTelefono = new VisualUtils.JLabelWithShadow("Teléfono:", new Color(0, 128, 128, 150));
		lblTelefono.setForeground(Color.WHITE);
		lblTelefono.setFont(new Font("Arial", Font.BOLD, 11));
		GridBagConstraints gbc_lblTelefono = new GridBagConstraints();
		gbc_lblTelefono.anchor = GridBagConstraints.EAST;
		gbc_lblTelefono.insets = new Insets(0, 0, 5, 5);
		gbc_lblTelefono.gridx = 1;
		gbc_lblTelefono.gridy = 1;
		panelCentro.add(lblTelefono, gbc_lblTelefono);
		
		textField = new JTextField();
		textField.setBackground(SystemColor.control);
		textField.setFont(new Font("Arial", Font.PLAIN, 11));
		textField.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textField.setColumns(20);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 1;
		panelCentro.add(textField, gbc_textField);
		
		JLabel lblContrasea = new VisualUtils.JLabelWithShadow("Contraseña:", new Color(0, 128, 128, 150));
		lblContrasea.setForeground(Color.WHITE);
		lblContrasea.setFont(new Font("Arial", Font.BOLD, 11));
		GridBagConstraints gbc_lblContrasea = new GridBagConstraints();
		gbc_lblContrasea.anchor = GridBagConstraints.EAST;
		gbc_lblContrasea.insets = new Insets(0, 0, 5, 5);
		gbc_lblContrasea.gridx = 1;
		gbc_lblContrasea.gridy = 2;
		panelCentro.add(lblContrasea, gbc_lblContrasea);
		
		passwordField = new JPasswordField();
		passwordField.setBackground(SystemColor.control);
		passwordField.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		passwordField.setFont(new Font("Arial", Font.PLAIN, 11));
		passwordField.setColumns(20);
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.gridx = 2;
		gbc_passwordField.gridy = 2;
		panelCentro.add(passwordField, gbc_passwordField);
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		frmAppchat.getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.rowWeights = new double[]{1.0};
		gbl_panel.columnWeights = new double[]{1.0};
		gbl_panel.columnWidths = new int[]{0};
		panel.setLayout(gbl_panel);
		panel.add(panelCentro);
		
		JLabel logo = new JLabel("");
		logo.setPreferredSize(new Dimension(256, 96));
		logo.setHorizontalAlignment(SwingConstants.CENTER);
		logo.setIcon(new ImageIcon(VentanaLogin.class.getResource("/umu/tds/apps/resources/titulo.png")));
		frmAppchat.getContentPane().add(logo, BorderLayout.NORTH);
		
	}
	
	
}