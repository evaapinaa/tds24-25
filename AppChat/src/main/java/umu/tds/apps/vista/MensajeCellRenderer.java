package umu.tds.apps.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
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

public class MensajeCellRenderer extends JPanel implements ListCellRenderer<Mensaje> {
    private static final long serialVersionUID = 1L;

    private JLabel nameLabel;
    private JLabel messageLabel;
    private JLabel imageLabel;

    // Ruta para guardar los avatares localmente
    private static final String AVATAR_FOLDER = "avatars/";

    public MensajeCellRenderer() {
        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5)); // Añadir márgenes para separación

        nameLabel = new JLabel();
        messageLabel = new JLabel();
        imageLabel = new JLabel();

        add(imageLabel, BorderLayout.WEST);
        add(nameLabel, BorderLayout.NORTH);
        add(messageLabel, BorderLayout.CENTER);

        // Crear la carpeta de avatares si no existe
        File avatarDir = new File(AVATAR_FOLDER);
        if (!avatarDir.exists()) {
            avatarDir.mkdirs();
        }
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Mensaje> list, Mensaje mensaje, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        // Set the text
        nameLabel.setText(mensaje.getEmisor().getUsuario());
        messageLabel.setText(mensaje.getTexto() + "  " + mensaje.getHora().getHour() + ":" + mensaje.getHora().getMinute());

        // OPTIMIZACIÓN: No se descarga la imagen si no se muestra
        // avatar desde caché o descarga si no existe
        String avatarPath = AVATAR_FOLDER + mensaje.getEmisor().getUsuario() + ".png";
        File avatarFile = new File(avatarPath);

        if (avatarFile.exists()) {
            // carga imagen desde caché
            imageLabel.setIcon(new ImageIcon(avatarPath));
        } else {
            // sino, descarga imagen y la guarda en cach
            new Thread(() -> {
                try {
                    URL imageUrl = new URL("https://robohash.org/" + mensaje.getEmisor().getUsuario() + "?size=50x50");
                    Image image = ImageIO.read(imageUrl);

                    // la guarda en caché
                    ImageIO.write((ImageIO.read(imageUrl)), "png", avatarFile);

                    // actualizar la imagen en la interfaz
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        imageLabel.setIcon(new ImageIcon(image.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
                        list.repaint(); // refresca lista
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    imageLabel.setIcon(null); // Default icon if error
                }
            }).start();
            
            
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
