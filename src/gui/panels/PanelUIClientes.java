package gui.panels;

import dao.ClienteDAO;
import gui.UIStyles;
import models.Cliente;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class PanelUIClientes extends JPanel {

    private static final long serialVersionUID = 1L;

    private final ClienteDAO clienteDAO = new ClienteDAO();

    private final JTextField txtDocumento = new JTextField();
    private final JButton btnBuscar = new JButton("Buscar Doc");
    private final JButton btnRefrescar = new JButton("Refrescar");
    private final JLabel lblTotal = new JLabel("Total de clientes: 0");

    private final DefaultTableModel modeloTabla =
            new DefaultTableModel(
                    new String[] {
                        "NRO DOCUMENTO",
                        "NOMBRE COMPLETO",
                        "TELÉFONO",
                        "ESTADO"
                    }, 0) {

                private static final long serialVersionUID = 1L;

                @Override
                public boolean isCellEditable(int fila, int columna) {
                    return false;
                }
            };

    public PanelUIClientes() {
        setBackground(UIStyles.COLOR_PANEL_BG);
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        crearEncabezado();
        crearListado();

        btnRefrescar.addActionListener(e -> {
            txtDocumento.setText("");
            cargarClientes();
        });

        btnBuscar.addActionListener(e -> buscarCliente());

        // También permite buscar presionando Enter.
        txtDocumento.addActionListener(e -> {
            if (btnBuscar.isEnabled()) {
                buscarCliente();
            }
        });

        // Carga inicial de los registros.
        cargarClientes();
    }

    private void crearEncabezado() {
        JPanel encabezado = UIStyles.crearTarjetaContenedora();
        encabezado.setLayout(new BorderLayout());

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);

        JLabel titulo = new JLabel("GESTIÓN DE CLIENTES");
        titulo.setFont(UIStyles.FONT_TITLE);
        titulo.setForeground(UIStyles.COLOR_PRIMARY);

        JLabel subtitulo = new JLabel(
                "Registro y búsqueda de clientes por documento "
                + "de identidad para el proceso de venta.");
        subtitulo.setFont(UIStyles.FONT_SUBTITLE);
        subtitulo.setForeground(UIStyles.COLOR_SUBTITLE);

        textos.add(titulo);
        textos.add(Box.createRigidArea(new Dimension(0, 3)));
        textos.add(subtitulo);

        encabezado.add(textos, BorderLayout.CENTER);
        add(encabezado, BorderLayout.NORTH);
    }

    private void crearListado() {
        JPanel listado = UIStyles.crearTarjetaContenedora();
        listado.setLayout(new BorderLayout(0, 12));

        JPanel buscador = new JPanel(new BorderLayout(10, 0));
        buscador.setOpaque(false);

        JLabel etiqueta = new JLabel("Documento:");
        etiqueta.setFont(UIStyles.FONT_REGULAR);

        txtDocumento.setFont(UIStyles.FONT_REGULAR);

        JPanel botones = new JPanel();
        botones.setOpaque(false);
        botones.add(btnBuscar);
        botones.add(btnRefrescar);

        buscador.add(etiqueta, BorderLayout.WEST);
        buscador.add(txtDocumento, BorderLayout.CENTER);
        buscador.add(botones, BorderLayout.EAST);

        JTable tabla = new JTable(modeloTabla);
        tabla.setFont(UIStyles.FONT_REGULAR);
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(UIStyles.FONT_BTN);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setAutoCreateRowSorter(true);
        tabla.setFillsViewportHeight(true);

        tabla.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(300);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100);

        lblTotal.setFont(UIStyles.FONT_SUBTITLE);

        listado.add(buscador, BorderLayout.NORTH);
        listado.add(new JScrollPane(tabla), BorderLayout.CENTER);
        listado.add(lblTotal, BorderLayout.SOUTH);

        add(listado, BorderLayout.CENTER);
    }

    private void cargarClientes() {
        consultarClientes(() -> clienteDAO.listar(), false);
    }

    private void buscarCliente() {
        String documento = txtDocumento.getText().trim();

        if (documento.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingresa un documento para buscar.",
                    "Documento requerido",
                    JOptionPane.WARNING_MESSAGE);

            txtDocumento.requestFocusInWindow();
            return;
        }

        consultarClientes(() -> {
            Cliente cliente = clienteDAO.buscarPorDocumento(documento);

            if (cliente == null) {
                return Collections.emptyList();
            }

            return Collections.singletonList(cliente);
        }, true);
    }

    private void consultarClientes(
            Supplier<List<Cliente>> consulta,
            boolean esBusqueda) {

        btnBuscar.setEnabled(false);
        btnRefrescar.setEnabled(false);
        lblTotal.setText("Consultando clientes...");

        // Consulta en segundo plano para no congelar la ventana.
        new SwingWorker<List<Cliente>, Void>() {

            @Override
            protected List<Cliente> doInBackground() {
                return consulta.get();
            }

            @Override
            protected void done() {
                try {
                    List<Cliente> clientes = get();
                    mostrarClientes(clientes);

                    if (esBusqueda && clientes.isEmpty()) {
                        JOptionPane.showMessageDialog(
                                PanelUIClientes.this,
                                "No se encontró un cliente con ese documento.",
                                "Resultado de búsqueda",
                                JOptionPane.INFORMATION_MESSAGE);
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    mostrarError();

                } catch (ExecutionException e) {
                    mostrarError();

                } finally {
                    btnBuscar.setEnabled(true);
                    btnRefrescar.setEnabled(true);
                }
            }
        }.execute();
    }

    private void mostrarClientes(List<Cliente> clientes) {
        // Vacía las filas anteriores para evitar duplicados.
        modeloTabla.setRowCount(0);

        for (Cliente cliente : clientes) {
            modeloTabla.addRow(new Object[] {
                cliente.getNumDocumento(),
                cliente.getNombreCompleto(),
                cliente.getTelefono(),
                cliente.isEstado() ? "Activo" : "Inactivo"
            });
        }

        lblTotal.setText("Total de clientes: " + clientes.size());
    }

    private void mostrarError() {
        lblTotal.setText("No se pudo completar la consulta.");

        JOptionPane.showMessageDialog(
                this,
                "No se pudo consultar clientes. Revisa la consola de Eclipse.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}