package dao;

import config.ConexionDB;
import models.Cliente;
import models.DetalleVenta;
import models.Producto;
import models.Venta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    public boolean registrarVenta(Venta v) {
        String sqlVenta = "INSERT INTO tb_venta (id_cliente, fecha_hora, subtotal, igv, total) VALUES (?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO tb_detalle_venta (id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        String sqlStock = "UPDATE tb_producto SET stock_actual = stock_actual - ? WHERE id_producto = ?";

        Connection cn = null;

        try {
            cn = ConexionDB.getInstance().getConnection();
            cn.setAutoCommit(false);

            int idVentaGenerado = -1;

            try (PreparedStatement psVenta = cn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                psVenta.setInt(1, v.getCliente().getIdPersona());
                psVenta.setTimestamp(2, Timestamp.valueOf(v.getFechaHora()));
                psVenta.setDouble(3, v.getSubtotal());
                psVenta.setDouble(4, v.getIgv());
                psVenta.setDouble(5, v.getTotal());

                psVenta.executeUpdate();

                try (ResultSet rs = psVenta.getGeneratedKeys()) {
                    if (rs.next()) {
                        idVentaGenerado = rs.getInt(1);
                        v.setIdVenta(idVentaGenerado);
                    }
                }
            }

            if (idVentaGenerado == -1) {
                cn.rollback();
                return false;
            }

            try (PreparedStatement psDetalle = cn.prepareStatement(sqlDetalle);
                 PreparedStatement psStock = cn.prepareStatement(sqlStock)) {

                for (DetalleVenta det : v.getDetalles()) {
                    psDetalle.setInt(1, idVentaGenerado);
                    psDetalle.setInt(2, det.getProducto().getIdProducto());
                    psDetalle.setInt(3, det.getCantidad());
                    psDetalle.setDouble(4, det.getPrecioUnitario());
                    psDetalle.setDouble(5, det.calcularSubtotal());
                    psDetalle.executeUpdate();

                    psStock.setInt(1, det.getCantidad());
                    psStock.setInt(2, det.getProducto().getIdProducto());
                    psStock.executeUpdate();
                }
            }

            cn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error en transacción de venta en VentaDAO: " + e.getMessage());
            if (cn != null) {
                try {
                    cn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error al realizar rollback en VentaDAO: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (cn != null) {
                try {
                    cn.setAutoCommit(true);
                    cn.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar conexión en VentaDAO: " + e.getMessage());
                }
            }
        }
    }

    public List<Venta> buscarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Venta> lista = new ArrayList<>();
        
        String sql = "SELECT v.id_venta, v.fecha_hora, v.subtotal, v.igv, v.total, "
                   + "c.id_cliente, c.num_documento, c.nombre_completo "
                   + "FROM tb_venta v "
                   + "INNER JOIN tb_cliente c ON v.id_cliente = c.id_cliente "
                   + "WHERE v.fecha_hora BETWEEN ? AND ? "
                   + "ORDER BY v.fecha_hora DESC";

        try (Connection con = ConexionDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Primer segundo del día inicial, basicamente 00000
            Timestamp desde = Timestamp.valueOf(fechaInicio.atStartOfDay());
            
            // Último segundo del día final osea las 23:59:9999 y muchos nueves
            Timestamp hasta = Timestamp.valueOf(fechaFin.atTime(java.time.LocalTime.MAX));

            ps.setTimestamp(1, desde);
            ps.setTimestamp(2, hasta);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Venta v = new Venta();
                    v.setIdVenta(rs.getInt("id_venta"));
                    v.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
                    v.setSubtotal(rs.getDouble("subtotal"));
                    v.setIgv(rs.getDouble("igv"));
                    v.setTotal(rs.getDouble("total"));

                    Cliente c = new Cliente();
                    c.setIdPersona(rs.getInt("id_cliente"));
                    c.setNumDocumento(rs.getString("num_documento"));
                    c.setNombreCompleto(rs.getString("nombre_completo"));

                    v.setCliente(c);
                    v.setDetalles(obtenerDetallesPorVenta(v.getIdVenta(), con));

                    lista.add(v);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar ventas por rango de fechas: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    private List<DetalleVenta> obtenerDetallesPorVenta(int idVenta, Connection con) throws SQLException {
        List<DetalleVenta> detalles = new ArrayList<>();
        String sql = "SELECT dv.cantidad, dv.precio_unitario, p.id_producto, p.codigo, p.nombre "
                   + "FROM tb_detalle_venta dv "
                   + "INNER JOIN tb_producto p ON dv.id_producto = p.id_producto "
                   + "WHERE dv.id_venta = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVenta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetalleVenta dv = new DetalleVenta();
                    dv.setCantidad(rs.getInt("cantidad"));
                    dv.setPrecioUnitario(rs.getDouble("precio_unitario"));

                    Producto p = new Producto();
                    p.setIdProducto(rs.getInt("id_producto"));
                    p.setCodigo(rs.getString("codigo"));
                    p.setNombre(rs.getString("nombre"));

                    dv.setProducto(p);
                    detalles.add(dv);
                }
            }
        }
        return detalles;
    }
}