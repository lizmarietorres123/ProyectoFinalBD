package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import logico.Cita;
import logico.Clinica;
import logico.Doctor;
import logico.Paciente;

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
}