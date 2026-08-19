package logico.enfermeria;

import logico.Clinica;
import logico.catalogo.Enfermera;
import logico.catalogo.Vacuna;
import logico.consultorio.Consulta;

import java.time.LocalDateTime;

public class DetalleVacuna {

    private String id;
    private int dosis;
    private String lote;
    private String estado;
    private LocalDateTime fecha_aplicacion;
    private String observaciones;
    private Consulta consulta;
    private Vacuna vacuna;
    private Enfermera enfermera;

    // Constructor completo mapeado para la Base de Datos
    public DetalleVacuna(int idNumber, int dosis, String lote, String estado, LocalDateTime fecha_aplicacion, String observaciones, Consulta consulta, Vacuna vacuna, Enfermera enfermera) {
        setId(idNumber);
        this.dosis = dosis;
        this.lote = lote;
        this.estado = estado;
        this.fecha_aplicacion = fecha_aplicacion;
        this.observaciones = observaciones;
        this.consulta = consulta;
        this.vacuna = vacuna;
        this.enfermera = enfermera;
    }

    // Constructor simplificado para cuando el médico la indica (aún no se ha aplicado)
    public DetalleVacuna(Consulta consulta, Vacuna vacuna) {
        this.dosis = 0;
        this.lote = null;
        this.estado = "Pendiente";
        this.consulta = consulta;
        this.vacuna = vacuna;
        this.enfermera = null;
        this.fecha_aplicacion = null;
    }

    public String getId() {
        return id;
    }

    public int getIdNumber() {
        return Clinica.getInstancia().getIdNumber(this.id, DetalleVacuna.class);
    }

    public void setId(int idNumber) {
        this.id = Clinica.getInstancia().genId(idNumber, DetalleVacuna.class);
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDosis() {
        return dosis;
    }

    public void setDosis(int dosis) {
        this.dosis = dosis;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha_aplicacion() {
        return fecha_aplicacion;
    }

    public void setFecha_aplicacion(LocalDateTime fecha_aplicacion) {
        this.fecha_aplicacion = fecha_aplicacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public Vacuna getVacuna() {
        return vacuna;
    }

    public void setVacuna(Vacuna vacuna) {
        this.vacuna = vacuna;
    }

    public Enfermera getEnfermera() {
        return enfermera;
    }

    public void setEnfermera(Enfermera enfermera) {
        this.enfermera = enfermera;
    }
}