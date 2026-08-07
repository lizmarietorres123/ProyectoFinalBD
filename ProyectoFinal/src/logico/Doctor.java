package logico;

import logico.catalogo.Usuario;
import logico.consultorio.Paciente;

import java.io.Serializable;
import java.util.ArrayList;

public class Doctor implements Serializable {
    private static final long serialVersionUID = 8080695571362501743L;
    
    private String idDoctor;
    private String nombre;
    private int cupoDiario;
    private ArrayList<Paciente> pacientes;
    private ArrayList<String> especialidades;
    private Usuario usuario;

    public Doctor(String idDoctor, String nombre, int cupoDiario, ArrayList<String> especialidades) {
        super();
        this.idDoctor = idDoctor;
        this.nombre = nombre;
        this.cupoDiario = cupoDiario;
        this.especialidades = especialidades;
        pacientes = new ArrayList<>();
        this.usuario = null;
    }

    public String getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(String idDoctor) {
        this.idDoctor = idDoctor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCupoDiario() {
        return cupoDiario;
    }

    public void setCupoDiario(int cupoDiario) {
        this.cupoDiario = cupoDiario;
    }

    public ArrayList<Paciente> getPacientes() {
        return pacientes;
    }

    public void setPacientes(ArrayList<Paciente> pacientes) {
        this.pacientes = pacientes;
    }

    public ArrayList<String> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(ArrayList<String> especialidades) {
        this.especialidades = especialidades;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}