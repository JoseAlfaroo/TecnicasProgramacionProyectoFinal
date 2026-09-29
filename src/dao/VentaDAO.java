package dao;

import config.ConexionDB;
import models.DetalleVenta;
import models.Venta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

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
}
