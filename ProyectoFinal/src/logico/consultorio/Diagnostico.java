package logico.consultorio;

import logico.catalogo.Enfermedad;

import java.io.Serializable;
import java.util.Date;

public class Diagnostico implements Serializable{
    
	private static final long serialVersionUID = -2057383297139900046L;
	
	private String id;
    private String descripcion;
    private String codigoDiagnostico;
    private Enfermedad enfermedadDiagnosticada;
    private Date fecha;
    
    public Diagnostico(String id, String descripcion, Date fecha) {
        this.id = id;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getCodigoDiagnostico() {
        return codigoDiagnostico;
    }
    
    public void setCodigoDiagnostico(String codigoDiagnostico) {
        this.codigoDiagnostico = codigoDiagnostico;
    }
    
    public Enfermedad getEnfermedadDiagnosticada() {
        return enfermedadDiagnosticada;
    }
    
    public void setEnfermedadDiagnosticada(Enfermedad enfermedadDiagnosticada) {
        this.enfermedadDiagnosticada = enfermedadDiagnosticada;
    }
    
    public Date getFecha() {
        return fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
}