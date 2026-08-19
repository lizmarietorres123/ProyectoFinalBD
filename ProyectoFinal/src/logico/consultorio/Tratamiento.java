package logico.consultorio;

import logico.Clinica;
import logico.catalogo.Medicamento;

import java.util.Date;

public class Tratamiento {

    private String id;
    private String descripcion;
    private int dosis;
    private String estado;
    private Date fechaInicio;
    private Date fechaFin;
    private String frecuencia;
    private Diagnostico diagnostico;
    private Medicamento medicamento;

    public Tratamiento() {
    }

    public Tratamiento(int idNumber, Diagnostico diagnostico, Medicamento medicamento, int dosis, String frecuencia, Date fechaInicio, Date fechaFin, String descripcion, String estado) {
        setId(idNumber);
        this.diagnostico = diagnostico;
        this.medicamento = medicamento;
        this.dosis = dosis;
        this.frecuencia = frecuencia;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public int getIdNumber() {
        return Clinica.getInstancia().getIdNumber(this.id, Tratamiento.class);
    }

    public void setId(int idNumber) {
        this.id = Clinica.getInstancia().genId(idNumber, Tratamiento.class);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDosis() {
        return dosis;
    }

    public void setDosis(int dosis) {
        this.dosis = dosis;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public Diagnostico getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(Diagnostico diagnostico) {
        this.diagnostico = diagnostico;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }
}