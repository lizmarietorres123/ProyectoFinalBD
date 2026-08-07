package bd;

import logico.consultorio.Consulta;

import java.sql.*;

public class ConsultaDAO {

    private static ConsultaDAO instance = null;

    private ConsultaDAO() {}

    public static ConsultaDAO getInstance() {
        if (instance == null) {
            instance = new ConsultaDAO();
        }
        return instance;
    }

    public void guardarConsulta(Consulta consulta) {
        final String sql = "INSERT INTO consulta (fecha, tratamiento, observaciones, id_cita) VALUES (?, ?, ?, ?)";

        //Se utiliza try para garantizar que la conexion se cierre automaticamente, evitando memory leaks
        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setTimestamp(1, consulta.getFecha());
            preparedStatement.setString(2, consulta.getTratamiento());
            preparedStatement.setString(3, consulta.getObservaciones());
            preparedStatement.executeUpdate();

            //Se inyecta el id generado en la base de datos al onjeto
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    consulta.setIdParada(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar la parada: " + e.getMessage());
        }
    }
}
