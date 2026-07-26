package controllers;

import java.util.Date;
import logico.Clinica;
import logico.Paciente;
import utilidad.Formato;

public class PacienteController {

	private Paciente pacienteCreado;

	public PacienteController() {
		this.pacienteCreado = null;
	}

	public Paciente getPacienteCreado() {
		return pacienteCreado;
	}

	public boolean existeCedula(String cedula) {
		if (cedula == null || Clinica.getInstancia().getPacientes() == null) {
			return false;
		}
		String cedulaLimpia = cedula.replace("-", "").replaceAll("\\s+", "");
		
		for (Paciente p : Clinica.getInstancia().getPacientes()) {
			String cedulaP = p.getCedula().replace("-", "").replaceAll("\\s+", "");
			if (cedulaP.equalsIgnoreCase(cedulaLimpia)) {
				return true;
			}
		}
		return false;
	}

	public boolean registrarPaciente(String nombre, String cedula, String telefono, Date fecNacim, 
	                                 String sexo, String pesoStr, String estaturaStr, 
	                                 String tipoSangre, String direccion) {

		if (Formato.entradaVacia(nombre, "Debe ingresar el nombre del paciente.")) return false;
		if (Formato.entradaVacia(cedula, "Debe ingresar la cédula del paciente.")) return false;
		if (existeCedula(cedula.trim())) {
			Formato.entradaVacia("", "Ya existe un paciente registrado con esta cédula.");
			return false;
		}
		if (Formato.entradaVacia(telefono, "Debe ingresar el teléfono del paciente.")) return false;
		if (Formato.verificarEntradaRegex(telefono.trim(), "^[0-9-]+$", "El teléfono solo puede contener números y guiones.")) return false;
		if (Formato.entradaVacia(direccion, "Debe ingresar la dirección del paciente.")) return false;
		if (Formato.entradaVacia(pesoStr, "Debe ingresar el peso del paciente.")) return false;
		if (Formato.verificarEntradaRegex(pesoStr.trim(), "^[0-9]+(\\.[0-9]+)?$", "El peso debe ser un número válido.")) return false;
		if (Formato.entradaVacia(estaturaStr, "Debe ingresar la estatura del paciente.")) return false;
		if (Formato.verificarEntradaRegex(estaturaStr.trim(), "^[0-9]+(\\.[0-9]+)?$", "La estatura debe ser un número válido.")) return false;

		String idPaciente = "PAC-" + Clinica.genCodigoPacientes;
		Paciente paciente = new Paciente(
				idPaciente,
				nombre.trim(),
				cedula.trim(),
				telefono.trim(),
				fecNacim,
				sexo,
				Float.parseFloat(pesoStr.trim()),
				Float.parseFloat(estaturaStr.trim()),
				tipoSangre,
				direccion.trim()
		);

		Clinica.getInstancia().regPaciente(paciente);
		this.pacienteCreado = paciente;
		return true;
	}

	public boolean modificarPaciente(Paciente paciente, String nombre, String telefono, Date fecNacim, 
	                                 String sexo, String pesoStr, String estaturaStr, 
	                                 String tipoSangre, String direccion) {

		if (paciente == null) return false;

		if (Formato.entradaVacia(nombre, "Debe ingresar el nombre del paciente.")) return false;
		if (Formato.entradaVacia(telefono, "Debe ingresar el teléfono del paciente.")) return false;
		if (Formato.verificarEntradaRegex(telefono.trim(), "^[0-9-]+$", "El teléfono solo puede contener números y guiones.")) return false;
		if (Formato.entradaVacia(direccion, "Debe ingresar la dirección del paciente.")) return false;
		if (Formato.entradaVacia(pesoStr, "Debe ingresar el peso del paciente.")) return false;
		if (Formato.verificarEntradaRegex(pesoStr.trim(), "^[0-9]+(\\.[0-9]+)?$", "El peso debe ser un número válido.")) return false;
		if (Formato.entradaVacia(estaturaStr, "Debe ingresar la estatura del paciente.")) return false;
		if (Formato.verificarEntradaRegex(estaturaStr.trim(), "^[0-9]+(\\.[0-9]+)?$", "La estatura debe ser un número válido.")) return false;

		paciente.setNombre(nombre.trim());
		paciente.setTelefono(telefono.trim());
		paciente.setFecNacim(fecNacim);
		paciente.setSexo(sexo);
		paciente.setPeso(Float.parseFloat(pesoStr.trim()));
		paciente.setEstatura(Float.parseFloat(estaturaStr.trim()));
		paciente.setTipoSangre(tipoSangre);
		paciente.setDireccion(direccion.trim());

		return true;
	}
}