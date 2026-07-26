package controllers;

import java.awt.event.KeyEvent;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import logico.Cita;
import logico.Clinica;
import logico.Doctor;
import logico.Paciente;
import utilidad.Formato;

public class CitaController {

	private Paciente auxPaciente;
	private Doctor auxDoctor;

	public CitaController() {
		this.auxPaciente = null;
		this.auxDoctor = null;
	}

	public String generarNuevoIdCita() {
		return "CI-" + Clinica.genCodigoCitas;
	}

	public Paciente getAuxPaciente() {
		return auxPaciente;
	}

	public void setAuxPaciente(Paciente auxPaciente) {
		this.auxPaciente = auxPaciente;
	}

	public Doctor getAuxDoctor() {
		return auxDoctor;
	}

	public void setAuxDoctor(Doctor auxDoctor) {
		this.auxDoctor = auxDoctor;
	}

	public void limpiarPaciente() {
		this.auxPaciente = null;
	}

	public void limpiarDoctor() {
		this.auxDoctor = null;
	}

	public Paciente buscarPacienteLive(String cedulaInput) {
		if (cedulaInput == null) return null;
		String cedulaLimpia = cedulaInput.replace("-", "").replaceAll("\\s+", "");

		if (cedulaLimpia.isEmpty()) {
			limpiarPaciente();
			return null;
		}

		if (Clinica.getInstancia().getPacientes() != null) {
			for (Paciente p : Clinica.getInstancia().getPacientes()) {
				String cedulaP = p.getCedula().replace("-", "").replaceAll("\\s+", "");
				if (cedulaP.equalsIgnoreCase(cedulaLimpia)) {
					this.auxPaciente = p;
					return p;
				}
			}
		}

		limpiarPaciente();
		return null;
	}

	public String autocompletarCedula(String textoActual, int keyCode) {
		if (keyCode == KeyEvent.VK_BACK_SPACE || keyCode == KeyEvent.VK_DELETE || 
			keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT || 
			keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN ||
			keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_ESCAPE) {
			buscarPacienteLive(textoActual);
			return null;
		}

		if (textoActual == null || textoActual.trim().isEmpty()) {
			limpiarPaciente();
			return null;
		}

		String textoLimpio = textoActual.replace("-", "").replaceAll("\\s+", "");

		if (Clinica.getInstancia().getPacientes() != null) {
			for (Paciente p : Clinica.getInstancia().getPacientes()) {
				String cedulaP = p.getCedula();
				String cedulaPLimpia = cedulaP.replace("-", "").replaceAll("\\s+", "");

				if (cedulaPLimpia.startsWith(textoLimpio) || cedulaP.startsWith(textoActual)) {
					this.auxPaciente = p;
					return cedulaP;
				}
			}
		}

		limpiarPaciente();
		return null;
	}

	public List<Doctor> obtenerDoctoresFiltrados(String filtro) {
		List<Doctor> doctoresEncontrados = new ArrayList<>();
		String filtroNorm = normalizar(filtro);

		if (Clinica.getInstancia().getDoctores() != null) {
			for (Doctor d : Clinica.getInstancia().getDoctores()) {
				boolean matchNombre = normalizar(d.getNombre()).contains(filtroNorm);
				boolean matchId = normalizar(d.getIdDoctor()).contains(filtroNorm);
				boolean matchEspecialidad = d.getEspecialidades() != null && d.getEspecialidades().stream()
						.anyMatch(esp -> normalizar(esp).contains(filtroNorm));

				if (filtroNorm.isEmpty() || matchNombre || matchId || matchEspecialidad) {
					doctoresEncontrados.add(d);
				}
			}
		}

		return doctoresEncontrados;
	}

	public boolean registrarCita(String idCita, String cedula, String nombre, Date fecha) {
		if (Formato.entradaVacia(cedula, "Debe ingresar su identificación.")) return false;
		if (Formato.entradaVacia(nombre, "Debe ingresar su nombre.")) return false;
		if (auxDoctor == null) {
			Formato.entradaVacia("", "Debe elegir un doctor válido de la lista.");
			return false;
		}

		// Obtenemos el paciente seleccionado o lo buscamos en el modelo de Clínica
		Paciente paciente = this.auxPaciente;
		if (paciente == null) {
			paciente = Clinica.getInstancia().buscarPacienteXIdentificacion(cedula.trim());
		}

		// Instanciamos Cita con el objeto Paciente correcto
		Cita cita = new Cita(
			idCita, 
			paciente, 
			auxDoctor, 
			fecha
		);

		Clinica.getInstancia().regCita(cita);
		return true;
	}

	public String normalizar(String texto) {
		if (texto == null) return "";
		return Normalizer.normalize(texto, Normalizer.Form.NFD)
				.replaceAll("\\p{M}", "")
				.toLowerCase()
				.trim();
	}
}