package logico.consultorio;

import logico.catalogo.Doctor;
import logico.catalogo.EstadoCita;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

public class Cita implements Serializable {

	private static final long serialVersionUID = -4304362059522165583L;

	private String id;
	private LocalDateTime fechaRegistro;
	private Date fechaConsulta;
	private Time horaConsulta;
	private EstadoCita estado;
	private Paciente paciente;
	private Doctor doctor;

	public Cita(String id, LocalDateTime fechaRegistro, Date fechaConsulta, Time horaConsulta, EstadoCita estado, Paciente paciente, Doctor doctor) {
		this.id = id;
		this.fechaRegistro = fechaRegistro;
		this.fechaConsulta = fechaConsulta;
		this.horaConsulta = horaConsulta;
		this.estado = estado;
		this.paciente = paciente;
		this.doctor = doctor;
	}

	public String getId() {
		return id;
	}

	public int getIdNumber(){
		return Integer.parseInt(id.replace("CIT-", ""));
	}

	public void setId(int idNumber) {
		this.id = "CIT-" + idNumber;
	}

	public LocalDateTime getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDateTime fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Date getFechaConsulta() {
		return fechaConsulta;
	}

	public void setFechaConsulta(Date fechaConsulta) {
		this.fechaConsulta = fechaConsulta;
	}

	public Time getHoraConsulta() {
		return horaConsulta;
	}

	public void setHoraConsulta(Time horaConsulta) {
		this.horaConsulta = horaConsulta;
	}

	public EstadoCita getEstado() {
		return estado;
	}

	public void setEstado(EstadoCita estado) {
		this.estado = estado;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
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
		if (estado == EstadoCita.PROGRAMADA) {
			estado = EstadoCita.COMPLETADA;
		}
	}
}