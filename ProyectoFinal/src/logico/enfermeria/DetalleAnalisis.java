package logico.enfermeria;

import logico.Clinica;
import logico.catalogo.Analisis;
import logico.catalogo.Enfermera;
import logico.consultorio.Consulta;

import java.time.LocalDateTime;

public class DetalleAnalisis {

    private String id;
    private Analisis analisis;
    private Consulta consulta;
    private Enfermera enfermera;
    private Double resultado;
    private String estado;
    private LocalDateTime fechaResultado;
    private String observaciones;

    // Constructor para cuando el médico indica el análisis desde la consulta (Aún pendiente)
    public DetalleAnalisis(Analisis analisis, Consulta consulta) {
        this.analisis = analisis;
        this.consulta = consulta;
        this.resultado = null; // Mejor usar null en BD para resultados no procesados aún
        this.estado = "Pendiente";
        this.observaciones = null;
        this.enfermera = null;
        this.fechaResultado = null;
    }

    // Constructor completo para mapear desde la Base de Datos
    public DetalleAnalisis(int idNumber, Analisis analisis, Consulta consulta, Enfermera enfermera, Double resultado, String estado, LocalDateTime fechaResultado, String observaciones) {
        setId(idNumber);
        this.analisis = analisis;
        this.consulta = consulta;
        this.enfermera = enfermera;
        this.resultado = resultado;
        this.estado = estado;
        this.fechaResultado = fechaResultado;
        this.observaciones = observaciones;
    }

    public String getId() {
        return id;
    }

    public int getIdNumber() {
        return Clinica.getInstancia().getIdNumber(this.id, DetalleAnalisis.class);
    }

    public void setId(int idNumber) {
        this.id = Clinica.getInstancia().genId(idNumber, DetalleAnalisis.class);
    }

    public Analisis getAnalisis() {
        return analisis;
    }

    public void setAnalisis(Analisis analisis) {
        this.analisis = analisis;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public Double getResultado() {
        return resultado;
    }

    public void setResultado(Double resultado) {
        this.resultado = resultado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaResultado() {
        return fechaResultado;
    }

    public void setFechaResultado(LocalDateTime fechaResultado) {
        this.fechaResultado = fechaResultado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Enfermera getEnfermera() {
        return enfermera;
    }

    public void setEnfermera(Enfermera enfermera) {
        this.enfermera = enfermera;
    }
}