package logico.consultorio;

import logico.Clinica;
import logico.catalogo.Doctor;
import logico.enfermeria.DetalleAnalisis;
import logico.enfermeria.DetalleVacuna;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Consulta implements Serializable {
   
	private static final long serialVersionUID = 675187258451876275L;
	
	private String id;
    private LocalDateTime fechaHora;
    private String observaciones;
    private Cita cita;
    private ArrayList<Diagnostico> diagnosticos;
    private ArrayList<DetalleAnalisis> analisis;
    private ArrayList<DetalleVacuna> vacunas;

    public Consulta(String observaciones, Cita cita) {
        this.observaciones = observaciones;
        this.cita = cita;
        this.diagnosticos = new ArrayList<>();
    }

    public Consulta(int idNumConsulta, LocalDateTime fechaHora, String observaciones, int idNumCita) {
        setId(idNumConsulta);
        this.fechaHora = fechaHora;
        this.observaciones = observaciones;
        setCita(idNumCita);
        this.diagnosticos = new ArrayList<>();
    }
    
    public String getId() {
        return id;
    }

    public int getIdNumber(){
        return Clinica.getInstancia().getIdNumber(this.id, Consulta.class);
    }
    
    public void setId(int idNumber) {
        this.id = Clinica.getInstancia().genId(idNumber, Consulta.class);
    }
    
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
    
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
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

    public void setCita(int idNumber) {
        String idCita = Clinica.getInstancia().genId(idNumber, Cita.class);
        this.cita = Clinica.getInstancia().buscarCitaXId(idCita);
    }

    public void addDiagnostico(Diagnostico diagnostico) {
        if (diagnostico != null) {
            this.diagnosticos.add(diagnostico);
        }
    }

    public void setDiagnosticos(ArrayList<Diagnostico> diagnosticos) {
        this.diagnosticos = diagnosticos;
    }

    public ArrayList<DetalleAnalisis> getAnalisis() {
        return analisis;
    }

    public void setAnalisis(ArrayList<DetalleAnalisis> analisis) {
        this.analisis = analisis;
    }

    public ArrayList<DetalleVacuna> getVacunas() {
        return vacunas;
    }

    public void setVacunas(ArrayList<DetalleVacuna> vacunas) {
        this.vacunas = vacunas;
    }

//    public Doctor getDoctor(){
//        return cita.getDoctor();
//    }

    public Doctor getDoctor() {

        if (this.cita != null) {
            return this.cita.getDoctor();
        }

        return Clinica.getDoctorActual();
    }

}