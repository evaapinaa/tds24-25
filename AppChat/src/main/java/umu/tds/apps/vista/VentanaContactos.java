package umu.tds.apps.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.border.LineBorder;

import umu.tds.apps.controlador.AppChat;
import umu.tds.apps.modelo.Contacto;
import umu.tds.apps.modelo.ContactoIndividual;
import umu.tds.apps.modelo.RepositorioUsuarios;
import umu.tds.apps.modelo.Usuario;
import umu.tds.apps.persistencia.AdaptadorContactoIndividualTDS;
import umu.tds.apps.persistencia.AdaptadorUsuarioTDS;

import javax.swing.border.EmptyBorder;

public class VentanaContactos extends JPanel {
    private JList<String> listaContactos;
    private JList<String> listaGrupos;
    private DefaultListModel<String> modeloContactos;
    private DefaultListModel<String> modeloGrupos;
    private JButton btnAgregarContacto;
    private JButton btnAgregarGrupo;
    private JButton btnMoverDerecha;
    private JButton btnMoverIzquierda;

    // Constructor principal
    public VentanaContactos() {
        // Obtener el usuario actual desde AppChat
        Usuario usuarioActual = AppChat.getUsuarioActual();

        // Inicializar la lista de contactos del usuario actual
        List<Contacto> contactos = usuarioActual.getListaContactos();

        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(new Color(240, 240, 240));
        setLayout(new GridBagLayout());

        // Panel izquierdo para contactos
        JPanel panelContactos = new JPanel(new BorderLayout());
        panelContactos.setBackground(new Color(240, 240, 240));
        modeloContactos = new DefaultListModel<>();
        contactos.forEach(contacto -> modeloContactos.addElement(contacto.getNombre()));
        listaContactos = new JList<>(modeloContactos);
        listaContactos.setBackground(new Color(102, 205, 170));
        listaContactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // ScrollPane para la lista de contactos
        JScrollPane scrollPaneContactos = new JScrollPane(listaContactos);
        scrollPaneContactos.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneContactos.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panelContactos.add(scrollPaneContactos, BorderLayout.CENTER);

        btnAgregarContacto = new JButton("Añadir Contacto");
        btnAgregarContacto.setForeground(new Color(255, 255, 255));
        btnAgregarContacto.setBorder(new LineBorder(new Color(128, 128, 150), 2));
        btnAgregarContacto.setBackground(new Color(0, 128, 128));
        panelContactos.add(btnAgregarContacto, BorderLayout.SOUTH);

        // Panel derecho para grupos
        JPanel panelGrupos = new JPanel(new BorderLayout());
        panelGrupos.setBackground(new Color(240, 240, 240));
        modeloGrupos = new DefaultListModel<>();
        listaGrupos = new JList<>(modeloGrupos);
        listaGrupos.setBackground(new Color(102, 205, 170));
        listaGrupos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // ScrollPane para la lista de grupos
        JScrollPane scrollPaneGrupos = new JScrollPane(listaGrupos);
        scrollPaneGrupos.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneGrupos.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panelGrupos.add(scrollPaneGrupos, BorderLayout.CENTER);

        btnAgregarGrupo = new JButton("Añadir Grupo");
        btnAgregarGrupo.setBorder(new LineBorder(new Color(143, 239, 208), 2));
        btnAgregarGrupo.setForeground(new Color(255, 255, 255));
        btnAgregarGrupo.setBackground(new Color(0, 128, 128));
        panelGrupos.add(btnAgregarGrupo, BorderLayout.SOUTH);

        // Panel central con botones de transferencia
        JPanel panelBotones = new JPanel(new GridLayout(2, 1, 5, 5));
        panelBotones.setBackground(new Color(240, 240, 240));
        btnMoverDerecha = new JButton(">>");
        btnMoverDerecha.setForeground(new Color(255, 255, 255));
        btnMoverDerecha.setFont(new Font("Arial", Font.BOLD, 11));
        btnMoverDerecha.setBackground(new Color(51, 134, 151));
        btnMoverIzquierda = new JButton("<<");
        btnMoverIzquierda.setForeground(new Color(255, 255, 255));
        btnMoverIzquierda.setFont(new Font("Arial", Font.BOLD, 11));
        btnMoverIzquierda.setBackground(new Color(51, 134, 151));
        panelBotones.add(btnMoverDerecha);
        panelBotones.add(btnMoverIzquierda);

        // Añadir los paneles al layout con instancias diferentes de GridBagConstraints
        GridBagConstraints gbcContactos = new GridBagConstraints();
        gbcContactos.gridx = 0;
        gbcContactos.gridy = 0;
        gbcContactos.gridheight = 2;
        gbcContactos.fill = GridBagConstraints.BOTH;
        gbcContactos.weightx = 0.5;
        gbcContactos.weighty = 1.0;
        add(panelContactos, gbcContactos);

        GridBagConstraints gbcBotones = new GridBagConstraints();
        gbcBotones.insets = new Insets(5, 5, 5, 5);
        gbcBotones.gridx = 1;
        gbcBotones.gridy = 0;
        gbcBotones.gridheight = 2;
        gbcBotones.fill = GridBagConstraints.NONE;
        gbcBotones.weightx = 0;
        gbcBotones.weighty = 0;
        add(panelBotones, gbcBotones);

        GridBagConstraints gbcGrupos = new GridBagConstraints();
        gbcGrupos.gridx = 2;
        gbcGrupos.gridy = 0;
        gbcGrupos.gridheight = 2;
        gbcGrupos.fill = GridBagConstraints.BOTH;
        gbcGrupos.weightx = 0.5;
        gbcGrupos.weighty = 1.0;
        add(panelGrupos, gbcGrupos);

        // Acción para añadir un contacto
        btnAgregarContacto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pedir nombre y teléfono del contacto
                String nuevoContactoNombre = JOptionPane.showInputDialog("Introduce el nombre del nuevo contacto:");
                if (nuevoContactoNombre == null || nuevoContactoNombre.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "El nombre del contacto no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String nuevoContactoTelefono = JOptionPane.showInputDialog("Introduce el número del nuevo contacto:");
                if (nuevoContactoTelefono == null || nuevoContactoTelefono.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "El número del contacto no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Buscar usuario en el repositorio
                Usuario usuarioExistente = RepositorioUsuarios.getUnicaInstancia().getUsuario(nuevoContactoTelefono);
                if (usuarioExistente == null) {
                    JOptionPane.showMessageDialog(null, "El número introducido no pertenece a ningún usuario registrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Registrar el usuario si no tiene código válido
                if (usuarioExistente.getCodigo() <= 0) {
                    AdaptadorUsuarioTDS.getUnicaInstancia().registrarUsuario(usuarioExistente);
                }

                // Crear y registrar el contacto
                ContactoIndividual nuevoContacto = new ContactoIndividual(nuevoContactoNombre, nuevoContactoTelefono, usuarioExistente);
                AdaptadorContactoIndividualTDS.getUnicaInstancia().registrarContactoIndividual(nuevoContacto);

                // Asociar el contacto al usuario actual
                Usuario usuarioActual = AppChat.getUsuarioActual();
                if (usuarioActual.añadirContacto(nuevoContacto)) {
                    AdaptadorUsuarioTDS.getUnicaInstancia().modificarUsuario(usuarioActual);
                    modeloContactos.addElement(nuevoContactoNombre);
                    JOptionPane.showMessageDialog(null, "Contacto añadido correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "El contacto ya existe.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });


        // Acción para añadir un grupo
        btnAgregarGrupo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nuevoGrupo = JOptionPane.showInputDialog("Introduce el nombre del nuevo grupo:");
                if (nuevoGrupo != null && !nuevoGrupo.trim().isEmpty()) {
                    modeloGrupos.addElement(nuevoGrupo.trim());
                }
            }
        });

        // Acción para mover un contacto al grupo
        btnMoverDerecha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String contactoSeleccionado = listaContactos.getSelectedValue();
                if (contactoSeleccionado != null) {
                    modeloGrupos.addElement(contactoSeleccionado);
                    modeloContactos.removeElement(contactoSeleccionado);
                }
            }
        });

        // Acción para mover un contacto del grupo de vuelta a los contactos
        btnMoverIzquierda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String grupoSeleccionado = listaGrupos.getSelectedValue();
                if (grupoSeleccionado != null) {
                    modeloContactos.addElement(grupoSeleccionado);
                    modeloGrupos.removeElement(grupoSeleccionado);
                }
            }
        });
    }

    // Método main para visualizar el panel de contactos en un JFrame independiente
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    JFrame frame = new JFrame("Ventana de Contactos");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setSize(600, 400);

                    VentanaContactos panelContactos = new VentanaContactos();
                    frame.getContentPane().add(panelContactos);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
