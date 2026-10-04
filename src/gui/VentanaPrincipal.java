package gui;

import gui.panels.PanelUIClientes;
import gui.panels.PanelUIInventario;
import gui.panels.PanelUIReportes;
import gui.panels.PanelUIVentas;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class VentanaPrincipal extends JFrame {

    static {
        System.setProperty("sun.java2d.uiScale", "1.0");
    }

    private CardLayout cardLayout;
    private JPanel panelContenido;

    private static final String CARD_INVENTARIO = "PANEL_INVENTARIO";
    private static final String CARD_CLIENTES = "PANEL_CLIENTES";
    private static final String CARD_VENTAS = "PANEL_VENTAS";
    private static final String CARD_REPORTES = "PANEL_REPORTES";

    public VentanaPrincipal() {
        setTitle("Sistema de Gestión - Distribuidora El Dorado S.A.C.");
        setSize(1680, 720);
        
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmarSalida();
            }
        });

        UIStyles.aplicarIconoVentana(this);

        int anchoMenu = 230;
        JPanel panelMenu = new JPanel(new BorderLayout());
        panelMenu.setBackground(UIStyles.COLOR_PRIMARY);
        panelMenu.setPreferredSize(new Dimension(anchoMenu, 0));

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        // Logo
        int dimensionLogo = 130;
        JLabel lblLogo = new JLabel();
        ImageIcon iconoLogo = UIStyles.obtenerLogoEscalado(dimensionLogo);
        if (iconoLogo != null) {
            lblLogo.setIcon(iconoLogo);
        }
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setPreferredSize(new Dimension(dimensionLogo, dimensionLogo));
        lblLogo.setMaximumSize(new Dimension(dimensionLogo, dimensionLogo));

        // Botones de Navegación
        JButton btnInventario = new JButton("Inventario".toUpperCase());
        JButton btnClientes = new JButton("Clientes".toUpperCase());
        JButton btnVentas = new JButton("Ventas".toUpperCase());
        JButton btnReportes = new JButton("Reportes".toUpperCase());

        UIStyles.aplicarEstiloBotonMenu(btnInventario);
        UIStyles.aplicarEstiloBotonMenu(btnClientes);
        UIStyles.aplicarEstiloBotonMenu(btnVentas);
        UIStyles.aplicarEstiloBotonMenu(btnReportes);

        int altoBoton = 50;

        btnInventario.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnInventario.setPreferredSize(new Dimension(anchoMenu, altoBoton));
        btnInventario.setMaximumSize(new Dimension(Integer.MAX_VALUE, altoBoton));

        btnClientes.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnClientes.setPreferredSize(new Dimension(anchoMenu, altoBoton));
        btnClientes.setMaximumSize(new Dimension(Integer.MAX_VALUE, altoBoton));

        btnVentas.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVentas.setPreferredSize(new Dimension(anchoMenu, altoBoton));
        btnVentas.setMaximumSize(new Dimension(Integer.MAX_VALUE, altoBoton));

        btnReportes.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnReportes.setPreferredSize(new Dimension(anchoMenu, altoBoton));
        btnReportes.setMaximumSize(new Dimension(Integer.MAX_VALUE, altoBoton));

        // Ensamblado del menú
        panelBotones.add(lblLogo);
        panelBotones.add(Box.createRigidArea(new Dimension(0, 10)));
        panelBotones.add(UIStyles.crearSeparadorSutil());
        panelBotones.add(Box.createRigidArea(new Dimension(0, 10)));
        panelBotones.add(btnInventario);
        panelBotones.add(Box.createRigidArea(new Dimension(0, 8)));
        panelBotones.add(btnClientes);
        panelBotones.add(Box.createRigidArea(new Dimension(0, 8)));
        panelBotones.add(btnVentas);
        panelBotones.add(Box.createRigidArea(new Dimension(0, 8)));
        panelBotones.add(btnReportes);

        panelMenu.add(panelBotones, BorderLayout.NORTH);

        // Contenedor Central con CardLayout
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);

        // Cargar vista inicial limpia
        mostrarPanel(CARD_INVENTARIO, new PanelUIInventario());

        // Al hacer clic, se instancia un panel totalmente nuevo (limpio)
        btnInventario.addActionListener(e -> mostrarPanel(CARD_INVENTARIO, new PanelUIInventario()));
        btnClientes.addActionListener(e -> mostrarPanel(CARD_CLIENTES, new PanelUIClientes()));
        btnVentas.addActionListener(e -> mostrarPanel(CARD_VENTAS, new PanelUIVentas()));
        btnReportes.addActionListener(e -> mostrarPanel(CARD_REPORTES, new PanelUIReportes()));

        getContentPane().add(panelMenu, BorderLayout.WEST);
        getContentPane().add(panelContenido, BorderLayout.CENTER);
    }

    private void mostrarPanel(String nombreCard, JPanel nuevoPanel) {
        panelContenido.add(nuevoPanel, nombreCard);
        cardLayout.show(panelContenido, nombreCard);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void confirmarSalida() {
        int opcion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de que desea salir del sistema?",
            "Confirmar Salida",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        UIStyles.aplicarTemaModerno();
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}