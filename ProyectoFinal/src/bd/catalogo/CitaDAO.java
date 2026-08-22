package bd.catalogo;

import bd.ConexionBD;
import logico.catalogo.EstadoCita;
import logico.consultorio.Cita;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CitaDAO {

    private static CitaDAO instance = null;

    private CitaDAO() {}

    public static CitaDAO getInstance() {
        if (instance == null) {
            instance = new CitaDAO();
        }
        return instance;
    }

    public void guardarCita(Cita cita) {
        final String sql = "{call str_insert_cita(?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            // La fecha de registro (GETDATE()) se maneja sola en la BD
            callableStatement.setDate(1, new java.sql.Date(cita.getFechaConsulta().getTime()));
            callableStatement.setTime(2, cita.getHoraConsulta());
            callableStatement.setString(3, cita.getEstado().name()); // Guarda el enum como String

            if (cita.getDoctor() != null) {
                callableStatement.setInt(4, cita.getDoctor().getIdNumber());
            } else {
                callableStatement.setNull(4, java.sql.Types.INTEGER);
            }

            if (cita.getPaciente() != null) {
                callableStatement.setInt(5, cita.getPaciente().getIdNumber());
            } else {
                callableStatement.setNull(5, java.sql.Types.INTEGER);
            }

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al guardar la cita: " + e.getMessage());
        }
    }

    public void actualizarCita(Cita cita) {
        final String sql = "{call str_update_cita(?, ?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, cita.getIdNumber());
            callableStatement.setDate(2, new java.sql.Date(cita.getFechaConsulta().getTime()));
            callableStatement.setTime(3, cita.getHoraConsulta());
            callableStatement.setString(4, cita.getEstado().name());

            if (cita.getDoctor() != null) {
                callableStatement.setInt(5, cita.getDoctor().getIdNumber());
            } else {
                callableStatement.setNull(5, java.sql.Types.INTEGER);
            }

            if (cita.getPaciente() != null) {
                callableStatement.setInt(6, cita.getPaciente().getIdNumber());
            } else {
                callableStatement.setNull(6, java.sql.Types.INTEGER);
            }

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al actualizar la cita: " + e.getMessage());
        }
    }

    public void eliminarCita(int idCita) {
        // Llamada al SP de tu compañera para eliminar o cancelar la cita
        final String sql = "{call str_delete_cita(?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, idCita);
            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al eliminar la cita: " + e.getMessage());
        }
    }

    public ArrayList<Cita> obtenerCitas() {
        ArrayList<Cita> citas = new ArrayList<>();
        final String sql = "SELECT * FROM cita";

        try (Connection connection = ConexionBD.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {

                java.sql.Timestamp tsRegistro = rs.getTimestamp("fecha_registro");
                java.time.LocalDateTime ldtRegistro = (tsRegistro != null) ? tsRegistro.toLocalDateTime() : null;

                String estadoStr = rs.getString("estado");
                EstadoCita estadoEnum = EstadoCita.valueOf(estadoStr.toUpperCase());

                citas.add(new Cita(
                        rs.getInt("id_cita"),
                        ldtRegistro,
                        rs.getDate("fecha_consulta"),
                        rs.getTime("hora_consulta"),
                        estadoEnum,
                        null,
                        null
                ));
            }

        } catch (SQLException | IllegalArgumentException e) {
            System.err.println("Error al obtener las citas: " + e.getMessage());
        }

        return citas;
    }
}