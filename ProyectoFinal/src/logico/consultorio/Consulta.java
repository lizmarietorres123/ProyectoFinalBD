package logico.consultorio;

import logico.Doctor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Consulta implements Serializable {
   
	private static final long serialVersionUID = 675187258451876275L;
	
	private String id;
    private LocalDateTime fecha;
    private String tratamiento;
    private String observaciones;
    private Cita cita;
    private ArrayList<Diagnostico> diagnosticos;

    
    public Consulta(String tratamiento, String observaciones, Cita cita) {
        this.tratamiento = tratamiento;
        this.observaciones = observaciones;
        this.cita = cita;
        this.diagnosticos = new ArrayList<>();
    }
    
    public String getId() {
        return id;
    }

    public int getIdNumber(){
        return Integer.parseInt(id.replace("CONS-", ""));
    }
    
    public void setId(int idNumber) {
        this.id = "CONS-" + idNumber;
    }
    
    public LocalDateTime getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
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
    
    public ArrayList<Diagnostico> getDiagnosticos() {
        return diagnosticos;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public void addDiagnostico(Diagnostico diagnostico) {
        if (diagnostico != null) {
            this.diagnosticos.add(diagnostico);
        }
    }

    public Doctor getDoctor(){
        return cita.getDoctor();
    }
}