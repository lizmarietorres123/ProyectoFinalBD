package controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import logico.consultorio.Cita;
import logico.consultorio.Clinica;
import logico.Doctor;
import logico.consultorio.Paciente;

public class CitaController {

	public String generarNuevoIdCita() {
		return "CI-" + Clinica.genCodigoCitas;
	}

	public Paciente buscarPacientePorCedula(String cedula) {
		if (cedula == null || cedula.trim().isEmpty()) {
			return null;
		}
		return Clinica.getInstancia().buscarPacienteXIdentificacion(cedula.trim());
	}

	public List<Doctor> obtenerDoctoresFiltrados(String filtro) {
		List<Doctor> doctoresEncontrados = new ArrayList<>();
		String filtroNorm = (filtro == null) ? "" : filtro.toLowerCase().trim();

		if (Clinica.getInstancia().getDoctores() != null) {
			for (Doctor d : Clinica.getInstancia().getDoctores()) {
				boolean matchNombre = d.getNombre() != null && d.getNombre().toLowerCase().contains(filtroNorm);
				boolean matchId = d.getIdDoctor() != null && d.getIdDoctor().toLowerCase().contains(filtroNorm);
				boolean matchEspecialidad = d.getEspecialidades() != null && d.getEspecialidades().stream()
						.anyMatch(esp -> esp.toLowerCase().contains(filtroNorm));

				if (filtroNorm.isEmpty() || matchNombre || matchId || matchEspecialidad) {
					doctoresEncontrados.add(d);
				}
			}
		}

		return doctoresEncontrados;
	}

	public boolean registrarCita(String idCita, Paciente paciente, Doctor doctor, Date fecha) {
		if (paciente == null || doctor == null || fecha == null) {
			return false;
		}

		Cita cita = new Cita(idCita, paciente, doctor, fecha);
		Clinica.getInstancia().regCita(cita);
		return true;
	}

	// --- FUNCIONES DE BASE DE DATOS / PERSISTENCIA ---

	public List<Cita> obtenerTodasLasCitas() {
		List<Cita> lista = Clinica.getInstancia().getCitas();
		return (lista != null) ? lista : new ArrayList<>();
	}

	public Cita buscarCitaPorId(String idCita) {
		if (idCita == null || idCita.trim().isEmpty()) {
			return null;
		}
		for (Cita c : obtenerTodasLasCitas()) {
			if (c != null && c.getIdCita() != null && c.getIdCita().equalsIgnoreCase(idCita.trim())) {
				return c;
			}
		}
		return null;
	}

	public List<Cita> filtrarCitas(String filtro) {
		List<Cita> resultado = new ArrayList<>();
		String f = (filtro == null) ? "" : filtro.toLowerCase().trim();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		for (Cita c : obtenerTodasLasCitas()) {
			if (c != null) {
				String id = c.getIdCita() != null ? c.getIdCita().toLowerCase() : "";
				String paciente = c.getNombrePersona() != null ? c.getNombrePersona().toLowerCase() : "";
				String cedula = c.getIdPersona() != null ? c.getIdPersona().toLowerCase() : "";
				String doctor = (c.getDoctor() != null && c.getDoctor().getNombre() != null) ? c.getDoctor().getNombre().toLowerCase() : "";
				String estado = c.getEstado() != null ? c.getEstado().toString().toLowerCase() : "";
				String fecha = (c.getFechaHora() != null) ? sdf.format(c.getFechaHora()).toLowerCase() : "";

				if (f.isEmpty() || id.contains(f) || paciente.contains(f) || cedula.contains(f) || doctor.contains(f) || estado.contains(f) || fecha.contains(f)) {
					resultado.add(c);
				}
			}
		}
		return resultado;
	}

	public boolean modificarCita(Cita citaExistente, Paciente nuevoPaciente, Doctor nuevoDoctor, Date nuevaFecha) {
		if (citaExistente == null || nuevoPaciente == null || nuevoDoctor == null || nuevaFecha == null) {
			return false;
		}

		citaExistente.setPaciente(nuevoPaciente);
		citaExistente.setDoctor(nuevoDoctor);
		citaExistente.setFechaHora(nuevaFecha);
		return true;
	}

	public boolean eliminarCita(Cita cita) {
		if (cita == null) {
			return false;
		}
		List<Cita> citas = Clinica.getInstancia().getCitas();
		if (citas != null) {
			return citas.remove(cita);
		}
		return false;
	}
}