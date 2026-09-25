package models;

import java.time.LocalDate;

public class Producto {

	// Atributos propios
	private int idProducto;
    private String codigo;
    private String nombre;
    private String categoria;
    private double precioCompra;
    private double precioVenta;
    private int stockActual;
    private int stockMinimo;
    private LocalDate fechaVencimiento;
    private boolean activo;

    // Constructores
    public Producto() {
        this.activo = true;
    }
    
    public Producto(int idProducto, String codigo, String nombre, String categoria, double precioCompra, double precioVenta, int stockActual, int stockMinimo, LocalDate fechaVencimiento, boolean activo) {
    	this.idProducto = idProducto;
    	this.codigo = codigo;
    	this.nombre = nombre;
    	this.categoria = categoria;
    	this.precioCompra = precioCompra;
    	this.precioVenta = precioVenta;
    	this.stockActual = stockActual;
    	this.stockMinimo = stockMinimo;
    	this.fechaVencimiento = fechaVencimiento;
    	this.activo = activo;
    }
    
    // Métodos
    public boolean esStockCritico() {
        return this.stockActual <= this.stockMinimo;
    }

    public boolean esPrecioValido() {
        return this.precioVenta > 0;
    }

    public boolean tieneStockSuficiente(int cantidad) {
        return this.stockActual >= cantidad;
    }
    
    // Getters + Setters
    public int getIdProducto() {
    	return idProducto;
    }
    
    public void setIdProducto(int idProducto) {
    	this.idProducto = idProducto;
    }

    public String getCodigo() {
    	return codigo;
    }
    
    public void setCodigo(String codigo) {
    	this.codigo = codigo;
    }

    public String getNombre() {
    	return nombre;
    }
    
    public void setNombre(String nombre) {
    	this.nombre = nombre;
    }

    public String getCategoria() {
    	return categoria;
    }
    
    public void setCategoria(String categoria) {
    	this.categoria = categoria;
    }

    public double getPrecioCompra() {
    	return precioCompra;
    }
    
    public void setPrecioCompra(double precioCompra) {
    	this.precioCompra = precioCompra;
    }

    public double getPrecioVenta() {
    	return precioVenta;
    }
    
    public void setPrecioVenta(double precioVenta) {
    	this.precioVenta = precioVenta;
    }

    public int getStockActual() {
    	return stockActual;
    }
    
    public void setStockActual(int stockActual) {
    	this.stockActual = stockActual;
    }

    public int getStockMinimo() {
    	return stockMinimo;
    }
    
    public void setStockMinimo(int stockMinimo) {
    	this.stockMinimo = stockMinimo;
    }

    public LocalDate getFechaVencimiento() {
    	return fechaVencimiento;
    }
    
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
    	this.fechaVencimiento = fechaVencimiento;
    }

    public boolean isActivo() {
    	return activo;
    }
    
    public void setActivo(boolean activo) {
    	this.activo = activo;
    }
    
}