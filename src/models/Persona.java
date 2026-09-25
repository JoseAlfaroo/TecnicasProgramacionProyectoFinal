package models;

public abstract class Persona {
	
	// Atributos propios
	protected int idPersona;
    protected String numDocumento;
    protected String nombreCompleto;
    protected String telefono;
   
    // Constructores
    public Persona() {
    }
    
    public Persona(int idPersona, String numDocumento, String nombreCompleto, String telefono) {
        this.idPersona = idPersona;
        this.numDocumento = numDocumento;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
    }
    
    // Getters + Setters
    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
}
