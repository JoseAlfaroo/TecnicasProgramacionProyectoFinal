package gui.panels;

import dao.ClienteDAO;
import dao.ProductoDAO;
import dao.VentaDAO;
import gui.UIStyles;
import models.Cliente;
import models.DetalleVenta;
import models.Producto;
import models.Venta;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Component;

public class PanelUIVentas extends JPanel {

    private ClienteDAO clienteDAO;
    private ProductoDAO productoDAO;
    private VentaDAO ventaDAO;

    private Cliente clienteSeleccionado;
    private List<Producto> listaProductosActivos;
    private List<DetalleVenta> carrito;

    // UI Cliente
    private JTextField txtDocCliente;
    private JButton btnBuscarCliente;
    private JLabel lblNombreCliente;

    // UI Producto
    private JComboBox<String> cboProductos;
    private JLabel lblPrecioVenta;
    private JLabel lblStockDisponible;
    private JSpinner spCantidad;
    private JButton btnAgregarItem;

    // UI Carrito & Totales
    private JTable tblCarrito;
    private DefaultTableModel tableModelCarrito;
    private JButton btnQuitarItem;
    private JButton btnCancelarVenta;
    private JButton btnProcesarVenta;

    private JLabel lblSubtotal;
    private JLabel lblIgv;
    private JLabel lblTotal;

    public PanelUIVentas() {
        clienteDAO = new ClienteDAO();
        productoDAO = new ProductoDAO();
        ventaDAO = new VentaDAO();
        carrito = new ArrayList<>();

        setBackground(UIStyles.COLOR_PANEL_BG);
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        add(crearPanelHeader(), BorderLayout.NORTH);

        JPanel panelCuerpo = new JPanel(new BorderLayout(12, 0));
        panelCuerpo.setOpaque(false);

        panelCuerpo.add(crearPanelSeleccion(), BorderLayout.WEST);
        panelCuerpo.add(crearPanelCarrito(), BorderLayout.CENTER);

        add(panelCuerpo, BorderLayout.CENTER);

        cargarComboProductos();
        resetearFormularioVenta();
    }

    private JPanel crearPanelHeader() {
        JPanel panelHeader = UIStyles.crearTarjetaContenedora();
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

        return panelHeader;
    }
    
    private JPanel crearPanelSeleccion() {
        JPanel panelLeft = UIStyles.crearTarjetaContenedora();
        panelLeft.setLayout(new BorderLayout(0, 12));
        panelLeft.setPreferredSize(new Dimension(380, 0));

        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setOpaque(false);
        
        JLabel lblSeccionCliente = new JLabel("1. Búsqueda de Cliente");
        lblSeccionCliente.setFont(UIStyles.FONT_TITLE);
        lblSeccionCliente.setForeground(UIStyles.COLOR_PRIMARY);
        lblSeccionCliente.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        
        JPanel gridCliente = new JPanel(new GridBagLayout());
        gridCliente.setOpaque(false);
        gridCliente.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        txtDocCliente = new JTextField();
        btnBuscarCliente = new JButton("Buscar");
        btnBuscarCliente.addActionListener(e -> buscarCliente());
        
        JPanel panelCampoYBot = new JPanel(new BorderLayout(6, 0));
        panelCampoYBot.setOpaque(false);
        panelCampoYBot.add(txtDocCliente, BorderLayout.CENTER);
        panelCampoYBot.add(btnBuscarCliente, BorderLayout.EAST);

        GridBagConstraints gbcCL = new GridBagConstraints();
        gbcCL.insets = new Insets(4, 0, 4, 8);
        gbcCL.anchor = GridBagConstraints.WEST;
        gbcCL.gridx = 0;
        gbcCL.gridy = 0;

        GridBagConstraints gbcCC = new GridBagConstraints();
        gbcCC.insets = new Insets(4, 0, 4, 2);
        gbcCC.fill = GridBagConstraints.HORIZONTAL;
        gbcCC.weightx = 1.0;
        gbcCC.gridx = 1;
        gbcCC.gridy = 0;

        JLabel lblDniRuc = new JLabel("DNI/RUC:");
        lblDniRuc.setFont(UIStyles.FONT_SUBTITLE);
        gridCliente.add(lblDniRuc, (GridBagConstraints) gbcCL.clone());
        gridCliente.add(panelCampoYBot, (GridBagConstraints) gbcCC.clone());

        lblNombreCliente = new JLabel("Cliente: [No Seleccionado]");
        lblNombreCliente.setFont(UIStyles.FONT_SUBTITLE);
        lblNombreCliente.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        
        JLabel lblSeccionProducto = new JLabel("2. Selección de Producto");
        lblSeccionProducto.setFont(UIStyles.FONT_TITLE);
        lblSeccionProducto.setForeground(UIStyles.COLOR_PRIMARY);
        lblSeccionProducto.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        JPanel gridProducto = new JPanel(new GridBagLayout());
        gridProducto.setOpaque(false);
        gridProducto.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        cboProductos = new JComboBox<>();
        cboProductos.addActionListener(e -> seleccionarProductoCombo());

        lblPrecioVenta = new JLabel("S/ 0.00");
        lblPrecioVenta.setFont(UIStyles.FONT_SUBTITLE);

        lblStockDisponible = new JLabel("0");
        lblStockDisponible.setFont(UIStyles.FONT_SUBTITLE);

        spCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        spCantidad.setPreferredSize(new Dimension(80, 26));

        JPanel panelSpinner = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelSpinner.setOpaque(false);
        panelSpinner.add(spCantidad);

        GridBagConstraints gbcL = new GridBagConstraints();
        gbcL.insets = new Insets(4, 0, 4, 8);
        gbcL.anchor = GridBagConstraints.WEST;
        gbcL.gridx = 0;

        GridBagConstraints gbcC = new GridBagConstraints();
        gbcC.insets = new Insets(4, 0, 4, 2);
        gbcC.fill = GridBagConstraints.HORIZONTAL;
        gbcC.weightx = 1.0;
        gbcC.gridx = 1;
        
        gbcL.gridy = 0; gridProducto.add(new JLabel("Producto:"), (GridBagConstraints) gbcL.clone());
        gbcC.gridy = 0; gridProducto.add(cboProductos, (GridBagConstraints) gbcC.clone());
        
        gbcL.gridy = 1; gridProducto.add(new JLabel("Precio Venta:"), (GridBagConstraints) gbcL.clone());
        gbcC.gridy = 1; gridProducto.add(lblPrecioVenta, (GridBagConstraints) gbcC.clone());
        
        gbcL.gridy = 2; gridProducto.add(new JLabel("Stock Dispon.:"), (GridBagConstraints) gbcL.clone());
        gbcC.gridy = 2; gridProducto.add(lblStockDisponible, (GridBagConstraints) gbcC.clone());
        
        gbcL.gridy = 3; gridProducto.add(new JLabel("Cantidad:"), (GridBagConstraints) gbcL.clone());
        gbcC.gridy = 3; gridProducto.add(panelSpinner, (GridBagConstraints) gbcC.clone());

        btnAgregarItem = new JButton("+ Agregar al Carrito");
        btnAgregarItem.addActionListener(e -> agregarAlCarrito());

        JPanel panelBotAgregar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelBotAgregar.setOpaque(false);
        panelBotAgregar.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        panelBotAgregar.add(btnAgregarItem);
        
        
        panelContenido.add(lblSeccionCliente);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 6)));
        panelContenido.add(gridCliente);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 6)));
        panelContenido.add(lblNombreCliente);

        panelContenido.add(Box.createRigidArea(new Dimension(0, 25)));

        panelContenido.add(lblSeccionProducto);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 8)));
        panelContenido.add(gridProducto);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 14)));
        panelContenido.add(panelBotAgregar);

        panelLeft.add(panelContenido, BorderLayout.NORTH);

        return panelLeft;
    }
    
    private JPanel crearPanelCarrito() {
        JPanel panelRight = UIStyles.crearTarjetaContenedora();
        panelRight.setLayout(new BorderLayout(0, 8));

        JLabel lblTituloCarrito = new JLabel("Detalle de la Venta (Carrito)");
        lblTituloCarrito.setFont(UIStyles.FONT_TITLE);
        lblTituloCarrito.setForeground(UIStyles.COLOR_PRIMARY);
        panelRight.add(lblTituloCarrito, BorderLayout.NORTH);

        String[] columnas = {"ID Prod", "Código", "Producto", "Cantidad", "P. Unitario", "Subtotal"};
        tableModelCarrito = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblCarrito = new JTable(tableModelCarrito);
        tblCarrito.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblCarrito.getTableHeader().setReorderingAllowed(false);

        tblCarrito.getColumnModel().getColumn(0).setMinWidth(0);
        tblCarrito.getColumnModel().getColumn(0).setMaxWidth(0);
        tblCarrito.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(tblCarrito);
        panelRight.add(scrollPane, BorderLayout.CENTER);
        
        JPanel panelBottom = new JPanel(new BorderLayout());
        panelBottom.setOpaque(false);

        JPanel panelAccionesTabla = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        panelAccionesTabla.setOpaque(false);

        btnQuitarItem = new JButton("- Quitar Seleccionado");
        btnQuitarItem.addActionListener(e -> quitarItemCarrito());

        btnCancelarVenta = new JButton("Cancelar / Limpiar");
        btnCancelarVenta.addActionListener(e -> resetearFormularioVenta());

        panelAccionesTabla.add(btnQuitarItem);
        panelAccionesTabla.add(btnCancelarVenta);
        
        JPanel panelTotales = new JPanel();
        panelTotales.setLayout(new BoxLayout(panelTotales, BoxLayout.Y_AXIS));
        panelTotales.setOpaque(false);

        lblSubtotal = new JLabel("Subtotal: S/ 0.00");
        lblIgv = new JLabel("IGV (18%): S/ 0.00");
        lblTotal = new JLabel("TOTAL: S/ 0.00");
        lblTotal.setFont(UIStyles.FONT_TITLE);
        lblTotal.setForeground(UIStyles.COLOR_PRIMARY);

        panelTotales.add(lblSubtotal);
        panelTotales.add(lblIgv);
        panelTotales.add(lblTotal);

        btnProcesarVenta = new JButton("PROCESAR Y GUARDAR VENTA");
        btnProcesarVenta.setPreferredSize(new Dimension(0, 40));
        btnProcesarVenta.addActionListener(e -> procesarVenta());

        JPanel panelContenedorBottom = new JPanel(new BorderLayout(0, 8));
        panelContenedorBottom.setOpaque(false);

        JPanel panelCalculos = new JPanel(new BorderLayout());
        panelCalculos.setOpaque(false);
        panelCalculos.add(panelAccionesTabla, BorderLayout.WEST);
        panelCalculos.add(panelTotales, BorderLayout.EAST);

        panelContenedorBottom.add(panelCalculos, BorderLayout.NORTH);
        panelContenedorBottom.add(btnProcesarVenta, BorderLayout.SOUTH);

        panelRight.add(panelContenedorBottom, BorderLayout.SOUTH);

        return panelRight;
    }
    
    private void cargarComboProductos() {
        cboProductos.removeAllItems();
        listaProductosActivos = productoDAO.listarActivos();

        for (Producto p : listaProductosActivos) {
            cboProductos.addItem(p.getCodigo() + " - " + p.getNombre());
        }

        if (!listaProductosActivos.isEmpty()) {
            cboProductos.setSelectedIndex(0);
            seleccionarProductoCombo();
        }
    }

    private void seleccionarProductoCombo() {
        int index = cboProductos.getSelectedIndex();
        if (index >= 0 && index < listaProductosActivos.size()) {
            Producto p = listaProductosActivos.get(index);
            lblPrecioVenta.setText(String.format("S/ %.2f", p.getPrecioVenta()));
            lblStockDisponible.setText(String.valueOf(p.getStockActual()));

            int maxStock = p.getStockActual() > 0 ? p.getStockActual() : 1;
            spCantidad.setModel(new SpinnerNumberModel(1, 1, maxStock, 1));
        }
    }

    private void buscarCliente() {
        String doc = txtDocCliente.getText().trim();
        if (doc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un número de documento.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente c = clienteDAO.buscarPorDocumento(doc);
        if (c != null) {
            clienteSeleccionado = c;
            lblNombreCliente.setText("Cliente: " + c.getNombreCompleto());
        } else {
            clienteSeleccionado = null;
            lblNombreCliente.setText("Cliente: [No Seleccionado]");
            JOptionPane.showMessageDialog(this, "Cliente no encontrado. Registre al cliente antes de continuar.", "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void agregarAlCarrito() {
        if (cboProductos.getSelectedIndex() == -1) return;

        Producto prod = listaProductosActivos.get(cboProductos.getSelectedIndex());
        int cantidadPedida = (int) spCantidad.getValue();

        if (prod.getStockActual() <= 0) {
            JOptionPane.showMessageDialog(this, "El producto seleccionado no tiene stock disponible.", "Sin Stock", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (DetalleVenta d : carrito) {
            if (d.getProducto().getIdProducto() == prod.getIdProducto()) {
                int nuevaCantidad = d.getCantidad() + cantidadPedida;
                if (nuevaCantidad > prod.getStockActual()) {
                    JOptionPane.showMessageDialog(this, "La cantidad total agregada supera el stock disponible (" + prod.getStockActual() + ").", "Stock Insuficiente", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                d.setCantidad(nuevaCantidad);
                actualizarTablaCarrito();
                return;
            }
        }

        DetalleVenta nuevoDetalle = new DetalleVenta();
        nuevoDetalle.setProducto(prod);
        nuevoDetalle.setCantidad(cantidadPedida);
        nuevoDetalle.setPrecioUnitario(prod.getPrecioVenta());

        carrito.add(nuevoDetalle);
        actualizarTablaCarrito();
    }

    private void quitarItemCarrito() {
        int row = tblCarrito.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un ítem del carrito para quitar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        carrito.remove(row);
        actualizarTablaCarrito();
    }

    private void actualizarTablaCarrito() {
        tableModelCarrito.setRowCount(0);
        double totalGlobal = 0;

        for (DetalleVenta d : carrito) {
            double subt = d.calcularSubtotal();
            totalGlobal += subt;

            Object[] fila = {
                d.getProducto().getIdProducto(),
                d.getProducto().getCodigo(),
                d.getProducto().getNombre(),
                d.getCantidad(),
                String.format("%.2f", d.getPrecioUnitario()),
                String.format("%.2f", subt)
            };
            tableModelCarrito.addRow(fila);
        }

        double subtotalCalc = totalGlobal / 1.18;
        double igvCalc = totalGlobal - subtotalCalc;

        lblSubtotal.setText(String.format("Subtotal: S/ %.2f", subtotalCalc));
        lblIgv.setText(String.format("IGV (18%%): S/ %.2f", igvCalc));
        lblTotal.setText(String.format("TOTAL: S/ %.2f", totalGlobal));
    }

    private void resetearFormularioVenta() {
        clienteSeleccionado = null;
        txtDocCliente.setText("");
        lblNombreCliente.setText("Cliente: [No Seleccionado]");

        carrito.clear();
        actualizarTablaCarrito();

        cargarComboProductos();
    }

    private void procesarVenta() {
        if (clienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe buscar y seleccionar un cliente antes de procesar la venta.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito de compras está vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double totalGlobal = 0;
        for (DetalleVenta d : carrito) {
            totalGlobal += d.calcularSubtotal();
        }

        double subtotalCalc = totalGlobal / 1.18;
        double igvCalc = totalGlobal - subtotalCalc;

        Venta v = new Venta();
        v.setCliente(clienteSeleccionado);
        v.setFechaHora(LocalDateTime.now());
        v.setSubtotal(subtotalCalc);
        v.setIgv(igvCalc);
        v.setTotal(totalGlobal);
        v.setDetalles(carrito);

        if (ventaDAO.registrarVenta(v)) {
            JOptionPane.showMessageDialog(this, "Venta registrada con éxito. N° Transacción: " + v.getIdVenta(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            resetearFormularioVenta();
        } else {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al procesar la transacción en la base de datos.", "Error de Venta", JOptionPane.ERROR_MESSAGE);
        }
    }
}