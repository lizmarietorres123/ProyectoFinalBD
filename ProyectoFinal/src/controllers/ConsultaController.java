package controllers;

import java.util.ArrayList;
import java.util.List;

import logico.Cita;
import logico.Clinica;
import logico.Consulta;
import logico.Diagnostico;
import logico.Doctor;
import logico.Enfermedad;
import logico.EstadoCita;
import logico.Paciente;

public class ConsultaController {

    public String generarNuevoIdConsulta() {
        return "CONS-" + Clinica.genCodigoConsultas;
    }

    public List<Cita> obtenerCitasProgramadas() {
        List<Cita> programadas = new ArrayList<>();
        if (Clinica.getInstancia().getCitas() != null) {
            for (Cita cita : Clinica.getInstancia().getCitas()) {
                if (cita.getEstado() == EstadoCita.PROGRAMADA && cita.getPaciente() != null) {
                    programadas.add(cita);
                }
            }
        }
        return programadas;
    }

    public List<Cita> obtenerTodasLasCitas() {
        return Clinica.getInstancia().getCitas();
    }

    public Cita obtenerCitaPorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return null;
        }
        return Clinica.getInstancia().buscarCitaXId(codigo.trim());
    }

    public Paciente buscarPacientePorIdentificacion(String idPersona) {
        return Clinica.getInstancia().buscarPacienteXIdentificacion(idPersona);
    }

    public List<Consulta> obtenerConsultasVisiblesXDoctor(Doctor doctor) {
        return Clinica.getInstancia().getConsultasVisiblesXDoctor(doctor);
    }

    public Consulta buscarConsultaPorCodigo(String codigo) {
        return Clinica.getInstancia().buscarConsultaXId(codigo);
    }

    public Consulta registrarConsulta(Cita citaElegida, List<Diagnostico> diagnosticos, String tratamiento, String observaciones, boolean esImportante) {
        if (citaElegida == null || citaElegida.getPaciente() == null) {
            return null;
        }

        Paciente pacienteActual = citaElegida.getPaciente();
        String idConsulta = generarNuevoIdConsulta();

        Consulta consulta = new Consulta(
                idConsulta,
                pacienteActual,
                citaElegida.getDoctor(),
                citaElegida.getFechaHora()
        );

        if (diagnosticos != null) {
            for (Diagnostico d : diagnosticos) {
                consulta.addDiagnostico(d);
                Enfermedad enfermedadDiag = d.getEnfermedadDiagnosticada();
                if (enfermedadDiag != null) {
                    pacienteActual.agregarEnfermedad(enfermedadDiag);
                }
            }
        }

        consulta.setTratamiento(tratamiento != null ? tratamiento.trim() : "");
        consulta.setObservaciones(observaciones != null ? observaciones.trim() : "");
        consulta.setEsImportante(esImportante);

        if (pacienteActual.getHistorialClinico() != null && !pacienteActual.getHistorialClinico().contains(consulta)) {
            pacienteActual.getHistorialClinico().add(consulta);
        }

        pacienteActual.addConsultaToResumen(consulta);
        Clinica.getInstancia().realizarConsulta(consulta, citaElegida);

        return consulta;
    }
}