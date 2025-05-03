package umu.tds.apps.vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Component;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JScrollPane;
import com.toedter.calendar.JDateChooser;

import umu.tds.apps.controlador.AppChat;
import umu.tds.apps.vista.customcomponents.VisualUtils;

import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Toolkit;
import java.awt.Cursor;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Clase que representa la ventana de registro de usuarios para la aplicación AppChat.
 * Permite a los usuarios crear una nueva cuenta proporcionando información personal
 * como nombre, apellidos, teléfono, contraseña, fecha de nacimiento, saludo y una 
 * imagen de perfil.
 * 
 * Esta ventana proporciona una interfaz gráfica con campos de entrada para toda la
 * información requerida y opcional del usuario, y botones para completar o cancelar
 * el proceso de registro.
 *
 * @author TDS-2025
 * @version 1.0
 */
public class VentanaRegistro extends JFrame {

	/**
	 * Número de serie utilizado para la serialización.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Panel principal que contiene todos los componentes de la ventana.
	 */
	private JPanel contentPane;
	
	/**
	 * Campo de texto para introducir los apellidos del usuario.
	 */
	private JTextField textField_1;
	
	/**
	 * Campo de texto para introducir el nombre del usuario.
	 */
	private JTextField textField;
	
	/**
	 * Etiqueta para el campo de teléfono.
	 */
	private JLabel lblNewLabel_2;

	/**
	 * Campo de texto para introducir el número de teléfono del usuario.
	 */
	private JTextField textField_2;
	
	/**
	 * Etiqueta para el campo de contraseña.
	 */
	private JLabel lblNewLabel_3;
	
	/**
	 * Campo para introducir la contraseña del usuario.
	 */
	private JPasswordField passwordField;
	
	/**
	 * Etiqueta para el campo de confirmación de contraseña.
	 */
	private JLabel lblNewLabel_4;
	
	/**
	 * Campo para confirmar la contraseña del usuario.
	 */
	private JPasswordField passwordField_1;
	
	/**
	 * Etiqueta para el campo de fecha de nacimiento.
	 */
	private JLabel lblNewLabel_5;
	
	/**
	 * Etiqueta para el campo de saludo personalizado.
	 */
	private JLabel lblNewLabel_6;
	
	/**
	 * Área de texto para introducir un saludo personalizado del usuario.
	 */
	private JTextArea textArea;
	
	/**
	 * Panel para los botones de aceptar y cancelar.
	 */
	private JPanel panel;

	/**
	 * Botón para confirmar el registro del usuario.
	 */
	private JButton btnNewButton;
	
	/**
	 * Botón para cancelar el registro y volver a la ventana de inicio de sesión.
	 */
	private JButton btnNewButton_1;
	
	/**
	 * Espacio horizontal flexible para el diseño de los botones.
	 */
	private Component horizontalGlue;
	
	/**
	 * Etiqueta para el campo de imagen de perfil.
	 */
	private JLabel lblNewLabel_7;
	
	/**
	 * Etiqueta que muestra la imagen de perfil seleccionada por el usuario.
	 */
	private JLabel lblNewLabel_8;
	
	/**
	 * Panel de desplazamiento para contener el área de texto del saludo.
	 */
	private JScrollPane scrollPane;
	
	/**
	 * Selector de fecha para la fecha de nacimiento del usuario.
	 */
	private JDateChooser dateChooser;

	/**
	 * Método principal que inicia la aplicación de registro.
	 * Crea y muestra una nueva instancia de la ventana de registro.
	 * 
	 * @param args argumentos de línea de comandos (no utilizados)
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaRegistro frame = new VentanaRegistro();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Constructor que inicializa la ventana de registro y configura todos sus componentes.
	 * Establece el diseño, añade los campos de entrada, etiquetas y botones necesarios para
	 * el registro de un nuevo usuario.
	 */
	public VentanaRegistro() {
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(VentanaRegistro.class.getResource("/umu/tds/apps/resources/icono.png")));
		setTitle("AppChat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 561, 421);
		setLocationRelativeTo(null);
		contentPane = new VisualUtils.JPanelGradient(new Color(60, 179, 113), new Color(135, 206, 235));
		contentPane.setBorder(null);

		setContentPane(contentPane);

		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 20, 0, 0, 0, 0, 20, 0 };
		gbl_contentPane.rowHeights = new int[] { 20, 0, 0, 0, 0, 0, 0, 0, 0, 20, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0,
				Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblNewLabel = new JLabel("Nombre:");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 13));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 1;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);

		textField = new JTextField();
		textField.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textField.setBackground(new Color(245, 245, 245));
		textField.setFont(new Font("Arial", Font.PLAIN, 13));
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.gridwidth = 3;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 1;
		contentPane.add(textField, gbc_textField);
		textField.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Apellidos:");
		lblNewLabel_1.setFont(new Font("Arial", Font.PLAIN, 13));
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 2;
		contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);

		textField_1 = new JTextField();
		textField_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textField_1.setBackground(new Color(245, 245, 245));
		textField_1.setFont(new Font("Arial", Font.PLAIN, 13));
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.gridwidth = 3;
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 2;
		gbc_textField_1.gridy = 2;
		contentPane.add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);

		lblNewLabel_2 = new JLabel("Teléfono:");
		lblNewLabel_2.setFont(new Font("Arial", Font.PLAIN, 13));
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 1;
		gbc_lblNewLabel_2.gridy = 3;
		contentPane.add(lblNewLabel_2, gbc_lblNewLabel_2);

		textField_2 = new JTextField();
		textField_2.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textField_2.setBackground(new Color(245, 245, 245));
		textField_2.setFont(new Font("Arial", Font.PLAIN, 13));
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 2;
		gbc_textField_2.gridy = 3;
		contentPane.add(textField_2, gbc_textField_2);
		textField_2.setColumns(10);

		lblNewLabel_3 = new JLabel("Contraseña:");
		lblNewLabel_3.setFont(new Font("Arial", Font.PLAIN, 13));
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 1;
		gbc_lblNewLabel_3.gridy = 4;
		contentPane.add(lblNewLabel_3, gbc_lblNewLabel_3);

		passwordField = new JPasswordField();
		passwordField.setBackground(new Color(245, 245, 245));
		passwordField.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		passwordField.setFont(new Font("Arial", Font.PLAIN, 13));
		passwordField.setColumns(10);
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 2;
		gbc_passwordField.gridy = 4;
		contentPane.add(passwordField, gbc_passwordField);

		lblNewLabel_4 = new JLabel("Contraseña:");
		lblNewLabel_4.setFont(new Font("Arial", Font.PLAIN, 13));
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 3;
		gbc_lblNewLabel_4.gridy = 4;
		contentPane.add(lblNewLabel_4, gbc_lblNewLabel_4);

		passwordField_1 = new JPasswordField();
		passwordField_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		passwordField_1.setBackground(new Color(245, 245, 245));
		passwordField_1.setFont(new Font("Arial", Font.PLAIN, 13));
		passwordField_1.setColumns(10);
		GridBagConstraints gbc_passwordField_1 = new GridBagConstraints();
		gbc_passwordField_1.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField_1.gridx = 4;
		gbc_passwordField_1.gridy = 4;
		contentPane.add(passwordField_1, gbc_passwordField_1);

		lblNewLabel_5 = new JLabel("Fecha:");
		lblNewLabel_5.setFont(new Font("Arial", Font.PLAIN, 13));
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.gridx = 1;
		gbc_lblNewLabel_5.gridy = 5;
		contentPane.add(lblNewLabel_5, gbc_lblNewLabel_5);

		dateChooser = new JDateChooser();
		dateChooser.getCalendarButton().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		dateChooser.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		dateChooser.setBackground(new Color(245, 245, 245));
		dateChooser.setFont(new Font("Arial", Font.PLAIN, 13));
		GridBagConstraints gbc_dateChooser = new GridBagConstraints();
		gbc_dateChooser.insets = new Insets(0, 0, 5, 5);
		gbc_dateChooser.fill = GridBagConstraints.BOTH;
		gbc_dateChooser.gridx = 2;
		gbc_dateChooser.gridy = 5;
		contentPane.add(dateChooser, gbc_dateChooser);

		lblNewLabel_6 = new JLabel("Saludo:");
		lblNewLabel_6.setFont(new Font("Arial", Font.PLAIN, 13));
		GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
		gbc_lblNewLabel_6.gridheight = 2;
		gbc_lblNewLabel_6.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_6.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_6.gridx = 1;
		gbc_lblNewLabel_6.gridy = 6;
		contentPane.add(lblNewLabel_6, gbc_lblNewLabel_6);

		lblNewLabel_7 = new JLabel("Imagen:");
		lblNewLabel_7.setFont(new Font("Arial", Font.PLAIN, 13));
		GridBagConstraints gbc_lblNewLabel_7 = new GridBagConstraints();
		gbc_lblNewLabel_7.gridheight = 2;
		gbc_lblNewLabel_7.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_7.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_7.gridx = 3;
		gbc_lblNewLabel_7.gridy = 6;
		contentPane.add(lblNewLabel_7, gbc_lblNewLabel_7);

		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 2;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 2;
		gbc_scrollPane.gridy = 6;
		contentPane.add(scrollPane, gbc_scrollPane);

		textArea = new JTextArea();
		textArea.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textArea.setBackground(new Color(245, 245, 245));
		textArea.setFont(new Font("Arial", Font.PLAIN, 13));
		scrollPane.setViewportView(textArea);
		textArea.setColumns(10);

		lblNewLabel_8 = new JLabel("");
		lblNewLabel_8.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		ImageIcon defaultIcon = new ImageIcon(VentanaRegistro.class.getResource("/umu/tds/apps/resources/usuario.png"));
		defaultIcon.setDescription("/umu/tds/apps/resources/usuario.png");  // Asigna manualmente la cadena deseada
		lblNewLabel_8.setIcon(defaultIcon);
		GridBagConstraints gbc_lblNewLabel_8 = new GridBagConstraints();
		gbc_lblNewLabel_8.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel_8.gridheight = 2;
		gbc_lblNewLabel_8.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_8.gridx = 4;
		gbc_lblNewLabel_8.gridy = 6;
		contentPane.add(lblNewLabel_8, gbc_lblNewLabel_8);
		lblNewLabel_8.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				seleccionarFotoPerfil();
			}
		});

		panel = new JPanel();
		panel.setFont(new Font("Arial", Font.PLAIN, 13));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		panel.setOpaque(false);
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 2;
		gbc_panel.gridy = 8;
		contentPane.add(panel, gbc_panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		btnNewButton_1 = new JButton("Cancelar");
		btnNewButton_1.setFocusPainted(false);
		btnNewButton_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton_1.setBackground(new Color(245, 245, 245));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					VentanaLogin login = new VentanaLogin();
					login.setVisible(true);
					dispose();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(contentPane, "No se pudo abrir la ventana de inicio de sesión.",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnNewButton_1.setFont(new Font("Arial", Font.PLAIN, 13));
		panel.add(btnNewButton_1);

		horizontalGlue = Box.createHorizontalGlue();
		panel.add(horizontalGlue);

		btnNewButton = new JButton("Aceptar");
		btnNewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton.setFocusPainted(false);
		btnNewButton.setBackground(new Color(0, 128, 128));
		btnNewButton.setFont(new Font("Arial", Font.BOLD, 13));
		panel.add(btnNewButton);
		// registrar
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nombre = textField.getText();
				String telefono = textField_2.getText();
				String contraseña = String.valueOf(passwordField.getPassword());

				if (nombre.isEmpty() || telefono.isEmpty() || contraseña.isEmpty()) {
					JOptionPane.showMessageDialog(contentPane, "Por favor, complete todos los campos.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (!contraseñasCoinciden()) {
					JOptionPane.showMessageDialog(contentPane, "Las contraseñas no coinciden.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				boolean resultado = false;
				try {
					AppChat controlador = AppChat.getUnicaInstancia();
					String rutaImagen = ((ImageIcon) lblNewLabel_8.getIcon()).getDescription();
					resultado = controlador.registrarUsuario(nombre, contraseña.toCharArray(), telefono, "",
							Optional.ofNullable(textArea.getText()), new ImageIcon(rutaImagen),
							dateChooser.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate(),
							LocalDate.now());
					
					System.out.println("Ruta de la imagen seleccionada: " + rutaImagen);
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(contentPane, "Error crítico al registrar el usuario.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (resultado) {
					JOptionPane.showMessageDialog(contentPane, "Usuario registrado correctamente.", "Registro",
							JOptionPane.INFORMATION_MESSAGE);
					setVisible(false);
					new VentanaLogin().setVisible(true);
				} else {
					JOptionPane.showMessageDialog(contentPane, "El número de teléfono indicado ya está registrado",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

	}
	
	
	
	/**
	 * Permite al usuario seleccionar una imagen de perfil ya sea desde una URL o desde un
	 * archivo local en el sistema.
	 * Muestra un diálogo para que el usuario elija el método de carga de la imagen, procesa
	 * la imagen seleccionada y la muestra en la interfaz.
	 */
	private void seleccionarFotoPerfil() {
	    // Opciones para URL o archivo local
	    String[] opciones = { "Introducir enlace", "Seleccionar archivo" };
	    int seleccion = JOptionPane.showOptionDialog(contentPane, "Seleccione cómo desea cargar la imagen:",
	            "Cargar Imagen", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones,
	            opciones[0]);

	    if (seleccion == 0) {
	        // Cargar imagen desde URL
	        String urlImagen = JOptionPane.showInputDialog(contentPane, "Introduzca el enlace de la imagen:",
	                "Cargar Imagen desde URL", JOptionPane.PLAIN_MESSAGE);

	        if (urlImagen != null && !urlImagen.isEmpty()) {
	            try {
	                @SuppressWarnings("deprecation")
					BufferedImage imagen = ImageIO.read(new URL(urlImagen));
	                ImageIcon icono = new ImageIcon(imagen.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
	                icono = VisualUtils.createCircularIcon(imagen, 100);
	                // Limpiar el prefijo "file:" en caso de que lo tenga (por ejemplo, si se hubiese copiado una URL local)
	                String descripcion = urlImagen;
	                if(descripcion.startsWith("file:")){
	                    descripcion = descripcion.substring(5);
	                }
	                icono.setDescription(descripcion); // Guardar la URL (limpia) como descripción
	                lblNewLabel_8.setIcon(icono);
	            } catch (IOException ex) {
	                JOptionPane.showMessageDialog(contentPane, "No se pudo cargar la imagen desde el enlace.", "Error",
	                        JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    } else if (seleccion == 1) {
	        // Cargar imagen desde archivo local
	        JFileChooser fileChooser = new JFileChooser();
	        fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png", "gif"));
	        int resultado = fileChooser.showOpenDialog(contentPane);
	        if (resultado == JFileChooser.APPROVE_OPTION) {
	            try {
	                BufferedImage imagen = ImageIO.read(fileChooser.getSelectedFile());
	                ImageIcon icono = new ImageIcon(imagen.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
	                icono = VisualUtils.createCircularIcon(imagen, 100);
	                // Obtenemos la ruta completa
	                String ruta = fileChooser.getSelectedFile().getAbsolutePath();
	                // Si por alguna razón la cadena empieza por "file:" (normalmente no ocurre con getAbsolutePath())
	                if(ruta.startsWith("file:")){
	                    ruta = ruta.substring(5);
	                }
	                icono.setDescription(ruta); // Guardar la ruta local (limpia) como descripción
	                lblNewLabel_8.setIcon(icono);
	            } catch (IOException ex) {
	                JOptionPane.showMessageDialog(contentPane, "No se pudo cargar la imagen desde el archivo.", "Error",
	                        JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    }
	}

	
	/**
	 * Verifica si las contraseñas introducidas por el usuario coinciden.
	 * Si no coinciden, cambia el fondo de los campos de contraseña a un color rojo claro
	 * para indicar el error.
	 * 
	 * @return true si las contraseñas coinciden, false en caso contrario
	 */
	private boolean contraseñasCoinciden() {
		@SuppressWarnings("deprecation")
		boolean coinciden = passwordField.getText().equals(passwordField_1.getText());

		if (!coinciden) {
			passwordField.setBackground(new Color(255, 200, 200));
			passwordField_1.setBackground(new Color(255, 200, 200));
		}

		return coinciden;
	}

}
