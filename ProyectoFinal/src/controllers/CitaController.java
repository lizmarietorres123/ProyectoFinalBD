package controllers;

import bd.catalogo.CitaDAO;
import logico.Clinica;
import logico.consultorio.Cita;
import java.util.ArrayList;

public class CitaController {

    public CitaController() {
    }

    public ArrayList<Cita> obtenerTodas() {
        return Clinica.getInstancia().getCitas();
    }

    public Cita buscarPorId(String id) {
        return Clinica.getInstancia().buscarCitaXId(id);
    }

    public boolean registrar(Cita cita) {
        if (cita != null) {
            CitaDAO.getInstance().guardarCita(cita);
            Clinica.getInstancia().regCita(cita);
            return true;
        }
        return false;
    }

    public boolean actualizar(Cita cita) {
        if (cita != null) {
            CitaDAO.getInstance().actualizarCita(cita);
            return true;
        }
        return false;
    }

    public boolean eliminar(Cita cita) {
        if (cita != null) {
            CitaDAO.getInstance().eliminarCita(cita.getIdNumber());
            return Clinica.getInstancia().getCitas().remove(cita);
        }
        return false;
    }
}