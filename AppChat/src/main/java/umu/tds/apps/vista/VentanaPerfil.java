package umu.tds.apps.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

import umu.tds.apps.controlador.AppChat;
import umu.tds.apps.modelo.Usuario;
import umu.tds.apps.vista.customcomponents.VisualUtils;

/**
 * La clase VentanaPerfil muestra la información personal del usuario actual
 * y permite modificar algunos aspectos como el saludo personalizado.
 * <p>
 * Esta ventana se presenta como un diálogo modal que muestra la foto de perfil,
 * datos personales y permite la edición del mensaje de saludo.
 * </p>
 *
 * @author TDS-2025
 * @version 1.0
 */
public class VentanaPerfil extends JDialog {
	
	/**
     * Número de versión para la serialización.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Área de texto para mostrar y editar el saludo del usuario.
     */
    private JTextArea txtSaludo;
    
    /**
     * Referencia al usuario actual cuyo perfil se está visualizando.
     */
    private Usuario usuarioActual;

    
    /**
     * Constructor que crea el diálogo de perfil vinculado a la ventana padre.
     *
     * @param parent La ventana padre desde la que se abre este diálogo
     */
    @SuppressWarnings("deprecation")
	public VentanaPerfil(JFrame parent) {
        // Configuración básica del diálogo
        super(parent, "Mi Perfil", true);
        setSize(400, 550);
        setLocationRelativeTo(parent);
        
        // Obtener usuario actual
        usuarioActual = AppChat.getUsuarioActual();
        
        // Panel principal con fondo degradado
        JPanel panelPrincipal = new VisualUtils.JPanelGradient(
            new Color(60, 179, 113), 
            new Color(135, 206, 235)
        );
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel para la foto de perfil
        JPanel panelFoto = new JPanel(new BorderLayout());
        panelFoto.setOpaque(false);
        
        // Foto de perfil
        JLabel labelFoto = new JLabel();
        labelFoto.setHorizontalAlignment(JLabel.CENTER);
        
        try {
            // Cargar y redimensionar imagen de perfil
            String rutaImagen = usuarioActual.getImagenPerfil().getDescription();
            BufferedImage imagen;
            
            if (rutaImagen.startsWith("http")) {
                imagen = ImageIO.read(new URL(rutaImagen));
            } else {
                imagen = ImageIO.read(new File(rutaImagen));
            }
            
            // Crear ícono circular
            ImageIcon iconoPerfil = VisualUtils.createCircularIcon(imagen, 200);
            labelFoto.setIcon(iconoPerfil);
        } catch (IOException e) {
            // Imagen por defecto si hay error
            ImageIcon iconoPorDefecto = new ImageIcon(
                VentanaPerfil.class.getResource("/umu/tds/apps/resources/usuario.png")
            );
            Image imagenEscalada = iconoPorDefecto.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            labelFoto.setIcon(new ImageIcon(imagenEscalada));
        }
        
        panelFoto.add(labelFoto, BorderLayout.CENTER);
        
        // Panel de información
        JPanel panelInfo = new JPanel();
        panelInfo.setOpaque(false);
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        
        // Estilos para etiquetas

        Font fuenteInfo = new Font("Arial", Font.PLAIN, 14);
        Color colorTexto = Color.WHITE;
        
        // Etiquetas de información
        JLabel lblNombre = new JLabel("Nombre: " + usuarioActual.getUsuario());
        lblNombre.setFont(fuenteInfo);
        lblNombre.setForeground(colorTexto);
        
        JLabel lblTelefono = new JLabel("Teléfono: " + usuarioActual.getTelefono());
        lblTelefono.setFont(fuenteInfo);
        lblTelefono.setForeground(colorTexto);
        
        JLabel lblFechaNacimiento = new JLabel("Fecha de Nacimiento: " + usuarioActual.getFechaNacimiento());
        lblFechaNacimiento.setFont(fuenteInfo);
        lblFechaNacimiento.setForeground(colorTexto);
        
        // Panel para saludo
        JPanel panelSaludo = new JPanel(new BorderLayout());
        panelSaludo.setOpaque(false);
        
        JLabel lblSaludoTitulo = new JLabel("Saludo:");
        lblSaludoTitulo.setFont(fuenteInfo);
        lblSaludoTitulo.setForeground(colorTexto);
        
        txtSaludo = new JTextArea(usuarioActual.getSaludo().orElse("Escribe tu saludo aquí..."));
        txtSaludo.setFont(fuenteInfo);
        txtSaludo.setForeground(Color.DARK_GRAY);
        txtSaludo.setLineWrap(true);
        txtSaludo.setWrapStyleWord(true);
        txtSaludo.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        txtSaludo.setPreferredSize(new Dimension(300, 80));
        
        JButton btnGuardarSaludo = new JButton("Guardar Saludo");
        btnGuardarSaludo.setBackground(new Color(0, 128, 128));
        btnGuardarSaludo.setForeground(Color.WHITE);
        btnGuardarSaludo.addActionListener(e -> guardarSaludo());
        
        panelSaludo.add(lblSaludoTitulo, BorderLayout.NORTH);
        panelSaludo.add(new JScrollPane(txtSaludo), BorderLayout.CENTER);
        panelSaludo.add(btnGuardarSaludo, BorderLayout.SOUTH);
        
        JLabel lblEstadoPremium = new JLabel(
            usuarioActual.isPremium() ? "Estado: Usuario Premium" : "Estado: Usuario Estándar"
        );
        lblEstadoPremium.setFont(fuenteInfo);
        lblEstadoPremium.setForeground(colorTexto);
        
        // Añadir componentes
        panelInfo.add(Box.createVerticalStrut(10));
        panelInfo.add(lblNombre);
        panelInfo.add(Box.createVerticalStrut(5));
        panelInfo.add(lblTelefono);
        panelInfo.add(Box.createVerticalStrut(5));
        panelInfo.add(lblFechaNacimiento);
        panelInfo.add(Box.createVerticalStrut(5));
        panelInfo.add(lblEstadoPremium);
        panelInfo.add(Box.createVerticalStrut(5));
        panelInfo.add(panelSaludo);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setOpaque(false);
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        
        panelBotones.add(btnCerrar);
        
        // Añadir al panel principal
        panelPrincipal.add(panelFoto, BorderLayout.NORTH);
        panelPrincipal.add(panelInfo, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        // Establecer contenido
        setContentPane(panelPrincipal);
    }

    /**
     * Guarda el saludo ingresado por el usuario en su perfil.
     * Muestra un mensaje de éxito si la operación es correcta o
     * un mensaje de error en caso contrario.
     */
    private void guardarSaludo() {
        String nuevoSaludo = txtSaludo.getText().trim();
        
        if (nuevoSaludo.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, 
                "El saludo no puede estar vacío.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        try {
            // Actualizar el saludo en el modelo
            AppChat.getUnicaInstancia().cambiarSaludo(nuevoSaludo);
            
            JOptionPane.showMessageDialog(
                this, 
                "Saludo actualizado correctamente.", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this, 
                "Error al guardar el saludo: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Método estático para mostrar fácilmente la ventana de perfil.
     * Crea e inicializa un nuevo diálogo de perfil y lo hace visible.
     *
     * @param padre La ventana padre desde la que se abre este diálogo
     */
    public static void mostrarPerfil(JFrame padre) {
        VentanaPerfil ventanaPerfil = new VentanaPerfil(padre);
        ventanaPerfil.setVisible(true);
    }
}