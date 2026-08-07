package logico.catalogo;

import java.util.ArrayList;
import java.util.List;

public class Especialidad {
    
    private String nombre;
    private String areaMedica;
    private String descripcion;
    private List<Enfermedad> enfermedades;

    public Especialidad() {
        this.enfermedades = new ArrayList<>();
    }

    public Especialidad(String nombre, String areaMedica, String descripcion) {
        this.nombre = nombre;
        this.areaMedica = areaMedica;
        this.descripcion = descripcion;
        this.enfermedades = new ArrayList<>();
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
