package umu.tds.apps.vista;

import javax.swing.*;
import tds.BubbleText;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.border.EtchedBorder;


/**
 * Panel que muestra una colección de emojis para seleccionar.
 * Esta clase implementa un panel gráfico que permite al usuario seleccionar
 * emojis para insertarlos en mensajes de chat.
 * 
 * <p>El panel se organiza como una cuadrícula de emojis clickeables, donde cada
 * emoji activa un ActionListener cuando es seleccionado.</p>
 * 
 * @author TDS-2025
 * @version 1.0
 */
public class EmojiPanel extends JPanel {
	
    /**
     * Identificador de serialización.
     */
	private static final long serialVersionUID = 1L;

	
	
    /**
     * Constructor que inicializa y configura el panel de emojis.
     * 
     * @param emojiClickListener El ActionListener que será notificado cuando un emoji sea seleccionado
     */
	public EmojiPanel(ActionListener emojiClickListener) {
        setLayout(new GridLayout(5,3,0,0)); // Grid para organizar los emojis
        setBackground(new Color(0, 128, 128));
        setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        setSize(265, 250); // Tamaño del panel de emojis
        setVisible(false); // Oculto inicialmente

        // Bucle para añadir emojis
        for (int i = 0; i < 26; i++) {
            JLabel emoji = new JLabel();
            emoji.setIcon(BubbleText.getEmoji(i)); // Icono del emoji
            emoji.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cursor de mano para indicar clic
            
            final int emojiIndex = i; // Variable final para capturar el índice del emoji
            
            emoji.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    // Llama al ActionListener proporcionado con el índice del emoji seleccionado
                    emojiClickListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, String.valueOf(emojiIndex)));
                    setVisible(false); // Ocultar el panel después de seleccionar
                }
            });
            
            add(emoji);
        }
    }
}
