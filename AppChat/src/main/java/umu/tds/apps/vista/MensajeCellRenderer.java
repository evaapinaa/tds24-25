package umu.tds.apps.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import umu.tds.apps.modelo.Mensaje;

public class MensajeCellRenderer extends JPanel 
		implements ListCellRenderer<Mensaje> {
	private JLabel nameLabel;
	private JLabel messageLabel;
	private JLabel imageLabel;

	public MensajeCellRenderer() {
		setLayout(new BorderLayout(5, 5));
		setBorder(new EmptyBorder(5, 5, 5, 5)); // Añadir márgenes para separación
		
		nameLabel = new JLabel();
		messageLabel = new JLabel();
		imageLabel = new JLabel();

		add(imageLabel, BorderLayout.WEST);
		add(nameLabel, BorderLayout.NORTH);
		add(messageLabel, BorderLayout.CENTER);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Mensaje> list, Mensaje mensaje, int index,
			boolean isSelected, boolean cellHasFocus) {
		// Set the text
		nameLabel.setText(mensaje.getEmisor().getUsuario());

		// Load the image from a random URL (for example, using "https://robohash.org")
		try {
			URL imageUrl = new URL("https://robohash.org/" + mensaje.getEmisor().getUsuario() + "?size=50x50");
			
			Image image = ImageIO.read(imageUrl);
			ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
			imageLabel.setIcon(imageIcon);
			messageLabel.setText(mensaje.getTexto());
			
			
		} catch (IOException e) {
			e.printStackTrace();
			imageLabel.setIcon(null); // Default to no image if there was an issue
		}

		// Set background and foreground based on selection
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		return this;
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dibuja una línea separadora en la parte inferior de cada celda
        g.setColor(Color.lightGray);
        g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
    }
}