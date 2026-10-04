package gui;

import javax.swing.SwingUtilities;

public class AppMain {
	
    static {
        System.setProperty("sun.java2d.uiScale", "1.0");
    }

	public static void main(String[] args) {
        UIStyles.aplicarTemaModerno();

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}