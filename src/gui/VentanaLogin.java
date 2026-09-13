package gui;

import java.awt.*;
import javax.swing.*;

public class VentanaLogin extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;

    public VentanaLogin() {
        setTitle("Iniciar Sesión - Distribuidora El Dorado");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        UIStyles.aplicarIconoVentana(this);
        
        JPanel panelForm = new JPanel(new GridLayout(3, 2, 10, 15));
        panelForm.setBackground(UIStyles.COLOR_BG);
        panelForm.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(UIStyles.FONT_REGULAR);
        txtUsuario = new JTextField();

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(UIStyles.FONT_REGULAR);
        txtPassword = new JPasswordField();

        JButton btnIngresar = new JButton("Ingresar");
        UIStyles.aplicarEstiloBotonMenu(btnIngresar);

        panelForm.add(lblUsuario);
        panelForm.add(txtUsuario);
        panelForm.add(lblPassword);
        panelForm.add(txtPassword);
        panelForm.add(new JLabel()); 
        panelForm.add(btnIngresar);

        // Encabezado
        JLabel lblTitulo = new JLabel("ACCESO AL SISTEMA", SwingConstants.CENTER);
        lblTitulo.setFont(UIStyles.FONT_TITLE);
        lblTitulo.setForeground(UIStyles.COLOR_PRIMARY);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        add(lblTitulo, BorderLayout.NORTH);
        add(panelForm, BorderLayout.CENTER);

        // Evento del Botón Ingresar
        btnIngresar.addActionListener(e -> validarAcceso());
    }

    private void validarAcceso() {
        String usuario = txtUsuario.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();

        if (usuario.equals("admin") && pass.equals("1234")) {
            this.dispose();

            SwingUtilities.invokeLater(() -> {
                new VentanaPrincipal().setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(this,
                    "Usuario o contraseña incorrectos",
                    "Error de Autenticación",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}