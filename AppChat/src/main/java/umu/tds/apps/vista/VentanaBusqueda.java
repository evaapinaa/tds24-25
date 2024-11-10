package umu.tds.apps.vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.ScrollPaneConstants;

public class VentanaBusqueda extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			VentanaBusqueda dialog = new VentanaBusqueda();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public VentanaBusqueda() {
		setResizable(false);
		setTitle("Búsqueda");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panelNorte = new JPanel();
			contentPanel.add(panelNorte, BorderLayout.NORTH);
			panelNorte.setLayout(new BorderLayout(0, 0));
			{
				JLabel lblNewLabel = new JLabel("LUPA IMAGEN");
				lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
				panelNorte.add(lblNewLabel);
			}
		}
		{
			JPanel panelCentro = new JPanel();
			contentPanel.add(panelCentro, BorderLayout.CENTER);
			panelCentro.setLayout(new BorderLayout(0, 0));
			{
				JPanel panelBuscar = new JPanel();
				panelBuscar.setBorder(new TitledBorder(null, "Buscar", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				panelCentro.add(panelBuscar, BorderLayout.NORTH);
				GridBagLayout gbl_panelBuscar = new GridBagLayout();
				gbl_panelBuscar.columnWidths = new int[]{5, 0, 0, 0, 0, 0, 5, 0};
				gbl_panelBuscar.rowHeights = new int[]{5, 0, 0, 5, 0};
				gbl_panelBuscar.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
				gbl_panelBuscar.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
				panelBuscar.setLayout(gbl_panelBuscar);
				{
					textField = new JTextField();
					GridBagConstraints gbc_textField = new GridBagConstraints();
					gbc_textField.gridwidth = 5;
					gbc_textField.insets = new Insets(0, 0, 5, 5);
					gbc_textField.fill = GridBagConstraints.HORIZONTAL;
					gbc_textField.gridx = 1;
					gbc_textField.gridy = 1;
					panelBuscar.add(textField, gbc_textField);
					textField.setColumns(10);
				}
				{
					textField_1 = new JTextField();
					GridBagConstraints gbc_textField_1 = new GridBagConstraints();
					gbc_textField_1.insets = new Insets(0, 0, 5, 5);
					gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
					gbc_textField_1.gridx = 1;
					gbc_textField_1.gridy = 2;
					panelBuscar.add(textField_1, gbc_textField_1);
					textField_1.setColumns(10);
				}
				{
					textField_2 = new JTextField();
					GridBagConstraints gbc_textField_2 = new GridBagConstraints();
					gbc_textField_2.insets = new Insets(0, 0, 5, 5);
					gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
					gbc_textField_2.gridx = 3;
					gbc_textField_2.gridy = 2;
					panelBuscar.add(textField_2, gbc_textField_2);
					textField_2.setColumns(10);
				}
				{
					JButton btnNewButton = new JButton("Buscar");
					GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
					gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
					gbc_btnNewButton.gridx = 5;
					gbc_btnNewButton.gridy = 2;
					panelBuscar.add(btnNewButton, gbc_btnNewButton);
				}
			}
			{
				JPanel panel = new JPanel();
				panelCentro.add(panel, BorderLayout.CENTER);
				panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
				{
					JScrollPane scrollPane = new JScrollPane();
					scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					panel.add(scrollPane);
					{
						JPanel panel_1 = new JPanel();
						scrollPane.setViewportView(panel_1);
					}
				}
			}
		}
	}

}
