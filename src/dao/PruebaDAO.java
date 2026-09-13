package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import config.ConexionDB;

public class PruebaDAO {

    public boolean insertarPrueba(String descripcion) {
        String sql = "INSERT INTO tb_prueba (descripcion) VALUES (?)";

        try (Connection cn = ConexionDB.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, descripcion);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar en tb_prueba: " + e.getMessage());
            return false;
        }
    }
}