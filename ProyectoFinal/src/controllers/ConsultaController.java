package controllers;

import bd.ConexionBD;
import bd.ConsultaDAO;
import bd.catalogo.DetalleAnalisisDAO;
import bd.catalogo.DetalleVacunaDAO;
import bd.catalogo.DiagnosticoDAO;
import bd.catalogo.TratamientoDAO;
import logico.consultorio.Consulta;
import logico.consultorio.Diagnostico;
import logico.consultorio.Tratamiento;
import logico.enfermeria.DetalleAnalisis;
import logico.enfermeria.DetalleVacuna;

import java.sql.Connection;
import java.sql.SQLException;

public class ConsultaController {

    public boolean guardarConsulta(Consulta consulta) {
        Connection connection = null;
        try {
            connection = ConexionBD.getConnection();
            connection.setAutoCommit(false);

            int idConsulta = ConsultaDAO.getInstance().guardarConsulta(connection, consulta);

            guardarDiagnosticos(connection, consulta);
            guardarDetalleAnalisis(connection, consulta);
            guardarDetalleVacunas(connection, consulta);

            connection.commit();
            return true;

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    System.err.println("Transacción revertida (Rollback): " + e.getMessage());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void guardarDiagnosticos(Connection connection, Consulta consulta) {
        if (consulta.getDiagnosticos() != null) {
            for (Diagnostico d : consulta.getDiagnosticos()) {
                DiagnosticoDAO.getInstance().guardarDiagnostico(connection, d);

                if (d.getTratamientos() != null) {
                    for (Tratamiento t : d.getTratamientos()) {
                        TratamientoDAO.getInstance().guardarTratamiento(connection, t);
                    }
                }
            }
        }
    }

    private void guardarDetalleAnalisis(Connection connection, Consulta consulta) {
        if (consulta.getAnalisis() != null) {
            for (DetalleAnalisis da : consulta.getAnalisis()) {
                DetalleAnalisisDAO.getInstance().guardarDetalleAnalisis(connection, da);
            }
        }
    }

    private void guardarDetalleVacunas(Connection connection, Consulta consulta) {
        if (consulta.getVacunas() != null) {
            for (DetalleVacuna dv : consulta.getVacunas()) {
                DetalleVacunaDAO.getInstance().guardarDetalleVacuna(connection, dv);
            }
        }
    }

    public boolean actualizarConsulta(Consulta consulta) {
        Connection connection = null;
        try {
            connection = ConexionBD.getConnection();
            connection.setAutoCommit(false);

            // CORRECCIÓN: En modo edición, el controlador SOLO actualiza la tabla maestra de consulta.
            // La inserción/eliminación de análisis, vacunas y diagnósticos ya fue gestionada
            // de manera atómica (uno por uno) por la clase CrearConsulta.java en sus métodos
            // procesarNuevosElementos() y procesarElementosEliminados().
            ConsultaDAO.getInstance().actualizarConsulta(connection, consulta);

            connection.commit();
            return true;

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    System.err.println("Error en actualización. Rollback aplicado: " + e.getMessage());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}