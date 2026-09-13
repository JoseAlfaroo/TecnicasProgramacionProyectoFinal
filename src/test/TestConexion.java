package test;

import dao.PruebaDAO;

public class TestConexion {

    public static void main(String[] args) {
        PruebaDAO dao = new PruebaDAO();
        
        System.out.println("Enviando registro de prueba a MySQL...");
        boolean insertado = dao.insertarPrueba("Prueba inicial de conexión exitosa");

        if (insertado) {
            System.out.println("¡ÉXITO! Se insertó el registro correctamente en tb_prueba.");
        } else {
            System.out.println("¡ERROR! No se pudo insertar el registro. Revisa la consola.");
        }
    }
}