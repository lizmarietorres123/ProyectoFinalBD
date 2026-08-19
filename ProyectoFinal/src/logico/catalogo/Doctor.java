package logico.catalogo;

import logico.Clinica;
import logico.consultorio.Paciente;

import java.io.Serializable;
import java.util.ArrayList;

public class Doctor implements Serializable {
    private static final long serialVersionUID = 8080695571362501743L;

    private String id;
    private String nombre;
    private String apellido;
    private int cupoDiario;
    private ArrayList<Paciente> pacientes;
    private ArrayList<String> especialidades;
    private Usuario usuario;

    // Constructor completo para mapear desde la Base de Datos
    public Doctor(int idNumber, String nombre, String apellido, int cupoDiario) {
        setId(idNumber);
        this.nombre = nombre;
        this.apellido = apellido;
        this.cupoDiario = cupoDiario;
        this.especialidades = new ArrayList<>();
        this.pacientes = new ArrayList<>();
        this.usuario = null;
    }

    // Constructor para cuando se crea desde la interfaz gráfica
    public Doctor(String nombre, String apellido, int cupoDiario, ArrayList<String> especialidades) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cupoDiario = cupoDiario;
        this.especialidades = (especialidades != null) ? especialidades : new ArrayList<>();
        this.pacientes = new ArrayList<>();
        this.usuario = null;
    }

    public String getId() {
        return id;
    }

    public int getIdNumber() {
        return Clinica.getInstancia().getIdNumber(this.id, Doctor.class);
    }

    public void setId(int idNumber) {
        this.id = Clinica.getInstancia().genId(idNumber, Doctor.class);
    }

    public void setId(String idDoctor) {
        this.id = idDoctor; // Corrección: Antes decía this.id = id;
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