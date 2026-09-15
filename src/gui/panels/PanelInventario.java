package gui.panels;

import gui.UIStyles;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.Date;
import java.time.LocalDate;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.lgooddatepicker.components.DatePicker;

public class PanelInventario extends JPanel {

    private DatePicker pickerVencimiento;

    public PanelInventario() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        
        JLabel lblFecha = new JLabel("Fecha de Vencimiento:");
        lblFecha.setFont(UIStyles.FONT_REGULAR);

        // Instanciar el selector formateado
        pickerVencimiento = UIStyles.crearDatePickerStandar();

        add(lblFecha);
        add(pickerVencimiento);
    }

    // Método helper para enviar a MySQL via PreparedStatement (ps.setDate(...))
    public Date getFechaVencimientoSQL() {
        LocalDate fecha = pickerVencimiento.getDate();
        return (fecha != null) ? Date.valueOf(fecha) : null;
    }

    // Método helper para setear la fecha al cargar desde un DAO
    public void setFechaVencimientoSQL(Date fechaSQL) {
        if (fechaSQL != null) {
            pickerVencimiento.setDate(fechaSQL.toLocalDate());
        } else {
            pickerVencimiento.setDate(null);
        }
    }
}