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
    
    
 // Componentes para registrar cliente
    private final JTextField txtNumDocumento = new JTextField();
    private final JTextField txtNombreCompleto = new JTextField();
    private final JTextField txtTelefono = new JTextField();

    private final javax.swing.JCheckBox chkActivo =
            new javax.swing.JCheckBox("Activo", true);

    private final JButton btnNuevo =
            new JButton("NUEVO / Limpiar");

    private final JButton btnRegistrar =
            new JButton("Registrar Cliente");
    

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
        crearContenidoClientes();

        btnRefrescar.addActionListener(e -> {
            txtDocumento.setText("");
            cargarClientes();
        });

        btnBuscar.addActionListener(e -> buscarCliente());
     // Eventos del formulario de registro - Contreras
        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnRegistrar.addActionListener(e -> registrarCliente());

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
    
    
    
 // ==========================================================
 // REGISTRO DE CLIENTES - IMPLEMENTACIÓN CONTRERAS
 // Conecta el formulario con ClienteDAO.registrar(Cliente)
 // ===
    
    private void crearContenidoClientes() {
        JPanel contenido = new JPanel(new BorderLayout(12, 0));
        contenido.setOpaque(false);

        // Panel izquierdo: formulario de registro
        JPanel formulario = UIStyles.crearTarjetaContenedora();
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setPreferredSize(new Dimension(400, 0));

        JLabel tituloFormulario = new JLabel("DATOS DEL CLIENTE");
        tituloFormulario.setFont(UIStyles.FONT_TITLE);
        tituloFormulario.setAlignmentX(LEFT_ALIGNMENT);

        formulario.add(tituloFormulario);
        formulario.add(Box.createRigidArea(new Dimension(0, 25)));

        formulario.add(crearCampoFormulario(
                "N° doc (DNI / RUC)", txtNumDocumento));
        formulario.add(Box.createRigidArea(new Dimension(0, 12)));

        formulario.add(crearCampoFormulario(
                "Nombre completo", txtNombreCompleto));
        formulario.add(Box.createRigidArea(new Dimension(0, 12)));

        formulario.add(crearCampoFormulario(
                "Teléfono", txtTelefono));
        formulario.add(Box.createRigidArea(new Dimension(0, 15)));

        chkActivo.setOpaque(false);
        chkActivo.setFont(UIStyles.FONT_REGULAR);
        chkActivo.setAlignmentX(LEFT_ALIGNMENT);
        formulario.add(chkActivo);

        formulario.add(Box.createVerticalGlue());

        JPanel botonesFormulario = new JPanel();
        botonesFormulario.setOpaque(false);
        botonesFormulario.setLayout(
                new BoxLayout(botonesFormulario, BoxLayout.X_AXIS));
        botonesFormulario.setAlignmentX(LEFT_ALIGNMENT);

        botonesFormulario.add(btnNuevo);
        botonesFormulario.add(Box.createRigidArea(new Dimension(10, 0)));
        botonesFormulario.add(btnRegistrar);

        formulario.add(botonesFormulario);

        // Panel derecho: listado/búsqueda de clientes
        JPanel listado = crearListado();

        contenido.add(formulario, BorderLayout.WEST);
        contenido.add(listado, BorderLayout.CENTER);

        add(contenido, BorderLayout.CENTER);
    }

    private JPanel crearCampoFormulario(String texto, JTextField campo) {
        JPanel fila = new JPanel(new BorderLayout(10, 0));
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        fila.setAlignmentX(LEFT_ALIGNMENT);

        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(UIStyles.FONT_REGULAR);
        etiqueta.setPreferredSize(new Dimension(155, 30));

        campo.setFont(UIStyles.FONT_REGULAR);

        fila.add(etiqueta, BorderLayout.WEST);
        fila.add(campo, BorderLayout.CENTER);

        return fila;
    }
    
    
    

    private JPanel crearListado() {
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

        return listado;
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
    
    
 // ==========================================================
 // REGISTRO DE CLIENTES - IMPLEMENTACIÓN CONTRERAS
 // ==========================================================

 private void limpiarFormulario() {
     txtNumDocumento.setText("");
     txtNombreCompleto.setText("");
     txtTelefono.setText("");
     chkActivo.setSelected(true);

     txtNumDocumento.requestFocusInWindow();
 }

 private void registrarCliente() {
     String documento = txtNumDocumento.getText().trim();
     String nombre = txtNombreCompleto.getText().trim();
     String telefono = txtTelefono.getText().trim();

     // Validar campos obligatorios
     if (documento.isEmpty() || nombre.isEmpty() || telefono.isEmpty()) {
         JOptionPane.showMessageDialog(
                 this,
                 "Completa todos los campos del cliente.",
                 "Datos incompletos",
                 JOptionPane.WARNING_MESSAGE);
         return;
     }

     Cliente cliente = new Cliente();
     cliente.setNumDocumento(documento);
     cliente.setNombreCompleto(nombre);
     cliente.setTelefono(telefono);
     cliente.setEstado(chkActivo.isSelected());

     boolean registrado = clienteDAO.registrar(cliente);

     if (registrado) {
         JOptionPane.showMessageDialog(
                 this,
                 "Cliente registrado correctamente.",
                 "Registro exitoso",
                 JOptionPane.INFORMATION_MESSAGE);

         limpiarFormulario();

         // Actualizar tabla con el nuevo registro
         cargarClientes();

     } else {
         JOptionPane.showMessageDialog(
                 this,
                 "No se pudo registrar el cliente.\n"
                 + "Verifica los datos o si el documento ya existe.",
                 "Error de registro",
                 JOptionPane.ERROR_MESSAGE);
     }
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