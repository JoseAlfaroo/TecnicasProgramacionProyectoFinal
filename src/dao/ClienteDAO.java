package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.ConexionDB;
import models.Cliente;

public class ClienteDAO {

    public List<Cliente> listar() {

        List<Cliente> lista = new ArrayList<>();

        String sql = "SELECT id_cliente, num_documento, nombre_completo, telefono, estado " +
                     "FROM tb_cliente";

        try (Connection cn = ConexionDB.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Cliente c = new Cliente();

                c.setIdPersona(rs.getInt("id_cliente"));
                c.setNumDocumento(rs.getString("num_documento"));
                c.setNombreCompleto(rs.getString("nombre_completo"));
                c.setTelefono(rs.getString("telefono"));
                c.setEstado(rs.getBoolean("estado"));

                lista.add(c);
            }

        } catch (SQLException e) {
            System.err.println(
                "Error al listar clientes en ClienteDAO: " + e.getMessage()
            );
        }

        return lista;
    }
}