package umu.tds.apps.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class VentanaContactos extends JPanel {
    private JList<String> listaContactos;
    private JList<String> listaGrupos;
    private DefaultListModel<String> modeloContactos;
    private DefaultListModel<String> modeloGrupos;
    private JButton btnAgregarContacto;
    private JButton btnAgregarGrupo;
    private JButton btnMoverDerecha;
    private JButton btnMoverIzquierda;

    // Constructor principal que acepta una lista de contactos
    public VentanaContactos(List<String> contactos) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Panel izquierdo para contactos
        JPanel panelContactos = new JPanel(new BorderLayout());
        panelContactos.setBorder(BorderFactory.createTitledBorder("Lista Contactos"));
        modeloContactos = new DefaultListModel<>();
        contactos.forEach(modeloContactos::addElement);
        listaContactos = new JList<>(modeloContactos);
        listaContactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // ScrollPane para la lista de contactos
        JScrollPane scrollPaneContactos = new JScrollPane(listaContactos);
        scrollPaneContactos.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneContactos.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panelContactos.add(scrollPaneContactos, BorderLayout.CENTER);
        
        btnAgregarContacto = new JButton("Añadir Contacto");
        panelContactos.add(btnAgregarContacto, BorderLayout.SOUTH);

        // Panel derecho para grupos
        JPanel panelGrupos = new JPanel(new BorderLayout());
        panelGrupos.setBorder(BorderFactory.createTitledBorder("Grupos"));
        modeloGrupos = new DefaultListModel<>();
        listaGrupos = new JList<>(modeloGrupos);
        listaGrupos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // ScrollPane para la lista de grupos
        JScrollPane scrollPaneGrupos = new JScrollPane(listaGrupos);
        scrollPaneGrupos.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneGrupos.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panelGrupos.add(scrollPaneGrupos, BorderLayout.CENTER);
        
        btnAgregarGrupo = new JButton("Añadir Grupo");
        panelGrupos.add(btnAgregarGrupo, BorderLayout.SOUTH);

        // Panel central con botones de transferencia
        JPanel panelBotones = new JPanel(new GridLayout(2, 1, 5, 5));
        btnMoverDerecha = new JButton(">>");
        btnMoverIzquierda = new JButton("<<");
        panelBotones.add(btnMoverDerecha);
        panelBotones.add(btnMoverIzquierda);

        // Añadir los paneles al layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        add(panelContactos, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        add(panelBotones, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        add(panelGrupos, gbc);

        // Acción para añadir un contacto
        btnAgregarContacto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nuevoContacto = JOptionPane.showInputDialog("Introduce el nombre del nuevo contacto:");
                if (nuevoContacto != null && !nuevoContacto.trim().isEmpty()) {
                    modeloContactos.addElement(nuevoContacto.trim());
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

                    // Inicializa VentanaContactos con algunos contactos de ejemplo
                    List<String> contactosEjemplo = new ArrayList<>();
                    contactosEjemplo.add("Contacto 1");
                    contactosEjemplo.add("Contacto 2");
                    contactosEjemplo.add("Contacto 3");

                    VentanaContactos panelContactos = new VentanaContactos(contactosEjemplo);
                    frame.add(panelContactos);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
