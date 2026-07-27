package controllers;

import java.util.ArrayList;
import logico.Clinica;
import logico.Enfermedad;
import logico.Paciente;
import logico.Vacuna;

public class VacunaController {

    public ArrayList<Vacuna> obtenerTodas() {
        ArrayList<Vacuna> vacunas = Clinica.getInstancia().getVacunas();
        return vacunas != null ? vacunas : new ArrayList<>();
    }

    public Vacuna buscarPorId(String id) {
        return Clinica.getInstancia().buscarVacunaXId(id);
    }

    public void registrar(String nombre, String fabricante, ArrayList<Enfermedad> enfermedades) throws Exception {
        validarCampos(nombre, fabricante);

        String id = "VAC-" + Clinica.genCodigoVacuna;
        Vacuna nuevaVacuna = new Vacuna(id, nombre.trim(), fabricante.trim(), enfermedades);

        Clinica.getInstancia().getVacunas().add(nuevaVacuna);
        Clinica.genCodigoVacuna++;
    }

    public void modificar(Vacuna vacuna, String nombre, String fabricante, ArrayList<Enfermedad> enfermedades) throws Exception {
        if (vacuna == null) {
            throw new Exception("La vacuna a modificar no existe.");
        }
        validarCampos(nombre, fabricante);

        vacuna.setNombre(nombre.trim());
        vacuna.setFabricante(fabricante.trim());
        vacuna.setEnfermedades(enfermedades);
    }

    public boolean eliminar(Vacuna vacuna) {
        if (vacuna == null) return false;
        return Clinica.getInstancia().getVacunas().remove(vacuna);
    }

    public void aplicarVacunaAPaciente(Paciente paciente, String idVacuna) throws Exception {
        if (paciente == null) {
            throw new Exception("Debe especificar un paciente válido.");
        }
        Vacuna vacuna = buscarPorId(idVacuna);
        if (vacuna == null) {
            throw new Exception("No se encontró la vacuna seleccionada.");
        }
        if (paciente.getVacunas() != null && paciente.getVacunas().contains(vacuna)) {
            throw new Exception("El paciente ya tiene aplicada la vacuna: " + vacuna.getNombre());
        }

        paciente.agregarVacuna(vacuna);
    }

    public ArrayList<Enfermedad> obtenerEnfermedades() {
        ArrayList<Enfermedad> enfermedades = Clinica.getInstancia().getEnfermedades();
        return enfermedades != null ? enfermedades : new ArrayList<>();
    }

    private void validarCampos(String nombre, String fabricante) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("Debe ingresar el nombre de la vacuna.");
        }
        if (fabricante == null || fabricante.trim().isEmpty()) {
            throw new Exception("Debe ingresar el fabricante.");
        }
    }
}