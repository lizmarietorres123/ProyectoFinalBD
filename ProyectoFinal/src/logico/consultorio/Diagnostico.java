package logico.consultorio;

import logico.catalogo.Enfermedad;
import logico.catalogo.Sintoma;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Diagnostico implements Serializable {

    private static final long serialVersionUID = -2057383297139900046L;

    private String id;
    private String descripcion;
    private String observacion;
    private String tipo;
    private String estado;
    private Consulta consulta;
    private Enfermedad enfermedad;
    private Map<Sintoma, String> sintomas;
    private ArrayList<Tratamiento> tratamientos = new ArrayList<>();

    public Diagnostico() {
    }

    public Diagnostico(Enfermedad enfermedad, String observacion, ArrayList<Tratamiento> tratamientos) {
        this.enfermedad = enfermedad;
        this.observacion = observacion;
        this.descripcion = observacion;
        this.tratamientos = (tratamientos != null) ? tratamientos : new ArrayList<>();
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

    public String getObservacion() {
        return observacion != null ? observacion : descripcion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
        this.descripcion = observacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public Enfermedad getEnfermedad() {
        return enfermedad;
    }

    public void setEnfermedad(Enfermedad enfermedad) {
        this.enfermedad = enfermedad;
    }

    public Map<Sintoma, String> getSintomas() {
        return sintomas;
    }

    public void setSintomas(Map<Sintoma, String> sintomas) {
        this.sintomas = sintomas;
    }

    public ArrayList<Tratamiento> getTratamientos() {
        return tratamientos;
    }

    public void setTratamientos(ArrayList<Tratamiento> tratamientos) {
        this.tratamientos = tratamientos;
    }
}