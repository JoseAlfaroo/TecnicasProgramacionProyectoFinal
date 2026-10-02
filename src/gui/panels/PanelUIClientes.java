package gui.panels;

import dao.ClienteDAO;
import gui.UIStyles;
import models.Cliente;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class PanelUIClientes extends JPanel {

    private static final long serialVersionUID = 1L;

    private ClienteDAO clienteDAO;

    private JTextField txtId;
    private JTextField txtNumDocumento;
    private JTextField txtNombreCompleto;
    private JTextField txtTelefono;
    private JCheckBox chkEstado;

    private JButton btnRegistrar;
    private JButton btnLimpiar;

    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnRefrescar;
    private JTable tblClientes;
    private DefaultTableModel tableModel;
    private JLabel lblTotal;

    public PanelUIClientes() {
        clienteDAO = new ClienteDAO();

        setBackground(UIStyles.COLOR_PANEL_BG);
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        add(crearPanelHeader(), BorderLayout.NORTH);

        JPanel panelCuerpo = new JPanel(new BorderLayout(12, 0));
        panelCuerpo.setOpaque(false);

        panelCuerpo.add(crearPanelFormulario(), BorderLayout.WEST);
        panelCuerpo.add(crearPanelTabla(), BorderLayout.CENTER);

        add(panelCuerpo, BorderLayout.CENTER);

        cargarDatosTabla(clienteDAO.listar());
        prepararNuevoRegistro();
    }

    private JPanel crearPanelHeader() {
        JPanel panelHeader = UIStyles.crearTarjetaContenedora();
        panelHeader.setLayout(new BorderLayout());

        JPanel containerTextos = new JPanel();
        containerTextos.setLayout(new BoxLayout(containerTextos, BoxLayout.Y_AXIS));
        containerTextos.setOpaque(false);

        JLabel lblTitulo = new JLabel("GESTIÓN DE CLIENTES");
        lblTitulo.setFont(UIStyles.FONT_TITLE);
        lblTitulo.setForeground(UIStyles.COLOR_PRIMARY);

        JLabel lblSubtitulo = new JLabel("Registro y búsqueda de clientes por documento de identidad para el proceso de venta.");
        lblSubtitulo.setFont(UIStyles.FONT_SUBTITLE);
        lblSubtitulo.setForeground(UIStyles.COLOR_SUBTITLE);

        containerTextos.add(lblTitulo);
        containerTextos.add(Box.createRigidArea(new Dimension(0, 3)));
        containerTextos.add(lblSubtitulo);

        JLabel lblIcono = new JLabel();
        URL iconUrl = getClass().getResource("/resources/icons/clientes.png");
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

        JLabel lblTituloForm = new JLabel("Datos del Cliente");
        lblTituloForm.setFont(UIStyles.FONT_TITLE);
        lblTituloForm.setForeground(UIStyles.COLOR_PRIMARY);
        panelForm.add(lblTituloForm, BorderLayout.NORTH);

        JPanel gridFields = new JPanel(new GridBagLayout());
        gridFields.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 4, 6, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField();
        txtId.setVisible(false);

        txtNumDocumento = new JTextField();
        txtNombreCompleto = new JTextField();
        txtTelefono = new JTextField();

        chkEstado = new JCheckBox("Cliente Activo", true);
        chkEstado.setOpaque(false);

        int row = 0;
        agregarCampoForm(gridFields, gbc, row++, "N° Doc (DNI/RUC):", txtNumDocumento);
        agregarCampoForm(gridFields, gbc, row++, "Nombre Completo:", txtNombreCompleto);
        agregarCampoForm(gridFields, gbc, row++, "Teléfono / Celular:", txtTelefono);

        gbc.gridx = 1;
        gbc.gridy = row++;
        gridFields.add(chkEstado, (GridBagConstraints) gbc.clone());

        panelForm.add(gridFields, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new java.awt.GridLayout(1, 2, 8, 0));
        panelBotones.setOpaque(false);

        btnLimpiar = new JButton("Nuevo / Limpiar");
        btnRegistrar = new JButton("Registrar Cliente");

        btnLimpiar.addActionListener(e -> prepararNuevoRegistro());
        btnRegistrar.addActionListener(e -> registrarCliente());

        panelBotones.add(btnLimpiar);
        panelBotones.add(btnRegistrar);

        panelForm.add(panelBotones, BorderLayout.SOUTH);

        return panelForm;
    }

    private void agregarCampoForm(JPanel panel, GridBagConstraints gbc, int row, String labelText, javax.swing.JComponent component) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        JLabel label = new JLabel(labelText);
        label.setFont(UIStyles.FONT_SUBTITLE);
        panel.add(label, (GridBagConstraints) gbc.clone());

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        panel.add(component, (GridBagConstraints) gbc.clone());
    }

    private JPanel crearPanelTabla() {
        JPanel panelTabla = UIStyles.crearTarjetaContenedora();
        panelTabla.setLayout(new BorderLayout(0, 8));

        JPanel panelBusqueda = new JPanel(new BorderLayout(8, 0));
        panelBusqueda.setOpaque(false);

        txtBuscar = new JTextField();
        txtBuscar.setToolTipText("Ingrese N° de documento a buscar...");
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ejecutarBusqueda();
                }
            }
        });

        btnBuscar = new JButton("Buscar Doc.");
        btnBuscar.addActionListener(e -> ejecutarBusqueda());

        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(e -> cargarDatosTabla(clienteDAO.listar()));

        JPanel panelAccionesBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        panelAccionesBusqueda.setOpaque(false);
        panelAccionesBusqueda.add(btnBuscar);
        panelAccionesBusqueda.add(btnRefrescar);

        panelBusqueda.add(new JLabel("Documento: "), BorderLayout.WEST);
        panelBusqueda.add(txtBuscar, BorderLayout.CENTER);
        panelBusqueda.add(panelAccionesBusqueda, BorderLayout.EAST);

        panelTabla.add(panelBusqueda, BorderLayout.NORTH);

        String[] columnas = {"ID", "N° Documento", "Nombre Completo / Razón Social", "Teléfono", "Estado"};
        tableModel = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblClientes = new JTable(tableModel);
        tblClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblClientes.getTableHeader().setReorderingAllowed(false);

        tblClientes.getColumnModel().getColumn(0).setMinWidth(0);
        tblClientes.getColumnModel().getColumn(0).setMaxWidth(0);
        tblClientes.getColumnModel().getColumn(0).setWidth(0);

        tblClientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblClientes.getSelectedRow() != -1) {
                cargarClienteSeleccionado();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblClientes);
        panelTabla.add(scrollPane, BorderLayout.CENTER);

        lblTotal = new JLabel("Total clientes listados: 0");
        lblTotal.setFont(UIStyles.FONT_SUBTITLE);
        panelTabla.add(lblTotal, BorderLayout.SOUTH);

        return panelTabla;
    }

    private void cargarDatosTabla(List<Cliente> lista) {
        tableModel.setRowCount(0);
        for (Cliente c : lista) {
            Object[] fila = {
                c.getIdPersona(),
                c.getNumDocumento(),
                c.getNombreCompleto(),
                c.getTelefono() != null ? c.getTelefono() : "",
                c.isEstado() ? "Activo" : "Inactivo"
            };
            tableModel.addRow(fila);
        }
        lblTotal.setText("Total clientes listados: " + lista.size());
    }

    private void cargarClienteSeleccionado() {
        int row = tblClientes.getSelectedRow();
        if (row == -1) return;

        txtId.setText(tableModel.getValueAt(row, 0).toString());
        txtNumDocumento.setText(tableModel.getValueAt(row, 1).toString());
        txtNombreCompleto.setText(tableModel.getValueAt(row, 2).toString());
        txtTelefono.setText(tableModel.getValueAt(row, 3).toString());
        chkEstado.setSelected(tableModel.getValueAt(row, 4).toString().equals("Activo"));

        btnRegistrar.setEnabled(false);
    }

    private void prepararNuevoRegistro() {
        tblClientes.clearSelection();

        txtId.setText("");
        txtNumDocumento.setText("");
        txtNombreCompleto.setText("");
        txtTelefono.setText("");
        chkEstado.setSelected(true);

        btnRegistrar.setEnabled(true);
        txtNumDocumento.requestFocus();
    }

    private void registrarCliente() {
        String numDoc = txtNumDocumento.getText().trim();
        String nombre = txtNombreCompleto.getText().trim();
        String telefono = txtTelefono.getText().trim();
        boolean estado = chkEstado.isSelected();

        if (numDoc.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el número de documento y el nombre completo.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente c = new Cliente();
        c.setNumDocumento(numDoc);
        c.setNombreCompleto(nombre);
        c.setTelefono(telefono);
        c.setEstado(estado);

        if (clienteDAO.registrar(c)) {
            JOptionPane.showMessageDialog(this, "Cliente registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarDatosTabla(clienteDAO.listar());
            prepararNuevoRegistro();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar el cliente. Verifique que el documento no esté duplicado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ejecutarBusqueda() {
        String doc = txtBuscar.getText().trim();
        if (doc.isEmpty()) {
            cargarDatosTabla(clienteDAO.listar());
        } else {
            Cliente c = clienteDAO.buscarPorDocumento(doc);
            if (c != null) {
                cargarDatosTabla(List.of(c));
            } else {
                tableModel.setRowCount(0);
                lblTotal.setText("Total clientes listados: 0");
                JOptionPane.showMessageDialog(this, "No se encontró ningún cliente con el documento ingresado.", "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}