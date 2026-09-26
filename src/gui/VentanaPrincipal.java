package gui;

import gui.panels.PanelUIClientes;
import gui.panels.PanelUIInventario;
import gui.panels.PanelUIVentas;
import java.awt.*;
import javax.swing.*;

public class VentanaPrincipal extends JFrame {

    static {
        System.setProperty("sun.java2d.uiScale", "1.0");
    }

    private CardLayout cardLayout;
    private JPanel panelContenido;

    public VentanaPrincipal() {
        setTitle("Sistema de Gestión - Distribuidora El Dorado S.A.C.");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        UIStyles.aplicarIconoVentana(this);

        // 1. Ampliar el ancho del menú lateral a 230px
        int anchoMenu = 230;
        JPanel panelMenu = new JPanel(new BorderLayout());
        panelMenu.setBackground(UIStyles.COLOR_PRIMARY);
        panelMenu.setPreferredSize(new Dimension(anchoMenu, 0));

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        // 2. Configuración y centrado del Logo
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

        // 3. Botones de Navegación adaptados al ancho de 230px
        JButton btnInventario = new JButton("Inventario".toUpperCase());
        JButton btnClientes = new JButton("Clientes".toUpperCase());
        JButton btnVentas = new JButton("Ventas".toUpperCase());

        UIStyles.aplicarEstiloBotonMenu(btnInventario);
        UIStyles.aplicarEstiloBotonMenu(btnClientes);
        UIStyles.aplicarEstiloBotonMenu(btnVentas);

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

        // 4. Armado del Panel de Menú con espacios y separador
        panelBotones.add(lblLogo);
        panelBotones.add(Box.createRigidArea(new Dimension(0, 10)));
        
        panelBotones.add(UIStyles.crearSeparadorSutil());
        
        panelBotones.add(Box.createRigidArea(new Dimension(0, 10)));
        panelBotones.add(btnInventario);
        panelBotones.add(Box.createRigidArea(new Dimension(0, 8)));
        panelBotones.add(btnClientes);
        panelBotones.add(Box.createRigidArea(new Dimension(0, 8)));
        panelBotones.add(btnVentas);

        panelMenu.add(panelBotones, BorderLayout.NORTH);

        // 5. Contenedor Central
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);

        panelContenido.add(new PanelUIInventario(), "PANEL_INVENTARIO");
        panelContenido.add(new PanelUIClientes(), "PANEL_CLIENTES");
        panelContenido.add(new PanelUIVentas(), "PANEL_VENTAS");

        btnInventario.addActionListener(e -> cardLayout.show(panelContenido, "PANEL_INVENTARIO"));
        btnClientes.addActionListener(e -> cardLayout.show(panelContenido, "PANEL_CLIENTES"));
        btnVentas.addActionListener(e -> cardLayout.show(panelContenido, "PANEL_VENTAS"));

        getContentPane().add(panelMenu, BorderLayout.WEST);
        getContentPane().add(panelContenido, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        UIStyles.aplicarTemaModerno();
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}