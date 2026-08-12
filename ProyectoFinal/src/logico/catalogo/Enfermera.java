package logico.catalogo;

import logico.Clinica;
import java.io.Serializable;

public class Enfermera {
    private static final long serialVersionUID = 1L;
    private String id;
    private String nombre;
    private String apellido;
    private String cedula;
    private String telefono;
    private Usuario usuario;

    public Enfermera(int idNumber, String nombre, String apellido, String cedula, String telefono, Usuario usuario) {
        setId(idNumber);
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.telefono = telefono;
        this.usuario = usuario;
    }

    public String getId() {
        return id;
    }

    public void setId(int idNumber) {
        this.id = Clinica.getInstancia().genId(idNumber, Enfermera.class);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreApellido() {
        return nombre + " " + apellido;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}