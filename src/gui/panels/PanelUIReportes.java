package gui.panels;

import com.github.lgooddatepicker.components.DatePicker;
import dao.VentaDAO;
import gui.UIStyles;
import models.DetalleVenta;
import models.Venta;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class PanelUIReportes extends JPanel {

    private VentaDAO ventaDAO;
    private List<Venta> ventasCargadas;

    private DatePicker pickerInicio;
    private DatePicker pickerFin;
    private JButton btnFiltrar;

    private JTable tblVentas;
    private DefaultTableModel modelVentas;

    private JTable tblDetalle;
    private DefaultTableModel modelDetalle;

    private JLabel lblTotalRecaudado;

    public PanelUIReportes() {
        ventaDAO = new VentaDAO();

        setBackground(UIStyles.COLOR_PANEL_BG);
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        add(crearPanelHeader(), BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new BorderLayout(0, 10));
        panelCentro.setOpaque(false);

        panelCentro.add(crearPanelFiltros(), BorderLayout.NORTH);
        panelCentro.add(crearPanelTablasDivididas(), BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);

        // Cargar por defecto las ventas del mes actual
        pickerInicio.setDate(LocalDate.now().withDayOfMonth(1));
        pickerFin.setDate(LocalDate.now());
        ejecutarConsulta();
    }

    private JPanel crearPanelHeader() {
        JPanel panelHeader = UIStyles.crearTarjetaContenedora();
        panelHeader.setLayout(new BorderLayout());

        JPanel containerTextos = new JPanel();
        containerTextos.setLayout(new BoxLayout(containerTextos, BoxLayout.Y_AXIS));
        containerTextos.setOpaque(false);

        JLabel lblTitulo = new JLabel("HISTORIAL Y REPORTES DE VENTAS");
        lblTitulo.setFont(UIStyles.FONT_TITLE);
        lblTitulo.setForeground(UIStyles.COLOR_PRIMARY);

        JLabel lblSubtitulo = new JLabel("Consulta de comprobantes emitidos, desglose de ítems por transacción y balance de ingresos.");
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

        return panelHeader;
    }

    private JPanel crearPanelFiltros() {
        JPanel panelFiltros = UIStyles.crearTarjetaContenedora();
        panelFiltros.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

        pickerInicio = UIStyles.crearDatePickerStandar();
        pickerFin = UIStyles.crearDatePickerStandar();

        btnFiltrar = new JButton("Filtrar por Fechas");
        btnFiltrar.addActionListener(e -> ejecutarConsulta());

        lblTotalRecaudado = new JLabel("Total Rango: S/ 0.00");
        lblTotalRecaudado.setFont(UIStyles.FONT_TITLE);
        lblTotalRecaudado.setForeground(UIStyles.COLOR_PRIMARY);

        panelFiltros.add(new JLabel("Desde:"));
        panelFiltros.add(pickerInicio);
        panelFiltros.add(Box.createRigidArea(new Dimension(10, 0)));
        panelFiltros.add(new JLabel("Hasta:"));
        panelFiltros.add(pickerFin);
        panelFiltros.add(Box.createRigidArea(new Dimension(10, 0)));
        panelFiltros.add(btnFiltrar);
        panelFiltros.add(Box.createRigidArea(new Dimension(30, 0)));
        panelFiltros.add(lblTotalRecaudado);

        return panelFiltros;
    }

    private JSplitPane crearPanelTablasDivididas() {
        // 1. Tabla Superior (Cabeceras de Venta)
        JPanel panelVentas = UIStyles.crearTarjetaContenedora();
        panelVentas.setLayout(new BorderLayout(0, 6));

        JLabel lblTitVentas = new JLabel("Ventas Realizadas");
        lblTitVentas.setFont(UIStyles.FONT_TITLE);
        lblTitVentas.setForeground(UIStyles.COLOR_PRIMARY);
        panelVentas.add(lblTitVentas, BorderLayout.NORTH);

        String[] colsVenta = {"N° Transacción", "Fecha / Hora", "N° Doc Cliente", "Cliente", "Subtotal", "IGV", "Total"};
        modelVentas = new DefaultTableModel(colsVenta, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tblVentas = new JTable(modelVentas);
        tblVentas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblVentas.getTableHeader().setReorderingAllowed(false);

        tblVentas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblVentas.getSelectedRow() != -1) {
                mostrarDetalleVentaSeleccionada();
            }
        });

        panelVentas.add(new JScrollPane(tblVentas), BorderLayout.CENTER);

        // 2. Tabla Inferior (Detalle del Carrito)
        JPanel panelDetalles = UIStyles.crearTarjetaContenedora();
        panelDetalles.setLayout(new BorderLayout(0, 6));

        JLabel lblTitDetalle = new JLabel("Detalle del Comprobante Seleccionado");
        lblTitDetalle.setFont(UIStyles.FONT_TITLE);
        lblTitDetalle.setForeground(UIStyles.COLOR_PRIMARY);
        panelDetalles.add(lblTitDetalle, BorderLayout.NORTH);

        String[] colsDetalle = {"Código Prod.", "Producto", "Cantidad", "Precio Unitario", "Subtotal"};
        modelDetalle = new DefaultTableModel(colsDetalle, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tblDetalle = new JTable(modelDetalle);
        tblDetalle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblDetalle.getTableHeader().setReorderingAllowed(false);

        panelDetalles.add(new JScrollPane(tblDetalle), BorderLayout.CENTER);

        // Divisor Vertical
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelVentas, panelDetalles);
        splitPane.setDividerLocation(240);
        splitPane.setResizeWeight(0.5);
        splitPane.setOpaque(false);

        return splitPane;
    }

    private void ejecutarConsulta() {
        LocalDate fInicio = pickerInicio.getDate();
        LocalDate fFin = pickerFin.getDate();

        if (fInicio == null || fFin == null) {
            JOptionPane.showMessageDialog(this, "Seleccione ambas fechas para filtrar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (fInicio.isAfter(fFin)) {
            JOptionPane.showMessageDialog(this, "La fecha inicial no puede ser posterior a la fecha final.", "Rango Inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ventasCargadas = ventaDAO.buscarPorRangoFechas(fInicio, fFin);
        modelVentas.setRowCount(0);
        modelDetalle.setRowCount(0);

        double sumaTotal = 0.0;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Venta v : ventasCargadas) {
            sumaTotal += v.getTotal();
            Object[] fila = {
                String.format("VEN-%05d", v.getIdVenta()),
                v.getFechaHora().format(fmt),
                v.getCliente().getNumDocumento(),
                v.getCliente().getNombreCompleto(),
                String.format("S/ %.2f", v.getSubtotal()),
                String.format("S/ %.2f", v.getIgv()),
                String.format("S/ %.2f", v.getTotal())
            };
            modelVentas.addRow(fila);
        }

        lblTotalRecaudado.setText(String.format("Total Rango: S/ %.2f", sumaTotal));
    }

    private void mostrarDetalleVentaSeleccionada() {
        int index = tblVentas.getSelectedRow();
        if (index == -1 || ventasCargadas == null || index >= ventasCargadas.size()) return;

        Venta v = ventasCargadas.get(index);
        modelDetalle.setRowCount(0);

        for (DetalleVenta d : v.getDetalles()) {
            Object[] fila = {
                d.getProducto().getCodigo(),
                d.getProducto().getNombre(),
                d.getCantidad(),
                String.format("S/ %.2f", d.getPrecioUnitario()),
                String.format("S/ %.2f", d.calcularSubtotal())
            };
            modelDetalle.addRow(fila);
        }
    }
}