package umu.tds.apps.vista;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import umu.tds.apps.controlador.AppChat;
import umu.tds.apps.modelo.Chat;
import umu.tds.apps.modelo.Contacto;
import umu.tds.apps.modelo.ContactoIndividual;
import umu.tds.apps.modelo.Grupo;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.Usuario;
import umu.tds.apps.persistencia.AdaptadorChatTDS;
import umu.tds.apps.persistencia.AdaptadorGrupoTDS;
import umu.tds.apps.persistencia.AdaptadorMensajeTDS;
import umu.tds.apps.persistencia.AdaptadorUsuarioTDS;
import umu.tds.apps.vista.customcomponents.VisualUtils;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
import javax.swing.Timer;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;

import javax.swing.DefaultComboBoxModel;


/**
 * Ventana principal de la aplicación AppChat que proporciona la interfaz de usuario
 * para chatear con contactos, administrar grupos y acceder a las funcionalidades de la aplicación.
 * <p>
 * Esta clase sirve como la interfaz principal de la aplicación de chat, gestionando:
 * <ul>
 *   <li>Visualización de conversaciones de chat (tanto individuales como grupales)</li>
 *   <li>Funcionalidad de envío y recepción de mensajes</li>
 *   <li>Gestión de contactos</li>
 *   <li>Modificación de imágenes de perfil</li>
 *   <li>Capacidades de búsqueda</li>
 *   <li>Acceso a funciones premium</li>
 * </ul>
 * 
 * @author TDS-2025
 * @version 1.0
 */

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
	
	private Grupo grupoActual;
	
	private JScrollPane scrollPanelChatMensajes;
	
	private CardLayout cardLayout;
	
	// lista de chats
	private JList<Object> list;
	
	
	/**
     * Obtiene el panel central que muestra la pantalla de bienvenida o el panel de chat.
     * 
     * @return El panel central de la interfaz
     */

	public JPanel getPanelCentro() {
	    return panelCentro;
	}

    /**
     * Inicia la aplicación creando y mostrando la ventana principal.
     * 
     * @param args Argumentos de la línea de comandos (no utilizados)
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
     * Envía un mensaje a todos los miembros de un grupo.
     * <p>
     * Este método crea un mensaje individual para cada miembro del grupo y lo registra
     * en la base de datos, actualizando las listas de mensajes del emisor y receptores.
     * 
     * @param emisor El usuario que envía el mensaje
     * @param grupo El grupo destinatario del mensaje
     * @param texto El contenido del mensaje a enviar
     * @return true si el mensaje se envió correctamente a todos los miembros, false en caso contrario
     */
	public boolean enviarMensajeAGrupo(Usuario emisor, Grupo grupo, String texto) {
	    if (texto == null || texto.trim().isEmpty()) {
	        System.err.println("El mensaje no puede estar vacío.");
	        return false;
	    }

	    if (grupo == null || grupo.getListaContactos().isEmpty()) {
	        System.err.println("El grupo no existe o no tiene contactos.");
	        return false;
	    }

	    if (emisor == null) {
	        System.err.println("El emisor no puede ser nulo.");
	        return false;
	    }

	    boolean enviado = true;
	    
	    // Crear un mensaje representativo para el grupo (usando el primer miembro como referencia)
	    Mensaje mensajeGrupo = null;
	    
	    for (ContactoIndividual miembro : grupo.getListaContactos()) {
	        Usuario receptor = miembro.getUsuario();
	        if (receptor != null) {
	            // Obtener o crear el chat con este receptor
	            Chat chat = emisor.obtenerChatCon(receptor);
	            if (chat == null) {
	                chat = new Chat(emisor, receptor);
	                AdaptadorChatTDS.getUnicaInstancia().registrarChat(chat);
	                emisor.añadirChat(chat);
	                receptor.añadirChat(chat);
	                AdaptadorUsuarioTDS.getUnicaInstancia().modificarUsuario(emisor);
	                AdaptadorUsuarioTDS.getUnicaInstancia().modificarUsuario(receptor);
	            }

	            // Crear mensaje individual para este receptor
	            Mensaje mensaje = new Mensaje(emisor, texto, receptor, chat);
	            AdaptadorMensajeTDS.getUnicaInstancia().registrarMensaje(mensaje);

	            // Actualizar listas de emisor y receptor
	            emisor.añadirMensajeEnviado(mensaje);
	            receptor.añadirMensajeRecibido(mensaje);

	            // Persistir cambios
	            AdaptadorUsuarioTDS.getUnicaInstancia().modificarUsuario(emisor);
	            AdaptadorUsuarioTDS.getUnicaInstancia().modificarUsuario(receptor);

	            // Actualizar chat individual
	            chat.addMensaje(mensaje);
	            AdaptadorChatTDS.getUnicaInstancia().modificarChat(chat);
	            
	            // Guardar el primer mensaje como representativo para el grupo
	            if (mensajeGrupo == null) {
	                mensajeGrupo = mensaje;
	            }
	            
	            enviado &= true;
	        } else {
	            System.err.println("No se pudo enviar mensaje a: " + miembro.getNombre());
	            enviado = false;
	        }
	    }
	    
	    // Añadir solo una vez el mensaje al grupo
	    if (mensajeGrupo != null) {
	        grupo.addMensajeEnviado(mensajeGrupo);
	        AdaptadorGrupoTDS.getUnicaInstancia().modificarGrupo(grupo);
	    }
	    
	    return enviado;
	}


    /**
     * Constructor que inicializa la ventana principal y todos sus componentes.
     * <p>
     * Configura la interfaz de usuario, incluyendo paneles de chat, lista de conversaciones,
     * botones de función y manejo de eventos.
     */

	public VentanaPrincipal() {

		setTitle("AppChat");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/icono.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 633);
		setLocationRelativeTo(null);
		
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
		        // Abrir chat con un contacto o grupo de la lista
		        String nombreContactoOGrupo = JOptionPane.showInputDialog("Introduce el nombre del contacto o grupo:");
		        if (nombreContactoOGrupo != null && !nombreContactoOGrupo.trim().isEmpty()) {
		            // 1) Localizar el contacto en la listaContactos del usuario actual
		            Usuario usuarioActual = AppChat.getUsuarioActual();
		            Contacto contacto = usuarioActual.getListaContactos().stream()
		                    .filter(c -> c.getNombre().equalsIgnoreCase(nombreContactoOGrupo))
		                    .findFirst()
		                    .orElse(null);

		            if (contacto == null) {
		                JOptionPane.showMessageDialog(this, "Contacto o grupo no encontrado.",
		                                              "Error", JOptionPane.ERROR_MESSAGE);
		                return;
		            }

		            // 2) Según sea Grupo o ContactoIndividual, abrimos el chat adecuado
		            if (contacto instanceof Grupo) {
		                // ABRIR CHAT DE GRUPO
		                Grupo grupo = (Grupo) contacto;
		                cargarChat(grupo); 
		                // Se mostrará la misma ventana de chat que para un contacto individual,
		                // pero ahora 'grupoActual' != null y 'receptorActual' == null
		            } else if (contacto instanceof ContactoIndividual) {
		                // ABRIR CHAT INDIVIDUAL
		                Usuario usuarioContacto = ((ContactoIndividual) contacto).getUsuario();
		                cargarChat(usuarioContacto);
		            }
		        }
		    }
		    else if ("Teléfono".equals(seleccion)) {
		        // Abrir chat con un usuario por número de teléfono
		        String telefono = JOptionPane.showInputDialog("Introduce el número de teléfono:");
		        if (telefono != null && !telefono.trim().isEmpty()) {
		            Usuario usuario = AppChat.getUnicaInstancia().obtenerUsuarioPorTelefono(telefono);
		            if (usuario != null) {
		                cargarChat(usuario);
		            } else {
		                JOptionPane.showMessageDialog(this, "Usuario no encontrado.",
		                                              "Error", JOptionPane.ERROR_MESSAGE);
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
		    VentanaBusqueda ventanaBusqueda = new VentanaBusqueda(this);
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
		    frameContactos.setLocationRelativeTo(null);

		    // Lista de contactos inicial o vacía
		    List<String> contactosIniciales = new ArrayList<>();
		    contactosIniciales.add("Contacto 1"); // EJEMPLO

		    // Crear el panel VentanaContactos y añadir al frame
		    VentanaContactos ventanaContactos = new VentanaContactos();
		    frameContactos.getContentPane().add(ventanaContactos);
		    
		    // Añadir listener para actualizar la lista cuando se cierra la ventana
		    frameContactos.addWindowListener(new WindowAdapter() {
		        @Override
		        public void windowClosed(WindowEvent e) {
		            actualizarListaChats(list);
		        }
		    });
		    
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
		        // Crear un diálogo de opciones
		        String[] opciones = {"Cambiar Foto de Perfil", "Ver Perfil", "Cancelar"};
		        int seleccion = JOptionPane.showOptionDialog(
		            VentanaPrincipal.this,  // padre
		            "Selecciona una opción", 
		            "Opciones de Perfil", 
		            JOptionPane.DEFAULT_OPTION,
		            JOptionPane.QUESTION_MESSAGE,
		            null,  // sin icono personalizado
		            opciones, 
		            opciones[2]  // opción por defecto
		        );

		        // Manejar la selección
		        switch (seleccion) {
		            case 0:  // Cambiar Foto de Perfil
		                seleccionarFotoPerfil(lblNewLabel_1);
		                break;
		            case 1:  // Ver Perfil
		                VentanaPerfil.mostrarPerfil(VentanaPrincipal.this);
		                break;
		            case 2:  // Cancelar
		                // No hacer nada
		                break;
		        }
		    }
		});
		// Para la lista de chats recientes
		JPanel panelMensajes = new JPanel(new BorderLayout());
		contentPane.add(panelMensajes, BorderLayout.WEST);

		list = new JList<>();
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
		ImageIcon gifIcon = new ImageIcon(getClass().getResource("/umu/tds/apps/resources/directo.gif"));
		panelBienvenida.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
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
		scrollPanelChatMensajes = new JScrollPane();
		scrollPanelChatMensajes.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanelChatMensajes.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanelChatMensajes.setBorder(null);

		// Auto-scroll al final del chat
		Adjustable vertical = scrollPanelChatMensajes.getVerticalScrollBar();
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
		



		panelChat.add(scrollPanelChatMensajes, BorderLayout.CENTER);

		// Panel con fondo degradado donde ponemos las burbujas
		panelChatContenido = new VisualUtils.JPanelGradient(new Color(0, 128, 128), new Color(172, 225, 175));
		panelChatContenido.setLayout(new BoxLayout(panelChatContenido, BoxLayout.Y_AXIS));
		scrollPanelChatMensajes.setViewportView(panelChatContenido);

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
		// En el método que maneja el clic en un emoji (en la clase VentanaPrincipal)
		JPanel panelEmojis = new EmojiPanel(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        int numEmoji = Integer.parseInt(e.getActionCommand());

		        if (grupoActual != null) {
		            // Chat de GRUPO
		            boolean enviado = AppChat.getUnicaInstancia()
		                                .enviarEmojiAGrupo(AppChat.getUsuarioActual(), grupoActual, numEmoji);
		            if (enviado) {
		                // Simplemente mostrar el emoji localmente
		                pintarBubblesEmoji(panelChatContenido, numEmoji, true);
		                panelChatContenido.revalidate();
		                panelChatContenido.repaint();
		                
		                // Actualizar la lista de chats
		                actualizarListaChats(list);
		            } else {
		                JOptionPane.showMessageDialog(VentanaPrincipal.this,
		                    "No se pudo enviar el emoji al grupo.", "Error",
		                    JOptionPane.ERROR_MESSAGE);
		            }
		        } else if (receptorActual != null) {
		            // Chat INDIVIDUAL (código existente)
		            boolean enviado = AppChat.getUnicaInstancia()
		                                .enviarEmoji(AppChat.getUsuarioActual(), receptorActual, numEmoji);
		            if (enviado) {
		                pintarBubblesEmoji(panelChatContenido, numEmoji, true);
		                panelChatContenido.revalidate();
		                panelChatContenido.repaint();
		                actualizarListaChats(list);
		            } else {
		                JOptionPane.showMessageDialog(VentanaPrincipal.this,
		                    "No se pudo enviar el emoji.", "Error",
		                    JOptionPane.ERROR_MESSAGE);
		            }
		        } else {
		            // Sin receptor actual
		            JOptionPane.showMessageDialog(VentanaPrincipal.this,
		                "No hay ningún chat seleccionado para enviar el emoji.",
		                "Advertencia", JOptionPane.WARNING_MESSAGE);
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

		        // 1) Si es un chat de grupo
		        if (grupoActual != null) {
		            // Llamar a tu método "enviarMensajeAGrupo(...)"
		            boolean exito = enviarMensajeAGrupo(AppChat.getUsuarioActual(), grupoActual, textoMensaje);
		            if (exito) {
		                // Pintar burbuja local (si quieres)
		                String textoConHora = textoMensaje + "  " + 
		                    String.format("%02d:%02d", LocalTime.now().getHour(), LocalTime.now().getMinute());
		                BubbleText burbuja = new BubbleText(panelChatContenido, textoConHora,
		                        Color.GREEN, "", BubbleText.SENT, 18);
		                panelChatContenido.add(burbuja);

		                refrescarChat();
		                // También puedes actualizar la lista si lo deseas
		                actualizarListaChats(list);
		            } else {
		                JOptionPane.showMessageDialog(this,
		                        "No se pudo enviar el mensaje al grupo.", "Error",
		                        JOptionPane.ERROR_MESSAGE);
		            }

		            txtMensaje.setText("");
		            return;
		        }

		        // 2) Si no es un chat de grupo, usar la lógica actual
		        Usuario receptor = receptorActual;
		        if (receptor != null) {
		            // Enviar a un usuario
		            AppChat.getUnicaInstancia().enviarMensaje(AppChat.getUsuarioActual(), receptor, textoMensaje);

		            // Pintar la burbuja local
		            String textoConHora = textoMensaje + "  " +
		                String.format("%02d:%02d", LocalTime.now().getHour(), LocalTime.now().getMinute());
		            BubbleText burbuja = new BubbleText(panelChatContenido, textoConHora,
		                    Color.GREEN, "", BubbleText.SENT, 18);
		            panelChatContenido.add(burbuja);
		            panelChatContenido.revalidate();
		            panelChatContenido.repaint();

		            actualizarListaChats(list);
		            txtMensaje.setText("");
		        } else {
		            JOptionPane.showMessageDialog(this,
		                    "Error al identificar el receptor del mensaje.", "Error",
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


		list.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int index = list.locationToIndex(e.getPoint());
		        if (index >= 0) {
		            Object selectedItem = list.getModel().getElementAt(index);
		            if (selectedItem instanceof Chat) {
		                Chat chat = (Chat) selectedItem;
		                
		                // Obtener el otro usuario
		                Usuario usuarioActual = AppChat.getUsuarioActual();
		                Usuario usuarioOtro = chat.getUsuario().equals(usuarioActual) ? 
		                                      chat.getOtroUsuarioChat() : chat.getUsuario();
		                
		                // IMPORTANTE: Verificar si ya existe como contacto
		                ContactoIndividual contactoExistente = AppChat.getUnicaInstancia()
		                        .obtenerContactoPorTelefono(usuarioOtro.getTelefono());
		                
		                // Solo proceder si NO es un contacto existente
		                if (contactoExistente == null) {
		                    Rectangle cellBounds = list.getCellBounds(index, index);
		                    if (cellBounds != null && cellBounds.contains(e.getPoint())) {
		                        int relativeX = e.getX() - cellBounds.x;
		                        
		                        // Usar coordenadas fijas para el área del botón
		                        int buttonX = cellBounds.width - 40;
		                        int buttonWidth = 30;
		                        
		                        if (relativeX >= buttonX && relativeX <= buttonX + buttonWidth) {
		                            // Solicitar nombre para el contacto
		                            String nombreContacto = JOptionPane.showInputDialog(
		                                          "Introduce el nombre del contacto:");
		                            
		                            if (nombreContacto != null && !nombreContacto.trim().isEmpty()) {
		                                boolean agregado = AppChat.getUnicaInstancia().añadirContacto(
		                                    nombreContacto, usuarioOtro.getTelefono());
		                                
		                                if (agregado) {
		                                    actualizarListaChats(list);
		                                }
		                            }
		                        }
		                    }
		                }
		            }
		        }
		    }
		});
		// al iniciar, cargamos tambien la lista de chats
		//panelCentro.add(panelBienvenida, BorderLayout.CENTER);
		actualizarListaChats(list);
		refrescarChat();
		
		cardLayout = new CardLayout();
		panelCentro.setLayout(cardLayout);

		panelCentro.add(panelBienvenida, "Bienvenida");
		panelCentro.add(panelChat, "Chat");

		// Mostrar panel de bienvenida al inicio
		cardLayout.show(panelCentro, "Bienvenida");
		
		list.addListSelectionListener(e -> {
		    if (!e.getValueIsAdjusting()) {
		        Object seleccionado = list.getSelectedValue(); // un Object
		        if (seleccionado instanceof Chat) {
		            // Abrir un Chat 
		            Chat chat = (Chat) seleccionado;
		            cargarChat(chat);
		            cardLayout.show(panelCentro, "Chat");
		        } else if (seleccionado instanceof Grupo) {
		            // Abrir el chat de Grupo
		            Grupo grupo = (Grupo) seleccionado;
		            cargarChat(grupo);
		            cardLayout.show(panelCentro, "Chat");
		        }
		    }
		});
		
		
		Component rigidArea_2 = Box.createRigidArea(new Dimension(10, 20));  // 10px de ancho
		panelNorte.add(rigidArea_2);
		
		// En VentanaPrincipal.java
		JButton btnGenerarPDF = new JButton("Generar PDF");
		btnGenerarPDF.setForeground(new Color(255, 255, 255));
		btnGenerarPDF.setBackground(new Color(0, 128, 128));
		btnGenerarPDF.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnGenerarPDF.setEnabled(AppChat.getUsuarioActual().isPremium()); // Solo disponible para usuarios Premium
		
		if (AppChat.getUsuarioActual().isPremium()) {
		    btnNewButton_3.setText(" Premium ");
		    btnNewButton_3.setBackground(new Color(0, 128, 0)); // Verde para indicar activo
		}
		
		// En la clase VentanaPrincipal, encuentra el botón premium
		btnNewButton_3.addActionListener(e -> {
		    // Abre la ventana de premium
		    VentanaPremium ventanaPremium = new VentanaPremium();
		    ventanaPremium.setModal(true); // Hacer la ventana modal
		    ventanaPremium.setLocationRelativeTo(this); // Centrar en la ventana principal
		    ventanaPremium.setVisible(true);
		    
		    // Actualizar la UI si el usuario se volvió premium
		    if (AppChat.getUsuarioActual().isPremium()) {
		        // Refrescar la UI
		        btnGenerarPDF.setEnabled(true);
		        btnNewButton_3.setText(" Premium ");
		        btnNewButton_3.setBackground(new Color(0, 128, 0)); // Verde para indicar activo
		    }
		});

		btnGenerarPDF.addActionListener(e -> {
		    if (!AppChat.getUsuarioActual().isPremium()) {
		        JOptionPane.showMessageDialog(this, 
		            "Esta función solo está disponible para usuarios Premium", 
		            "Acceso denegado", JOptionPane.WARNING_MESSAGE);
		        return;
		    }
		    
		    // Mostrar un diálogo para seleccionar dónde guardar el archivo
		    JFileChooser fileChooser = new JFileChooser();
		    fileChooser.setDialogTitle("Guardar PDF");
		    fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos PDF (*.pdf)", "pdf"));
		    
		    int seleccion = fileChooser.showSaveDialog(this);
		    if (seleccion == JFileChooser.APPROVE_OPTION) {
		        String rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath();
		        if (!rutaArchivo.toLowerCase().endsWith(".pdf")) {
		            rutaArchivo += ".pdf";
		        }
		        
		        boolean exito = false;
		        
		        if (receptorActual != null) {
		            // Generar PDF de mensajes con el contacto seleccionado
		            exito = AppChat.getUnicaInstancia().generarPDFMensajesUsuario(receptorActual, rutaArchivo);
		        } else {
		            // Generar PDF de todos los contactos
		            exito = AppChat.getUnicaInstancia().generarPDFContactos(rutaArchivo);
		        }
		        
		        if (exito) {
		            JOptionPane.showMessageDialog(this, 
		                "PDF generado correctamente en: " + rutaArchivo, 
		                "Éxito", JOptionPane.INFORMATION_MESSAGE);
		        } else {
		            JOptionPane.showMessageDialog(this, 
		                "Error al generar el PDF", 
		                "Error", JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});

		// Añadir el botón a la interfaz en el lugar adecuado
		panelNorte.add(btnGenerarPDF);

;
	}
	
	

    /**
     * Carga una imagen de perfil circular en un JLabel.
     * <p>
     * Si la ruta de la imagen no es válida o está vacía, se carga una imagen por defecto.
     * 
     * @param label El JLabel donde se mostrará la imagen
     * @param path La ruta o URL de la imagen a cargar
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

	
    /**
     * Permite al usuario seleccionar una nueva imagen de perfil desde una URL o archivo local.
     * 
     * @param label El JLabel que muestra la imagen de perfil actual
     */

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

	
    /**
     * Carga el chat con un usuario específico, mostrando los mensajes intercambiados.
     * 
     * @param contacto El usuario con quien se desea iniciar o continuar un chat
     */

	private void cargarChat(Usuario contacto) {
	    receptorActual = contacto;
	    grupoActual = null; // Esta línea es crucial - asegúrate de que grupoActual sea null
	    
	    panelChatContenido.removeAll(); // Limpiar contenido del chat


	    // Verificar si ya existe un chat entre el usuario actual y el contacto
	    Chat chatExistente = AppChat.getUsuarioActual().obtenerChatCon(contacto);

	    if (chatExistente != null && !chatExistente.getMensajes().isEmpty()) {
	        // Cargar los mensajes existentes en el chat
	        for (Mensaje mensaje : chatExistente.getMensajes()) {
	            boolean esMio = mensaje.getEmisor().equals(AppChat.getUsuarioActual());
	            pintarBubblesMensaje(panelChatContenido, mensaje, esMio);
	        }
	    } else {
	        // Si no hay chat o mensajes, mostrar mensaje predeterminado
	        JLabel noMessagesLabel = new JLabel("No hay mensajes con este contacto.");
	        noMessagesLabel.setHorizontalAlignment(SwingConstants.CENTER);
	        noMessagesLabel.setForeground(Color.GRAY);
	        noMessagesLabel.setFont(new Font("Arial", Font.ITALIC, 14));
	        panelChatContenido.add(noMessagesLabel);
	    }
	    
	    refrescarChat();
	    cardLayout.show(panelCentro, "Chat");
	}
	
	
    /**
     * Carga el chat de un grupo, mostrando los mensajes enviados al grupo.
     * 
     * @param grupo El grupo cuyos mensajes se mostrarán
     */

	private void cargarChat(Grupo grupo) {
	    // Guardamos el grupo actual y limpiamos el receptor
	    this.grupoActual = grupo;
	    this.receptorActual = null;

	    // Limpiar el panel de chat
	    panelChatContenido.removeAll();

	    // Obtener mensajes del grupo
	    List<Mensaje> mensajes = grupo.getListaMensajesEnviados();
	    
	    System.out.println("Cargando " + (mensajes != null ? mensajes.size() : 0) + " mensajes para el grupo " + grupo.getNombre());
	    
	    if (mensajes != null && !mensajes.isEmpty()) {
	        for (Mensaje mensaje : mensajes) {
	            boolean esMio = mensaje.getEmisor().equals(AppChat.getUsuarioActual());
	            String texto = mensaje.getTexto();
	            
	            System.out.println("Procesando mensaje: " + texto + " (esMio=" + esMio + ")");
	            
	            if (texto != null && texto.startsWith("EMOJI:")) {
	                try {
	                    int emojiCode = Integer.parseInt(texto.substring(6));
	                    System.out.println("Renderizando emoji: " + emojiCode);
	                    pintarBubblesEmoji(panelChatContenido, emojiCode, esMio);
	                } catch (NumberFormatException e) {
	                    System.err.println("Error al parsear código de emoji: " + e.getMessage());
	                    pintarBubblesMensaje(panelChatContenido, mensaje, esMio);
	                }
	            } else {
	                pintarBubblesMensaje(panelChatContenido, mensaje, esMio);
	            }
	        }
	    } else {
	        JLabel noMessagesLabel = new JLabel("No hay mensajes con este grupo.");
	        noMessagesLabel.setHorizontalAlignment(SwingConstants.CENTER);
	        noMessagesLabel.setForeground(Color.GRAY);
	        noMessagesLabel.setFont(new Font("Arial", Font.ITALIC, 14));
	        panelChatContenido.add(noMessagesLabel);
	    }

	    // Actualizar la interfaz
	    refrescarChat();
	    cardLayout.show(panelCentro, "Chat");
	}


	
    /**
     * Carga un chat existente, determinando automáticamente el receptor según el usuario actual.
     * 
     * @param chatSeleccionado El objeto Chat que contiene la conversación a mostrar
     */

	private void cargarChat(Chat chatSeleccionado) {
	    Usuario usuarioActual = AppChat.getUsuarioActual();
	    Usuario usuario1 = chatSeleccionado.getUsuario();
	    Usuario usuario2 = chatSeleccionado.getOtroUsuarioChat();
	    Usuario receptor;
	    
	    grupoActual = null; 

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
	            pintarBubblesMensaje(panelChatContenido, mensaje, esMio);         
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

	
    /**
     * Actualiza la lista de chats y grupos mostrados en el panel lateral.
     * <p>
     * Ordena los chats por fecha y hora del último mensaje en orden descendente.
     * 
     * @param listaChats El componente JList que muestra los chats disponibles
     */
	private void actualizarListaChats(JList<Object> listaChats) {
		
	    if (listaChats == null) {
	        System.out.println("La lista de chats es nula, no se puede actualizar");
	        return;
	    }
	    
	    // 1) Obtener Chats
	    List<Chat> chats = AppChat.getUsuarioActual().getListaChats();

	    // Ordenar Chats
	    chats.sort((c1, c2) -> {
	        List<Mensaje> m1 = c1.getMensajes();
	        List<Mensaje> m2 = c2.getMensajes();
	        if (m1.isEmpty() && m2.isEmpty()) return 0;
	        if (m1.isEmpty()) return 1;   // chats sin mensajes al final
	        if (m2.isEmpty()) return -1;

	        // Tomar último mensaje
	        Mensaje ultimo1 = m1.get(m1.size() - 1);
	        Mensaje ultimo2 = m2.get(m2.size() - 1);

	        // Fecha DESC
	        int fechaCompare = ultimo2.getFecha().compareTo(ultimo1.getFecha());
	        if (fechaCompare != 0) return fechaCompare;

	        // Hora DESC
	        return ultimo2.getHora().compareTo(ultimo1.getHora());
	    });

	    // 2) Obtener todos los Grupos (sin filtrar por mensajes)
	    List<Grupo> grupos = AppChat.getUsuarioActual().getGrupos();
	    
	    // 3) Combinar en una lista de Object
	    List<Object> items = new ArrayList<>();
	    // Primero añadimos los chats
	    items.addAll(chats);
	    // Luego todos los grupos (sin filtrar)
	    items.addAll(grupos);

	    // 4) Construir un AbstractListModel<Object> con la lista combinada
	    AbstractListModel<Object> modelo = new AbstractListModel<Object>() {
	        private static final long serialVersionUID = 1L;
	        
	        @Override
	        public int getSize() {
	            return items.size();
	        }
	        
	        @Override
	        public Object getElementAt(int index) {
	            return items.get(index);
	        }
	    };
	    
	    // 5) Asignar el modelo a la lista
	    listaChats.setModel(modelo);

	    // 6) Repintar la lista
	    listaChats.repaint();
	}

	
	
    /**
     * Muestra un emoji en una burbuja de chat.
     * 
     * @param panel El panel donde se añadirá la burbuja
     * @param emoji El código del emoji a mostrar
     * @param esMio Indica si el emoji fue enviado por el usuario actual
     */
	public void pintarBubblesEmoji(JPanel panel, int emoji, boolean esMio) {
	    BubbleText burbuja = new BubbleText(panel, emoji, 
	            esMio ? Color.GREEN : Color.LIGHT_GRAY, "",
	            esMio ? BubbleText.SENT : BubbleText.RECEIVED, 18);
	    panel.add(burbuja);
	}
	
	
    /**
     * Muestra un mensaje en una burbuja de chat, diferenciando entre texto normal y emojis.
     * 
     * @param panel El panel donde se añadirá la burbuja
     * @param mensaje El mensaje a mostrar
     * @param esMio Indica si el mensaje fue enviado por el usuario actual
     */
	public void pintarBubblesMensaje(JPanel panel, Mensaje mensaje, boolean esMio) {
	    String texto = mensaje.getTexto();
	    if (texto != null && texto.startsWith("EMOJI:")) {
	        try {
	            int emojiCode = Integer.parseInt(texto.substring(6));
	            // Renderizar emoji
	            pintarBubblesEmoji(panel, emojiCode, esMio);
	        } catch (NumberFormatException e) {
	            // Si el código del emoji no es válido, tratarlo como texto normal
	            BubbleText burbuja = new BubbleText(panel, "[Emoji inválido]", 
	                    esMio ? Color.GREEN : Color.LIGHT_GRAY, "", 
	                    esMio ? BubbleText.SENT : BubbleText.RECEIVED, 18);
	            panel.add(burbuja);
	        }
	        
	    } else if (texto != null) {
	        // Renderizar texto normal
	        BubbleText burbuja = new BubbleText(panel, texto + "  " +
	                String.format("%02d:%02d", mensaje.getHora().getHour(), mensaje.getHora().getMinute()),
	                esMio ? Color.GREEN : Color.LIGHT_GRAY, "", 
	                esMio ? BubbleText.SENT : BubbleText.RECEIVED, 18);
	        panel.add(burbuja);
	    } else {
	        // Texto es null
	        BubbleText burbuja = new BubbleText(panel, "[Mensaje sin contenido] " +
	                String.format("%02d:%02d", mensaje.getHora().getHour(), mensaje.getHora().getMinute()),
	                esMio ? Color.GREEN : Color.LIGHT_GRAY, "", 
	                esMio ? BubbleText.SENT : BubbleText.RECEIVED, 18);
	        panel.add(burbuja);
	    }
	}


    /**
     * Actualiza la visualización del panel de chat.
     */
	public void refrescarChat() {
	    SwingUtilities.invokeLater(() -> {
	        panelChatContenido.revalidate();
	        panelChatContenido.repaint();
	    });
	}


	/**
	 * Carga un mensaje específico en el chat, resaltándolo visualmente.
	 * 
	 * @param contacto       El usuario con quien se desea cargar el chat
	 * @param mensajeBuscado El mensaje a resaltar
	 */
	public void cargarMensajeEnChat(Usuario contacto, Mensaje mensajeBuscado) {
	    // Cargamos el chat normalmente
	    receptorActual = contacto;
	    grupoActual = null;
	    
	    // Variable para guardar la posición Y del mensaje buscado
	    final int[] posicionYMensaje = {-1};
	    
	    // Deshabilitar temporalmente cualquier autoscroll que puedas tener configurado
	    if (scrollPanelChatMensajes.getVerticalScrollBar().getAdjustmentListeners().length > 0) {
	        // Guardar y desactivar cualquier autoscroll
	        for (AdjustmentListener listener : scrollPanelChatMensajes.getVerticalScrollBar().getAdjustmentListeners()) {
	            scrollPanelChatMensajes.getVerticalScrollBar().removeAdjustmentListener(listener);
	        }
	    }
	    
	    // Asegurarnos de que el scroll horizontal está desactivado
	    scrollPanelChatMensajes.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    
	    panelChatContenido.removeAll(); // Limpiar contenido del chat

	    // Verificar si ya existe un chat entre el usuario actual y el contacto
	    Chat chatExistente = AppChat.getUsuarioActual().obtenerChatCon(contacto);

	    if (chatExistente != null && !chatExistente.getMensajes().isEmpty()) {
	        // Cargar los mensajes existentes en el chat
	        List<Mensaje> mensajes = chatExistente.getMensajes();
	        int totalMensajes = mensajes.size();
	        
	        // Añadimos un panel para cada mensaje
	        for (int i = 0; i < totalMensajes; i++) {
	            Mensaje mensaje = mensajes.get(i);
	            boolean esMio = mensaje.getEmisor().equals(AppChat.getUsuarioActual());
	            
	            // Verificar si es el mensaje buscado
	            boolean esElMensajeBuscado = (mensajeBuscado != null && mensaje.getCodigo() == mensajeBuscado.getCodigo());
	            
	            if (esElMensajeBuscado) {
	                // Si es el mensaje buscado, lo marcamos visualmente
	                
	                // 1. Crear un panel de marca superior que ocupe todo el ancho
	                JPanel panelMarcaSuperior = new JPanel();
	                panelMarcaSuperior.setBackground(new Color(255, 253, 170)); // Amarillo claro
	                JLabel etiquetaMarcaSuperior = new JLabel("▼ MENSAJE ENCONTRADO ▼");
	                etiquetaMarcaSuperior.setFont(new Font("Arial", Font.BOLD, 12));
	                etiquetaMarcaSuperior.setForeground(new Color(220, 0, 0)); // Rojo oscuro
	                etiquetaMarcaSuperior.setHorizontalAlignment(SwingConstants.CENTER);
	                panelMarcaSuperior.setLayout(new BorderLayout());
	                panelMarcaSuperior.add(etiquetaMarcaSuperior, BorderLayout.CENTER);
	                // Asegurar que el panel ocupa todo el ancho disponible
	                panelMarcaSuperior.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelMarcaSuperior.getPreferredSize().height));
	                panelChatContenido.add(panelMarcaSuperior);
	                
	                // 2. Pintar el mensaje normalmente pero con un borde y fondo especial
	                // Usando el método existente pero con colores diferentes
	                // Esto evita modificar la estructura de layout que ya funciona
	                pintarBubblesMensajeResaltado(panelChatContenido, mensaje, esMio);
	                
	                // 3. Crear un panel de marca inferior que ocupe todo el ancho
	                JPanel panelMarcaInferior = new JPanel();
	                panelMarcaInferior.setBackground(new Color(255, 253, 170)); // Amarillo claro
	                JLabel etiquetaMarcaInferior = new JLabel("▲ MENSAJE ENCONTRADO ▲");
	                etiquetaMarcaInferior.setFont(new Font("Arial", Font.BOLD, 12));
	                etiquetaMarcaInferior.setForeground(new Color(220, 0, 0)); // Rojo oscuro
	                etiquetaMarcaInferior.setHorizontalAlignment(SwingConstants.CENTER);
	                panelMarcaInferior.setLayout(new BorderLayout());
	                panelMarcaInferior.add(etiquetaMarcaInferior, BorderLayout.CENTER);
	                // Asegurar que el panel ocupa todo el ancho disponible
	                panelMarcaInferior.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelMarcaInferior.getPreferredSize().height));
	                panelChatContenido.add(panelMarcaInferior);
	                
	                // Guardar la posición para hacer scroll
	                posicionYMensaje[0] = panelChatContenido.getComponentCount() - 2; // Apuntar al mensaje, no a las marcas
	            } else {
	                // Si no es el mensaje buscado, lo pintamos normalmente
	                pintarBubblesMensaje(panelChatContenido, mensaje, esMio);
	            }
	        }
	    } else {
	        // Código para manejar el caso de que no haya mensajes
	        JLabel noMessagesLabel = new JLabel("No hay mensajes con este contacto.");
	        noMessagesLabel.setHorizontalAlignment(SwingConstants.CENTER);
	        noMessagesLabel.setForeground(Color.GRAY);
	        noMessagesLabel.setFont(new Font("Arial", Font.ITALIC, 14));
	        panelChatContenido.add(noMessagesLabel);
	    }
	    
	    // Refrescar la UI
	    refrescarChat();
	    cardLayout.show(panelCentro, "Chat");
	    
	    // Realizar el scroll hacia el mensaje buscado después de un retraso
	    if (posicionYMensaje[0] >= 0) {
	        // Realizar varios intentos de scroll con retrasos incrementales
	        for (int delay : new int[] {200, 500, 1000}) {
	            Timer timer = new Timer(delay, new ActionListener() {
	                @Override
	                public void actionPerformed(ActionEvent e) {
	                    try {
	                        // Obtener los componentes actuales después del refresco de la UI
	                        Component[] componentes = panelChatContenido.getComponents();
	                        
	                        if (posicionYMensaje[0] < componentes.length) {
	                            Component targetComponent = componentes[posicionYMensaje[0]];
	                            
	                            // Calcular posición en el viewport
	                            Rectangle bounds = targetComponent.getBounds();
	                            
	                            // Hacer scroll solo verticalmente (no horizontalmente)
	                            scrollPanelChatMensajes.getViewport().setViewPosition(new Point(0, bounds.y));
	                            
	                            // Asegurarse de que el panel se actualice
	                            panelChatContenido.revalidate();
	                            scrollPanelChatMensajes.revalidate();
	                        }
	                    } catch (Exception ex) {
	                        ex.printStackTrace();
	                    }
	                }
	            });
	            timer.setRepeats(false);
	            timer.start();
	        }
	    }
	}


	/**
	 * Pintar un mensaje resaltado en el chat.
	 * <p>
	 * Este método se utiliza para mostrar mensajes destacados, como emojis o
	 * mensajes resaltados, con colores específicos.
	 * 
	 * @param panel   El panel donde se añadirá la burbuja
	 * @param mensaje El mensaje a mostrar
	 * @param esMio   Indica si el mensaje fue enviado por el usuario actual
	 */
	private void pintarBubblesMensajeResaltado(JPanel panel, Mensaje mensaje, boolean esMio) {
	    String texto = mensaje.getTexto();
	    
	    if (texto != null && texto.startsWith("EMOJI:")) {
	        try {
	            int emojiCode = Integer.parseInt(texto.substring(6));
	            // Renderizar emoji con colores especiales para resaltado
	            BubbleText burbuja = new BubbleText(panel, emojiCode, 
	                    esMio ? new Color(200, 255, 200) : new Color(255, 253, 170), // Fondo verde claro o amarillo
	                    "", 
	                    esMio ? BubbleText.SENT : BubbleText.RECEIVED, 
	                    18);
	            panel.add(burbuja);
	        } catch (NumberFormatException e) {
	            // Si el código del emoji no es válido, tratarlo como texto normal
	            BubbleText burbuja = new BubbleText(panel, "[Emoji inválido]", 
	                    esMio ? new Color(200, 255, 200) : new Color(255, 253, 170),
	                    "", 
	                    esMio ? BubbleText.SENT : BubbleText.RECEIVED, 
	                    18);
	            panel.add(burbuja);
	        }
	    } else if (texto != null) {
	        // Renderizar texto normal con colores especiales para resaltado
	        BubbleText burbuja = new BubbleText(panel, texto + "  " +
	                String.format("%02d:%02d", mensaje.getHora().getHour(), mensaje.getHora().getMinute()),
	                esMio ? new Color(200, 255, 200) : new Color(255, 253, 170),
	                "", 
	                esMio ? BubbleText.SENT : BubbleText.RECEIVED, 
	                18);
	        panel.add(burbuja);
	    } else {
	        // Texto es null
	        BubbleText burbuja = new BubbleText(panel, "[Mensaje sin contenido] " +
	                String.format("%02d:%02d", mensaje.getHora().getHour(), mensaje.getHora().getMinute()),
	                esMio ? new Color(200, 255, 200) : new Color(255, 253, 170),
	                "", 
	                esMio ? BubbleText.SENT : BubbleText.RECEIVED, 
	                18);
	        panel.add(burbuja);
	    }
	}


	


}