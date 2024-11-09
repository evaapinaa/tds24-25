package umu.tds.apps.vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import umu.tds.apps.controlador.AppChat;
import umu.tds.apps.modelo.Mensaje;

import java.awt.BorderLayout;
import javax.swing.JComboBox;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.FlowLayout;
import javax.swing.border.TitledBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;

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
		setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/icono.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelNorte = new JPanel();
		panelNorte.setBorder(new LineBorder(new Color(245, 245, 245), 5, true));
		contentPane.add(panelNorte, BorderLayout.NORTH);
		panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.X_AXIS));
		
		JComboBox comboBoxContactos = new JComboBox();
		panelNorte.add(comboBoxContactos);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/enviar.png")));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panelNorte.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/lupa.png")));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panelNorte.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton(" Contactos");
		btnNewButton_2.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/personas.png")));
		panelNorte.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton(" Premium");
		btnNewButton_3.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/garantia.png")));
		panelNorte.add(btnNewButton_3);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		panelNorte.add(horizontalGlue);
		
		JLabel lblNewLabel = new JLabel("Usuario Actual");
		lblNewLabel.setIcon(null);
		panelNorte.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/avatar.png")));
		panelNorte.add(lblNewLabel_1);
		
		JPanel panelMensajes = new JPanel();
		contentPane.add(panelMensajes, BorderLayout.WEST);
		panelMensajes.setLayout(new BorderLayout(0, 0));
		
		JList<Mensaje> list = new JList<Mensaje>();
		list.setSelectionBackground(new Color(176, 224, 230));
		list.setBackground(new Color(240, 248, 255));
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
		panelMensajes.add(list, BorderLayout.WEST);
		
		JPanel panelCentro = new JPanel();
		contentPane.add(panelCentro, BorderLayout.CENTER);
		panelCentro.setLayout(new BorderLayout(0, 0));
		
		JPanel panelChat = new JPanel();
		panelChat.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelChat.setBackground(new Color(175, 238, 238));
		panelCentro.add(panelChat, BorderLayout.CENTER);
		
		JPanel panelEnvio = new JPanel();
		panelCentro.add(panelEnvio, BorderLayout.SOUTH);
		panelEnvio.setLayout(new BorderLayout(0, 0));
		
		textField = new JTextField();
		textField.setBackground(new Color(245, 245, 245));
		textField.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelEnvio.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton_4 = new JButton("Enviar");
		panelEnvio.add(btnNewButton_4, BorderLayout.EAST);
		
		
	}

}
