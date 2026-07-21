package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import logico.Especialidad;
import logico.Clinica;

public class DoctorController {

	public List<Especialidad> obtenerTodasLasEspecialidades() {
		List<Especialidad> catalogo = new ArrayList<>();
		for (Especialidad esp : Clinica.getInstancia().getEspecialidades()) {
			if (esp != null) {
				catalogo.add(esp);
			}
		}
		return catalogo;
	}

	public boolean guardarDoctor(String nombre, String sexo, String telefono, int cupo, List<Especialidad> especialidadesSeleccionadas) {
		if (utilidad.Formato.entradaVacia(nombre, "El nombre del doctor es obligatorio.") || 
			utilidad.Formato.entradaVacia(telefono, "El teléfono es obligatorio.")) {
			return false;
		}
		
		if (especialidadesSeleccionadas.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Debe seleccionar al menos una especialidad.", "Campo Requerido", javax.swing.JOptionPane.WARNING_MESSAGE);
			return false;
		}

		return true;
	}
}