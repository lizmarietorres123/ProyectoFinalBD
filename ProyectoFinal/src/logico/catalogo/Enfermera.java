package logico.catalogo;

import logico.Clinica;
import java.io.Serializable;
import java.util.Objects;

public class Enfermera implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String nombre;
    private String apellido;
    private String cedula;
    private String telefono;
    private Usuario usuario;
    private String estado;

    public Enfermera(int idNumber, String nombre, String apellido, String cedula, String telefono, Usuario usuario, String estado) {
        setId(idNumber);
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.telefono = telefono;
        this.usuario = usuario;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(int idNumber) {
        this.id = Clinica.getInstancia().genId(idNumber, Enfermera.class);
    }

    public int getIdNumber() {
        return Clinica.getInstancia().getIdNumber(this.id, Enfermera.class);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreApellido() {
        String nom = (nombre != null) ? nombre.trim() : "";
        String ape = (apellido != null) ? apellido.trim() : "";
        String res = (nom + " " + ape).trim();
        return res;
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

    public void setUsuario(int id) {
        this.usuario = Clinica.getInstancia().buscarUsuarioXIdNumber(id);
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        String nomCompleto = getNombreApellido();
        if (nomCompleto != null && !nomCompleto.isEmpty()) {
            return nomCompleto;
        }
        return (id != null) ? id : "";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Enfermera other = (Enfermera) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }
}