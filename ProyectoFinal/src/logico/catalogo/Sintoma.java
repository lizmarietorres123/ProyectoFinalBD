package logico.catalogo;

import logico.Clinica;

public class Sintoma {
    private String id;
    private String nombre;
    private String descripcion;

    public Sintoma(int idNumber, String nombre, String descripcion) {
        setId(idNumber);
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public int getIdNumber() {
        return Clinica.getInstancia().getIdNumber(this.id, Sintoma.class);
    }

    public void setId(int idNumber) {
        this.id = Clinica.getInstancia().genId(idNumber, Sintoma.class);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}