package models;

public class Cliente extends Persona {
	
	// Atributos propios
	private boolean estado;
	
	// Constructores
	public Cliente() {
        super();
        this.estado = true;
    }
	
	public Cliente(int idPersona, String numDocumento, String nombreCompleto, String telefono, boolean estado) {
        super(idPersona, numDocumento, nombreCompleto, telefono);
        this.estado = estado;
    }
	
	// Getters + Setters
	public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
}
