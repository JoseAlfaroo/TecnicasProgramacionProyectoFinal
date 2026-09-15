package gui;

import com.formdev.flatlaf.FlatLightLaf;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class UIStyles {

	public static void aplicarIconoVentana(JFrame ventana) {
        try {
            List<Image> icons = new ArrayList<>();
            Image imgOriginal = new ImageIcon(UIStyles.class.getResource("/resources/logo.png")).getImage();
            
            icons.add(imgOriginal.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
            icons.add(imgOriginal.getScaledInstance(32, 32, Image.SCALE_SMOOTH));
            icons.add(imgOriginal.getScaledInstance(64, 64, Image.SCALE_SMOOTH));

            ventana.setIconImages(icons);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el logo desde /resources/logo.png");
        }
    }
	
    public static void aplicarTemaModerno() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());

            System.setProperty("flatlaf.useWindowDecorations", "true");
            UIManager.put("TitlePane.height", 38);
            
        } catch (Exception e) {
            System.err.println("No se pudo cargar el tema FlatLaf: " + e.getMessage());
        }
    }

    public static final Color COLOR_PRIMARY = new Color(30, 41, 59);
    public static final Color COLOR_BG = new Color(241, 245, 249);
    public static final Color COLOR_TEXT = new Color(15, 23, 42);

    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BTN = new Font("Segoe UI", Font.BOLD, 13);
    public static final Border BORDER_PADDING = BorderFactory.createEmptyBorder(15, 15, 15, 15);

    public static void aplicarEstiloBotonMenu(JButton btn) {
        btn.setFont(FONT_BTN);
        btn.setFocusPainted(false);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
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
}