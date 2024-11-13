package umu.tds.apps.vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import umu.tds.apps.controlador.AppChat;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.vista.customcomponents.VisualUtils;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import javax.swing.JComboBox;
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
import javax.swing.AbstractListModel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.border.TitledBorder;

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
import java.awt.Cursor;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.JPopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtMensaje;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal frame = new VentanaPrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaPrincipal() {
		setTitle("AppChat");
		setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/icono.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 633);
		contentPane = new JPanel();
		contentPane.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, new Color(176, 196, 222), null));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelNorte = new JPanel();
		panelNorte.setBackground(new Color(0, 107, 107));
		panelNorte.setBorder(new CompoundBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), new LineBorder(new Color(0, 107, 107), 8, true)));
		contentPane.add(panelNorte, BorderLayout.NORTH);
		panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.X_AXIS));
		
		JComboBox comboBoxContactos = new JComboBox();
		panelNorte.add(comboBoxContactos);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.setFocusPainted(false);
		btnNewButton.setBackground(new Color(245, 245, 245));
		btnNewButton.setBorder(new CompoundBorder(new TitledBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), new EmptyBorder(5, 17, 5, 17)));
		btnNewButton.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/enviar.png")));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		panelNorte.add(rigidArea);
		panelNorte.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.setFocusPainted(false);
		btnNewButton_1.setBackground(new Color(245, 245, 245));
		btnNewButton_1.setBorder(new CompoundBorder(new TitledBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), new EmptyBorder(5, 17, 5, 17)));
		btnNewButton_1.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/lupa.png")));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// BUSCAR CONTACTO
				VentanaBusqueda ventanaBusqueda = new VentanaBusqueda();
				ventanaBusqueda.setVisible(true);
				
			}
		});
		panelNorte.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton(" Contactos ");
		btnNewButton_2.setFocusPainted(false);
		btnNewButton_2.setBackground(new Color(245, 245, 245));
		btnNewButton_2.setBorder(new CompoundBorder(new TitledBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), new EmptyBorder(5, 17, 5, 17)));
		btnNewButton_2.setFont(new Font("Arial", Font.PLAIN, 11));
		btnNewButton_2.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/personas.png")));
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

		        // Crear el marco que contendrá la ventana de contactos
		        JFrame frameContactos = new JFrame("Ventana de Contactos");
		        frameContactos.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Para cerrar solo esta ventana
		        frameContactos.setSize(600, 400);

		        // Crear una lista de contactos inicial o vacía
		        List<String> contactosIniciales = new ArrayList<>();
		        contactosIniciales.add("Contacto 1"); // EJEMPLO DE CONTACTO
		        
		        // Crear el panel VentanaContactos y lo añado al frame?????
		        VentanaContactos ventanaContactos = new VentanaContactos(contactosIniciales);
		        frameContactos.add(ventanaContactos);

		        // Pongo visible el frame
		        frameContactos.setVisible(true);

			}
		});
		panelNorte.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton(" Premium ");
		btnNewButton_3.setFocusPainted(false);
		btnNewButton_3.setBackground(new Color(245, 245, 245));
		btnNewButton_3.setBorder(new CompoundBorder(new TitledBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), new EmptyBorder(5, 17, 5, 17)));
		btnNewButton_3.setFont(new Font("Arial", Font.PLAIN, 11));
		btnNewButton_3.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/garantia.png")));
		panelNorte.add(btnNewButton_3);
		btnNewButton_3.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        // Abre la ventana de premium
		        VentanaPremium ventanaPremium = new VentanaPremium();
		        ventanaPremium.setVisible(true);
		    }
		});
		
		
		
		Component horizontalGlue = Box.createHorizontalGlue();
		panelNorte.add(horizontalGlue);
		
		JLabel lblNewLabel = new JLabel("Usuario Actual");
		lblNewLabel.setForeground(new Color(248, 248, 255));
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 11));
		lblNewLabel.setIcon(null);
		panelNorte.add(lblNewLabel);
		
		Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		panelNorte.add(rigidArea_1);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/avatar.png")));
		panelNorte.add(lblNewLabel_1);
		
		JPanel panelMensajes = new JPanel();
		contentPane.add(panelMensajes, BorderLayout.WEST);
		panelMensajes.setLayout(new BorderLayout(0, 0));
		
		JList<Mensaje> list = new JList<Mensaje>();
		list.setSelectionBackground(new Color(102, 205, 170));
		list.setBackground(new Color(205, 235, 234));
		list.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		list.setFont(new Font("Arial", Font.PLAIN, 11));
		list.setCellRenderer(new MensajeCellRenderer());
		list.setModel(new AbstractListModel<Mensaje>() {
			private static final long serialVersionUID = 1L;
			List<Mensaje> mensajes = AppChat.obtenerListaMensajesRecientesPorUsuario();
			
			public int getSize() {
				return mensajes.size();
			}
			public Mensaje getElementAt(int index) {
				return mensajes.get(index);
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		scrollPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelMensajes.add(scrollPane, BorderLayout.WEST);
		
		JPanel panelCentro = new JPanel();
		contentPane.add(panelCentro, BorderLayout.CENTER);
		panelCentro.setLayout(new BorderLayout(0, 0));
		
		
		JPanel panelChat = new JPanel();
		panelChat.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelCentro.add(panelChat, BorderLayout.CENTER);
		panelChat.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		// auto-scroll al final del chat
		Adjustable vertical = scrollPane_1.getVerticalScrollBar();
		final boolean[] autoScrollEnabled = {true};  // manejar el auto-scroll

		// controlar el auto-scroll
		vertical.addAdjustmentListener(new AdjustmentListener() {
		    @Override
		    public void adjustmentValueChanged(AdjustmentEvent e) {
		        if (autoScrollEnabled[0]) {
		            // Solo auto-scroll si está habilitado
		            vertical.setValue(vertical.getMaximum()); // desplaza al final
		        }
		    }
		});

		// si el usuario ajusta manualmente el scroll, desactivar el auto-scroll
		vertical.addAdjustmentListener(new AdjustmentListener() {
		    @Override
		    public void adjustmentValueChanged(AdjustmentEvent e) {
		        if (e.getValueIsAdjusting()) {
		            autoScrollEnabled[0] = false; // desactiva el auto-scroll
		        } else {
		            // si usuario está al final del chat, se reactiva el auto-scroll
		            if (vertical.getValue() + vertical.getVisibleAmount() >= vertical.getMaximum()) {
		                autoScrollEnabled[0] = true;
		            }
		        }
		    }
		});

		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_1.setBorder(null);

		
		
		
		
		panelChat.add(scrollPane_1);
		
		JPanel panelChatContenido = new VisualUtils.JPanelGradient(new Color(0, 128, 128), new Color(172, 225, 175));
		scrollPane_1.setViewportView(panelChatContenido);
		panelChatContenido.setLayout(new BoxLayout(panelChatContenido, BoxLayout.Y_AXIS));
		
		
		
		JPanel panelEnvio = new JPanel();
		panelEnvio.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelEnvio.setBackground(new Color(95, 158, 160));
		panelCentro.add(panelEnvio, BorderLayout.SOUTH);
		GridBagLayout gbl_panelEnvio = new GridBagLayout();
		gbl_panelEnvio.columnWidths = new int[]{5, 0, 0, 2, 197, 43, 2, 0};
		gbl_panelEnvio.rowHeights = new int[]{5, 21, 0, 0};
		gbl_panelEnvio.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelEnvio.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelEnvio.setLayout(gbl_panelEnvio);
		

		JButton btnNewButton_5 = new JButton("");
		btnNewButton_5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton_5.setFocusPainted(false);
		btnNewButton_5.setBackground(new Color(240, 255, 240));
		btnNewButton_5.setBorder(new CompoundBorder(new TitledBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), new EmptyBorder(3, 10, 3, 10)));
		btnNewButton_5.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/feliz.png")));
		GridBagConstraints gbc_btnNewButton_5 = new GridBagConstraints();
		gbc_btnNewButton_5.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_5.gridx = 1;
		gbc_btnNewButton_5.gridy = 1;
		panelEnvio.add(btnNewButton_5, gbc_btnNewButton_5);
		
		
		JLayeredPane layeredPane = getLayeredPane();
		
		JPanel panelEmojis = new EmojiPanel(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int numEmoji = Integer.parseInt(e.getActionCommand());
				
				// CAMBIAR USUARIO
				BubbleText burbuja=new BubbleText(panelChatContenido, numEmoji, Color.GREEN, "J.Ramón", BubbleText.SENT,18);
				panelChatContenido.add(burbuja);
			}
		});
		
		layeredPane.add(panelEmojis, JLayeredPane.POPUP_LAYER);
		
		 // Acción del botón para mostrar/ocultar el panel de emojis
        btnNewButton_5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!panelEmojis.isVisible()) {
                    // Posicionar el panel de emojis justo encima del botón
                    Point location = btnNewButton_5.getLocationOnScreen();
                    SwingUtilities.convertPointFromScreen(location, layeredPane);
                    panelEmojis.setLocation(location.x, location.y - panelEmojis.getHeight());
                    panelEmojis.setVisible(true); // Mostrar el panel de emojis
                } else {
                    panelEmojis.setVisible(false); // Oculta el panel si ya estaba visible
                }
            }
        });
		

		
		
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

		JButton btnNewButton_4 = new JButton("Enviar ");
		btnNewButton_4.setForeground(new Color(0, 128, 128));
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String textoMensaje = txtMensaje.getText();  //Obtengo el texto del mensaje
				if(!textoMensaje.trim().isEmpty()) {         // Compruebo que el mensaje no esté vacío
					
					//CREO UN MENSAJE NUEVO
					BubbleText burbuja = new BubbleText(panelChatContenido, textoMensaje, Color.GREEN, "Usuario Actual", BubbleText.SENT, 18);
		            panelChatContenido.add(burbuja);
		            
		            txtMensaje.setText(""); // Tengo que limpiar el campo después de enviarlo??
		            
		            // REFRESCO EL PANEL PARA QUE SE VEA EL MENSAJE ENVIADO
					panelChatContenido.revalidate();
					panelChatContenido.repaint();
					
					// BAJO EL SCROLL
					scrollPane_1.getVerticalScrollBar().setValue(scrollPane_1.getVerticalScrollBar().getMaximum());
				}
			}
		});
		
		// ENVIAR MENSAJE CON ENTER
		txtMensaje.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnNewButton_4.doClick();
            }
         });
		
		btnNewButton_4.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/enviar-mensaje.png")));
		btnNewButton_4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton_4.setBackground(new Color(240, 255, 240));
		btnNewButton_4.setBorder(new CompoundBorder(new TitledBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), new EmptyBorder(3, 16, 3, 16)));
		btnNewButton_4.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 12));
		btnNewButton_4.setFocusPainted(false);
		GridBagConstraints gbc_btnNewButton_4 = new GridBagConstraints();
		gbc_btnNewButton_4.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_4.gridx = 5;
		gbc_btnNewButton_4.gridy = 1;
		panelEnvio.add(btnNewButton_4, gbc_btnNewButton_4);
		
		
	}
	
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}