package umu.tds.apps.vista;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import umu.tds.apps.controlador.AppChat;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.Usuario;
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
import javax.imageio.ImageIO;
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
import javax.swing.SwingConstants;

import java.awt.Cursor;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.JPopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    // Panel (centro) donde iremos poniendo o bien “bienvenida” o bien “panel de chat”
    private JPanel panelCentro;
    
    // Panel de bienvenida (se muestra al inicio)
    private JPanel panelBienvenida;
    
    // Panel completo de chat (fondo degradado + envío de mensajes)
    private JPanel panelChat;
    
    // Panel interno dentro del chat donde se añaden las burbujas
    private JPanel panelChatContenido;
    
    private JTextField txtMensaje;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                VentanaPrincipal frame = new VentanaPrincipal();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public VentanaPrincipal() {
        setTitle("AppChat");
        setIconImage(Toolkit.getDefaultToolkit()
                   .getImage(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/icono.png")));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 633);

        contentPane = new JPanel();
        contentPane.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, 
                                                  new Color(176, 196, 222), null));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        
        /* ------------------------------------------------------------------
         * Panel superior (Norte)
         * ------------------------------------------------------------------ */
        JPanel panelNorte = new JPanel();
        panelNorte.setBackground(new Color(0, 107, 107));
        panelNorte.setBorder(new CompoundBorder(
                new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED,
                           new Color(255, 255, 255), new Color(160, 160, 160)),
                           "", TitledBorder.LEADING, TitledBorder.TOP,
                           null, new Color(0, 0, 0)),
                new LineBorder(new Color(0, 107, 107), 8, true)));
        contentPane.add(panelNorte, BorderLayout.NORTH);
        panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.X_AXIS));
        
        JComboBox<String> comboBoxContactos = new JComboBox<>();
        panelNorte.add(comboBoxContactos);
        
        // Botones de la barra superior
        JButton btnNewButton = new JButton("");
        btnNewButton.setFocusPainted(false);
        btnNewButton.setBackground(new Color(245, 245, 245));
        btnNewButton.setBorder(new CompoundBorder(
                new TitledBorder(new TitledBorder(
                    new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255),
                                     new Color(160, 160, 160)), "",
                    TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)),
                                 "", TitledBorder.LEADING, TitledBorder.TOP,
                                 null, new Color(0, 0, 0)),
                new EmptyBorder(5, 17, 5, 17)));
        btnNewButton.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/enviar.png")));
        btnNewButton.addActionListener(e -> {
            // Acciones del botón si deseas
        });
        
        Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
        panelNorte.add(rigidArea);
        panelNorte.add(btnNewButton);
        
        JButton btnNewButton_1 = new JButton("");
        btnNewButton_1.setFocusPainted(false);
        btnNewButton_1.setBackground(new Color(245, 245, 245));
        btnNewButton_1.setBorder(new CompoundBorder(
                new TitledBorder(new TitledBorder(
                    new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255),
                                     new Color(160, 160, 160)), "",
                    TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)),
                                 "", TitledBorder.LEADING, TitledBorder.TOP,
                                 null, new Color(0, 0, 0)),
                new EmptyBorder(5, 17, 5, 17)));
        btnNewButton_1.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/lupa.png")));
        btnNewButton_1.addActionListener(e -> {
            // BUSCAR CONTACTO
            VentanaBusqueda ventanaBusqueda = new VentanaBusqueda();
            ventanaBusqueda.setVisible(true);
        });
        panelNorte.add(btnNewButton_1);
        
        JButton btnNewButton_2 = new JButton(" Contactos ");
        btnNewButton_2.setFocusPainted(false);
        btnNewButton_2.setBackground(new Color(245, 245, 245));
        btnNewButton_2.setBorder(new CompoundBorder(
                new TitledBorder(new TitledBorder(
                    new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255),
                                     new Color(160, 160, 160)), "",
                    TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)),
                                 "", TitledBorder.LEADING, TitledBorder.TOP,
                                 null, new Color(0, 0, 0)),
                new EmptyBorder(5, 17, 5, 17)));
        btnNewButton_2.setFont(new Font("Arial", Font.PLAIN, 11));
        btnNewButton_2.setIcon(new ImageIcon(VentanaPrincipal.class
                                   .getResource("/umu/tds/apps/resources/personas.png")));
        btnNewButton_2.addActionListener(e -> {
            // Crear el marco que contendrá la ventana de contactos
            JFrame frameContactos = new JFrame("Ventana de Contactos");
            frameContactos.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
            frameContactos.setSize(600, 400);

            // Lista de contactos inicial o vacía
            List<String> contactosIniciales = new ArrayList<>();
            contactosIniciales.add("Contacto 1"); // EJEMPLO

            // Crear el panel VentanaContactos y añadir al frame
            VentanaContactos ventanaContactos = new VentanaContactos(contactosIniciales);
            frameContactos.add(ventanaContactos);
            frameContactos.setVisible(true);
        });
        panelNorte.add(btnNewButton_2);
        
        JButton btnNewButton_3 = new JButton(" Premium ");
        btnNewButton_3.setFocusPainted(false);
        btnNewButton_3.setBackground(new Color(245, 245, 245));
        btnNewButton_3.setBorder(new CompoundBorder(
                new TitledBorder(new TitledBorder(
                    new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255),
                                     new Color(160, 160, 160)), "",
                    TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)),
                                 "", TitledBorder.LEADING, TitledBorder.TOP,
                                 null, new Color(0, 0, 0)),
                new EmptyBorder(5, 17, 5, 17)));
        btnNewButton_3.setFont(new Font("Arial", Font.PLAIN, 11));
        btnNewButton_3.setIcon(new ImageIcon(VentanaPrincipal.class
                                   .getResource("/umu/tds/apps/resources/garantia.png")));
        btnNewButton_3.addActionListener(e -> {
            // Abre la ventana de premium
            VentanaPremium ventanaPremium = new VentanaPremium();
            ventanaPremium.setVisible(true);
        });
        panelNorte.add(btnNewButton_3);

        // Usuario actual en la esquina derecha
        Usuario usuarioActual = AppChat.getUsuarioActual();
        
        Component horizontalGlue = Box.createHorizontalGlue();
        panelNorte.add(horizontalGlue);
        
        JLabel lblNewLabel = new JLabel(usuarioActual.getUsuario());
        lblNewLabel.setForeground(new Color(248, 248, 255));
        lblNewLabel.setFont(new Font("Arial", Font.BOLD, 11));
        lblNewLabel.setIcon(null);
        panelNorte.add(lblNewLabel);
        
        Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
        panelNorte.add(rigidArea_1);
        
        JLabel lblNewLabel_1 = new JLabel("");
        String path = usuarioActual.getImagenPerfil().getDescription();
        cargarImagenPerfil(lblNewLabel_1, path);
        panelNorte.add(lblNewLabel_1);

        /* ------------------------------------------------------------------
         * Panel IZQUIERDO (Lista de Mensajes Recientes)
         * ------------------------------------------------------------------ */
        JPanel panelMensajes = new JPanel(new BorderLayout());
        contentPane.add(panelMensajes, BorderLayout.WEST);
        
        JList<Mensaje> list = new JList<>();
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

        /* ------------------------------------------------------------------
         * Panel CENTRO: por defecto mostramos “panelBienvenida”.
         * Cuando se seleccione un chat, lo sustituiremos por “panelChat”.
         * ------------------------------------------------------------------ */
        panelCentro = new JPanel(new BorderLayout());
        contentPane.add(panelCentro, BorderLayout.CENTER);

        // Panel de Bienvenida (se ve al inicio)
        panelBienvenida = new JPanel(new BorderLayout());
        panelBienvenida.setBackground(new Color(240, 248, 255));
        JLabel lblBienvenida = new JLabel("Seleccione un chat para comenzar a hablar", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 18));
        lblBienvenida.setForeground(new Color(100, 100, 100));
        panelBienvenida.add(lblBienvenida, BorderLayout.CENTER);

        // Lo ponemos por defecto en el centro
        panelCentro.add(panelBienvenida, BorderLayout.CENTER);

        // Panel de chat (lo creamos, pero NO se añade aún a “panelCentro”)
        panelChat = new JPanel(new BorderLayout());
        panelChat.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
        
        // Scroll del panel de chat
        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane_1.setBorder(null);
        
        // Auto-scroll al final del chat
        Adjustable vertical = scrollPane_1.getVerticalScrollBar();
        final boolean[] autoScrollEnabled = {true};
        vertical.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (autoScrollEnabled[0]) {
                    vertical.setValue(vertical.getMaximum());
                }
            }
        });
        // Si el usuario ajusta manualmente el scroll, lo desactivamos
        vertical.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (e.getValueIsAdjusting()) {
                    autoScrollEnabled[0] = false;
                } else {
                    if (vertical.getValue() + vertical.getVisibleAmount() >= vertical.getMaximum()) {
                        autoScrollEnabled[0] = true;
                    }
                }
            }
        });

        panelChat.add(scrollPane_1, BorderLayout.CENTER);

        // Panel con fondo degradado donde ponemos las burbujas
        panelChatContenido = new VisualUtils.JPanelGradient(new Color(0, 128, 128), new Color(172, 225, 175));
        panelChatContenido.setLayout(new BoxLayout(panelChatContenido, BoxLayout.Y_AXIS));
        scrollPane_1.setViewportView(panelChatContenido);

        // Panel de envío en la parte Sur
        JPanel panelEnvio = new JPanel();
        panelEnvio.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelEnvio.setBackground(new Color(95, 158, 160));
        panelChat.add(panelEnvio, BorderLayout.SOUTH);

        GridBagLayout gbl_panelEnvio = new GridBagLayout();
        gbl_panelEnvio.columnWidths = new int[] {5, 0, 0, 2, 197, 43, 2, 0};
        gbl_panelEnvio.rowHeights = new int[]{5, 21, 0, 0};
        gbl_panelEnvio.columnWeights = new double[]{0.0,0.0,0.0,1.0,0.0,0.0,0.0,Double.MIN_VALUE};
        gbl_panelEnvio.rowWeights = new double[]{0.0,0.0,0.0,Double.MIN_VALUE};
        panelEnvio.setLayout(gbl_panelEnvio);

        // Botón para mostrar emojis
        JButton btnEmojis = new JButton("");
        btnEmojis.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEmojis.setFocusPainted(false);
        btnEmojis.setBackground(new Color(240, 255, 240));
        btnEmojis.setBorder(new CompoundBorder(
                new TitledBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED,
                                     new Color(255, 255, 255), new Color(160, 160, 160)), "",
                                TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)),
                                 "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)),
                new EmptyBorder(3, 10, 3, 10)));
        btnEmojis.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/feliz.png")));
        GridBagConstraints gbc_btnEmojis = new GridBagConstraints();
        gbc_btnEmojis.insets = new Insets(0, 0, 5, 5);
        gbc_btnEmojis.gridx = 1;
        gbc_btnEmojis.gridy = 1;
        panelEnvio.add(btnEmojis, gbc_btnEmojis);

        // Panel flotante de Emojis
        JLayeredPane layeredPane = getLayeredPane();
        JPanel panelEmojis = new EmojiPanel(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int numEmoji = Integer.parseInt(e.getActionCommand());
                // Insertamos la burbuja de emoji
                BubbleText burbuja = new BubbleText(
                    panelChatContenido,
                    numEmoji,        // constructor especial para emojis
                    Color.GREEN,
                    "Usuario Actual",
                    BubbleText.SENT,
                    18
                );
                panelChatContenido.add(burbuja);
                panelChatContenido.revalidate();
                panelChatContenido.repaint();
            }
        });
        layeredPane.add(panelEmojis, JLayeredPane.POPUP_LAYER);

        // Al pulsar el botón, mostramos/ocultamos panelEmojis
        btnEmojis.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!panelEmojis.isVisible()) {
                    Point location = btnEmojis.getLocationOnScreen();
                    SwingUtilities.convertPointFromScreen(location, layeredPane);
                    panelEmojis.setLocation(location.x, location.y - panelEmojis.getHeight());
                    panelEmojis.setVisible(true);
                } else {
                    panelEmojis.setVisible(false);
                }
            }
        });

        // Campo de texto para enviar mensaje
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

        // Botón “Enviar”
        JButton btnEnviar = new JButton("Enviar ");
        btnEnviar.setForeground(new Color(0, 128, 128));
        btnEnviar.setIcon(new ImageIcon(VentanaPrincipal.class
                               .getResource("/umu/tds/apps/resources/enviar-mensaje.png")));
        btnEnviar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEnviar.setBackground(new Color(240, 255, 240));
        btnEnviar.setBorder(new CompoundBorder(
                new TitledBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED,
                                     new Color(255, 255, 255), new Color(160, 160, 160)), "",
                                TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)),
                                 "", TitledBorder.LEADING, TitledBorder.TOP,
                                 null, new Color(0, 0, 0)),
                new EmptyBorder(3, 16, 3, 16)));
        btnEnviar.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 12));
        btnEnviar.setFocusPainted(false);

        // Al pulsar “Enviar”:
        btnEnviar.addActionListener(e -> {
            String textoMensaje = txtMensaje.getText();
            if (!textoMensaje.trim().isEmpty()) {
                // Añadimos una burbuja con el texto
                BubbleText burbuja = new BubbleText(
                    panelChatContenido,
                    textoMensaje,
                    Color.GREEN,
                    "Usuario Actual",
                    BubbleText.SENT,
                    18
                );
                panelChatContenido.add(burbuja);
                txtMensaje.setText("");
                // si quieres forzar scroll al final:
                // panelChatContenido.revalidate();
                // scrollPane_1.getVerticalScrollBar().setValue(...);
            }
        });
        // Enviar con ENTER
        txtMensaje.addActionListener(e -> btnEnviar.doClick());

        GridBagConstraints gbc_btnEnviar = new GridBagConstraints();
        gbc_btnEnviar.insets = new Insets(0, 0, 5, 5);
        gbc_btnEnviar.gridx = 5;
        gbc_btnEnviar.gridy = 1;
        panelEnvio.add(btnEnviar, gbc_btnEnviar);

        /* ------------------------------------------------------------------
         * AL SELECCIONAR UN MENSAJE EN LA LISTA (IZQUIERDA) =>
         * Mostramos el panelChat en el centro + creamos burbujas
         * ------------------------------------------------------------------ */
        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Mensaje seleccionado = list.getSelectedValue();
                if (seleccionado == null) return;

                // 1) Quitamos panelBienvenida del centro y ponemos panelChat
                panelCentro.removeAll();
                panelCentro.add(panelChat, BorderLayout.CENTER);
                panelCentro.revalidate();
                panelCentro.repaint();

                // 2) Limpiamos el contenido actual del chat
                panelChatContenido.removeAll();

                // 3) Creamos una burbuja con la info del mensaje seleccionado (ejemplo simple)
                BubbleText burbuja = new BubbleText(
                    panelChatContenido,
                    seleccionado.getTexto(),
                    Color.GREEN,
                    "Usuario Actual",
                    BubbleText.SENT,
                    18
                );
                panelChatContenido.add(burbuja);

                // 4) Refrescamos
                panelChatContenido.revalidate();
                panelChatContenido.repaint();
            }
        });
    }

    /**
     * Ejemplo de método para cargar imagen de perfil circular en un JLabel.
     */
    private void cargarImagenPerfil(JLabel label, String path) {
        if (path == null || path.isEmpty()) {
        	ImageIcon iconoBase = new ImageIcon(getClass().getResource("/umu/tds/apps/resources/usuario.png"));
            Image imagenEscalada = iconoBase.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(imagenEscalada));
            return;
        }
        try {
            BufferedImage imagen;
            if (path.startsWith("http")) {
                imagen = ImageIO.read(new URL(path));
            } else {
                imagen = ImageIO.read(new File(path));
            }
            if (imagen == null) {
                throw new IOException("La imagen devuelta es nula");
            }
            // Redondeamos a forma circular
            ImageIcon icon = VisualUtils.createCircularIcon(imagen, 50);
            label.setIcon(icon);
        } catch (IOException e) {
            ImageIcon iconoBase = new ImageIcon(getClass().getResource("/umu/tds/apps/resources/usuario.png"));
            Image imagenEscalada = iconoBase.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(imagenEscalada));
            
        }
    }

    /**
     * Si quieres cargar todo el chat de un usuario, puedes usar algo como esto:
     */
    private void cargarChat(Usuario contacto) {
        // Limpiar mensajes actuales
        panelChatContenido.removeAll();
        // Suponiendo que hay un método para obtener mensajes con ese contacto
        List<Mensaje> mensajes = AppChat.getUnicaInstancia().obtenerMensajesConContacto(contacto);
        if (mensajes == null || mensajes.isEmpty()) {
            JLabel noMessagesLabel = new JLabel("No hay mensajes con este contacto.");
            noMessagesLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            noMessagesLabel.setForeground(Color.GRAY);
            panelChatContenido.add(noMessagesLabel);
        } else {
            for (Mensaje mensaje : mensajes) {
                boolean esMio = mensaje.getEmisor().equals(AppChat.getUsuarioActual());
                BubbleText burbuja = new BubbleText(
                    panelChatContenido,
                    mensaje.getTexto(),
                    esMio ? Color.GREEN : Color.LIGHT_GRAY,
                    mensaje.getEmisor().getUsuario(),
                    esMio ? BubbleText.SENT : BubbleText.RECEIVED,
                    18
                );
                panelChatContenido.add(burbuja);
            }
        }
        panelChatContenido.revalidate();
        panelChatContenido.repaint();
    }
}
