package umu.tds.apps.vista;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.List;


import tds.BubbleText;
import umu.tds.apps.controlador.AppChat;
import umu.tds.apps.modelo.Chat;
import umu.tds.apps.modelo.ContactoIndividual;
import umu.tds.apps.modelo.Grupo;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.Usuario;
import umu.tds.apps.vista.customcomponents.VisualUtils;

/**
 * Muestra en cada celda:
 * - Foto de perfil (o icono por defecto)
 * - Nombre del emisor o grupo
 * - Texto del mensaje / preview
 * - Botón '+' si no está en contactos (solo para Chat).
 */
public class ChatCellRenderer extends JPanel implements ListCellRenderer<Object> {

    private static final long serialVersionUID = 1L;

    private JLabel imageLabel;      // Foto de perfil (usuario o grupo)
    private JLabel nameLabel;       // Nombre de contacto o nombre de grupo
    private JLabel previewLabel;    // Último mensaje (texto o emoji) o "Sin historial"
    private JButton addButton;      
    private Rectangle addButtonBounds;

    
    /**
     * Constructor que inicializa y configura el renderizador de celdas para chats y grupos.
     * Establece el diseño del panel, crea las etiquetas y botones necesarios, y configura
     * los estilos visuales iniciales.
     */
    public ChatCellRenderer() {
        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        imageLabel = new JLabel();
        nameLabel = new JLabel();
        previewLabel = new JLabel();
        
        // boton de agregar
        addButton = new JButton("+");
        addButton.setToolTipText("Añadir a contactos");
        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        addButton.setForeground(new Color(0, 128, 128));
        addButtonBounds = new Rectangle();

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setOpaque(false);
        topPanel.add(imageLabel, BorderLayout.WEST);
        topPanel.add(nameLabel, BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(previewLabel, BorderLayout.CENTER);
        
        
    }
    
    /**
     * Obtiene el botón para añadir contactos.
     * 
     * @return El botón de añadir contacto
     */
    public JButton getAddButton() {
        return addButton;
    }

    
    /**
     * Implementación del método de la interfaz ListCellRenderer que configura la apariencia
     * de cada elemento de la lista (Chat o Grupo).
     * 
     * @param list La lista que contiene el elemento
     * @param value El objeto a renderizar (Chat o Grupo)
     * @param index El índice del elemento en la lista
     * @param isSelected Indica si el elemento está seleccionado
     * @param cellHasFocus Indica si el elemento tiene el foco
     * @return El componente configurado para mostrar el elemento
     */
    @Override
    public Component getListCellRendererComponent(
            JList<? extends Object> list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus)
    {
        // 1) Comprobamos si 'value' es un Chat o un Grupo
        if (value instanceof Chat) {
            Chat chat = (Chat) value;
            renderChat(list, chat, index, isSelected);
        } 
        else if (value instanceof Grupo) {
            Grupo grupo = (Grupo) value;
            renderGrupo(list, grupo, index, isSelected);
        } 
        else {
            // Fallback si aparece otro tipo de objeto
            setBackground(list.getBackground());
            nameLabel.setText("(Objeto desconocido)");
            previewLabel.setText("");
            imageLabel.setIcon(null);
            addButton.setVisible(false);

            // Colores de selección
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                nameLabel.setForeground(list.getSelectionForeground());
                previewLabel.setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                nameLabel.setForeground(list.getForeground());
                previewLabel.setForeground(Color.DARK_GRAY);
            }
        }

        // Verificar si debemos mostrar el botón de añadir (solo para Chats)
        if (value instanceof Chat) {
            Chat chat = (Chat) value;
            Usuario usuarioActual = AppChat.getUsuarioActual();
            Usuario usuarioOtro = chat.getUsuario().equals(usuarioActual) ? 
                                  chat.getOtroUsuarioChat() : chat.getUsuario();
            
            // Verificar si ya está en contactos
            ContactoIndividual contactoExistente = AppChat.getUnicaInstancia()
                    .obtenerContactoPorTelefono(usuarioOtro.getTelefono());
            
            // Mostrar u ocultar el botón según corresponda
            addButton.setVisible(contactoExistente == null);
        } else {
            // No es un Chat, no necesitamos botón
            addButton.setVisible(false);
        }

        return this;
    }

    /**
     * Renderiza un objeto de tipo Chat mostrando la información del usuario, 
     * su foto de perfil y el último mensaje intercambiado.
     * 
     * @param list La lista que contiene el elemento
     * @param chat El objeto Chat a renderizar
     * @param index El índice del elemento en la lista
     * @param isSelected Indica si el elemento está seleccionado
     */
    private void renderChat(JList<?> list, Chat chat, int index, boolean isSelected) {
        // 1. Usuario actual y "otro usuario"
        Usuario usuarioActual = AppChat.getUsuarioActual();
        Usuario usuarioOtro = (chat.getUsuario().equals(usuarioActual))
                ? chat.getOtroUsuarioChat()
                : chat.getUsuario();

        // 2. Nombre o teléfono
        ContactoIndividual contacto = AppChat.getUnicaInstancia()
                .obtenerContactoPorTelefono(usuarioOtro.getTelefono());
        if (contacto != null) {
            nameLabel.setText(contacto.getNombre());
            addButton.setVisible(false);
        } else {
            nameLabel.setText(usuarioOtro.getTelefono());
            addButton.setVisible(true);
        }

        // 3. Foto de perfil del otro usuario
        String path = usuarioOtro.getImagenPerfil().getDescription();
        setProfileImage(path);

        // 4. Último mensaje
        List<Mensaje> mensajes = chat.getMensajes();
        if (!mensajes.isEmpty()) {
            Mensaje ultimo = mensajes.get(mensajes.size() - 1);
            String texto = ultimo.getTexto();
            String horaStr = String.format("%02d:%02d",
                    ultimo.getHora().getHour(), 
                    ultimo.getHora().getMinute());

            if (texto != null && texto.startsWith("EMOJI:")) {
                try {
                    int emojiCode = Integer.parseInt(texto.substring(6));
                    ImageIcon emojiIcon = BubbleText.getEmoji(emojiCode);
                    if (emojiIcon != null) {
                        previewLabel.setIcon(emojiIcon);
                        previewLabel.setText(" " + horaStr);
                    } else {
                        previewLabel.setText("[Emoji] " + horaStr);
                        previewLabel.setIcon(null);
                    }
                } catch (NumberFormatException e) {
                    previewLabel.setText(texto + "  " + horaStr);
                    previewLabel.setIcon(null);
                }
            } else if (texto != null) {
                // Acortar si es muy largo
                if (texto.length() > 20) {
                    texto = texto.substring(0, 20) + "...";
                }
                previewLabel.setText(texto + "  " + horaStr);
                previewLabel.setIcon(null);
            } else {
                previewLabel.setText("[Mensaje sin contenido] " + horaStr);
                previewLabel.setIcon(null);
            }
        } else {
            previewLabel.setText("No hay mensajes");
            previewLabel.setIcon(null);
        }

        // 5. Colores de selección
        applySelectionColors(list, isSelected);
    }

    /**
     * Renderiza un objeto de tipo Grupo mostrando su nombre, imagen y
     * una vista previa del último mensaje si existe.
     * 
     * @param list La lista que contiene el elemento
     * @param grupo El objeto Grupo a renderizar
     * @param index El índice del elemento en la lista
     * @param isSelected Indica si el elemento está seleccionado
     */
    private void renderGrupo(JList<?> list, Grupo grupo, int index, boolean isSelected) {
        // Asigna nombre del grupo
        nameLabel.setText(grupo.getNombreGrupo());
        addButton.setVisible(false);  // No tiene sentido el botón '+' en un Grupo

        // Imagen del grupo
        ImageIcon iconGrupo = grupo.getImagenGrupo(); 
        if (iconGrupo != null) {
            // Si quieres que sea circular:
            try {
                Image img = iconGrupo.getImage();
                BufferedImage buffImg;
                if (img instanceof BufferedImage) {
                    buffImg = (BufferedImage) img;
                } else {
                    // Crear BufferedImage desde Image
                    buffImg = new BufferedImage(
                        img.getWidth(null), 
                        img.getHeight(null), 
                        BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = buffImg.createGraphics();
                    g2d.drawImage(img, 0, 0, null);
                    g2d.dispose();
                }
                imageLabel.setIcon(VisualUtils.createCircularIcon(buffImg, 50));
            } catch (Exception e) {
                // Si hay error, mostrar el icono tal cual
                imageLabel.setIcon(iconGrupo);
            }
        } else {
            // Sin imagen => usar icono por defecto
            imageLabel.setIcon(new ImageIcon(ChatCellRenderer.class.getResource("/umu/tds/apps/resources/personas.png")));
        }

        // Obtener el último mensaje del grupo para mostrar en la previsualización
        List<Mensaje> mensajes = grupo.getListaMensajesEnviados();
        if (mensajes != null && !mensajes.isEmpty()) {
            // Encontrar el mensaje más reciente
            Mensaje ultimoMensaje = mensajes.get(mensajes.size() - 1);
            String texto = ultimoMensaje.getTexto();
            String horaStr = String.format("%02d:%02d",
                    ultimoMensaje.getHora().getHour(), 
                    ultimoMensaje.getHora().getMinute());

            // Mostrar el último mensaje (texto o emoji)
            if (texto != null && texto.startsWith("EMOJI:")) {
                try {
                    int emojiCode = Integer.parseInt(texto.substring(6));
                    ImageIcon emojiIcon = BubbleText.getEmoji(emojiCode);
                    if (emojiIcon != null) {
                        previewLabel.setIcon(emojiIcon);
                        previewLabel.setText(" " + horaStr);
                    } else {
                        previewLabel.setText("[Emoji] " + horaStr);
                        previewLabel.setIcon(null);
                    }
                } catch (NumberFormatException e) {
                    previewLabel.setText(texto + "  " + horaStr);
                    previewLabel.setIcon(null);
                }
            } else if (texto != null) {
                // Acortar si es muy largo
                if (texto.length() > 20) {
                    texto = texto.substring(0, 20) + "...";
                }
                previewLabel.setText(texto + "  " + horaStr);
                previewLabel.setIcon(null);
            } else {
                previewLabel.setText("[Mensaje sin contenido] " + horaStr);
                previewLabel.setIcon(null);
            }
        } else {
            previewLabel.setText("Sin historial"); 
            previewLabel.setIcon(null);
        }

        // Colores de selección
        applySelectionColors(list, isSelected);
    }

    /**
     * Aplica los colores y estilos visuales dependiendo de si el elemento está seleccionado o no.
     * 
     * @param list La lista que contiene el elemento
     * @param isSelected Indica si el elemento está seleccionado
     */
    private void applySelectionColors(JList<?> list, boolean isSelected) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            nameLabel.setForeground(list.getSelectionForeground());
            previewLabel.setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            nameLabel.setForeground(list.getForeground());
            previewLabel.setForeground(Color.DARK_GRAY);
        }
    }

    /**
     * Carga y procesa una imagen de perfil desde una ruta o URL,
     * la transforma en una imagen circular y la asigna al imageLabel.
     * 
     * @param path La ruta o URL de la imagen a cargar
     */
    @SuppressWarnings("deprecation")
    private void setProfileImage(String path) {
        try {
            BufferedImage image;
            if (path.startsWith("http")) {
                // Si es una URL, cargar la imagen desde la URL
                image = ImageIO.read(new URL(path));
            } else if (path.startsWith("/")) {
                // Si empieza por '/', se asume que es un recurso del classpath
                image = ImageIO.read(VentanaRegistro.class.getResource(path));
            } else {
                // Sino, se asume que es una ruta de archivo en el sistema
                image = ImageIO.read(new File(path));
            }

            ImageIcon icon = VisualUtils.createCircularIcon(image, 50);
            imageLabel.setIcon(icon);

        } catch (IOException e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
            e.printStackTrace();
            imageLabel.setIcon(null);
        }
    }

    
    /**
     * Sobrescribe el método paintComponent para dibujar elementos gráficos personalizados,
     * como una línea separadora entre cada celda de la lista.
     * 
     * @param g El contexto gráfico utilizado para pintar
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // línea separadora al final de cada celda
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
    }

    
    /**
     * Obtiene los límites del botón de añadir contacto.
     * 
     * @return Un objeto Rectangle con las coordenadas y dimensiones del botón
     */
    public Rectangle getAddButtonBounds() {
        return addButtonBounds;
    }
}
