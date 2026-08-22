package bd.catalogo;

import bd.ConexionBD;
import logico.catalogo.Doctor;
import logico.catalogo.Usuario;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DoctorDAO {

    private static DoctorDAO instance = null;

    private DoctorDAO() {}

    public static DoctorDAO getInstance() {
        if (instance == null) {
            instance = new DoctorDAO();
        }
        return instance;
    }

    public void guardarDoctor(Doctor doctor) {

        final String sql = "{call str_insert_doctor(?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setString(1, doctor.getNombre());
            callableStatement.setString(2, doctor.getApellido());
            callableStatement.setInt(3, doctor.getCupoDiario());

            if (doctor.getUsuario() != null) {
                callableStatement.setInt(4, doctor.getUsuario().getIdNumber());
            } else {
                callableStatement.setNull(4, java.sql.Types.INTEGER);
            }

            // Ojo: Si la BD espera un INT (id_especialidad), el SP debe manejar esta conversión.
            callableStatement.setString(5, doctor.getEspecialidad());

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al guardar el doctor: " + e.getMessage());
        }
    }

    public void actualizarDoctor(Doctor doctor) {
        // El SP recibe el estado al final para el borrado lógico.
        final String sql = "{call str_update_doctor(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, doctor.getIdNumber());
            callableStatement.setString(2, doctor.getNombre());
            callableStatement.setString(3, doctor.getApellido());
            callableStatement.setInt(4, doctor.getCupoDiario());

            if (doctor.getUsuario() != null) {
                callableStatement.setInt(5, doctor.getUsuario().getIdNumber());
            } else {
                callableStatement.setNull(5, java.sql.Types.INTEGER);
            }

            callableStatement.setString(6, doctor.getEspecialidad());
            callableStatement.setString(7, doctor.getEstado());

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al actualizar el doctor: " + e.getMessage());
        }
    }

    public void eliminarDoctor(int idDoctor) {
        // Llamada al SP de tu compañera para eliminar/inactivar
        final String sql = "{call str_delete_doctor(?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setInt(1, idDoctor);
            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al eliminar el doctor: " + e.getMessage());
        }
    }

    public ArrayList<Doctor> obtenerDoctores() {
        ArrayList<Doctor> doctores = new ArrayList<>();
        // Recomendación: Si tienes un id_especialidad en la tabla original,
        // usa una vista (ej. vw_doctores_completos) que haga el JOIN con la tabla especialidad
        final String sql = "SELECT * FROM doctor";

        try (Connection connection = ConexionBD.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                Doctor doc = new Doctor(
                        rs.getInt("id_doctor"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getInt("cupo_diario"),
                        rs.getString("especialidad"), // Ajusta si la columna en la BD se llama distinto
                        rs.getString("estado")
                );

                // Si la vista/tabla incluye info del usuario, podrías mapearlo aquí o dejarlo null
                // y que un controlador se encargue de ensamblarlo luego.

                doctores.add(doc);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener los doctores: " + e.getMessage());
        }

        return doctores;
    }
}