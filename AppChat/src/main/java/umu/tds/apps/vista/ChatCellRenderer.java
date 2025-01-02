package umu.tds.apps.vista;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import tds.BubbleText;
import umu.tds.apps.controlador.AppChat;
import umu.tds.apps.modelo.Chat;
import umu.tds.apps.modelo.ContactoIndividual;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.Usuario;
import umu.tds.apps.vista.customcomponents.VisualUtils;

/**
 * Muestra en cada celda: 
 * - Foto de perfil (o icono por defecto) 
 * - Nombre del emisor si está en contactos; sino, su telefono 
 * - Texto del mensaje + hora 
 * - Botón '+' si no está en contactos
 */
public class ChatCellRenderer extends JPanel implements ListCellRenderer<Chat> {
	private static final long serialVersionUID = 1L;

	private JLabel imageLabel;
	private JLabel nameLabel;
	private JLabel previewLabel;
	private JButton addButton;
	private Rectangle addButtonBounds;
	
	public ChatCellRenderer() {
		setLayout(new BorderLayout(5, 5));
		setBorder(new EmptyBorder(5, 5, 5, 5));

		imageLabel = new JLabel();
		nameLabel = new JLabel();
		previewLabel = new JLabel();
		addButton = new JButton("+");
		addButtonBounds = new Rectangle();

		JPanel topPanel = new JPanel(new BorderLayout(5, 5));
		topPanel.setOpaque(false);
		topPanel.add(imageLabel, BorderLayout.WEST);
		topPanel.add(nameLabel, BorderLayout.CENTER);
		topPanel.add(addButton, BorderLayout.EAST);

		add(topPanel, BorderLayout.NORTH);
		add(previewLabel, BorderLayout.CENTER);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Component getListCellRendererComponent(JList<? extends Chat> list, Chat chat, int index, boolean isSelected,
			boolean cellHasFocus) {
		// 1. Usuario actual y "otro usuario"
		Usuario usuarioActual = AppChat.getUsuarioActual();
		Usuario usuarioOtro = (chat.getUsuario().equals(usuarioActual)) ? chat.getOtroUsuarioChat() : chat.getUsuario();

		// 2. Nombre o teléfono
		ContactoIndividual contacto = AppChat.getUnicaInstancia().obtenerContactoPorTelefono(usuarioOtro.getTelefono());
		if (contacto != null) {
			nameLabel.setText(contacto.getNombre());
			addButton.setVisible(false);
		} else {
			nameLabel.setText(usuarioOtro.getTelefono());
			addButton.setVisible(true);
		}

		// 3. Foto de perfil

		// si es url
		String path = usuarioOtro.getImagenPerfil().getDescription();

		try {
		    BufferedImage image;

		    if (path.startsWith("http")) {
		        // Si es una URL, carga la imagen desde la URL
		        image = ImageIO.read(new URL(path));
		    } else {
		        // Si no es una URL, carga la imagen desde un archivo local
		        image = ImageIO.read(new File(path));
		    }

		    // Crea un icono circular con la imagen cargada
		    ImageIcon icon = VisualUtils.createCircularIcon(image, 50);
		    imageLabel.setIcon(icon);

		} catch (IOException e) {
		    // Manejo de excepciones
		    System.err.println("Error al cargar la imagen: " + e.getMessage());
		    e.printStackTrace();
		}


	// 4. Último mensaje (si es largo, no se muestra completo)
	    java.util.List<Mensaje> mensajes = chat.getMensajes();
	    if (!mensajes.isEmpty()) {
	        Mensaje ultimo = mensajes.get(mensajes.size() - 1);
	        String texto = ultimo.getTexto();
	        String horaStr = String.format("%02d:%02d", ultimo.getHora().getHour(), ultimo.getHora().getMinute());

	        if (texto != null && texto.startsWith("EMOJI:")) {
	            // Es un emoji
	            try {
	                int emojiCode = Integer.parseInt(texto.substring(6));
	                ImageIcon emojiIcon = BubbleText.getEmoji(emojiCode);
	                if (emojiIcon != null) {
	                    previewLabel.setIcon(emojiIcon);
	                    previewLabel.setText(" " + horaStr); // Espacio para separar el icono de la hora
	                } else {
	                    // Icono por defecto si no se encuentra el emoji
	                    previewLabel.setText("[Emoji] " + horaStr);
	                    previewLabel.setIcon(null);
	                }
	            } catch (NumberFormatException e) {
	                // Si falla el parseo, tratarlo como texto normal
	                previewLabel.setText(texto + "  " + horaStr);
	                previewLabel.setIcon(null);
	            }
	        } else if (texto != null) {
	            // Es texto
	            if (texto.length() > 20) {
	                texto = texto.substring(0, 20) + "...";
	            }
	            previewLabel.setText(texto + "  " + horaStr);
	            previewLabel.setIcon(null);
	        } else {
	            // Texto es null
	            previewLabel.setText("[Mensaje sin contenido] " + horaStr);
	            previewLabel.setIcon(null);
	        }
	    } else {
	        previewLabel.setText("No hay mensajes");
	        previewLabel.setIcon(null);
	    }
	// 5. Colores de selección
	if(isSelected)
	{
		setBackground(list.getSelectionBackground());
		nameLabel.setForeground(list.getSelectionForeground());
		previewLabel.setForeground(list.getSelectionForeground());
	}else
	{
		setBackground(list.getBackground());
		nameLabel.setForeground(list.getForeground());
		previewLabel.setForeground(Color.DARK_GRAY);
	}
	
    addButton.setSize(addButton.getPreferredSize());
    addButtonBounds.setBounds(addButton.getLocation().x, addButton.getLocation().y, 
                              addButton.getWidth(), addButton.getHeight());

	return this;
	}

	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // línea separadora al final de cada celda
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
    }
	
    public Rectangle getAddButtonBounds() {
        return addButtonBounds;
    }
}
