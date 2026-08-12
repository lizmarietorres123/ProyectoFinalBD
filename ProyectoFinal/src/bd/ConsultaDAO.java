package bd;

import logico.consultorio.Consulta;

import java.sql.*;
import java.util.ArrayList;

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
        final String sql =
                "INSERT INTO consulta (observaciones, id_cita) " +
                "OUTPUT INSERTED.id_consulta, INSERTED.fecha_hora " +
                "VALUES (?, ?)";

        //Se utiliza try para garantizar que la conexion se cierre automaticamente, evitando memory leaks
        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, consulta.getObservaciones());
            //preparedStatement.setInt(3, consulta.getCita().getIdNumber());
            preparedStatement.setNull(2, java.sql.Types.INTEGER);

            //Asignar el id y fecha generada en bd al objeto local;
            try(ResultSet rs = preparedStatement.executeQuery()) {

                if (rs.next()) {
                    consulta.setId(rs.getInt("id_consulta"));
                    consulta.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al guardar la consulta: " + e.getMessage());
        }
    }

    public ArrayList<Consulta> obtenerConsultas(){
        ArrayList<Consulta> consultas = new ArrayList<>();
        final String sql = "SELECT * FROM consulta";

        try (Connection connection = ConexionBD.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)){

            while(rs.next()){
                consultas.add(new Consulta(
                        rs.getInt("id_consulta"),
                        rs.getTimestamp("fecha_hora").toLocalDateTime(),
                        rs.getString("tratamiento"),
                        rs.getString("observaciones"),
                        rs.getInt("id_cita")
                ));
            }

        }catch(SQLException e) {
            System.err.println("Error al guardar la consulta: " + e.getMessage());
        }

        return consultas;
    }

    public void actualizarConsulta(Consulta consulta) {
        final String sql =
                "UPDATE INTO consulta (observaciones, id_cita) " + "VALUES (?, ?)";

        //Se utiliza try para garantizar que la conexion se cierre automaticamente, evitando memory leaks
        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, consulta.getObservaciones());
            //preparedStatement.setInt(2, consulta.getCita().getIdNumber());
            preparedStatement.setNull(3, java.sql.Types.INTEGER);


        } catch (SQLException e) {
            System.err.println("Error al guardar la consulta: " + e.getMessage());
        }
    }
}
