package gui;

import javax.swing.SwingUtilities;

public class AppMain {

    public static void main(String[] args) {
    	
    	UIStyles.aplicarTemaModerno();
    	
        SwingUtilities.invokeLater(() -> {
            new VentanaLogin().setVisible(true);
        });
        
    }
}