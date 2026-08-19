package bd.catalogo;

import bd.ConexionBD;
import logico.catalogo.EstadoCita;
import logico.consultorio.Cita;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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

            // Conversión de java.util.Date a java.sql.Date
            callableStatement.setDate(1, new java.sql.Date(cita.getFechaConsulta().getTime()));
            callableStatement.setTime(2, cita.getHoraConsulta());

            // El Enum se envía como String
            callableStatement.setString(3, cita.getEstado().name());

            // Relaciones obligatorias
            callableStatement.setInt(4, cita.getDoctor().getIdNumber());
            callableStatement.setInt(5, cita.getPaciente().getIdNumber());

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
            callableStatement.setInt(5, cita.getDoctor().getIdNumber());
            callableStatement.setInt(6, cita.getPaciente().getIdNumber());

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al actualizar la cita: " + e.getMessage());
        }
    }

    public ArrayList<Cita> obtenerCitas() {
        ArrayList<Cita> citas = new ArrayList<>();
        final String sql = "SELECT * FROM cita";

        try (Connection connection = ConexionBD.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {

                // Mapeo seguro de LocalDateTime (fechaRegistro suele ser autogenerado por SQL Server)
                LocalDateTime fechaReg = null;
                Timestamp tsRegistro = rs.getTimestamp("fecha_registro");
                if (tsRegistro != null) {
                    fechaReg = tsRegistro.toLocalDateTime();
                }

                // Conversión de String a Enum
                EstadoCita estado = EstadoCita.valueOf(rs.getString("estado").toUpperCase());

                citas.add(new Cita(
                        rs.getInt("id_cita"),
                        fechaReg,
                        rs.getDate("fecha_consulta"),
                        rs.getTime("hora_consulta"),
                        estado,
                        null, // Reemplazar con lógica para cargar el Paciente desde rs.getInt("id_paciente")
                        null  // Reemplazar con lógica para cargar el Doctor desde rs.getInt("id_doctor")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener las citas: " + e.getMessage());
        }

        return citas;
    }
}