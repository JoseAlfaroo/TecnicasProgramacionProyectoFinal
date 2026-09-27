package dao;

import config.ConexionDB;
import models.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public boolean registrar(Cliente c) {
        String sql = "INSERT INTO tb_cliente (num_documento, nombre_completo, telefono, estado) VALUES (?, ?, ?, ?)";

        try (Connection cn = ConexionDB.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, c.getNumDocumento());
            ps.setString(2, c.getNombreCompleto());
            ps.setString(3, c.getTelefono());
            ps.setBoolean(4, c.isEstado()); 

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al registrar cliente en ClienteDAO: " + e.getMessage());
            return false;
        }
    }

    public List<Cliente> buscarPorDocumentoONombre(String criterio) {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT id_cliente, num_documento, nombre_completo, telefono, estado " +
                     "FROM tb_cliente WHERE num_documento = ? OR nombre_completo LIKE ?";

        try (Connection cn = ConexionDB.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, criterio);
            ps.setString(2, "%" + criterio + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cliente c = new Cliente();
                    c.setIdPersona(rs.getInt("id_cliente"));
                    c.setNumDocumento(rs.getString("num_documento"));
                    c.setNombreCompleto(rs.getString("nombre_completo"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setEstado(rs.getBoolean("estado")); 

                    lista.add(c);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar clientes en ClienteDAO: " + e.getMessage());
        }

        return lista;
    }
}