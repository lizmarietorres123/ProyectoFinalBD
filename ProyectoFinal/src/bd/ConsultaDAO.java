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
        // Se utiliza la sintaxis de llamada a procedimiento almacenado
        final String sql = "{call str_insert_consulta(?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setString(1, consulta.getObservaciones());

            // Manejo correcto de nulos asumiendo que tienes un objeto Cita
            if (consulta.getCita() != null) {
                callableStatement.setInt(2, consulta.getCita().getIdNumber());
            } else {
                callableStatement.setNull(2, java.sql.Types.INTEGER);
            }

            callableStatement.execute();


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
                        rs.getString("observaciones"),
                        rs.getInt("id_cita")
                ));
            }

        } catch(SQLException e) {
            System.err.println("Error al obtener las consultas: " + e.getMessage());
        }

        return consultas;
    }

    public void actualizarConsulta(Consulta consulta) {

        final String sql = "{call str_update_consulta(?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, consulta.getIdNumber());
            callableStatement.setString(2, consulta.getObservaciones());

            if (consulta.getCita() != null) {
                callableStatement.setInt(3, consulta.getCita().getIdNumber());
            } else {
                callableStatement.setNull(3, Types.INTEGER);
            }

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al actualizar la consulta: " + e.getMessage());
        }
    }
}