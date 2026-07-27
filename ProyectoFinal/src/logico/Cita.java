package logico;

import java.io.Serializable;
import java.util.Date;

public class Cita implements Serializable {

	private static final long serialVersionUID = -4304362059522165583L;

	private String idCita;
	private Paciente paciente;
	private Doctor doctor;
	private Date fechaHora;
	private EstadoCita estado;
	private Consulta consultaGenerada;

	public Cita(String idCita, Paciente paciente, Doctor doctor, Date fechaHora) {
		super();
		this.idCita = idCita;
		this.paciente = paciente;
		this.doctor = doctor;
		this.fechaHora = fechaHora;
		this.estado = EstadoCita.PROGRAMADA;
		this.consultaGenerada = null;
	}

	public String getIdCita() {
		return idCita;
	}

	public void setIdCita(String idCita) {
		this.idCita = idCita;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public String getIdPersona() {
		return (paciente != null && paciente.getCedula() != null) ? paciente.getCedula() : "";
	}

	public String getNombrePersona() {
		if (paciente == null) return "";
		String nombreComp = paciente.getNombre() != null ? paciente.getNombre() : "";
		if (paciente.getApellido() != null && !paciente.getApellido().isEmpty()) {
			nombreComp += " " + paciente.getApellido();
		}
		return nombreComp;
	}

	public String getTelefonoPersona() {
		return (paciente != null && paciente.getTelefono() != null) ? paciente.getTelefono() : "";
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public Date getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
	}

	public Date getFecha() {
		return fechaHora;
	}

	public void setFecha(Date fecha) {
		this.fechaHora = fecha;
	}

	public EstadoCita getEstado() {
		return estado;
	}

	public void setEstado(EstadoCita estado) {
		this.estado = estado;
	}

	public Consulta getConsultaGenerada() {
		return consultaGenerada;
	}

	public void setConsultaGenerada(Consulta consultaGenerada) {
		this.consultaGenerada = consultaGenerada;
	}

	public void cancelar() {
		if (estado == EstadoCita.PROGRAMADA) {
			estado = EstadoCita.CANCELADA;
		}
	}

	public void marcarNoAsistio() {
		if (estado == EstadoCita.PROGRAMADA) {
			estado = EstadoCita.NO_ASISTIO;
		}
	}

	public void completar() {
		if (consultaGenerada != null) {
			estado = EstadoCita.COMPLETADA;
		}
	}
}