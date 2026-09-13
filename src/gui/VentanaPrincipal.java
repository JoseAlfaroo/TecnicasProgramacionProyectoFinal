package gui;

import gui.panels.PanelInventario;
import gui.panels.PanelVentas;
import java.awt.*;
import javax.swing.*;

public class VentanaPrincipal extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelContenido;

    public VentanaPrincipal() {
        setTitle("Sistema de Gestión - Distribuidora El Dorado S.A.C.");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        UIStyles.aplicarIconoVentana(this);
        
        // 1. Crear Menú Lateral
        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new GridLayout(6, 1, 5, 5));
        panelMenu.setBackground(UIStyles.COLOR_PRIMARY);
        panelMenu.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton btnInventario = new JButton("Inventario");
        JButton btnVentas = new JButton("Ventas");

        UIStyles.aplicarEstiloBotonMenu(btnInventario);
        UIStyles.aplicarEstiloBotonMenu(btnVentas);

        panelMenu.add(btnInventario);
        panelMenu.add(btnVentas);

        // 2. Crear Contenedor Central con CardLayout
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);

        // Instanciar y agregar los paneles modulares
        panelContenido.add(new PanelInventario(), "PANEL_INVENTARIO");
        panelContenido.add(new PanelVentas(), "PANEL_VENTAS");

        // 3. Eventos de navegación
        btnInventario.addActionListener(e -> cardLayout.show(panelContenido, "PANEL_INVENTARIO"));
        btnVentas.addActionListener(e -> cardLayout.show(panelContenido, "PANEL_VENTAS"));

        // 4. Agregar a la ventana principal
        add(panelMenu, BorderLayout.WEST);
        add(panelContenido, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}