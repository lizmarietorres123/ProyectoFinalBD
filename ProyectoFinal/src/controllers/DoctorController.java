package controllers;

import java.util.ArrayList;
import java.util.List;

import logico.Clinica;
import logico.Doctor;
import logico.Especialidad;

public class DoctorController {

	public List<Especialidad> obtenerTodasLasEspecialidades() {
		List<Especialidad> catalogo = new ArrayList<>();
		if (Clinica.getInstancia().getEspecialidades() != null) {
			for (Especialidad esp : Clinica.getInstancia().getEspecialidades()) {
				if (esp != null) {
					catalogo.add(esp);
				}
			}
		}
		return catalogo;
	}

	public boolean guardarDoctor(Doctor doctor) {
		if (doctor == null) {
			return false;
		}
		return true;
	}
}