package bd.catalogo;

import bd.ConexionBD;
import logico.enfermeria.DetalleAnalisis;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DetalleAnalisisDAO {

    private static DetalleAnalisisDAO instance = null;

    private DetalleAnalisisDAO() {}

    public static DetalleAnalisisDAO getInstance() {
        if (instance == null) {
            instance = new DetalleAnalisisDAO();
        }
        return instance;
    }

    public void guardarDetalleAnalisis(DetalleAnalisis detalle) {
        final String sql = "{call str_insert_detalle_analisis(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            // Relaciones obligatorias
            callableStatement.setInt(1, detalle.getAnalisis().getIdNumber());
            callableStatement.setInt(2, detalle.getConsulta().getIdNumber());

            // Enfermera puede ser null si está pendiente
            if (detalle.getEnfermera() != null) {
                callableStatement.setInt(3, detalle.getEnfermera().getIdNumber());
            } else {
                callableStatement.setNull(3, java.sql.Types.INTEGER);
            }

            // Resultado numérico
            if (detalle.getResultado() != null) {
                callableStatement.setDouble(4, detalle.getResultado());
            } else {
                callableStatement.setNull(4, java.sql.Types.DECIMAL);
            }

            callableStatement.setString(5, detalle.getEstado());

            // Fecha (Convertir LocalDateTime a Timestamp)
            if (detalle.getFechaResultado() != null) {
                callableStatement.setTimestamp(6, Timestamp.valueOf(detalle.getFechaResultado()));
            } else {
                callableStatement.setNull(6, java.sql.Types.TIMESTAMP);
            }

            // Observaciones
            if (detalle.getObservaciones() != null && !detalle.getObservaciones().isEmpty()) {
                callableStatement.setString(7, detalle.getObservaciones());
            } else {
                callableStatement.setNull(7, java.sql.Types.VARCHAR);
            }

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al guardar el detalle del análisis: " + e.getMessage());
        }
    }

    public void guardarDetalleAnalisis(Connection connection, DetalleAnalisis detalle) {
        final String sql = "{call str_insert_detalle_analisis(?, ?, ?, ?, ?, ?, ?)}";

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {

            // Relaciones obligatorias
            callableStatement.setInt(1, detalle.getAnalisis().getIdNumber());
            callableStatement.setInt(2, detalle.getConsulta().getIdNumber());

            // Enfermera puede ser null si está pendiente
            if (detalle.getEnfermera() != null) {
                callableStatement.setInt(3, detalle.getEnfermera().getIdNumber());
            } else {
                callableStatement.setNull(3, java.sql.Types.INTEGER);
            }

            // Resultado numérico
            if (detalle.getResultado() != null) {
                callableStatement.setDouble(4, detalle.getResultado());
            } else {
                callableStatement.setNull(4, java.sql.Types.DECIMAL);
            }

            callableStatement.setString(5, detalle.getEstado());

            // Fecha (Convertir LocalDateTime a Timestamp)
            if (detalle.getFechaResultado() != null) {
                callableStatement.setTimestamp(6, Timestamp.valueOf(detalle.getFechaResultado()));
            } else {
                callableStatement.setNull(6, java.sql.Types.TIMESTAMP);
            }

            // Observaciones
            if (detalle.getObservaciones() != null && !detalle.getObservaciones().isEmpty()) {
                callableStatement.setString(7, detalle.getObservaciones());
            } else {
                callableStatement.setNull(7, java.sql.Types.VARCHAR);
            }

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al guardar el detalle del análisis: " + e.getMessage());
        }
    }

    public void actualizarDetalleAnalisis(DetalleAnalisis detalle) {
        final String sql = "{call str_update_detalle_analisis(?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, detalle.getIdNumber());
            callableStatement.setInt(2, detalle.getAnalisis().getIdNumber());
            callableStatement.setInt(3, detalle.getConsulta().getIdNumber());

            if (detalle.getEnfermera() != null) {
                callableStatement.setInt(4, detalle.getEnfermera().getIdNumber());
            } else {
                callableStatement.setNull(4, java.sql.Types.INTEGER);
            }

            if (detalle.getResultado() != null) {
                callableStatement.setDouble(5, detalle.getResultado());
            } else {
                callableStatement.setNull(5, java.sql.Types.DECIMAL);
            }

            callableStatement.setString(6, detalle.getEstado());

            if (detalle.getFechaResultado() != null) {
                callableStatement.setTimestamp(7, Timestamp.valueOf(detalle.getFechaResultado()));
            } else {
                callableStatement.setNull(7, java.sql.Types.TIMESTAMP);
            }

            if (detalle.getObservaciones() != null && !detalle.getObservaciones().isEmpty()) {
                callableStatement.setString(8, detalle.getObservaciones());
            } else {
                callableStatement.setNull(8, java.sql.Types.VARCHAR);
            }

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al actualizar el detalle del análisis: " + e.getMessage());
        }
    }

    // --- NUEVO MÉTODO DE ELIMINACIÓN ---
    public void eliminarDetalleAnalisis(int idDetalleAnalisis) {
        final String sql = "{call str_delete_detalle_analisis(?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, idDetalleAnalisis);
            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al eliminar el detalle del análisis: " + e.getMessage());
        }
    }

    public ArrayList<DetalleAnalisis> obtenerDetallesAnalisis() {
        ArrayList<DetalleAnalisis> detalles = new ArrayList<>();
        final String sql = "SELECT * FROM detalle_analisis";

        try (Connection connection = ConexionBD.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {

                // Conversión de Timestamp a LocalDateTime
                LocalDateTime fechaReg = null;
                Timestamp tsRegistro = rs.getTimestamp("fecha_resultado");
                if (tsRegistro != null) {
                    fechaReg = tsRegistro.toLocalDateTime();
                }

                // Manejo de Double null
                Double resultado = rs.getDouble("resultado");
                if (rs.wasNull()) {
                    resultado = null;
                }

                detalles.add(new DetalleAnalisis(
                        rs.getInt("id_detalle"),
                        null, // Reemplazar con lógica para cargar Analisis desde rs.getInt("id_analisis")
                        null, // Reemplazar con lógica para cargar Consulta desde rs.getInt("id_consulta")
                        null, // Reemplazar con lógica para cargar Enfermera desde rs.getInt("id_enfermera")
                        resultado,
                        rs.getString("estado"),
                        fechaReg,
                        rs.getString("observaciones")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener los detalles de análisis: " + e.getMessage());
        }

        return detalles;
    }
}