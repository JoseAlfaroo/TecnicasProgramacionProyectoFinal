package gui.panels;

import gui.UIStyles;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelInventario extends JPanel {

    public PanelInventario() {
        setLayout(new BorderLayout());
        setBackground(UIStyles.COLOR_BG);
        setBorder(UIStyles.BORDER_PADDING);

        JLabel lblTitulo = new JLabel("Gestión de Inventario - Distribuidora El Dorado");
        lblTitulo.setFont(UIStyles.FONT_TITLE);
        lblTitulo.setForeground(UIStyles.COLOR_TEXT);

        add(lblTitulo, BorderLayout.NORTH);
    }
}