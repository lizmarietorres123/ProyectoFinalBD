package controllers;

import bd.catalogo.PacienteDAO;
import logico.Clinica;
import logico.consultorio.Paciente;
import java.util.ArrayList;

public class PacienteController {

    public PacienteController() {
    }

    public ArrayList<Paciente> obtenerTodos() {
        return Clinica.getInstancia().getPacientes();
    }

    public Paciente buscarPorId(String id) {
        return Clinica.getInstancia().buscarPacienteXId(id);
    }

    public Paciente buscarPorCedula(String cedula) {
        return Clinica.getInstancia().buscarPacienteXIdentificacion(cedula);
    }

    public boolean registrar(Paciente paciente) {
        if (paciente != null) {
            PacienteDAO.getInstance().guardarPaciente(paciente);
            Clinica.getInstancia().regPaciente(paciente);
            return true;
        }
        return false;
    }

    public boolean actualizar(Paciente paciente) {
        if (paciente != null) {
            PacienteDAO.getInstance().actualizarPaciente(paciente);
            return true;
        }
        return false;
    }

    public boolean eliminar(Paciente paciente) {
        if (paciente != null) {
            PacienteDAO.getInstance().eliminarPaciente(paciente.getIdNumber());
            return Clinica.getInstancia().getPacientes().remove(paciente);
        }
        return false;
    }
}