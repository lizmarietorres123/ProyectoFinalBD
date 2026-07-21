package logico;

import java.io.Serializable;
import java.util.Date;

public class Consulta implements Serializable{
   
	private static final long serialVersionUID = 675187258451876275L;
	
	private String id;
    private Paciente paciente;
    private Doctor doctor;
    private Date fecha;
    private String sintomas;
    private Diagnostico diagnostico;
    private String tratamiento;
    private String observaciones;
    //Que tenga el doctor sobre la consulta.
    private boolean esImportante;
    // LAS IMPORTANTES VAN DENTRO DE LA HISTORIA CLINICA DEL PACIENTE
    
    public Consulta(String id, Paciente paciente, Doctor doctor, Date fecha) {
        this.id = id;
        this.paciente = paciente;
        this.doctor = doctor;
        this.fecha = fecha;
        this.esImportante = false;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public Paciente getPaciente() {
        return paciente;
    }
    
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
    
    public Doctor getDoctor() {
        return doctor;
    }
    
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    
    public Date getFecha() {
        return fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    public String getSintomas() {
        return sintomas;
    }
    
    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }
    
    public Diagnostico getDiagnostico() {
        return diagnostico;
    }
    
    public void setDiagnostico(Diagnostico diagnostico) {
        this.diagnostico = diagnostico;
    }
    
    public String getTratamiento() {
        return tratamiento;
    }
    
    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public boolean getEsImportante() {
        return esImportante;
    }
    
    public void setEsImportante(boolean esImportante) {
        this.esImportante = esImportante;
    }
    
    
}