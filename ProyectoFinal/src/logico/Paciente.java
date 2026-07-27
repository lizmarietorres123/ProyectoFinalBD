package logico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Paciente implements Serializable {

	private static final long serialVersionUID = 2532314229066693215L;

	private String idPaciente;
	private String nombre;
	private String apellido;
	private String cedula;
	private String telefono;
	private Date fecNacim;
	private String sexo;
	private float peso;
	private float estatura;
	private String tipoSangre;
	private String direccion;

	private ArrayList<Vacuna> vacunas;
	private ArrayList<Consulta> resumen;
	private ArrayList<Consulta> historialClinico;
	private ArrayList<Enfermedad> enfermedades;

	public Paciente(String idPaciente, String nombre, String apellido, String cedula, String telefono, Date fecNacim,
	                String sexo, float peso, float estatura, String tipoSangre, String direccion) {
		super();
		this.idPaciente = idPaciente;
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

		this.vacunas = new ArrayList<>();
		this.resumen = new ArrayList<>();
		this.historialClinico = new ArrayList<>();
		this.enfermedades = new ArrayList<>();
	}

	public String getIdPaciente() {
		return idPaciente;
	}

	public void setIdPaciente(String idPaciente) {
		this.idPaciente = idPaciente;
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

	public float getPeso() {
		return peso;
	}

	public void setPeso(float peso) {
		this.peso = peso;
	}

	public float getEstatura() {
		return estatura;
	}

	public void setEstatura(float estatura) {
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

	public ArrayList<Vacuna> getVacunas() {
		return vacunas;
	}

	public void setVacunas(ArrayList<Vacuna> vacunas) {
		this.vacunas = vacunas;
	}

	public ArrayList<Consulta> getResumen() {
		return resumen;
	}

	public void setResumen(ArrayList<Consulta> resumen) {
		this.resumen = resumen;
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

	public void addConsultaToResumen(Consulta consulta) {
		if (consulta != null && consulta.getEsImportante()) {
			resumen.add(consulta);
		}
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

	public ArrayList<Enfermedad> getEnfermedadesBajoVigilancia() {
		ArrayList<Enfermedad> lista = new ArrayList<>();
		for (Enfermedad e : enfermedades) {
			if (e != null && e.isVigilancia()) {
				lista.add(e);
			}
		}
		return lista;
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