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
            // 1. Obtener la conexión y desactivar el autocommit (Inicia la transacción)
            connection = ConexionBD.getConnection();
            connection.setAutoCommit(false);

            // 2. Guardar la consulta principal usando la conexión compartida
            int idConsulta = ConsultaDAO.getInstance().guardarConsulta(connection, consulta);

            // 3. Guardar los diagnósticos asociados
            guardarDiagnosticos(connection,consulta);

            //4.Guardar detalle analisis asociados
            guardarDetalleAnalisis(connection,consulta);

            //5.Guardar detalle vacunas asociados
            guardarDetalleVacunas(connection,consulta);

            // 6. Si todo salió bien, confirmamos los cambios permanentemente en la BD
            connection.commit();
            return true;

        } catch (SQLException e) {
            // Si ocurre CUALQUIER error, se deshacen todos los cambios realizados
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
            // Asegurarse de cerrar la conexión al finalizar
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

    private void guardarDiagnosticos(Connection connection, Consulta consulta){
        if (consulta.getDiagnosticos() != null) {
            for (Diagnostico d : consulta.getDiagnosticos()) {
                DiagnosticoDAO.getInstance().guardarDiagnostico(connection, d);

                // Guardar tratamientos del diagnóstico si existen
                if (d.getTratamientos() != null) {
                    for (Tratamiento t : d.getTratamientos()) {
                        TratamientoDAO.getInstance().guardarTratamiento(connection, t);
                    }
                }
            }
        }
    }

    private void guardarDetalleAnalisis(Connection connection, Consulta consulta){
        if(consulta.getAnalisis() != null){
            for(DetalleAnalisis da : consulta.getAnalisis()){
                DetalleAnalisisDAO.getInstance().guardarDetalleAnalisis(connection,da);
            }
        }
    }

    private void guardarDetalleVacunas(Connection connection, Consulta consulta){
        if(consulta.getVacunas() != null){
            for(DetalleVacuna da : consulta.getVacunas()){
                DetalleVacunaDAO.getInstance().guardarDetalleVacuna(connection,da);
            }
        }
    }

    public boolean actualizarConsulta(Consulta consulta) {
        Connection connection = null;
        try {
            // 1. Obtener conexión y abrir transacción
            connection = ConexionBD.getConnection();
            connection.setAutoCommit(false);

            // 2. Actualizar los datos maestros de la consulta
            // (Llamando al DAO pasando la conexión compartida)


            //El sp se encarga de eliminar los elementos relacionados a la consulta
            ConsultaDAO.getInstance().actualizarConsulta(connection, consulta);

            // 3. Guardar los diagnósticos asociados
            guardarDiagnosticos(connection,consulta);

            //4.Guardar detalle analisis asociados
            guardarDetalleAnalisis(connection,consulta);

            //5.Guardar detalle vacunas asociados
            guardarDetalleVacunas(connection,consulta);

            // 6. Todo salió bien: confirmamos cambios permanentemente
            connection.commit();
            return true;

        } catch (SQLException e) {
            // Si ocurre un error, revertimos todo para no dejar la BD a medias
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
            // Cerramos la conexión
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