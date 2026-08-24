package logico.catalogo;

import logico.Clinica;

import java.io.Serializable;

public class Doctor implements Serializable {
    private static final long serialVersionUID = 8080695571362501743L;

    private String id;
    private String nombre;
    private String apellido;
    private int cupoDiario;
    private Especialidad especialidad;
    private Usuario usuario;
    private String estado;

    public Doctor(int idNumber, String nombre, String apellido, int cupoDiario, int usuario, int especialidad, String estado) {
        setId(idNumber);
        this.nombre = nombre;
        this.apellido = apellido;
        this.cupoDiario = cupoDiario;
        setUsuario(usuario);
        setEspecialidad(especialidad);
        this.estado = estado;
        this.usuario = null;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(int id) {
        this.especialidad = Clinica.getInstancia().buscarEspecialidadXIdNumber(id);
    }

    public String getId() { return id; }
    public int getIdNumber() { return Clinica.getInstancia().getIdNumber(this.id, Doctor.class); }
    public void setId(int idNumber) { this.id = Clinica.getInstancia().genId(idNumber, Doctor.class); }
    public void setId(String idDoctor) { this.id = idDoctor; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public int getCupoDiario() { return cupoDiario; }
    public void setCupoDiario(int cupoDiario) { this.cupoDiario = cupoDiario; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(int id) { this.usuario = Clinica.getInstancia().buscarUsuarioXIdNumber(id); }
}