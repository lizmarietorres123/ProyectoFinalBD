package bd.catalogo;

import bd.ConexionBD;
import logico.catalogo.Doctor;

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
        final String sql = "{call str_insert_doctor(?, ?, ?, ?)}";

        try (Connection connection = ConexionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.setString(1, doctor.getNombre());
            callableStatement.setString(2, doctor.getApellido());
            callableStatement.setInt(3, doctor.getCupoDiario());

            // Si el doctor tiene un usuario asignado en el sistema
            if (doctor.getUsuario() != null) {
                // Se asume que Usuario tiene getIdNumber() o similar
                callableStatement.setInt(4, doctor.getUsuario().getIdNumber());
            } else {
                callableStatement.setNull(4, java.sql.Types.INTEGER);
            }

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al guardar el doctor: " + e.getMessage());
        }
    }

    public void actualizarDoctor(Doctor doctor) {
        final String sql = "{call str_update_doctor(?, ?, ?, ?, ?)}";

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

            callableStatement.execute();

        } catch (SQLException e) {
            System.err.println("Error al actualizar el doctor: " + e.getMessage());
        }
    }

    public ArrayList<Doctor> obtenerDoctores() {
        ArrayList<Doctor> doctores = new ArrayList<>();
        final String sql = "SELECT * FROM doctor";

        try (Connection connection = ConexionBD.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                doctores.add(new Doctor(
                        rs.getInt("id_doctor"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getInt("cupo_diario")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener los doctores: " + e.getMessage());
        }

        return doctores;
    }
}