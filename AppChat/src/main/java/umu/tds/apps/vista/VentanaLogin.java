package umu.tds.apps.vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.Dimension;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.BevelBorder;
import javax.swing.BoxLayout;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.MatteBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.LineBorder;
import java.awt.Rectangle;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import java.awt.SystemColor;
import javax.swing.AbstractAction;
import javax.swing.Action;

public class VentanaLogin {

	private JFrame frmAppchat;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField textField;
	private JPasswordField passwordField;


	/**
	 * Launch the application.
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
	 * Create the application.
	 */
	public VentanaLogin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAppchat = new JFrame();
		frmAppchat.setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaLogin.class.getResource("/umu/tds/apps/resources/icono.png")));
		frmAppchat.setTitle("AppChat");
		frmAppchat.setBounds(300, 300, 450, 261);
		frmAppchat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanelGradient gradientPanel = new JPanelGradient();
	    gradientPanel.setLayout(new BorderLayout());
	    frmAppchat.setContentPane(gradientPanel);
	    
		JPanel panelSur = new JPanel();
		panelSur.setOpaque(false);
		FlowLayout flowLayout = (FlowLayout) panelSur.getLayout();
		frmAppchat.getContentPane().add(panelSur, BorderLayout.SOUTH);
		
		JButton btnRegistrar = new JButton("Registrar");
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
		btnLogin.setBackground(new Color(240, 255, 255));
		btnLogin.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		btnLogin.setFont(new Font("Arial", Font.BOLD, 11));
		btnLogin.setIcon(null);
		btnLogin.setPreferredSize(new Dimension(115, 23));
		panelSur.add(btnLogin);
		
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
		
		JLabelWithShadow lblTelefono = new JLabelWithShadow("Teléfono:");
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
		
		JLabelWithShadow lblContrasea = new JLabelWithShadow("Contraseña:");
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
	
	@SuppressWarnings("serial")
	class JPanelGradient extends JPanel {
	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        Graphics2D g2d = (Graphics2D) g;
	        int width = getWidth();
	        int height = getHeight();

	        // Colores más suaves y armoniosos
	        Color color1 = new Color(60, 179, 113);
	        Color color2 = new Color(135, 206, 235);
	        GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2); // Degradado vertical

	        g2d.setPaint(gp);
	        g2d.fillRect(0, 0, width, height);
	    }
	}
	
	public class JLabelWithShadow extends JLabel {

	    public JLabelWithShadow(String text) {
	        super(text);
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        Graphics2D g2d = (Graphics2D) g.create();

	        // Agregar la sombra: dibuja el texto con una ligera desviación
	        g2d.setColor(new Color(0, 128, 128, 150)); 
	        g2d.drawString(getText(), getInsets().left + 2, getInsets().top + getFontMetrics(getFont()).getAscent() + 2);

	        // Dibuja el texto normal
	        g2d.setColor(getForeground());
	        g2d.drawString(getText(), getInsets().left, getInsets().top + getFontMetrics(getFont()).getAscent());

	        g2d.dispose();
	    }
	}
	//private static void addPopup(Component component, final JPopupMenu popup) {}
	
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	private class SwingAction_1 extends AbstractAction {
		public SwingAction_1() {
			putValue(NAME, "SwingAction_1");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
}
