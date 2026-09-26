package gui;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.ui.FlatRoundBorder;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class UIStyles {

    public static final Color COLOR_PRIMARY = new Color(30, 41, 59);
    public static final Color COLOR_BG = new Color(241, 245, 249);
    public static final Color COLOR_TEXT = new Color(15, 23, 42);

    public static final Color COLOR_PANEL_BG = new Color(0xE5, 0xEB, 0xF3);
    public static final Color COLOR_CARD_BG = new Color(0xF3, 0xF6, 0xFA);
    public static final Color COLOR_BORDER = new Color(0xA3, 0xB4, 0xD0); //A3B4D0
    public static final Color COLOR_SUBTITLE = new Color(100, 116, 139);
    
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 16);
    public static final Font FONT_BTN = new Font("Segoe UI", Font.BOLD, 15);
    public static final Border BORDER_PADDING = BorderFactory.createEmptyBorder(15, 15, 15, 15);

    public static void aplicarIconoVentana(JFrame ventana) {
        try {
            List<Image> icons = new ArrayList<>();
            URL imgUrl = UIStyles.class.getResource("/resources/logo.png");
            if (imgUrl != null) {
                Image imgOriginal = new ImageIcon(imgUrl).getImage();
                icons.add(imgOriginal.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
                icons.add(imgOriginal.getScaledInstance(32, 32, Image.SCALE_SMOOTH));
                icons.add(imgOriginal.getScaledInstance(64, 64, Image.SCALE_SMOOTH));
                ventana.setIconImages(icons);
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar el logo desde /resources/logo.png");
        }
    }

    public static void aplicarTemaModerno() {
        try {
            // Permitir la escala nativa de Windows/HiDPI
            System.setProperty("flatlaf.useWindowDecorations", "true");

            System.setProperty("flatlaf.uiScale", "1.25");
            
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("TitlePane.height", 38);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el tema FlatLaf: " + e.getMessage());
        }
    }

    public static void aplicarEstiloBotonMenu(JButton btn) {
        btn.setFont(FONT_BTN);
        btn.setFocusPainted(false);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        btn.putClientProperty("JButton.buttonType", "toolBarButton");
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        
        btn.setForeground(Color.WHITE);
        btn.setBackground(COLOR_PRIMARY);
        
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
    }

    
    public static ImageIcon obtenerLogoEscalado(int lado) {
        try {
            URL imgUrl = UIStyles.class.getResource("/resources/transp.png");
            if (imgUrl == null) {
                System.err.println("No se encontró el recurso: /resources/transp.png");
                return null;
            }

            ImageIcon icon = new ImageIcon(imgUrl);
            Image img = icon.getImage();
            
            Image scaledImg = img.getScaledInstance(lado, lado, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        } catch (Exception e) {
            System.err.println("Error al cargar el logo: " + e.getMessage());
            return null;
        }
    }

    public static JSeparator crearSeparadorSutil() {
        JSeparator separador = new JSeparator(SwingConstants.HORIZONTAL);
        separador.setForeground(new Color(71, 85, 105)); 
        separador.setBackground(COLOR_PRIMARY);
        
        separador.setPreferredSize(new Dimension(180, 10));
        separador.setMaximumSize(new Dimension(180, 10));
        separador.setAlignmentX(Component.CENTER_ALIGNMENT);
        return separador;
    }
    
    public static DatePicker crearDatePickerStandar() {
        DatePickerSettings settings = new DatePickerSettings(new Locale("es", "PE"));
        settings.setFormatForDatesCommonEra("dd/MM/yyyy");
        settings.setAllowEmptyDates(true);
        settings.setFontValidDate(FONT_REGULAR);

        DatePicker picker = new DatePicker(settings);
        picker.setDateToToday();
        return picker;
    }
    
    public static JPanel crearTarjetaContenedora() {
        JPanel card = new JPanel();
        card.setBackground(COLOR_CARD_BG);
        

        int borderRadius = 10;

        card.putClientProperty("FlatLaf.style", "arc:" + borderRadius);
        
        Border lineBorder = new com.formdev.flatlaf.ui.FlatLineBorder(
            new java.awt.Insets(1, 1, 1, 1), 
            COLOR_BORDER, 
            1, 
            borderRadius
        );
        
        Border innerPadding = BorderFactory.createEmptyBorder(12, 18, 12, 18);
        card.setBorder(BorderFactory.createCompoundBorder(lineBorder, innerPadding));
        
        return card;
    }
}