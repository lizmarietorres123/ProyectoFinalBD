package logico.enfermeria;

import logico.catalogo.Especialidad;
import logico.catalogo.Usuario;

public class Enfermera {
    private String id_enfermera;
    private String nombre;
    private String apellido;
    private String cedula;
    private String telefono;
    private Especialidad especialidad;
    private Usuario usuario;

    public Enfermera(String id_enfermera, String nombre, String apellido, String cedula, String telefono, Especialidad especialidad, Usuario usuario) {
        this.id_enfermera = id_enfermera;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.telefono = telefono;
        this.especialidad = especialidad;
        this.usuario = usuario;
    }

    public String getId_enfermera() {
        return id_enfermera;
    }

    public void setId_enfermera(String id_enfermera) {
        this.id_enfermera = id_enfermera;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}