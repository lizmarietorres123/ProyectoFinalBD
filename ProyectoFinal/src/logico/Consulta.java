package logico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Consulta implements Serializable {
   
	private static final long serialVersionUID = 675187258451876275L;
	
	private String id;
    private Paciente paciente;
    private Doctor doctor;
    private Date fecha;
    private ArrayList<Diagnostico> diagnosticos;
    private String tratamiento;
    private String observaciones;
    private boolean esImportante;
    
    public Consulta(String id, Paciente paciente, Doctor doctor, Date fecha) {
        this.id = id;
        this.paciente = paciente;
        this.doctor = doctor;
        this.fecha = fecha;
        this.diagnosticos = new ArrayList<>();
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
    
    public ArrayList<Diagnostico> getDiagnosticos() {
        return diagnosticos;
    }
    
    public void setDiagnosticos(ArrayList<Diagnostico> diagnosticos) {
        this.diagnosticos = diagnosticos;
    }
    
    public void addDiagnostico(Diagnostico diagnostico) {
        if (diagnostico != null) {
            this.diagnosticos.add(diagnostico);
        }
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