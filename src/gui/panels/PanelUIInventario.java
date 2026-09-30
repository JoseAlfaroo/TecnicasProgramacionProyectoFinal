package gui.panels;

import dao.ProductoDAO;
import gui.UIStyles;
import models.Producto;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.github.lgooddatepicker.components.DatePicker;

public class PanelUIInventario extends JPanel {

    private ProductoDAO productoDAO;

    private JTextField txtId;
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JComboBox<String> cboCategoria;
    private JTextField txtPrecioCompra;
    private JTextField txtPrecioVenta;
    private JTextField txtStockActual;
    private JTextField txtStockMinimo;
    private DatePicker datePickerVencimiento;
    private JCheckBox chkActivo;

    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnRefrescar;
    private JTable tblProductos;
    private DefaultTableModel tableModel;
    private JLabel lblTotal;

    public PanelUIInventario() {
        productoDAO = new ProductoDAO();

        setBackground(UIStyles.COLOR_PANEL_BG);
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        add(crearPanelHeader(), BorderLayout.NORTH);

        JPanel panelCuerpo = new JPanel(new BorderLayout(12, 0));
        panelCuerpo.setOpaque(false);

        panelCuerpo.add(crearPanelFormulario(), BorderLayout.WEST);
        panelCuerpo.add(crearPanelTabla(), BorderLayout.CENTER);

        add(panelCuerpo, BorderLayout.CENTER);

        cargarDatosTabla(productoDAO.listarActivos());
        prepararNuevoRegistro();
    }

    private JPanel crearPanelHeader() {
        JPanel panelHeader = UIStyles.crearTarjetaContenedora();
        panelHeader.setLayout(new BorderLayout());

        JPanel containerTextos = new JPanel();
        containerTextos.setLayout(new BoxLayout(containerTextos, BoxLayout.Y_AXIS));
        containerTextos.setOpaque(false);

        JLabel lblTitulo = new JLabel("GESTIÓN DE INVENTARIO");
        lblTitulo.setFont(UIStyles.FONT_TITLE);
        lblTitulo.setForeground(UIStyles.COLOR_PRIMARY);

        JLabel lblSubtitulo = new JLabel("Administración de catálogo de productos, control de precios y actualización de stock.");
        lblSubtitulo.setFont(UIStyles.FONT_SUBTITLE);
        lblSubtitulo.setForeground(UIStyles.COLOR_SUBTITLE);

        containerTextos.add(lblTitulo);
        containerTextos.add(Box.createRigidArea(new Dimension(0, 3)));
        containerTextos.add(lblSubtitulo);

        JLabel lblIcono = new JLabel();
        URL iconUrl = getClass().getResource("/resources/icons/inventario.png");
        if (iconUrl != null) {
            lblIcono.setIcon(new ImageIcon(iconUrl));
        }

        panelHeader.add(containerTextos, BorderLayout.WEST);
        panelHeader.add(lblIcono, BorderLayout.EAST);

        return panelHeader;
    }

    private JPanel crearPanelFormulario() {
        JPanel panelForm = UIStyles.crearTarjetaContenedora();
        panelForm.setLayout(new BorderLayout(0, 10));
        panelForm.setPreferredSize(new Dimension(380, 0));

        JLabel lblTituloForm = new JLabel("Datos del Producto");
        lblTituloForm.setFont(UIStyles.FONT_TITLE);
        lblTituloForm.setForeground(UIStyles.COLOR_PRIMARY);
        panelForm.add(lblTituloForm, BorderLayout.NORTH);

        JPanel gridFields = new JPanel(new GridBagLayout());
        gridFields.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField();
        txtId.setVisible(false);

        txtCodigo = new JTextField();
        txtCodigo.setEditable(false);

        txtNombre = new JTextField();
        cboCategoria = new JComboBox<>(new String[]{"Abarrotes", "Lácteos", "Bebidas", "Limpieza", "Golosinas", "Otros"});
        txtPrecioCompra = new JTextField();
        txtPrecioVenta = new JTextField();
        txtStockActual = new JTextField();
        txtStockMinimo = new JTextField();
        datePickerVencimiento = UIStyles.crearDatePickerStandar();

        chkActivo = new JCheckBox("Producto Activo", true);
        chkActivo.setOpaque(false);
        chkActivo.setEnabled(false);

        int row = 0;
        agregarCampoForm(gridFields, gbc, row++, "Código Correlativo:", txtCodigo);
        agregarCampoForm(gridFields, gbc, row++, "Nombre Producto:", txtNombre);
        agregarCampoForm(gridFields, gbc, row++, "Categoría:", cboCategoria);
        agregarCampoForm(gridFields, gbc, row++, "Precio Compra (S/):", txtPrecioCompra);
        agregarCampoForm(gridFields, gbc, row++, "Precio Venta (S/):", txtPrecioVenta);
        agregarCampoForm(gridFields, gbc, row++, "Stock Actual:", txtStockActual);
        agregarCampoForm(gridFields, gbc, row++, "Stock Mínimo:", txtStockMinimo);
        agregarCampoForm(gridFields, gbc, row++, "F. Vencimiento:", datePickerVencimiento);

        gbc.gridx = 1;
        gbc.gridy = row++;
        gridFields.add(chkActivo, (GridBagConstraints) gbc.clone());

        panelForm.add(gridFields, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new java.awt.GridLayout(2, 2, 6, 6));
        panelBotones.setOpaque(false);

        btnLimpiar = new JButton("Nuevo / Limpiar");
        btnRegistrar = new JButton("Registrar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");

        btnLimpiar.addActionListener(e -> prepararNuevoRegistro());
        btnRegistrar.addActionListener(e -> registrarProducto());
        btnActualizar.addActionListener(e -> actualizarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());

        panelBotones.add(btnLimpiar);
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);

        panelForm.add(panelBotones, BorderLayout.SOUTH);

        return panelForm;
    }

    private void agregarCampoForm(JPanel panel, GridBagConstraints gbc, int row, String labelText, javax.swing.JComponent component) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(UIStyles.FONT_SUBTITLE);
        panel.add(label, (GridBagConstraints) gbc.clone());

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(component, (GridBagConstraints) gbc.clone());
    }

    private JPanel crearPanelTabla() {
        JPanel panelTabla = UIStyles.crearTarjetaContenedora();
        panelTabla.setLayout(new BorderLayout(0, 8));

        JPanel panelBusqueda = new JPanel(new BorderLayout(8, 0));
        panelBusqueda.setOpaque(false);

        txtBuscar = new JTextField();
        txtBuscar.setToolTipText("Escriba código o nombre para buscar...");
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                ejecutarBusqueda();
            }
        });

        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> ejecutarBusqueda());

        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(e -> cargarDatosTabla(productoDAO.listarActivos()));

        JPanel panelAccionesBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        panelAccionesBusqueda.setOpaque(false);
        panelAccionesBusqueda.add(btnBuscar);
        panelAccionesBusqueda.add(btnRefrescar);

        panelBusqueda.add(new JLabel("Filtro: "), BorderLayout.WEST);
        panelBusqueda.add(txtBuscar, BorderLayout.CENTER);
        panelBusqueda.add(panelAccionesBusqueda, BorderLayout.EAST);

        panelTabla.add(panelBusqueda, BorderLayout.NORTH);

        String[] columnas = {"ID", "Código", "Nombre", "Categoría", "P. Compra", "P. Venta", "Stock", "St. Min", "Vencimiento", "Activo"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblProductos = new JTable(tableModel);
        tblProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblProductos.getTableHeader().setReorderingAllowed(false);

        tblProductos.getColumnModel().getColumn(0).setMinWidth(0);
        tblProductos.getColumnModel().getColumn(0).setMaxWidth(0);
        tblProductos.getColumnModel().getColumn(0).setWidth(0);

        tblProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblProductos.getSelectedRow() != -1) {
                cargarProductoSeleccionado();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblProductos);
        panelTabla.add(scrollPane, BorderLayout.CENTER);

        lblTotal = new JLabel("Total registros listados: 0");
        lblTotal.setFont(UIStyles.FONT_SUBTITLE);
        panelTabla.add(lblTotal, BorderLayout.SOUTH);

        return panelTabla;
    }

    private void cargarDatosTabla(List<Producto> lista) {
        tableModel.setRowCount(0);
        for (Producto p : lista) {
            Object[] fila = {
                p.getIdProducto(),
                p.getCodigo(),
                p.getNombre(),
                p.getCategoria(),
                String.format("%.2f", p.getPrecioCompra()),
                String.format("%.2f", p.getPrecioVenta()),
                p.getStockActual(),
                p.getStockMinimo(),
                p.getFechaVencimiento() != null ? p.getFechaVencimiento().toString() : "N/A",
                p.isActivo() ? "Sí" : "No"
            };
            tableModel.addRow(fila);
        }
        lblTotal.setText("Total registros listados: " + lista.size());
    }

    private void cargarProductoSeleccionado() {
        int row = tblProductos.getSelectedRow();
        if (row == -1) return;

        txtId.setText(tableModel.getValueAt(row, 0).toString());
        txtCodigo.setText(tableModel.getValueAt(row, 1).toString());
        txtNombre.setText(tableModel.getValueAt(row, 2).toString());
        cboCategoria.setSelectedItem(tableModel.getValueAt(row, 3).toString());
        txtPrecioCompra.setText(tableModel.getValueAt(row, 4).toString().replace(",", "."));
        txtPrecioVenta.setText(tableModel.getValueAt(row, 5).toString().replace(",", "."));
        txtStockActual.setText(tableModel.getValueAt(row, 6).toString());
        txtStockMinimo.setText(tableModel.getValueAt(row, 7).toString());

        String fechaStr = tableModel.getValueAt(row, 8).toString();
        if ("N/A".equals(fechaStr) || fechaStr.isEmpty()) {
            datePickerVencimiento.setDate(null);
        } else {
            datePickerVencimiento.setDate(LocalDate.parse(fechaStr));
        }

        chkActivo.setSelected(tableModel.getValueAt(row, 9).toString().equals("Sí"));

        btnRegistrar.setEnabled(false);
        btnActualizar.setEnabled(true);
        btnEliminar.setEnabled(true);
    }

    private void prepararNuevoRegistro() {
        tblProductos.clearSelection();

        txtId.setText("");
        txtCodigo.setText(productoDAO.generarSiguienteCodigo());
        txtNombre.setText("");
        cboCategoria.setSelectedIndex(0);
        txtPrecioCompra.setText("");
        txtPrecioVenta.setText("");
        txtStockActual.setText("");
        txtStockMinimo.setText("");
        datePickerVencimiento.setDate(null);
        chkActivo.setSelected(true);

        btnRegistrar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);

        txtNombre.requestFocus();
    }

    private void registrarProducto() {
        Producto p = obtenerDatosFormulario();
        if (p == null) return;

        if (productoDAO.registrar(p)) {
            JOptionPane.showMessageDialog(this, "Producto registrado correctamente con código: " + p.getCodigo(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarDatosTabla(productoDAO.listarActivos());
            prepararNuevoRegistro();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar el producto en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarProducto() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla para actualizar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Producto p = obtenerDatosFormulario();
        if (p == null) return;

        p.setIdProducto(Integer.parseInt(txtId.getText().trim()));
        p.setCodigo(txtCodigo.getText().trim());

        if (productoDAO.actualizar(p)) {
            JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarDatosTabla(productoDAO.listarActivos());
            prepararNuevoRegistro();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProducto() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla para eliminar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idProducto = Integer.parseInt(txtId.getText().trim());
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de desactivar el producto [" + codigo + "] " + nombre + "?\nNo se borrará el historial de ventas.",
            "Confirmar Eliminación Lógica",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (productoDAO.eliminarLogico(idProducto)) {
                JOptionPane.showMessageDialog(this, "Producto desactivado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla(productoDAO.listarActivos());
                prepararNuevoRegistro();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo desactivar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void ejecutarBusqueda() {
        String criterio = txtBuscar.getText().trim();
        if (criterio.isEmpty()) {
            cargarDatosTabla(productoDAO.listarActivos());
        } else {
            cargarDatosTabla(productoDAO.buscarPorCodigoONombre(criterio));
        }
    }

    private Producto obtenerDatosFormulario() {
        String nombre = txtNombre.getText().trim();
        String categoria = cboCategoria.getSelectedItem().toString();
        String strPrecioCompra = txtPrecioCompra.getText().trim();
        String strPrecioVenta = txtPrecioVenta.getText().trim();
        String strStockActual = txtStockActual.getText().trim();
        String strStockMinimo = txtStockMinimo.getText().trim();
        LocalDate fechaVencimiento = datePickerVencimiento.getDate();

        if (nombre.isEmpty() || strPrecioCompra.isEmpty() || strPrecioVenta.isEmpty() || strStockActual.isEmpty() || strStockMinimo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        try {
            double precioCompra = Double.parseDouble(strPrecioCompra);
            double precioVenta = Double.parseDouble(strPrecioVenta);
            int stockActual = Integer.parseInt(strStockActual);
            int stockMinimo = Integer.parseInt(strStockMinimo);
            boolean activo = chkActivo.isSelected();

            return new Producto(0, null, nombre, categoria, precioCompra, precioVenta, stockActual, stockMinimo, fechaVencimiento, activo);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese montos y niveles de stock válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}