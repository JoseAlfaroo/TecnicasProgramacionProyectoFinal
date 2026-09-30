package gui.panels;

import gui.UIStyles;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.net.URL;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class PanelUIVentas extends JPanel {

    private JPanel panelHeader;
    private JPanel panelCuerpo;

    public PanelUIVentas() {
        setBackground(UIStyles.COLOR_PANEL_BG);
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Header
        panelHeader = UIStyles.crearTarjetaContenedora();
        panelHeader.setLayout(new BorderLayout());

        JPanel containerTextos = new JPanel();
        containerTextos.setLayout(new BoxLayout(containerTextos, BoxLayout.Y_AXIS));
        containerTextos.setOpaque(false);

        JLabel lblTitulo = new JLabel("GESTIÓN DE VENTAS");
        lblTitulo.setFont(UIStyles.FONT_TITLE);
        lblTitulo.setForeground(UIStyles.COLOR_PRIMARY);

        JLabel lblSubtitulo = new JLabel("Procesamiento de ventas en caja, cálculo automático de montos y descuento de inventario.");
        lblSubtitulo.setFont(UIStyles.FONT_SUBTITLE);
        lblSubtitulo.setForeground(UIStyles.COLOR_SUBTITLE);

        containerTextos.add(lblTitulo);
        containerTextos.add(Box.createRigidArea(new Dimension(0, 3)));
        containerTextos.add(lblSubtitulo);

        JLabel lblIcono = new JLabel();
        URL iconUrl = getClass().getResource("/resources/icons/ventas.png");
        if (iconUrl != null) {
            lblIcono.setIcon(new ImageIcon(iconUrl));
        }

        panelHeader.add(containerTextos, BorderLayout.WEST);
        panelHeader.add(lblIcono, BorderLayout.EAST);

        // Cuerpo
        panelCuerpo = UIStyles.crearTarjetaContenedora();
        panelCuerpo.setLayout(new BorderLayout());

        add(panelHeader, BorderLayout.NORTH);
        add(panelCuerpo, BorderLayout.CENTER);
    }
}