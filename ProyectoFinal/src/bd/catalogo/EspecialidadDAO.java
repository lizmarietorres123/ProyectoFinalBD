package bd.catalogo;

import bd.ConexionBD;
import logico.catalogo.Especialidad;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EspecialidadDAO {

    private static EspecialidadDAO instance = null;

    private EspecialidadDAO() {}

    public static EspecialidadDAO getInstance() {
        if (instance == null) {
            instance = new EspecialidadDAO();
        }
        return instance;
    }

    public void guardarEspecialidad(Especialidad especialidad) {
        final String sql = "{call str_insert_especialidad(?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setString(1, especialidad.getNombre());
            callableStatement.setString(2, especialidad.getAreaMedica());
            callableStatement.setString(3, especialidad.getDescripcion());

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al guardar la especialidad: " + e.getMessage());
        }
    }

    public void actualizarEspecialidad(Especialidad especialidad) {
        final String sql = "{call str_update_especialidad(?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, especialidad.getIdNumber());
            callableStatement.setString(2, especialidad.getNombre());
            callableStatement.setString(3, especialidad.getAreaMedica());
            callableStatement.setString(4, especialidad.getDescripcion());

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al actualizar la especialidad: " + e.getMessage());
        }
    }

    public ArrayList<Especialidad> obtenerEspecialidades() {
        ArrayList<Especialidad> especialidades = new ArrayList<>();
        final String sql = "SELECT * FROM especialidad";

        try (Connection connection = ConexionBD.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                especialidades.add(new Especialidad(
                        rs.getInt("id_especialidad"),
                        rs.getString("nombre"),
                        rs.getString("area_medica"),
                        rs.getString("descripcion")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener las especialidades: " + e.getMessage());
        }

        return especialidades;
    }
}