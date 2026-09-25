package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venta {
	
	// Atributos propios
	private int idVenta;
    private Cliente cliente;
    private LocalDateTime fechaHora;
    private double subtotal;
    private double igv;
    private double total;
    private List<DetalleVenta> detalles;

    // Constantes
    public static final double TASA_IGV = 0.18;
    public static final double FACTOR_IGV = 1.0 + TASA_IGV;
    
    // Constructores
    public Venta() {
        this.detalles = new ArrayList<>();
        this.fechaHora = LocalDateTime.now();
    }

    public Venta(int idVenta, Cliente cliente, LocalDateTime fechaHora, double subtotal, double igv, double total) {
        this.idVenta = idVenta;
        this.cliente = cliente;
        this.fechaHora = fechaHora;
        this.subtotal = subtotal;
        this.igv = igv;
        this.total = total;
        this.detalles = new ArrayList<>();
    }
    
    // Métodos
    public void agregarDetalle(DetalleVenta detalle) {
        this.detalles.add(detalle);
        calcularTotales();
    }

    public void calcularTotales() {
        double sumaSubtotales = 0.0;
        for (DetalleVenta d : detalles) {
            sumaSubtotales += d.getSubtotal();
        }
        
        this.total = Math.round(sumaSubtotales * 100.0) / 100.0;
        this.subtotal = Math.round((this.total / FACTOR_IGV) * 100.0) / 100.0;
        this.igv = Math.round((this.total - this.subtotal) * 100.0) / 100.0;
    }

    // Getters + Setters
    public int getIdVenta() {
    	return idVenta;
    }
    
    public void setIdVenta(int idVenta) {
    	this.idVenta = idVenta;
    }

    public Cliente getCliente() {
    	return cliente;
    }
    
    public void setCliente(Cliente cliente) {
    	this.cliente = cliente;
    }

    public LocalDateTime getFechaHora() {
    	return fechaHora;
    }
    
    public void setFechaHora(LocalDateTime fechaHora) {
    	this.fechaHora = fechaHora;
    }

    public double getSubtotal() {
    	return subtotal;
    }
    
    public double getIgv() {
    	return igv;
    }
    
    public double getTotal() {
    	return total;
    }

    public List<DetalleVenta> getDetalles() {
    	return detalles;
    }
    
    public void setDetalles(List<DetalleVenta> detalles) {
    	this.detalles = detalles;
    }
    
}