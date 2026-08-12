package logico.enfermeria;

import logico.catalogo.Analisis;
import logico.catalogo.Enfermera;
import logico.consultorio.Consulta;

import java.time.LocalDateTime;

public class DetalleAnalisis {

    String id;
    Analisis analisis;
    Consulta consulta;
    Enfermera enfermera;
    Double resultado;
    String estado;
    LocalDateTime fechaResultado;
    String observaciones;

    public DetalleAnalisis(Analisis analisis, Consulta consulta) {
        this.analisis = analisis;
        this.consulta = consulta;
        this.resultado = 0.0;
        this.estado = "Pendiente";
        this.observaciones = null;
        this.enfermera = null;
    }

    // Constructor completo actualizado
    public DetalleAnalisis(String id, Analisis analisis, Consulta consulta, Enfermera enfermera, Double resultado, String estado, LocalDateTime fechaResultado, String observaciones) {
        this.id = id;
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

    public void setId(String id) {
        this.id = id;
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

    // Getters y Setters agregados para Enfermera
    public Enfermera getEnfermera() {
        return enfermera;
    }

    public void setEnfermera(Enfermera enfermera) {
        this.enfermera = enfermera;
    }
}