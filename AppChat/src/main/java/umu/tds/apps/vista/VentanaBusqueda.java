package umu.tds.apps.vista;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import umu.tds.apps.controlador.AppChat;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.Usuario;

import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.Toolkit;
import javax.swing.UIManager;



public class VentanaBusqueda extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtTexto;
	private JTextField textField_1;
	private JTextField textField_2;
	private JPanel panelMensajes = new JPanel();
	private JList<Mensaje> listaResultados;
	private VentanaPrincipal ventanaPrincipal;

	/**
	 * Create the dialog.
	 */
	public VentanaBusqueda(VentanaPrincipal ventanaPrincipal) {
	    this.ventanaPrincipal = ventanaPrincipal;
	    initialize();
	}
	
	public void initialize() {
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(VentanaBusqueda.class.getResource("/umu/tds/apps/resources/lupaGrande.png")));
		setResizable(false);
		setTitle("Búsqueda");
		setBounds(100, 100, 495, 465);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(205, 235, 234));
		contentPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(150, 150, 150)));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel panelNorte = new JPanel();
		panelNorte.setBorder(new LineBorder(new Color(47, 79, 79), 2, true));
		panelNorte.setBackground(new Color(205, 235, 234));
		contentPanel.add(panelNorte, BorderLayout.NORTH);
		panelNorte.setLayout(new BorderLayout(0, 0));
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBackground(new Color(205, 235, 234));
		lblNewLabel.setIcon(new ImageIcon(VentanaBusqueda.class.getResource("/umu/tds/apps/resources/lupaGrande.png")));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panelNorte.add(lblNewLabel);

		JPanel panelCentro = new JPanel();
		contentPanel.add(panelCentro, BorderLayout.CENTER);
		panelCentro.setLayout(new BorderLayout(0, 0));
		JPanel panelBuscar = new JPanel();
		panelBuscar.setBackground(new Color(205, 235, 234));
		panelBuscar.setFont(new Font("Arial", Font.PLAIN, 11));
		panelBuscar.setBorder(new TitledBorder(new LineBorder(new Color(47, 79, 79), 2, true), "Buscar",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelCentro.add(panelBuscar, BorderLayout.NORTH);

		GridBagLayout gbl_panelBuscar = new GridBagLayout();
		gbl_panelBuscar.columnWidths = new int[] { 10, 0, 0, 0, 0, 0, 10, 0 };
		gbl_panelBuscar.rowHeights = new int[] { 5, 0, 0, 5, 0 };
		gbl_panelBuscar.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panelBuscar.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panelBuscar.setLayout(gbl_panelBuscar);

		txtTexto = new JTextField("Texto");
		txtTexto.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		txtTexto.setBackground(new Color(255, 255, 255));
		setupPlaceholder(txtTexto, "Texto");
		GridBagConstraints gbc_txtTexto = new GridBagConstraints();
		gbc_txtTexto.gridwidth = 5;
		gbc_txtTexto.insets = new Insets(0, 0, 5, 5);
		gbc_txtTexto.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTexto.gridx = 1;
		gbc_txtTexto.gridy = 1;
		panelBuscar.add(txtTexto, gbc_txtTexto);

		textField_1 = new JTextField("Teléfono");
		textField_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textField_1.setBackground(new Color(255, 255, 255));
		setupPlaceholder(textField_1, "Teléfono");
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 2;
		panelBuscar.add(textField_1, gbc_textField_1);

		textField_2 = new JTextField("Contacto");
		textField_2.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textField_2.setBackground(new Color(255, 255, 255));
		setupPlaceholder(textField_2, "Contacto");
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 3;
		gbc_textField_2.gridy = 2;
		panelBuscar.add(textField_2, gbc_textField_2);

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.setBorder(UIManager.getBorder("Button.border"));
		btnBuscar.setBackground(new Color(0, 107, 95));
		btnBuscar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnBuscar.setFocusPainted(false);
		btnBuscar.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 11));
		btnBuscar.setForeground(new Color(255, 255, 255));
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 5;
		gbc_btnNewButton.gridy = 2;
		panelBuscar.add(btnBuscar, gbc_btnNewButton);
		btnBuscar.addActionListener(e -> {
			buscarMensajes();
		});

		// foco inicial al abrir la ventana, en el botón de buscar y así poder ver el
		// texto de los textfields
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				btnBuscar.requestFocusInWindow();
			}
		});

		JPanel panel = new JPanel();
		panelCentro.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(new Color(205, 235, 234));
		scrollPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel.add(scrollPane);

		panelMensajes = new JPanel();
		panelMensajes.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelMensajes.setFont(new Font("Arial", Font.PLAIN, 11));
		scrollPane.setViewportView(panelMensajes);

		// Lista de resultados (Mensajes) con el renderer
		listaResultados = new JList<>();
		listaResultados.setCellRenderer(new BusquedaCellRenderer());

		JScrollPane scroll = new JScrollPane(listaResultados);
		scroll.setBorder(BorderFactory.createTitledBorder("Resultados de la Búsqueda"));
		panelCentro.add(scroll, BorderLayout.CENTER);
		
		// al pulsar tecla enter, se pulsa el boton de buscar
		getRootPane().setDefaultButton(btnBuscar);
		
		listaResultados.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        if (e.getClickCount() == 2) { // Double-click para abrir chat
		            int index = listaResultados.locationToIndex(e.getPoint());
		            if (index >= 0) {
		                Mensaje mensajeSeleccionado = listaResultados.getModel().getElementAt(index);

		                // Determina el usuario con el que abrir el chat
		                Usuario emisor = mensajeSeleccionado.getEmisor();
		                Usuario receptor = mensajeSeleccionado.getReceptor();
		                Usuario otroUsuario = emisor.equals(AppChat.getUsuarioActual()) ? receptor : emisor;

		                // Navega al chat y enfoca el mensaje seleccionado
		                ventanaPrincipal.cargarMensajeEnChat(otroUsuario, mensajeSeleccionado);

		                // Cambiar la tarjeta para mostrar el chat
		                CardLayout cardLayout = (CardLayout) ventanaPrincipal.getPanelCentro().getLayout();
		                cardLayout.show(ventanaPrincipal.getPanelCentro(), "Chat");

		                // Cierra la ventana de búsqueda
		                VentanaBusqueda.this.dispose();
		            }
		        }
		    }
		});


		
	}

	// comportamiento de placeholder en un JTextField
	private void setupPlaceholder(JTextField textField, String placeholder) {
		textField.setForeground(Color.GRAY);
		textField.setFont(new Font("Arial", Font.ITALIC, 14));
		textField.setText(placeholder);

		textField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (textField.getText().equals(placeholder)) {
					textField.setText("");
					textField.setForeground(Color.BLACK);
					textField.setFont(new Font("Arial", Font.PLAIN, 14));
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (textField.getText().isEmpty()) {
					textField.setForeground(Color.GRAY);
					textField.setFont(new Font("Arial", Font.ITALIC, 14));
					textField.setText(placeholder);
				}
			}
		});
	}

	private void buscarMensajes() {
		String texto = txtTexto.getText();
		String telefono = textField_1.getText();
		String contacto = textField_2.getText();

		// Reemplazar los placeholders por cadenas vacías si aún están presentes
		if (texto.equals("Texto"))
			texto = "";
		if (telefono.equals("Teléfono"))
			telefono = "";
		if (contacto.equals("Contacto"))
			contacto = "";

		AppChat controlador = AppChat.getUnicaInstancia();
		List<Mensaje> resultados = controlador.filtrarMensajes(texto, telefono, contacto);

		System.out.println("Resultados encontrados: " + resultados.size());
		for (Mensaje mensaje : resultados) {
			System.out.println(mensaje.getTexto()); // Muestra los mensajes en consola
		}

		// Refrescar la lista
		listaResultados.setListData(resultados.toArray(new Mensaje[0]));
		refrescar();
	}

	private void refrescar() {
		panelMensajes.revalidate();
		panelMensajes.repaint();
	}
	
}
