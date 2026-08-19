package logico.catalogo;

import logico.Clinica;

import java.util.ArrayList;
import java.util.List;

public class Especialidad {

    private String id;
    private String nombre;
    private String areaMedica;
    private String descripcion;
    private List<Enfermedad> enfermedades;

    public Especialidad() {
        this.enfermedades = new ArrayList<>();
    }

    public Especialidad(int idNumber, String nombre, String areaMedica, String descripcion) {
        setId(idNumber);
        this.nombre = nombre;
        this.areaMedica = areaMedica;
        this.descripcion = descripcion;
        this.enfermedades = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public int getIdNumber() {
        return Clinica.getInstancia().getIdNumber(this.id, Especialidad.class);
    }

    public void setId(int idNumber) {
        this.id = Clinica.getInstancia().genId(idNumber, Especialidad.class);
    }

    public void agregarEnfermedad(Enfermedad enfermedad) {
        if (enfermedad != null) {
            this.enfermedades.add(enfermedad);
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAreaMedica() {
        return areaMedica;
    }

    public void setAreaMedica(String areaMedica) {
        this.areaMedica = areaMedica;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Enfermedad> getEnfermedades() {
        return enfermedades;
    }

    public void setEnfermedades(List<Enfermedad> enfermedades) {
        this.enfermedades = enfermedades;
    }

    @Override
    public String toString() {
        return this.nombre;
    }
}