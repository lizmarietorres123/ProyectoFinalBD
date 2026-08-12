package controllers;

import java.util.ArrayList;
import logico.Clinica;
import logico.catalogo.Doctor;

public class DoctorController {

	public DoctorController() {
	}

	public ArrayList<Doctor> obtenerTodos() {
		return Clinica.getInstancia().getDoctores();
	}

	public Doctor buscarPorId(String id) {
		if (id == null) return null;
		for (Doctor d : obtenerTodos()) {
			if (d.getId().equalsIgnoreCase(id)) {
				return d;
			}
		}
		return null;
	}

	public boolean registrar(Doctor doctor) {
		if (doctor != null && buscarPorId(doctor.getId()) == null) {
			Clinica.getInstancia().getDoctores().add(doctor);
			return true;
		}
		return false;
	}

	public boolean actualizar(Doctor doctorActualizado) {
		Doctor actual = buscarPorId(doctorActualizado.getId());
		if (actual != null) {
			actual.setNombre(doctorActualizado.getNombre());
			actual.setCupoDiario(doctorActualizado.getCupoDiario());
			actual.setEspecialidades(doctorActualizado.getEspecialidades()); //[cite: 33]
			return true;
		}
		return false;
	}

	public boolean eliminar(Doctor doctor) {
		if (doctor != null) {
			return Clinica.getInstancia().getDoctores().remove(doctor);
		}
		return false;
	}
}