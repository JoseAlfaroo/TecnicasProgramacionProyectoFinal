package dao;

import config.ConexionDB;
import models.Producto;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public String generarSiguienteCodigo() {
        String sql = "SELECT COUNT(*) + 1 FROM tb_producto";
        try (Connection cn = ConexionDB.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return String.format("PROD-%04d", rs.getInt(1));
            }

        } catch (SQLException e) {
            System.err.println("Error al correlacionar código: " + e.getMessage());
        }

        return "PROD-0001";
    }

    public boolean registrar(Producto p) {
        String codigoGenerado = generarSiguienteCodigo();
        p.setCodigo(codigoGenerado);

        String sql = "INSERT INTO tb_producto (codigo, nombre, categoria, precio_compra, " +
                     "precio_venta, stock_actual, stock_minimo, fecha_vencimiento, activo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection cn = ConexionDB.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getCategoria());
            ps.setDouble(4, p.getPrecioCompra());
            ps.setDouble(5, p.getPrecioVenta());
            ps.setInt(6, p.getStockActual());
            ps.setInt(7, p.getStockMinimo());

            if (p.getFechaVencimiento() != null) {
                ps.setDate(8, Date.valueOf(p.getFechaVencimiento()));
            } else {
                ps.setNull(8, java.sql.Types.DATE);
            }

            ps.setBoolean(9, p.isActivo());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al registrar producto en ProductoDAO: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Producto p) {
        String sql = "UPDATE tb_producto SET codigo = ?, nombre = ?, categoria = ?, " +
                     "precio_compra = ?, precio_venta = ?, stock_actual = ?, " +
                     "stock_minimo = ?, fecha_vencimiento = ? " +
                     "WHERE id_producto = ?";

        try (Connection cn = ConexionDB.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getCategoria());
            ps.setDouble(4, p.getPrecioCompra());
            ps.setDouble(5, p.getPrecioVenta());
            ps.setInt(6, p.getStockActual());
            ps.setInt(7, p.getStockMinimo());

            if (p.getFechaVencimiento() != null) {
                ps.setDate(8, Date.valueOf(p.getFechaVencimiento()));
            } else {
                ps.setNull(8, java.sql.Types.DATE);
            }

            ps.setInt(9, p.getIdProducto());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar producto en ProductoDAO: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarLogico(int id) {
        String sql = "UPDATE tb_producto SET activo = FALSE WHERE id_producto = ?";

        try (Connection cn = ConexionDB.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar lógicamente producto en ProductoDAO: " + e.getMessage());
            return false;
        }
    }

    public List<Producto> listarActivos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT id_producto, codigo, nombre, categoria, precio_compra, " +
                     "precio_venta, stock_actual, stock_minimo, fecha_vencimiento, activo " +
                     "FROM tb_producto WHERE activo = TRUE";

        try (Connection cn = ConexionDB.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("id_producto"));
                p.setCodigo(rs.getString("codigo"));
                p.setNombre(rs.getString("nombre"));
                p.setCategoria(rs.getString("categoria"));
                p.setPrecioCompra(rs.getDouble("precio_compra"));
                p.setPrecioVenta(rs.getDouble("precio_venta"));
                p.setStockActual(rs.getInt("stock_actual"));
                p.setStockMinimo(rs.getInt("stock_minimo"));

                Date fechaSql = rs.getDate("fecha_vencimiento");
                if (fechaSql != null) {
                    p.setFechaVencimiento(fechaSql.toLocalDate());
                }

                p.setActivo(rs.getBoolean("activo"));
                lista.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar productos activos en ProductoDAO: " + e.getMessage());
        }

        return lista;
    }

    public List<Producto> buscarPorCodigoONombre(String criterio) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT id_producto, codigo, nombre, categoria, precio_compra, " +
                     "precio_venta, stock_actual, stock_minimo, fecha_vencimiento, activo " +
                     "FROM tb_producto " +
                     "WHERE activo = TRUE AND (codigo = ? OR nombre LIKE ?)";

        try (Connection cn = ConexionDB.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, criterio);
            ps.setString(2, "%" + criterio + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto();
                    p.setIdProducto(rs.getInt("id_producto"));
                    p.setCodigo(rs.getString("codigo"));
                    p.setNombre(rs.getString("nombre"));
                    p.setCategoria(rs.getString("categoria"));
                    p.setPrecioCompra(rs.getDouble("precio_compra"));
                    p.setPrecioVenta(rs.getDouble("precio_venta"));
                    p.setStockActual(rs.getInt("stock_actual"));
                    p.setStockMinimo(rs.getInt("stock_minimo"));

                    Date fechaSql = rs.getDate("fecha_vencimiento");
                    if (fechaSql != null) {
                        p.setFechaVencimiento(fechaSql.toLocalDate());
                    }

                    p.setActivo(rs.getBoolean("activo"));
                    lista.add(p);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar productos en ProductoDAO: " + e.getMessage());
        }

        return lista;
    }
}