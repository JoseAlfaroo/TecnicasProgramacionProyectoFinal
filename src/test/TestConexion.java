package test;

import java.time.LocalDate;
import java.util.List;

import dao.*;
import models.*;

public class TestConexion {

	public static void main(String[] args) {

		//probarGeneracionCorrelativo();
        
		probarBusqueda("PROD-0001");
    }
	
	public static void probarConexion() {
        PruebaDAO dao = new PruebaDAO();
        
        System.out.println("Enviando registro de prueba a MySQL...");
        boolean insertado = dao.insertarPrueba("Prueba inicial de conexión exitosa");

        if (insertado) {
            System.out.println("¡ÉXITO! Se insertó el registro correctamente en tb_prueba.");
        } else {
            System.out.println("¡ERROR! No se pudo insertar el registro. Revisa la consola.");
        }
    }
	
	public static void probarGeneracionCorrelativo() {
        ProductoDAO productoDAO = new ProductoDAO();
        System.out.println("Siguiente código correlativo disponible: " + productoDAO.generarSiguienteCodigo());
    }
	
	public static void poblarProductos() {
        ProductoDAO productoDAO = new ProductoDAO();

        Producto[] productosPrueba = new Producto[] {
            new Producto(0, null, "Arroz Superior 5kg", "Abarrotes", 18.50, 22.00, 50, 10, LocalDate.of(2027, 6, 30), true),
            new Producto(0, null, "Aceite Vegetal 1L", "Abarrotes", 7.20, 9.50, 40, 8, LocalDate.of(2027, 12, 15), true),
            new Producto(0, null, "Azúcar Rubia 1kg", "Abarrotes", 3.10, 4.20, 60, 15, LocalDate.of(2028, 1, 10), true),
            new Producto(0, null, "Leche Evaporada 400g", "Lácteos", 3.20, 4.00, 100, 20, LocalDate.of(2026, 11, 20), true),
            new Producto(0, null, "Yogurt Fresa 1L", "Lácteos", 4.80, 6.50, 25, 5, LocalDate.of(2026, 10, 15), true),
            new Producto(0, null, "Detergente en Polvo 800g", "Limpieza", 6.00, 8.20, 30, 6, null, true),
            new Producto(0, null, "Jabón de Tocador 3pk", "Limpieza", 4.50, 6.00, 45, 10, null, true),
            new Producto(0, null, "Gaseosa Inka Kola 1.5L", "Bebidas", 4.50, 6.00, 35, 10, LocalDate.of(2027, 3, 31), true),
            new Producto(0, null, "Agua Mineral Sin Gas 2.5L", "Bebidas", 2.20, 3.50, 50, 12, LocalDate.of(2027, 8, 20), true),
            new Producto(0, null, "Fideos Tallarín 500g", "Abarrotes", 1.80, 2.70, 80, 15, LocalDate.of(2027, 9, 30), true)
        };

        int insertados = 0;
        for (Producto p : productosPrueba) {
            if (productoDAO.registrar(p)) {
                insertados++;
                System.out.println("✓ Registrado: " + p.getCodigo() + " - " + p.getNombre());
            } else {
                System.err.println("✗ Falló registro: " + p.getCodigo() + " - " + p.getNombre());
            }
        }

        System.out.println("\nResumen: " + insertados + " de 10 productos registrados correctamente en tb_producto.");
    }
		
	public static void probarBusqueda(String criterio) {
        ProductoDAO productoDAO = new ProductoDAO();
        
        System.out.println("\n--- Resultado de Búsqueda para Criterio: '" + criterio + "' ---");
        List<Producto> resultados = productoDAO.buscarPorCodigoONombre(criterio);

        if (resultados.isEmpty()) {
            System.out.println("No se encontraron coincidencias en la base de datos.");
        } else {
            for (Producto p : resultados) {
                System.out.printf("-> ID: %-3d | Código: %-9s | Nombre: %-25s | Precio: S/ %-6.2f | Stock: %-3d\n",
                        p.getIdProducto(), p.getCodigo(), p.getNombre(), p.getPrecioVenta(), p.getStockActual());
            }
        }
    }
}