package logico.consultorio;

import logico.Clinica;
import logico.catalogo.Enfermedad;
import logico.catalogo.Vacuna;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class Paciente implements Serializable {

    private static final long serialVersionUID = 2532314229066693215L;

    private String id;
    private String nombre;
    private String apellido;
    private String cedula;
    private String telefono;
    private Date fecNacim;
    private String sexo;
    private BigDecimal peso;
    private BigDecimal estatura;
    private String tipoSangre;
    private String direccion;
    private String estado;

    private ArrayList<Vacuna> vacunas;
    private ArrayList<Consulta> historialClinico;
    private ArrayList<Enfermedad> enfermedades;

    public Paciente(int idNumber, String nombre, String apellido, String cedula, String telefono, Date fecNacim,
                    String sexo, BigDecimal peso, BigDecimal estatura, String tipoSangre, String direccion, String estado) {
        super();
        setId(idNumber);
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.telefono = telefono;
        this.fecNacim = fecNacim;
        this.sexo = sexo;
        this.peso = peso;
        this.estatura = estatura;
        this.tipoSangre = tipoSangre;
        this.direccion = direccion;
        this.estado = estado;

        this.vacunas = new ArrayList<>();
        this.historialClinico = new ArrayList<>();
        this.enfermedades = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public int getIdNumber() {
        return Clinica.getInstancia().getIdNumber(this.id, Paciente.class);
    }

    public void setId(int idNumber) {
        this.id = Clinica.getInstancia().genId(idNumber, Paciente.class);
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

    public Date getFecNacim() {
        return fecNacim;
    }

    public void setFecNacim(Date fecNacim) {
        this.fecNacim = fecNacim;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getEstatura() {
        return estatura;
    }

    public void setEstatura(BigDecimal estatura) {
        this.estatura = estatura;
    }

    public String getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(String tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public ArrayList<Vacuna> getVacunas() {
        return vacunas;
    }

    public void setVacunas(ArrayList<Vacuna> vacunas) {
        this.vacunas = vacunas;
    }

    public ArrayList<Consulta> getHistorialClinico() {
        return historialClinico;
    }

    public void setHistorialClinico(ArrayList<Consulta> historialClinico) {
        this.historialClinico = historialClinico;
    }

    public ArrayList<Enfermedad> getEnfermedades() {
        return enfermedades;
    }

    public void setEnfermedades(ArrayList<Enfermedad> enfermedades) {
        this.enfermedades = enfermedades;
    }

    public ArrayList<String> getDoctores() {
        ArrayList<String> doctores = new ArrayList<>();

        for (Consulta consulta : historialClinico) {
            if (consulta.getDoctor() != null) {
                String nomDoc = consulta.getDoctor().getNombre();
                if (!doctores.contains(nomDoc)) {
                    doctores.add(nomDoc);
                }
            }
        }

        return doctores.isEmpty() ? null : doctores;
    }

    public void agregarEnfermedad(Enfermedad e) {
        if (e != null && !enfermedades.contains(e)) {
            enfermedades.add(e);
        }
    }

    public Enfermedad buscarEnfermedadPorId(String id) {
        if (id == null) return null;
        for (Enfermedad e : enfermedades) {
            if (e != null && e.getId().equalsIgnoreCase(id)) {
                return e;
            }
        }
        return null;
    }

    public void agregarVacuna(Vacuna v) {
        if (v != null && !vacunas.contains(v)) {
            vacunas.add(v);
        }
    }

    public Vacuna buscarVacunaPorId(String id) {
        if (id == null || vacunas == null) return null;
        for (Vacuna v : vacunas) {
            if (v != null && v.getId().equalsIgnoreCase(id)) {
                return v;
            }
        }
        return null;
    }
}