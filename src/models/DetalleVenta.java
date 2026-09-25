package models;

public class DetalleVenta {
	
	// Atributos propios
	private int idDetalle;
    private Producto producto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    // Constructores
    public DetalleVenta() {
    }

    public DetalleVenta(Producto producto, int cantidad, double precioUnitario) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = calcularSubtotal();
    }

    // Métodos
    public double calcularSubtotal() {
        return this.cantidad * this.precioUnitario;
    }

    // Getters + Setters
    public int getIdDetalle() {
    	return idDetalle;
    }
    
    public void setIdDetalle(int idDetalle) {
    	this.idDetalle = idDetalle; 
    }

    public Producto getProducto() {
    	return producto;
    }
    
    public void setProducto(Producto producto) {
    	this.producto = producto;
    }

    public int getCantidad() {
    	return cantidad;
    }
    
    public void setCantidad(int cantidad) { 
        this.cantidad = cantidad; 
        this.subtotal = calcularSubtotal(); 
    }

    public double getPrecioUnitario() {
    	 return precioUnitario; 
    }
    
    public void setPrecioUnitario(double precioUnitario) { 
        this.precioUnitario = precioUnitario; 
        this.subtotal = calcularSubtotal(); 
    }

    public double getSubtotal() {
    	return subtotal;
    }
    
    public void setSubtotal(double subtotal) {
    	this.subtotal = subtotal;
    }
    
}