package bd.catalogo;

import bd.ConexionBD;
import logico.enfermeria.DetalleVacuna;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DetalleVacunaDAO {

    private static DetalleVacunaDAO instance = null;

    private DetalleVacunaDAO() {}

    public static DetalleVacunaDAO getInstance() {
        if (instance == null) {
            instance = new DetalleVacunaDAO();
        }
        return instance;
    }

    public void guardarDetalleVacuna(DetalleVacuna detalle) {
        final String sql = "{call str_insert_detalle_vacuna(?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, detalle.getVacuna().getIdNumber());
            callableStatement.setInt(2, detalle.getConsulta().getIdNumber());

            if (detalle.getEnfermera() != null) {
                callableStatement.setInt(3, detalle.getEnfermera().getIdNumber());
            } else {
                callableStatement.setNull(3, java.sql.Types.INTEGER);
            }

            if (detalle.getDosis() > 0) {
                callableStatement.setInt(4, detalle.getDosis());
            } else {
                callableStatement.setNull(4, java.sql.Types.INTEGER);
            }

            if (detalle.getLote() != null && !detalle.getLote().isEmpty()) {
                callableStatement.setString(5, detalle.getLote());
            } else {
                callableStatement.setNull(5, java.sql.Types.VARCHAR);
            }

            callableStatement.setString(6, detalle.getEstado());

            if (detalle.getFecha_aplicacion() != null) {
                callableStatement.setTimestamp(7, Timestamp.valueOf(detalle.getFecha_aplicacion()));
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
            System.err.println("Error al guardar el detalle de vacuna: " + e.getMessage());
        }
    }

    public void guardarDetalleVacuna(Connection connection, DetalleVacuna detalle) {
        final String sql = "{call str_insert_detalle_vacuna(?, ?, ?, ?, ?, ?, ?, ?)}";

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, detalle.getVacuna().getIdNumber());
            callableStatement.setInt(2, detalle.getConsulta().getIdNumber());

            if (detalle.getEnfermera() != null) {
                callableStatement.setInt(3, detalle.getEnfermera().getIdNumber());
            } else {
                callableStatement.setNull(3, java.sql.Types.INTEGER);
            }

            if (detalle.getDosis() > 0) {
                callableStatement.setInt(4, detalle.getDosis());
            } else {
                callableStatement.setNull(4, java.sql.Types.INTEGER);
            }

            if (detalle.getLote() != null && !detalle.getLote().isEmpty()) {
                callableStatement.setString(5, detalle.getLote());
            } else {
                callableStatement.setNull(5, java.sql.Types.VARCHAR);
            }

            callableStatement.setString(6, detalle.getEstado());

            if (detalle.getFecha_aplicacion() != null) {
                callableStatement.setTimestamp(7, Timestamp.valueOf(detalle.getFecha_aplicacion()));
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
            System.err.println("Error al guardar el detalle de vacuna: " + e.getMessage());
        }
    }

    public void actualizarDetalleVacuna(DetalleVacuna detalle) {
        final String sql = "{call str_update_detalle_vacuna(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, detalle.getIdNumber());
            callableStatement.setInt(2, detalle.getVacuna().getIdNumber());
            callableStatement.setInt(3, detalle.getConsulta().getIdNumber());

            if (detalle.getEnfermera() != null) {
                callableStatement.setInt(4, detalle.getEnfermera().getIdNumber());
            } else {
                callableStatement.setNull(4, java.sql.Types.INTEGER);
            }

            if (detalle.getDosis() > 0) {
                callableStatement.setInt(5, detalle.getDosis());
            } else {
                callableStatement.setNull(5, java.sql.Types.INTEGER);
            }

            if (detalle.getLote() != null && !detalle.getLote().isEmpty()) {
                callableStatement.setString(6, detalle.getLote());
            } else {
                callableStatement.setNull(6, java.sql.Types.VARCHAR);
            }

            callableStatement.setString(7, detalle.getEstado());

            if (detalle.getFecha_aplicacion() != null) {
                callableStatement.setTimestamp(8, Timestamp.valueOf(detalle.getFecha_aplicacion()));
            } else {
                callableStatement.setNull(8, java.sql.Types.TIMESTAMP);
            }

            if (detalle.getObservaciones() != null && !detalle.getObservaciones().isEmpty()) {
                callableStatement.setString(9, detalle.getObservaciones());
            } else {
                callableStatement.setNull(9, java.sql.Types.VARCHAR);
            }

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al actualizar el detalle de vacuna: " + e.getMessage());
        }
    }

    // --- NUEVO MÉTODO DE ELIMINACIÓN ---
    public void eliminarDetalleVacuna(int idDetalleVacuna) {
        final String sql = "{call str_delete_detalle_vacuna(?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, idDetalleVacuna);
            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al eliminar el detalle de vacuna: " + e.getMessage());
        }
    }

    public ArrayList<DetalleVacuna> obtenerDetallesVacuna() {
        ArrayList<DetalleVacuna> detalles = new ArrayList<>();
        final String sql = "SELECT * FROM detalle_vacuna";

        try (Connection connection = ConexionBD.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {

                LocalDateTime fechaApli = null;
                Timestamp tsApli = rs.getTimestamp("fecha_aplicacion");
                if (tsApli != null) {
                    fechaApli = tsApli.toLocalDateTime();
                }

                int idConsulta = rs.getInt("id_consulta");
                int idVacuna = rs.getInt("id_vacuna");
                int idEnfermera = rs.getInt("id_enfermera");

                logico.consultorio.Consulta consulta = (idConsulta > 0) ? logico.Clinica.getInstancia().buscarConsultaXIdNumber(idConsulta) : null;
                logico.catalogo.Vacuna vacuna = (idVacuna > 0) ? logico.Clinica.getInstancia().buscarVacunaXIdNumber(idVacuna) : null;
                logico.catalogo.Enfermera enfermera = (idEnfermera > 0) ? logico.Clinica.getInstancia().buscarEnfermeraXIdNumber(idEnfermera) : null;

                detalles.add(new DetalleVacuna(
                        rs.getInt("id_detalle"),
                        rs.getInt("dosis"),
                        rs.getString("lote"),
                        rs.getString("estado"),
                        fechaApli,
                        rs.getString("observaciones"),
                        consulta,
                        vacuna,
                        enfermera
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener los detalles de vacuna: " + e.getMessage());
        }

        return detalles;
    }
}