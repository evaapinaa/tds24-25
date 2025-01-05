package umu.tds.apps.vista;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import umu.tds.apps.controlador.AppChat;
import umu.tds.apps.modelo.Chat;
import umu.tds.apps.modelo.ContactoIndividual;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.Usuario;
import umu.tds.apps.vista.customcomponents.VisualUtils;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import java.awt.Component;
import javax.swing.Box;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;
import javax.swing.AbstractListModel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import tds.BubbleText;

import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import java.awt.Dimension;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import java.awt.Cursor;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.swing.DefaultComboBoxModel;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	// Panel (centro) donde iremos poniendo o bien “bienvenida” o bien “panel de
	// chat”
	private JPanel panelCentro;

	// Panel de bienvenida (se muestra al inicio)
	private JPanel panelBienvenida;

	// Panel completo de chat (fondo degradado + envío de mensajes)
	private JPanel panelChat;

	// Panel interno dentro del chat donde se añaden las burbujas
	private JPanel panelChatContenido;

	private JTextField txtMensaje;

	private Usuario receptorActual;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				VentanaPrincipal frame = new VentanaPrincipal();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaPrincipal() {

		setTitle("AppChat");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/icono.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 633);

		contentPane = new JPanel();
		contentPane.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, new Color(176, 196, 222), null));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		// Panel superior con la barra de búsqueda y botones
		JPanel panelNorte = new JPanel();
		panelNorte.setBackground(new Color(0, 107, 107));
		panelNorte.setBorder(new CompoundBorder(
				new TitledBorder(
						new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "",
						TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)),
				new LineBorder(new Color(0, 107, 107), 8, true)));
		contentPane.add(panelNorte, BorderLayout.NORTH);
		panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.X_AXIS));

		JComboBox<String> comboBoxContactos = new JComboBox<>();
		comboBoxContactos.setModel(new DefaultComboBoxModel<String>(new String[] { "Contacto", "Teléfono" }));

		comboBoxContactos.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		panelNorte.add(comboBoxContactos);

		// Botones de la barra superior
		JButton btnNewButton = new JButton("");
		btnNewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton.setFocusPainted(false);
		btnNewButton.setBackground(new Color(245, 245, 245));
		btnNewButton
				.setBorder(new CompoundBorder(new TitledBorder(
						new TitledBorder(
								new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255),
										new Color(160, 160, 160)),
								"", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)),
						"", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)),
						new EmptyBorder(5, 17, 5, 17)));
		btnNewButton.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/enviar.png")));
		btnNewButton.addActionListener(e -> {
			String seleccion = (String) comboBoxContactos.getSelectedItem();

			if ("Contacto".equals(seleccion)) {
				// Abrir chat con un contacto de la lista
				String nombreContacto = JOptionPane.showInputDialog("Introduce el nombre del contacto:");
				if (nombreContacto != null && !nombreContacto.trim().isEmpty()) {
					Usuario contacto = AppChat.getUnicaInstancia().obtenerUsuarioPorNombre(nombreContacto);
					if (contacto != null) {
						cargarChat(contacto);
					} else {
						JOptionPane.showMessageDialog(this, "Contacto no encontrado.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			} else if ("Teléfono".equals(seleccion)) {
				// Abrir chat con un usuario por número de teléfono
				String telefono = JOptionPane.showInputDialog("Introduce el número de teléfono:");
				if (telefono != null && !telefono.trim().isEmpty()) {
					Usuario usuario = AppChat.getUnicaInstancia().obtenerUsuarioPorTelefono(telefono);
					if (usuario != null) {
						cargarChat(usuario);
					} else {
						JOptionPane.showMessageDialog(this, "Usuario no encontrado.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		panelNorte.add(rigidArea);
		panelNorte.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton_1.setFocusPainted(false);
		btnNewButton_1.setBackground(new Color(245, 245, 245));
		btnNewButton_1
				.setBorder(new CompoundBorder(new TitledBorder(
						new TitledBorder(
								new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255),
										new Color(160, 160, 160)),
								"", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)),
						"", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)),
						new EmptyBorder(5, 17, 5, 17)));
		btnNewButton_1.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/lupa.png")));
		btnNewButton_1.addActionListener(e -> {
			// BUSCAR
			VentanaBusqueda ventanaBusqueda = new VentanaBusqueda();
			ventanaBusqueda.setVisible(true);
		});

		Component rigidArea_1_1 = Box.createRigidArea(new Dimension(20, 20));
		panelNorte.add(rigidArea_1_1);
		panelNorte.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton(" Contactos ");
		btnNewButton_2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton_2.setFocusPainted(false);
		btnNewButton_2.setBackground(new Color(245, 245, 245));
		btnNewButton_2
				.setBorder(new CompoundBorder(new TitledBorder(
						new TitledBorder(
								new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255),
										new Color(160, 160, 160)),
								"", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)),
						"", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)),
						new EmptyBorder(5, 17, 5, 17)));
		btnNewButton_2.setFont(new Font("Arial", Font.PLAIN, 11));
		btnNewButton_2
				.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/personas.png")));
		btnNewButton_2.addActionListener(e -> {
			// Crear el marco que contendrá la ventana de contactos
			JFrame frameContactos = new JFrame("Ventana de Contactos");
			frameContactos.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frameContactos.setSize(600, 400);

			// Lista de contactos inicial o vacía
			List<String> contactosIniciales = new ArrayList<>();
			contactosIniciales.add("Contacto 1"); // EJEMPLO

			// Crear el panel VentanaContactos y añadir al frame
			VentanaContactos ventanaContactos = new VentanaContactos();
			frameContactos.getContentPane().add(ventanaContactos);
			frameContactos.setVisible(true);
		});
		panelNorte.add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton(" Premium ");
		btnNewButton_3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton_3.setFocusPainted(false);
		btnNewButton_3.setBackground(new Color(245, 245, 245));
		btnNewButton_3
				.setBorder(new CompoundBorder(new TitledBorder(
						new TitledBorder(
								new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255),
										new Color(160, 160, 160)),
								"", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)),
						"", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)),
						new EmptyBorder(5, 17, 5, 17)));
		btnNewButton_3.setFont(new Font("Arial", Font.PLAIN, 11));
		btnNewButton_3
				.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/garantia.png")));
		btnNewButton_3.addActionListener(e -> {
			// Abre la ventana de premium
			VentanaPremium ventanaPremium = new VentanaPremium();
			ventanaPremium.setVisible(true);
		});
		panelNorte.add(btnNewButton_3);

		// Usuario actual en la esquina derecha
		Usuario usuarioActual = AppChat.getUsuarioActual();

		Component horizontalGlue = Box.createHorizontalGlue();
		panelNorte.add(horizontalGlue);

		JLabel lblNewLabel = new JLabel(usuarioActual.getUsuario());
		lblNewLabel.setForeground(new Color(248, 248, 255));
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 11));
		lblNewLabel.setIcon(null);
		panelNorte.add(lblNewLabel);

		Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		panelNorte.add(rigidArea_1);

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		String path = usuarioActual.getImagenPerfil().getDescription();
		cargarImagenPerfil(lblNewLabel_1, path);
		panelNorte.add(lblNewLabel_1);

		lblNewLabel_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				seleccionarFotoPerfil(lblNewLabel_1);
			}
		});

		// Para la lista de chats recientes
		JPanel panelMensajes = new JPanel(new BorderLayout());
		contentPane.add(panelMensajes, BorderLayout.WEST);

		JList<Chat> list = new JList<>();
		list.setSelectionBackground(new Color(102, 205, 170));
		list.setBackground(new Color(205, 235, 234));
		list.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		list.setFont(new Font("Arial", Font.PLAIN, 11));
		list.setFixedCellHeight(120); // Altura fija para cada celda
		list.setFixedCellWidth(200); // Ancho fijo para cada celda
		list.setCellRenderer(new ChatCellRenderer());

		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		scrollPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelMensajes.add(scrollPane, BorderLayout.WEST);

		
		
		panelCentro = new JPanel(new BorderLayout());
		contentPane.add(panelCentro, BorderLayout.CENTER);

		// Panel de bienvenida (se ve al inicio)
		panelBienvenida = new JPanel();
		panelBienvenida.setBackground(new Color(255, 255, 255));
		panelBienvenida.setLayout(new GridBagLayout()); // Usamos GridBagLayout para adaptabilidad

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0; // Para que ocupe todo el espacio horizontal disponible
		gbc.weighty = 1.0; // Para que ocupe todo el espacio vertical disponible
		gbc.anchor = GridBagConstraints.CENTER; // Centra los elementos en el medio
		gbc.fill = GridBagConstraints.NONE; // No estira los componentes

		// Etiqueta para el GIF
		JLabel gifLabel = new JLabel();
		gifLabel.setHorizontalAlignment(SwingConstants.CENTER);
		gifLabel.setVerticalAlignment(SwingConstants.CENTER);

		// Usar un nuevo GridBagConstraints para gifLabel
		GridBagConstraints gbcGifLabel = new GridBagConstraints();
		gbcGifLabel.gridx = 0;
		gbcGifLabel.gridy = 0;
		gbcGifLabel.weightx = 1.0; // Para que ocupe todo el espacio horizontal disponible
		gbcGifLabel.weighty = 1.0; // Para que ocupe todo el espacio vertical disponible
		gbcGifLabel.anchor = GridBagConstraints.CENTER; // Centra los elementos en el medio
		gbcGifLabel.fill = GridBagConstraints.NONE; // No estira los componentes

		panelBienvenida.add(gifLabel, gbcGifLabel); // Añadimos el logo al panel

		// Configuración del texto debajo del GIF
		JLabel lblBienvenida = new JLabel("Agregue o seleccione un chat para comenzar a hablar");
		lblBienvenida.setFont(new Font("Arial", Font.BOLD, 18));
		lblBienvenida.setForeground(new Color(34, 112, 147));
		lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER); // Alineación horizontal
		lblBienvenida.setVerticalAlignment(SwingConstants.CENTER); // Alineación vertical

		// Usar un nuevo GridBagConstraints para lblBienvenida
		GridBagConstraints gbcLblBienvenida = new GridBagConstraints();
		gbcLblBienvenida.gridx = 0;
		gbcLblBienvenida.gridy = 1; // Colocar debajo del GIF
		gbcLblBienvenida.weightx = 1.0; // Expandir horizontalmente
		gbcLblBienvenida.weighty = 0.2; // Ajustar peso verticalmente
		gbcLblBienvenida.insets = new Insets(20, 0, 0, 0); // Margen superior para separarlo del GIF
		gbcLblBienvenida.anchor = GridBagConstraints.CENTER; // Centrar el componente
		gbcLblBienvenida.fill = GridBagConstraints.HORIZONTAL; // Hacerlo ocupar todo el ancho

		panelBienvenida.add(lblBienvenida, gbcLblBienvenida);

		// Configurar el tamaño dinámico del GIF
		ImageIcon gifIcon = new ImageIcon(getClass().getResource("/umu/tds/apps/resources/revision-de-chat.gif"));
		panelBienvenida.addComponentListener(new java.awt.event.ComponentAdapter() {
			@Override
			public void componentResized(java.awt.event.ComponentEvent e) {
				// Obtener el tamaño del panel
				Dimension size = panelBienvenida.getSize();
				int scaledWidth = (int) (size.width * 0.4); // GIF será el 40% del ancho del panel
				int scaledHeight = (int) (scaledWidth * gifIcon.getIconHeight() / gifIcon.getIconWidth()); // Mantener
																											// proporción

				// Escalar el GIF
				Image scaledImage = gifIcon.getImage().getScaledInstance(scaledWidth, scaledHeight,
						Image.SCALE_DEFAULT);
				gifLabel.setIcon(new ImageIcon(scaledImage));
			}
		});

		// Añadir el panelBienvenida al panelCentro
		panelCentro.add(panelBienvenida, BorderLayout.CENTER);

		// Panel de chat (lo creamos, pero NO se añade aún a “panelCentro”)
		panelChat = new JPanel(new BorderLayout());
		panelChat.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));

		// Scroll del panel de chat
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_1.setBorder(null);

		// Auto-scroll al final del chat
		Adjustable vertical = scrollPane_1.getVerticalScrollBar();
		final boolean[] autoScrollEnabled = { true };
		vertical.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				if (autoScrollEnabled[0]) {
					vertical.setValue(vertical.getMaximum());
				}
			}
		});
		// Si el usuario ajusta manualmente el scroll, lo desactivamos
		vertical.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				if (e.getValueIsAdjusting()) {
					autoScrollEnabled[0] = false;
				} else {
					if (vertical.getValue() + vertical.getVisibleAmount() >= vertical.getMaximum()) {
						autoScrollEnabled[0] = true;
					}
				}
			}
		});

		panelChat.add(scrollPane_1, BorderLayout.CENTER);

		// Panel con fondo degradado donde ponemos las burbujas
		panelChatContenido = new VisualUtils.JPanelGradient(new Color(0, 128, 128), new Color(172, 225, 175));
		panelChatContenido.setLayout(new BoxLayout(panelChatContenido, BoxLayout.Y_AXIS));
		scrollPane_1.setViewportView(panelChatContenido);

		// Panel de envío en la parte Sur
		JPanel panelEnvio = new JPanel();
		panelEnvio.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelEnvio.setBackground(new Color(95, 158, 160));
		panelChat.add(panelEnvio, BorderLayout.SOUTH);

		GridBagLayout gbl_panelEnvio = new GridBagLayout();
		gbl_panelEnvio.columnWidths = new int[] { 5, 0, 0, 2, 197, 43, 2, 0 };
		gbl_panelEnvio.rowHeights = new int[] { 5, 21, 0, 0 };
		gbl_panelEnvio.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panelEnvio.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panelEnvio.setLayout(gbl_panelEnvio);

		// Botón para mostrar emojis
		JButton btnEmojis = new JButton("");
		btnEmojis.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnEmojis.setFocusPainted(false);
		btnEmojis.setBackground(new Color(240, 255, 240));
		btnEmojis.setBorder(new CompoundBorder(new TitledBorder(
						new TitledBorder(
								new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255),
										new Color(160, 160, 160)),
								"", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)),
						"", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)),
						new EmptyBorder(3, 10, 3, 10)));
		btnEmojis.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/feliz.png")));
		GridBagConstraints gbc_btnEmojis = new GridBagConstraints();
		gbc_btnEmojis.insets = new Insets(0, 0, 5, 5);
		gbc_btnEmojis.gridx = 1;
		gbc_btnEmojis.gridy = 1;
		panelEnvio.add(btnEmojis, gbc_btnEmojis);

		// Panel flotante de Emojis
		JLayeredPane layeredPane = getLayeredPane();
		JPanel panelEmojis = new EmojiPanel(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        int numEmoji = Integer.parseInt(e.getActionCommand());
		        // Enviar el emoji utilizando el método enviarEmoji
		        boolean enviado = AppChat.getUnicaInstancia().enviarEmoji(AppChat.getUsuarioActual(), receptorActual, numEmoji);
		        if (enviado) {
		            // Insertar la burbuja de emoji en el chat
		            pintarBubblesEmoji(panelChatContenido, numEmoji, true, "Yo");
		            // Actualizar la lista de conversaciones recientes
		            actualizarListaChats(list);
		        } else {
		            JOptionPane.showMessageDialog(VentanaPrincipal.this, "No se pudo enviar el emoji.", "Error",
		                    JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});
		layeredPane.add(panelEmojis, JLayeredPane.POPUP_LAYER);

		// Al pulsar el botón, mostramos/ocultamos panelEmojis
		btnEmojis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!panelEmojis.isVisible()) {
					Point location = btnEmojis.getLocationOnScreen();
					SwingUtilities.convertPointFromScreen(location, layeredPane);
					panelEmojis.setLocation(location.x, location.y - panelEmojis.getHeight());
					panelEmojis.setVisible(true);
				} else {
					panelEmojis.setVisible(false);
				}
			}
		});

		// Campo de texto para enviar mensaje
		txtMensaje = new JTextField();
		txtMensaje.setToolTipText("Mensaje");
		txtMensaje.setFont(new Font("Arial", Font.PLAIN, 18));
		txtMensaje.setBackground(new Color(245, 245, 245));
		txtMensaje.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_txtMensaje = new GridBagConstraints();
		gbc_txtMensaje.gridwidth = 3;
		gbc_txtMensaje.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMensaje.insets = new Insets(0, 0, 5, 5);
		gbc_txtMensaje.gridx = 2;
		gbc_txtMensaje.gridy = 1;
		panelEnvio.add(txtMensaje, gbc_txtMensaje);
		txtMensaje.setColumns(10);

		// Botón “Enviar”
		JButton btnEnviar = new JButton("Enviar ");
		btnEnviar.setForeground(new Color(0, 128, 128));
		btnEnviar.setIcon(
				new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/enviar-mensaje.png")));
		btnEnviar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnEnviar.setBackground(new Color(240, 255, 240));
		btnEnviar
				.setBorder(new CompoundBorder(new TitledBorder(
						new TitledBorder(
								new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255),
										new Color(160, 160, 160)),
								"", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)),
						"", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)),
						new EmptyBorder(3, 16, 3, 16)));
		btnEnviar.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 12));
		btnEnviar.setFocusPainted(false);

		btnEnviar.addActionListener(e -> {
			String textoMensaje = txtMensaje.getText().trim();
			if (!textoMensaje.isEmpty()) {
				Usuario receptor = receptorActual; // Método para obtener el usuario del chat actual
				if (receptor != null) {
					// Crear y persistir mensaje
					AppChat.getUnicaInstancia().enviarMensaje(AppChat.getUsuarioActual(), receptor, textoMensaje);
					// Actualizar el panel de chat
					BubbleText burbuja = new BubbleText(panelChatContenido, textoMensaje, Color.GREEN, "Yo",
							BubbleText.SENT, 18);
					panelChatContenido.add(burbuja);
					panelChatContenido.revalidate();
					panelChatContenido.repaint();

					// Actualizar la lista de conversaciones recientes
					actualizarListaChats(list);

					// Limpiar campo de texto
					txtMensaje.setText("");
				} else {
					JOptionPane.showMessageDialog(this, "Error al identificar el receptor del mensaje.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// Enviar con ENTER
		txtMensaje.addActionListener(e -> btnEnviar.doClick());

		GridBagConstraints gbc_btnEnviar = new GridBagConstraints();
		gbc_btnEnviar.insets = new Insets(0, 0, 5, 5);
		gbc_btnEnviar.gridx = 5;
		gbc_btnEnviar.gridy = 1;
		panelEnvio.add(btnEnviar, gbc_btnEnviar);

		// Seleccionar mensaje de la lista de chats
		list.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				Chat chatSeleccionado = list.getSelectedValue();
				if (chatSeleccionado == null)
					return;
				cargarChat(chatSeleccionado);
			}
		});

		// Listener de Mouse para detectar clics en el botón '+'
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = list.locationToIndex(e.getPoint());
				if (index >= 0) {
					Rectangle cellBounds = list.getCellBounds(index, index);
					if (cellBounds != null && cellBounds.contains(e.getPoint())) {
						int relativeX = e.getX() - cellBounds.x;
						int relativeY = e.getY() - cellBounds.y;

						// Definir el área del botón '+' (últimos 30 píxeles de la celda)
						int cellWidth = list.getWidth();
						int cellHeight = cellBounds.height;
						Rectangle addButtonArea = new Rectangle(cellWidth - 30, 0, 30, cellHeight);

						if (addButtonArea.contains(relativeX, relativeY)) {
							// El clic fue en el botón '+'
							Chat chat = list.getModel().getElementAt(index);

							// Obtener el otro usuario del chat
							Usuario usuarioActual = AppChat.getUsuarioActual();
							Usuario usuarioOtro = (chat.getUsuario().equals(usuarioActual)) ? chat.getOtroUsuarioChat()
									: chat.getUsuario();

							// Verificar si ya está en contactos
							ContactoIndividual contactoExistente = AppChat.getUnicaInstancia()
									.obtenerContactoPorTelefono(usuarioOtro.getTelefono());
							if (contactoExistente != null) {
								JOptionPane.showMessageDialog(VentanaPrincipal.this,
										"Este usuario ya está en tus contactos.", "Información",
										JOptionPane.INFORMATION_MESSAGE);
								return;
							}

							// Solicitar el nombre para el contacto
							String nombreContacto = JOptionPane.showInputDialog(VentanaPrincipal.this,
									"Introduce el nombre del contacto:", "Agregar Contacto", JOptionPane.PLAIN_MESSAGE);
							if (nombreContacto != null && !nombreContacto.trim().isEmpty()) {
								boolean agregado = AppChat.getUnicaInstancia().añadirContacto(nombreContacto,
										usuarioOtro.getTelefono());
								if (agregado) {
									actualizarListaChats(list);
									JOptionPane.showMessageDialog(VentanaPrincipal.this,
											"Contacto agregado exitosamente.", "Éxito",
											JOptionPane.INFORMATION_MESSAGE);
								} else {
									JOptionPane.showMessageDialog(VentanaPrincipal.this,
											"No se pudo agregar el contacto.", "Error", JOptionPane.ERROR_MESSAGE);
								}
							}
						}
					}
				}
			}
		});
		// al iniciar, cargamos tambien la lista de chats
		actualizarListaChats(list);

	}

	/**
	 * Ejemplo de método para cargar imagen de perfil circular en un JLabel.
	 */
	@SuppressWarnings("deprecation")
	private void cargarImagenPerfil(JLabel label, String path) {
		if (path == null || path.isEmpty()) {
			ImageIcon iconoBase = new ImageIcon(getClass().getResource("/umu/tds/apps/resources/usuario.png"));
			Image imagenEscalada = iconoBase.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			label.setIcon(new ImageIcon(imagenEscalada));
			return;
		}
		try {
			BufferedImage imagen;
			if (path.startsWith("http")) {
				imagen = ImageIO.read(new URL(path));
			} else {
				imagen = ImageIO.read(new File(path));
			}
			if (imagen == null) {
				throw new IOException("La imagen devuelta es nula");
			}
			// Redondeamos a forma circular
			ImageIcon icon = VisualUtils.createCircularIcon(imagen, 50);
			label.setIcon(icon);
		} catch (IOException e) {
			ImageIcon iconoBase = new ImageIcon(getClass().getResource("/umu/tds/apps/resources/usuario.png"));
			Image imagenEscalada = iconoBase.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			label.setIcon(new ImageIcon(imagenEscalada));

		}
	}

	@SuppressWarnings("deprecation")
	private void seleccionarFotoPerfil(JLabel label) {
		String[] opciones = { "Introducir enlace", "Seleccionar archivo" };
		int seleccion = JOptionPane.showOptionDialog(contentPane, "Seleccione cómo desea cargar la imagen:",
				"Cargar Imagen", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones,
				opciones[0]);

		if (seleccion == 0) {
			// Desde URL
			String urlImagen = JOptionPane.showInputDialog(contentPane, "Introduzca el enlace de la imagen:",
					"Cargar Imagen desde URL", JOptionPane.PLAIN_MESSAGE);

			if (urlImagen != null && !urlImagen.isEmpty()) {
				try {
					BufferedImage imagen = ImageIO.read(new URL(urlImagen));
					ImageIcon icono = new ImageIcon(imagen.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
					icono.setDescription(urlImagen);

					// Cambiar modelo
					AppChat.getUnicaInstancia().cambiarImagenPerfil(icono);

					// Actualizar JLabel
					label.setIcon(VisualUtils.createCircularIcon(imagen, 50));
					label.revalidate();
					label.repaint();

					JOptionPane.showMessageDialog(contentPane, "Foto de perfil actualizada correctamente.");
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(contentPane, "No se pudo cargar la imagen desde el enlace.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} else if (seleccion == 1) {
			// Desde archivo local
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png", "gif"));
			int resultado = fileChooser.showOpenDialog(contentPane);
			if (resultado == JFileChooser.APPROVE_OPTION) {
				try {
					File archivo = fileChooser.getSelectedFile();
					BufferedImage imagen = ImageIO.read(archivo);
					ImageIcon icono = new ImageIcon(imagen.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
					icono.setDescription(archivo.getAbsolutePath());

					// Cambiar modelo
					AppChat.getUnicaInstancia().cambiarImagenPerfil(icono);

					// Actualizar JLabel
					label.setIcon(VisualUtils.createCircularIcon(imagen, 50));
					label.revalidate();
					label.repaint();

					JOptionPane.showMessageDialog(contentPane, "Foto de perfil actualizada correctamente.");
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(contentPane, "No se pudo cargar la imagen desde el archivo.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	private void cargarChat(Usuario contacto) {
	    receptorActual = contacto;
	    panelChatContenido.removeAll(); // Limpiar contenido del chat

	    // Verificar si ya existe un chat entre el usuario actual y el contacto
	    Chat chatExistente = AppChat.getUsuarioActual().obtenerChatCon(contacto);

	    if (chatExistente != null && !chatExistente.getMensajes().isEmpty()) {
	        // Cargar los mensajes existentes en el chat
	        for (Mensaje mensaje : chatExistente.getMensajes()) {
	            boolean esMio = mensaje.getEmisor().equals(AppChat.getUsuarioActual());
	            String nombre = esMio ? "Yo" : contacto.getUsuario();
	            pintarBubblesMensaje(panelChatContenido, mensaje, esMio, nombre);
	        }
	    } else {
	        // Si no hay chat o mensajes, mostrar mensaje predeterminado
	        JLabel noMessagesLabel = new JLabel("No hay mensajes con este contacto.");
	        noMessagesLabel.setHorizontalAlignment(SwingConstants.CENTER);
	        noMessagesLabel.setForeground(Color.GRAY);
	        noMessagesLabel.setFont(new Font("Arial", Font.ITALIC, 14));
	        panelChatContenido.add(noMessagesLabel);
	    }

	    refrescarChat(); // Actualizar el panel del chat
	}


	private void cargarChat(Chat chatSeleccionado) {
	    Usuario usuarioActual = AppChat.getUsuarioActual();
	    Usuario usuario1 = chatSeleccionado.getUsuario();
	    Usuario usuario2 = chatSeleccionado.getOtroUsuarioChat();
	    Usuario receptor;

	    // Determinar el receptor según el usuario actual
	    if (usuario1.equals(usuarioActual)) {
	        receptor = usuario2;
	    } else if (usuario2.equals(usuarioActual)) {
	        receptor = usuario1;
	    } else {
	        JOptionPane.showMessageDialog(this, "El chat seleccionado no involucra al usuario actual.", "Error",
	                JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    receptorActual = receptor;
	    panelChatContenido.removeAll(); // Limpia el área del chat

	    // Obtener mensajes del chat seleccionado
	    List<Mensaje> mensajes = chatSeleccionado.getMensajes();

	    if (mensajes != null && !mensajes.isEmpty()) {
	        // Si hay mensajes, los mostramos en el chat
	        for (Mensaje mensaje : mensajes) {
	            boolean esMio = mensaje.getEmisor().equals(AppChat.getUsuarioActual());
	            String nombre = esMio ? "Yo" : mensaje.getEmisor().getUsuario();
	            pintarBubblesMensaje(panelChatContenido, mensaje, esMio, nombre);
	        }
	    } else {
	        // Si no hay mensajes, mostramos un mensaje de "sin mensajes"
	        JLabel noMessagesLabel = new JLabel("No hay mensajes con este contacto.");
	        noMessagesLabel.setHorizontalAlignment(SwingConstants.CENTER);
	        noMessagesLabel.setForeground(Color.GRAY);
	        noMessagesLabel.setFont(new Font("Arial", Font.ITALIC, 14));
	        panelChatContenido.add(noMessagesLabel);
	    }

	    // Actualizar y refrescar el panel de chat
	    refrescarChat();
	}


	private void actualizarListaChats(JList<Chat> listaChats) {
		List<Chat> chats = AppChat.getUsuarioActual().getListaChats();

		chats.sort((c1, c2) -> {
			List<Mensaje> m1 = c1.getMensajes();
			List<Mensaje> m2 = c2.getMensajes();
			if (m1.isEmpty() && m2.isEmpty())
				return 0;
			if (m1.isEmpty())
				return 1; // chats sin mensajes al final
			if (m2.isEmpty())
				return -1;

			// Tomar el último mensaje de cada chat
			Mensaje ultimo1 = m1.get(m1.size() - 1);
			Mensaje ultimo2 = m2.get(m2.size() - 1);

			// Comparamos primero la fecha (desc), luego la hora (desc)
			int fechaCompare = ultimo2.getFecha().compareTo(ultimo1.getFecha());
			if (fechaCompare != 0) {
				return fechaCompare;
			} else {
				return ultimo2.getHora().compareTo(ultimo1.getHora());
			}
		});

		// Crear un nuevo modelo con el orden resultante
		listaChats.setModel(new AbstractListModel<Chat>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public int getSize() {
				return chats.size();
			}

			@Override
			public Chat getElementAt(int index) {
				return chats.get(index);
			}
		});

		// Repintar la lista
		listaChats.repaint();
	}

	
	public void pintarBubblesEmoji(JPanel panel, int emoji, boolean esMio, String nombre) {
	    BubbleText burbuja = new BubbleText(panel, emoji, 
	            esMio ? Color.GREEN : Color.LIGHT_GRAY, nombre,
	            esMio ? BubbleText.SENT : BubbleText.RECEIVED, 18);
	    panel.add(burbuja);
	}
	
	public void pintarBubblesMensaje(JPanel panel, Mensaje mensaje, boolean esMio, String nombre) {
	    String texto = mensaje.getTexto();
	    if (texto != null && texto.startsWith("EMOJI:")) {
	        try {
	            int emojiCode = Integer.parseInt(texto.substring(6));
	            // Renderizar emoji
	            pintarBubblesEmoji(panel, emojiCode, esMio, nombre);
	        } catch (NumberFormatException e) {
	            // Si el código del emoji no es válido, tratarlo como texto normal
	            BubbleText burbuja = new BubbleText(panel, "[Emoji inválido]", 
	                    esMio ? Color.GREEN : Color.LIGHT_GRAY, nombre, 
	                    esMio ? BubbleText.SENT : BubbleText.RECEIVED, 18);
	            panel.add(burbuja);
	        }
	    } else if (texto != null) {
	        // Renderizar texto normal
	        BubbleText burbuja = new BubbleText(panel, texto + "  " +
	                String.format("%02d:%02d", mensaje.getHora().getHour(), mensaje.getHora().getMinute()),
	                esMio ? Color.GREEN : Color.LIGHT_GRAY, nombre, 
	                esMio ? BubbleText.SENT : BubbleText.RECEIVED, 18);
	        panel.add(burbuja);
	    } else {
	        // Texto es null
	        BubbleText burbuja = new BubbleText(panel, "[Mensaje sin contenido] " +
	                String.format("%02d:%02d", mensaje.getHora().getHour(), mensaje.getHora().getMinute()),
	                esMio ? Color.GREEN : Color.LIGHT_GRAY, nombre, 
	                esMio ? BubbleText.SENT : BubbleText.RECEIVED, 18);
	        panel.add(burbuja);
	    }
	}


	public void refrescarChat() {
		panelChatContenido.revalidate();
		panelChatContenido.repaint();
		panelCentro.removeAll();
		panelCentro.add(panelChat, BorderLayout.CENTER);
		panelCentro.revalidate();
		panelCentro.repaint();
	}

}